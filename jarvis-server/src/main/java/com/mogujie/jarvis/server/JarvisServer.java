/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月21日 下午4:11:14
 */

package com.mogujie.jarvis.server;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.joda.time.DateTime;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.exception.JobScheduleException;
import com.mogujie.jarvis.core.expression.CronExpression;
import com.mogujie.jarvis.core.expression.FixedDelayExpression;
import com.mogujie.jarvis.core.expression.FixedRateExpression;
import com.mogujie.jarvis.core.expression.ISO8601Expression;
import com.mogujie.jarvis.core.expression.ScheduleExpression;
import com.mogujie.jarvis.core.metrics.Metrics;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.ReflectionUtils;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.actor.ServerActor;
import com.mogujie.jarvis.server.alarm.AlarmScheduler;
import com.mogujie.jarvis.server.dispatcher.TaskDispatcher;
import com.mogujie.jarvis.server.domain.JobEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.JobSchedulerController;
import com.mogujie.jarvis.server.scheduler.TaskRetryScheduler;
import com.mogujie.jarvis.server.scheduler.dag.DAGJob;
import com.mogujie.jarvis.server.scheduler.dag.DAGJobType;
import com.mogujie.jarvis.server.scheduler.dag.DAGScheduler;
import com.mogujie.jarvis.server.scheduler.dag.JobGraph;
import com.mogujie.jarvis.server.scheduler.event.FailedEvent;
import com.mogujie.jarvis.server.scheduler.event.RetryTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.StartEvent;
import com.mogujie.jarvis.server.scheduler.task.DAGTask;
import com.mogujie.jarvis.server.scheduler.task.TaskGraph;
import com.mogujie.jarvis.server.scheduler.task.TaskScheduler;
import com.mogujie.jarvis.server.scheduler.time.TimePlan;
import com.mogujie.jarvis.server.scheduler.time.TimeScheduler;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.PlanService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.timer.AbstractTimerTask;
import com.mogujie.jarvis.server.util.PlanUtil;

public class JarvisServer {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting Jarvis server...");

        try {
            ActorSystem system = Injectors.getInjector().getInstance(ActorSystem.class);

            Configuration config = ConfigUtils.getServerConfig();
            int serverActorNum = config.getInt(ServerConigKeys.SERVER_ACTOR_NUM, 500);
            system.actorOf(ServerActor.props().withRouter(new RoundRobinPool(serverActorNum)), JarvisConstants.SERVER_AKKA_SYSTEM_NAME);

            LOGGER.info("start dispatcher...");
            int taskDispatcherThreads = config.getInt(ServerConigKeys.SERVER_DISPATCHER_THREADS, 5);
            ExecutorService executorService = Executors.newFixedThreadPool(taskDispatcherThreads);
            for (int i = 0; i < taskDispatcherThreads; i++) {
                Thread taskDispatcher = new TaskDispatcher();
                taskDispatcher.setName("Jarvis-task-dispatcher-" + i);
                executorService.submit(taskDispatcher);
            }
            executorService.shutdown();

            LOGGER.info("start TaskRetryScheduler...");
            TaskRetryScheduler taskRetryScheduler = TaskRetryScheduler.INSTANCE;
            taskRetryScheduler.start();

            init();
            Metrics.start(ConfigUtils.getServerConfig());
            LOGGER.info("Jarvis server started.");

        } catch (Exception ex) {
            LOGGER.error("Jarvis server start error", ex);
            Throwables.propagate(ex);
        }
    }

    public static void init() throws Exception {
        LOGGER.info("start init TimerTask...");
        initTimerTask();
        LOGGER.info("start init plan table, please wait for several minutes...");
        initPlan();
        LOGGER.info("start init Scheduler...");
        initScheduler();
    }

    private static void initScheduler() throws JobScheduleException, CycleFoundException {
        // 1. register schedulers to controller
        JobSchedulerController controller = JobSchedulerController.getInstance();
        DAGScheduler dagScheduler = DAGScheduler.getInstance();
        TaskScheduler taskScheduler = TaskScheduler.getInstance();
        TimeScheduler timeScheduler = TimeScheduler.getInstance();
        AlarmScheduler alarmScheduler = Injectors.getInjector().getInstance(AlarmScheduler.class);
        JobGraph jobGraph = JobGraph.INSTANCE;
        TaskGraph taskGraph = TaskGraph.INSTANCE;
        TimePlan plan = TimePlan.INSTANCE;
        controller.register(dagScheduler);
        controller.register(taskScheduler);
        controller.register(timeScheduler);
        controller.register(alarmScheduler);

        // 2. start schedulers
        controller.notify(new StartEvent());

        // 3. initialize DAGScheduler and TimeScheduler
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
        List<Job> jobs = jobService.getNotDeletedJobs();

        // 3.1 先添加job
        for (Job job : jobs) {
            long jobId = job.getJobId();
            JobEntry jobEntry = jobService.get(jobId);
            int cycleFlag = 0;
            int timeFlag = 0;
            Map<Long, ScheduleExpression> timeExpressions = jobEntry.getScheduleExpressions();
            if (timeExpressions != null && !timeExpressions.isEmpty()) {
                for (ScheduleExpression expression : timeExpressions.values()) {
                    if (expression instanceof CronExpression || expression instanceof FixedRateExpression
                            || expression instanceof ISO8601Expression) {
                        timeFlag = 1;
                    } else if (expression instanceof FixedDelayExpression) {
                        cycleFlag = 1;
                    }
                }
            }
            //过滤DELETED父任务
            Set<Long> dependencies = Sets.newHashSet();
            for (long preJobId : jobEntry.getDependencies().keySet()) {
                if (jobService.get(preJobId).getJob().getStatus() != JobStatus.DELETED.getValue()) {
                    dependencies.add(preJobId);
                }
            }
            int dependFlag = (!dependencies.isEmpty()) ? 1 : 0;
            DAGJobType type = DAGJobType.getDAGJobType(timeFlag, dependFlag, cycleFlag);
            DAGJob dagJob = new DAGJob(jobId, type);
            jobGraph.addJob(jobId, dagJob, null);
            if (type.equals(DAGJobType.TIME) && dagJob.getJobStatus().equals(JobStatus.ENABLE)
                    && jobService.isActive(jobId) && !job.getIsTemp()) {
                plan.recoverJob(jobId);
            }
        }
        // 3.2 再添加依赖关系
        for (Job job : jobs) {
            long jobId = job.getJobId();
            JobEntry jobEntry = jobService.get(jobId);
            Set<Long> dependencies = jobEntry.getDependencies().keySet();
            for (long parentId : dependencies) {
                try {
                    jobGraph.addDependency(parentId, jobId);
                } catch (Exception e) {
                    Throwables.propagate(e);
                }
            }
            //触发任务执行
            DAGJob dagJob = jobGraph.getDAGJob(jobId);
            if (dagJob != null && dagJob.getType().implies(DAGJobType.TIME)) {
                //重新计算下一次时间
                DateTime now = DateTime.now();
                DateTime lastTime = PlanUtil.getScheduleTimeBefore(jobId, now);
                if (lastTime == null) {
                    lastTime = JarvisConstants.DATETIME_MIN;
                }
                DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, now);
                if (nextTime == null) {
                    nextTime = JarvisConstants.DATETIME_MAX;
                }
                List<Task> tasks = taskService.getTasksBetween(jobId, Range.closed(lastTime.minusSeconds(1), nextTime));
                if (tasks == null || tasks.isEmpty()) {
                    //如果当前周期内没有跑过，则重新检查依赖关系
                    DateTime scheduleDateTime = lastTime;
                    jobGraph.submitJobWithCheck(dagJob, scheduleDateTime);
                }
            }
        }

        // 4. initialize TaskScheduler
        List<Task> recoveryTasks = taskService
                .getTasksByStatusNotIn(Lists.newArrayList(TaskStatus.SUCCESS.getValue(), TaskStatus.REMOVED.getValue()));
        // 4.1 先恢复task
        for (Task task : recoveryTasks) {
            DAGTask dagTask = new DAGTask(task.getJobId(), task.getTaskId(), task.getAttemptId(),
                    task.getScheduleTime().getTime(), task.getDataTime().getTime());
            taskGraph.addTask(task.getTaskId(), dagTask);
        }
        // 4.2 再构造task依赖关系
        for (Task task : recoveryTasks) {
            DAGTask dagTask = taskGraph.getTask(task.getTaskId());
            if (dagTask != null) {
                List<Long> dependTaskIds = dagTask.getDependTaskIds();
                if (dependTaskIds != null) {
                    for (Long parentId : dependTaskIds) {
                        taskGraph.addDependency(parentId, task.getTaskId());
                    }
                }
            } else {
                LOGGER.warn("DAGTask {} is not found in TaskGraph", task.getTaskId() );
            }
        }
        // 4.3 重试waiting和ready的task
        List<Task> readyTasks = taskService.getTasksByStatus(Lists.newArrayList(TaskStatus.WAITING.getValue(), TaskStatus.READY.getValue()));
        DateTime now = DateTime.now();
        for (Task task : readyTasks) {
            long jobId = task.getJobId();
            JobEntry jobEntry = jobService.get(jobId);
            if (jobEntry != null) {
                Job job = jobEntry.getJob();
                int expiredTime = job.getExpiredTime(); //秒
                DateTime scheduleTime = new DateTime(task.getScheduleTime());
                DateTime expiredDateTime = scheduleTime.plusSeconds(expiredTime);
                // 如果该任务没有过期
                if (expiredDateTime.isAfter(now)) {
                    // 如果该任务状态是ENABLE&ACTIVE
                    if (job.getStatus() == JobStatus.ENABLE.getValue() && jobService.isActive(jobId)) {
                        controller.notify(new RetryTaskEvent(jobId, task.getTaskId(), task.getExecuteUser()));
                    } else {
                        //如果该任务不是ACTIVE
                        LOGGER.warn("{} is not enable nor active.", jobId);
                        controller.notify(new FailedEvent(jobId, task.getTaskId(), "job is not enable nor active"));
                    }
                } else {
                    //如果该任务已过期，置为失败
                    LOGGER.warn("{} is expired.", task.getTaskId());
                    controller.notify(new FailedEvent(jobId, task.getTaskId(), "task expired"));
                }
            } else {
                LOGGER.warn("job {} is not existed.", jobId);
            }
        }
    }

    private static void initTimerTask() throws Exception {
        Configuration config = ConfigUtils.getServerConfig();
        List<AbstractTimerTask> timerTasks = ReflectionUtils.getInstancesByConf(config, ServerConigKeys.SERVER_TIMER_TASKS);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(timerTasks.size());
        DateTime now = DateTime.now();
        for (AbstractTimerTask timerTask : timerTasks) {
            long initialDelay = (timerTask.getFirstTime(now).getMillis() - now.getMillis()) / 1000; //单位：秒
            ses.scheduleAtFixedRate(timerTask, initialDelay, timerTask.getPeriod(), TimeUnit.SECONDS);
        }
    }

    private static void initPlan() {
        // 系统启动的时候自动更新执行计划
        PlanService planService = Injectors.getInjector().getInstance(PlanService.class);
        DateTime now = DateTime.now();
        Range<DateTime> range = Range.closedOpen(now, now.plusDays(1).withTimeAtStartOfDay());
        planService.refreshAllPlan(range);
    }
}

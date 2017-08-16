/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月29日 下午4:17:40
 */

package com.mogujie.jarvis.server.scheduler.time;

/**
 * Scheduler used to handle time based jobs.
 *
 * @author guangming
 *
 */
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.eventbus.Subscribe;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.JobSchedulerController;
import com.mogujie.jarvis.server.scheduler.Scheduler;
import com.mogujie.jarvis.server.scheduler.dag.DAGJob;
import com.mogujie.jarvis.server.scheduler.dag.DAGJobType;
import com.mogujie.jarvis.server.scheduler.dag.JobGraph;
import com.mogujie.jarvis.server.scheduler.event.AddPlanEvent;
import com.mogujie.jarvis.server.scheduler.event.AddTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.StartEvent;
import com.mogujie.jarvis.server.scheduler.event.StopEvent;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.util.PlanUtil;

public class TimeScheduler extends Scheduler {

    private static TimeScheduler instance = new TimeScheduler();
    private TimeScheduler() {}
    public static TimeScheduler getInstance() {
        return instance;
    }

    private TimePlan plan = TimePlan.INSTANCE;
    private JobGraph jobGraph = JobGraph.INSTANCE;
    private volatile boolean running = true;
    private JobSchedulerController controller = JobSchedulerController.getInstance();
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);

    private static final Logger LOGGER = LogManager.getLogger();

    class TimeScanThread extends Thread {
        public TimeScanThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                if (running) {
                    DateTime now = DateTime.now();
                    Queue<TimePlanEntry> planQueue = plan.getPlan();
                    while (!planQueue.isEmpty()) {
                        TimePlanEntry entry = planQueue.peek();
                        if (!entry.getDateTime().isAfter(now)) {
                            // 1. start this time based job
                            LOGGER.info("{} time ready", entry);
                            submitJob(entry);
                            // 2. remove this from plan
                            plan.removePlan(entry);
                        } else {
                            break;
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private TimeScanThread scanThread;

    @Override
    public void handleStartEvent(StartEvent event) {
        if (scanThread == null) {
            scanThread = new TimeScanThread("TimeScanThread");
            scanThread.start();
        }
        running = true;
    }

    @Override
    public void handleStopEvent(StopEvent event) {
        running = false;
    }

    @Subscribe
    public void handleAddPlanEvent(AddPlanEvent event) {
        long jobId = event.getJobId();
        long scheduleTime = event.getScheduleTime();
        Map<Long, List<Long>> dependTaskIdMap = event.getDependTaskIdMap();
        LOGGER.info("start handleAddPlanEvent, jobId={}, scheduleTime={}, dependTaskIdMap={}",
                jobId, new DateTime(scheduleTime), dependTaskIdMap);
        TimePlanEntry entry = new TimePlanEntry(jobId, new DateTime(scheduleTime), dependTaskIdMap);
        try {
            if (!plan.addPlan(entry)) {
                LOGGER.error("add plan {} failed.", entry);
            }
        } catch (Throwable e) {
            LOGGER.error("add plan {} failed, {}", entry, e.getMessage());
        }
    }

    private void submitJob(TimePlanEntry entry) {
        long jobId = entry.getJobId();
        DateTime dt = entry.getDateTime();
        Map<Long, List<Long>> dependTaskIdMap = entry.getDependTaskIdMap();
        Job job = jobService.get(jobId).getJob();
        int expiredTime = job.getExpiredTime();
        DateTime expiredDateTime = dt.plusSeconds(expiredTime);
        // 如果该任务没有过期，提交Task给TaskScheduler
        if (expiredDateTime.isAfter(DateTime.now())) {
            AddTaskEvent event = new AddTaskEvent(jobId, dt.getMillis(), dependTaskIdMap);
            controller.notify(event);
        } else {
            LOGGER.warn("{} is expired.", entry);
        }

        // 如果是纯时间任务，并且不是临时任务，自动计算下一次
        DAGJob dagJob = jobGraph.getDAGJob(jobId);
        if (dagJob.getType().equals(DAGJobType.TIME) &&
                job.getStatus() == JobStatus.ENABLE.getValue() &&
                jobService.isActive(jobId) &&
                !job.getIsTemp()) {
            DateTime nextTime = PlanUtil.getScheduleTimeAfter(jobId, dt);
            if (nextTime != null) {
                plan.addPlan(new TimePlanEntry(jobId, nextTime));
            } else {
                LOGGER.warn("next time is null, jobId={}, dateTime={}", jobId, dt);
            }
        }
    }
}


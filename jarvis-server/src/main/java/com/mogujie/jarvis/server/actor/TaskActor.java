/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月23日 上午10:16:53
 */

package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.mybatis.guice.transactional.Transactional;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.domain.JobRelationType;
import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.observer.Event;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.protocol.AlarmProtos.RestServerUpdateTaskAlarmStatusRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerUpdateTaskAlarmStatusResponse;
import com.mogujie.jarvis.protocol.KillTaskProtos.RestServerKillTaskRequest;
import com.mogujie.jarvis.protocol.KillTaskProtos.ServerKillTaskRequest;
import com.mogujie.jarvis.protocol.KillTaskProtos.ServerKillTaskResponse;
import com.mogujie.jarvis.protocol.KillTaskProtos.WorkerKillTaskResponse;
import com.mogujie.jarvis.protocol.ManualRerunTaskProtos.RestServerManualRerunTaskRequest;
import com.mogujie.jarvis.protocol.ManualRerunTaskProtos.ServerManualRerunTaskResponse;
import com.mogujie.jarvis.protocol.ModifyTaskStatusProtos.RestServerModifyTaskStatusRequest;
import com.mogujie.jarvis.protocol.ModifyTaskStatusProtos.ServerModifyTaskStatusResponse;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.RestServerQueryTaskByJobIdRequest;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.ServerQueryTaskByJobIdResponse;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.TaskEntry;
import com.mogujie.jarvis.protocol.QueryTaskProtos.RestQueryTaskCriticalPathRequest;
import com.mogujie.jarvis.protocol.QueryTaskProtos.ServerQueryTaskCriticalPathResponse;
import com.mogujie.jarvis.protocol.QueryTaskRelationProtos.RestServerQueryTaskRelationRequest;
import com.mogujie.jarvis.protocol.QueryTaskRelationProtos.ServerQueryTaskRelationResponse;
import com.mogujie.jarvis.protocol.QueryTaskRelationProtos.TaskMapEntry;
import com.mogujie.jarvis.protocol.QueryTaskStatusProtos.RestServerQueryTaskStatusRequest;
import com.mogujie.jarvis.protocol.QueryTaskStatusProtos.ServerQueryTaskStatusResponse;
import com.mogujie.jarvis.protocol.RemoveTaskProtos.RestServerRemoveTaskRequest;
import com.mogujie.jarvis.protocol.RemoveTaskProtos.ServerRemoveTaskResponse;
import com.mogujie.jarvis.protocol.RetryTaskProtos.RestServerRetryTaskRequest;
import com.mogujie.jarvis.protocol.RetryTaskProtos.ServerRetryTaskResponse;
import com.mogujie.jarvis.protocol.SearchJobProtos.RestSearchTaskStatusRequest;
import com.mogujie.jarvis.protocol.SearchJobProtos.ServerSearchTaskStatusResponse;
import com.mogujie.jarvis.protocol.TaskInfoEntryProtos.TaskInfoEntry;
import com.mogujie.jarvis.server.dispatcher.TaskManager;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.domain.JobDependencyEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.JobSchedulerController;
import com.mogujie.jarvis.server.scheduler.event.FailedEvent;
import com.mogujie.jarvis.server.scheduler.event.ManualRerunTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.RemoveTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.RetryTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.SuccessEvent;
import com.mogujie.jarvis.server.scheduler.event.UnhandleEvent;
import com.mogujie.jarvis.server.scheduler.task.DAGTask;
import com.mogujie.jarvis.server.scheduler.task.TaskGraph;
import com.mogujie.jarvis.server.scheduler.time.TimePlanEntry;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.TaskActorLogService;
import com.mogujie.jarvis.server.service.TaskDependService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.util.FutureUtils;
import com.mogujie.jarvis.server.util.PlanUtil;

/**
 * @author guangming
 */
public class TaskActor extends UntypedActor {
    private static final Logger LOGGER = LogManager.getLogger();
    private TaskManager taskManager = Injectors.getInjector().getInstance(TaskManager.class);
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);
    private TaskDependService taskDependService = Injectors.getInjector().getInstance(TaskDependService.class);
    private TaskGraph taskGraph = TaskGraph.INSTANCE;
    private JobSchedulerController controller = JobSchedulerController.getInstance();
    private TaskActorLogService taskActorLogService = Injectors.getInjector().getInstance(TaskActorLogService.class);

    public static Props props() {
        return Props.create(TaskActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(RestServerKillTaskRequest.class, ServerKillTaskResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerRetryTaskRequest.class, ServerRetryTaskResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerManualRerunTaskRequest.class, ServerManualRerunTaskResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerModifyTaskStatusRequest.class, ServerModifyTaskStatusResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerQueryTaskRelationRequest.class, ServerQueryTaskRelationResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerRemoveTaskRequest.class, ServerRemoveTaskResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerQueryTaskStatusRequest.class, ServerQueryTaskStatusResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestServerQueryTaskByJobIdRequest.class, ServerQueryTaskByJobIdResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestQueryTaskCriticalPathRequest.class, ServerQueryTaskCriticalPathResponse.class, MessageType.GENERAL));
        list.add(new ActorEntry(RestSearchTaskStatusRequest.class, ServerSearchTaskStatusResponse.class, MessageType.GENERAL));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        taskActorLogService.handleLog(obj);
        if (obj instanceof RestServerKillTaskRequest) {
            RestServerKillTaskRequest msg = (RestServerKillTaskRequest) obj;
            killTask(msg);
        } else if (obj instanceof RestServerRetryTaskRequest) {
            RestServerRetryTaskRequest msg = (RestServerRetryTaskRequest) obj;
            retryTask(msg);
        } else if (obj instanceof RestServerManualRerunTaskRequest) {
            RestServerManualRerunTaskRequest msg = (RestServerManualRerunTaskRequest) obj;
            manualRerunTask(msg);
        } else if (obj instanceof RestServerModifyTaskStatusRequest) {
            RestServerModifyTaskStatusRequest msg = (RestServerModifyTaskStatusRequest) obj;
            modifyTaskStatus(msg);
        } else if (obj instanceof RestServerQueryTaskRelationRequest) {
            RestServerQueryTaskRelationRequest msg = (RestServerQueryTaskRelationRequest) obj;
            queryTaskRelation(msg);
        } else if (obj instanceof RestServerQueryTaskStatusRequest) {
            RestServerQueryTaskStatusRequest msg = (RestServerQueryTaskStatusRequest) obj;
            queryTaskStatus(msg);
        } else if (obj instanceof RestServerRemoveTaskRequest) {
            RestServerRemoveTaskRequest msg = (RestServerRemoveTaskRequest) obj;
            removeTask(msg);
        } else if (obj instanceof RestServerQueryTaskByJobIdRequest) {
            RestServerQueryTaskByJobIdRequest msg = (RestServerQueryTaskByJobIdRequest) obj;
            queryTaskByJobId(msg);
        } else if (obj instanceof RestQueryTaskCriticalPathRequest) {
            RestQueryTaskCriticalPathRequest msg = (RestQueryTaskCriticalPathRequest) obj;
            queryCriticalPath(msg);
        } else if (obj instanceof RestSearchTaskStatusRequest) {
            RestSearchTaskStatusRequest msg = (RestSearchTaskStatusRequest) obj;
            searchTaskStatusByDataDate(msg);
        } else if (obj instanceof RestServerUpdateTaskAlarmStatusRequest) {
            RestServerUpdateTaskAlarmStatusRequest msg = (RestServerUpdateTaskAlarmStatusRequest) obj;
            updateTaskAlarmStatus(msg);
        } else {
            unhandled(obj);
        }
    }

    /**
     * kill Task
     *
     * @param msg
     */
    private void killTask(RestServerKillTaskRequest msg) throws Exception {
        ServerKillTaskResponse response;
        try {
            List<Long> taskIds = msg.getTaskIdList();
            LOGGER.info("start killTask taskIds={}", taskIds);
            for (long taskId : taskIds) {
                Task task = taskService.get(taskId);
                if (task == null) {
                    response = ServerKillTaskResponse.newBuilder().setSuccess(false).setMessage("can't find task, taskId=" + taskId).build();
                    getSender().tell(response, getSelf());
                    return;
                }

                String fullId = IdUtils.getFullId(task.getJobId(), taskId, task.getAttemptId());
                WorkerInfo workerInfo = taskManager.getWorkerInfo(fullId);
                if (workerInfo == null) {
                    response = ServerKillTaskResponse.newBuilder().setSuccess(false).setMessage("can't find worker by fullId=" + fullId).build();
                    getSender().tell(response, getSelf());
                    return;
                }

                ActorSelection actorSelection = getContext().actorSelection(workerInfo.getAkkaRootPath() + JarvisConstants.WORKER_AKKA_USER_PATH);
                ServerKillTaskRequest serverRequest = ServerKillTaskRequest.newBuilder().setFullId(fullId).build();
                WorkerKillTaskResponse workerResponse = (WorkerKillTaskResponse) FutureUtils.awaitResult(actorSelection, serverRequest, 30);
                if (!workerResponse.getSuccess()) {
                    response = ServerKillTaskResponse.newBuilder().setSuccess(false).setMessage(workerResponse.getMessage()).build();
                    getSender().tell(response, getSelf());
                    return;
                }
            }
            response = ServerKillTaskResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerKillTaskResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("killTask error", e);
            throw e;
        }
    }

    /**
     * 按照taskId原地重跑
     *
     * @param msg
     */
    private void retryTask(RestServerRetryTaskRequest msg) {
        ServerRetryTaskResponse response;
        try {
            long taskId = msg.getTaskId();
            LOGGER.info("start retryTask taskId:{}", taskId);

            Task task = taskService.get(taskId);
            TaskStatus oldStatus = TaskStatus.parseValue(task.getStatus());

            if (!oldStatus.equals(TaskStatus.FAILED) && !oldStatus.equals(TaskStatus.KILLED)) {
                //
                throw new IllegalArgumentException("Only status FAILED | KILLED could be retried.");
            }

            controller.notify(new RetryTaskEvent(taskId, msg.getUser()));
            response = ServerRetryTaskResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception ex) {
            response = ServerRetryTaskResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(ex)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("retryTask error", ex);
            throw ex;
        }
    }

    /**
     * 根据jobId和起止时间，按照新依赖关系重跑
     *
     * @param msg
     */
    @Transactional
    private void manualRerunTask(RestServerManualRerunTaskRequest msg) {
        ServerManualRerunTaskResponse response;
        String user = msg.getUser();
        try {
            List<Long> jobIdList = msg.getJobIdList();
            List<Long> taskIdList = new ArrayList<Long>();
            DateTime startDate = new DateTime(msg.getStartTime());
            DateTime endDate = new DateTime(msg.getEndTime());
            LOGGER.info("start manualRerunTask, jobIdList={}, startDate={}, endDate={}", jobIdList, startDate, endDate);
            // 1.生成所有任务的执行计划
            Range<DateTime> range = Range.closed(startDate, endDate);
            Map<Long, List<TimePlanEntry>> planMap = PlanUtil.getReschedulePlan(jobIdList, range);
            // 2.生成新的task
            long scheduleTime = DateTime.now().getMillis();
            for (long jobId : jobIdList) {
                List<TimePlanEntry> planList = planMap.get(jobId);
                for (TimePlanEntry planEntry : planList) {
                    // create new task
                    long dataTime = planEntry.getDateTime().getMillis();
                    long taskId = taskService.createTaskByJobId(jobId, scheduleTime, dataTime, TaskType.RERUN, user);
                    planEntry.setTaskId(taskId);
                    taskIdList.add(taskId);
                }
            }
            // 3.确定task依赖关系，添加DAGTask到TaskGraph中
            for (long jobId : jobIdList) {
                List<TimePlanEntry> planList = planMap.get(jobId);
                for (int i = 0; i < planList.size(); i++) {
                    TimePlanEntry planEntry = planList.get(i);
                    long taskId = planEntry.getTaskId();
                    long dataTime = planEntry.getDateTime().getMillis();
                    Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();
                    Map<Long, JobDependencyEntry> dependencyMap = jobService.get(jobId).getDependencies();
                    if (dependencyMap != null) {
                        for (Entry<Long, JobDependencyEntry> entry : dependencyMap.entrySet()) {
                            long preJobId = entry.getKey();
                            if (jobIdList.contains(preJobId)) {
                                JobDependencyEntry dependencyEntry = entry.getValue();
                                DependencyExpression dependencyExpression = dependencyEntry.getDependencyExpression();
                                List<Long> dependTaskIds = getDependTaskIds(planMap.get(preJobId), dataTime, dependencyExpression);
                                dependTaskIdMap.put(preJobId, dependTaskIds);
                            }
                        }
                    }
                    //如果是串行任务
                    if (jobService.get(jobId).getJob().getIsSerial()) {
                        if (i > 0) {
                            // 增加自依赖
                            long preTaskId = planList.get(i - 1).getTaskId();
                            List<Long> dependTaskIds = Lists.newArrayList(preTaskId);
                            dependTaskIdMap.put(jobId, dependTaskIds);
                        }
                    }
                    // add to taskGraph
                    DAGTask dagTask = new DAGTask(jobId, taskId, scheduleTime, dataTime, dependTaskIdMap);
                    taskGraph.addTask(taskId, dagTask);
                }
            }
            // 4.添加依赖关系
            for (long taskId : taskIdList) {
                DAGTask dagTask = taskGraph.getTask(taskId);
                List<Long> dependTaskIds = dagTask.getDependTaskIds();
                for (long parentId : dependTaskIds) {
                    taskGraph.addDependency(parentId, taskId);
                }
            }
            // 5. 重跑任务
            controller.notify(new ManualRerunTaskEvent(taskIdList));

            response = ServerManualRerunTaskResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerManualRerunTaskResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("manualRerunTask error", e);
            throw e;
        }
    }

    private List<Long> getDependTaskIds(List<TimePlanEntry> planList, long dataTime, DependencyExpression dependencyExpression) {
        List<Long> dependTaskIds = new ArrayList<Long>();
        if (dependencyExpression == null) {
            for (TimePlanEntry entry : planList) {
                if (entry.getDateTime().getMillis() == dataTime) {
                    dependTaskIds.add(entry.getTaskId());
                    break;
                }
            }
        } else {
            Range<DateTime> range = dependencyExpression.getRange(new DateTime(dataTime));
            for (TimePlanEntry entry : planList) {
                if (range.contains(entry.getDateTime())) {
                    dependTaskIds.add(entry.getTaskId());
                }
            }
        }
        return dependTaskIds;
    }

    /**
     * 强制修改task状态（非管理员禁止使用！！）
     *
     * @param msg
     */
    private void modifyTaskStatus(RestServerModifyTaskStatusRequest msg) {
        ServerModifyTaskStatusResponse response;
        try {
            long taskId = msg.getTaskId();
            TaskStatus status = TaskStatus.parseValue(msg.getStatus());
            LOGGER.info("start modifyTaskStatus, taskId={}, status={}", taskId, status);
            Event event = new UnhandleEvent();
            String reason = "Manual modify task status.";
            if (status.equals(TaskStatus.SUCCESS)) {
                Task task = taskService.get(taskId);
                event = new SuccessEvent(task.getJobId(), taskId, task.getScheduleTime().getTime(), TaskType.parseValue(task.getType()), reason);
            } else if (status.equals(TaskStatus.FAILED)) {
                event = new FailedEvent(taskId, reason);
            }
            // handle success/failed event
            JobSchedulerController schedulerController = JobSchedulerController.getInstance();
            schedulerController.notify(event);

            response = ServerModifyTaskStatusResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerModifyTaskStatusResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("modifyTaskStatus error", e);
            throw e;
        }
    }

    /**
     * 查询task的依赖关系
     *
     * @param msg
     */
    private void queryTaskRelation(RestServerQueryTaskRelationRequest msg) throws Exception {
        ServerQueryTaskRelationResponse response;
        try {
            long taskId = msg.getTaskId();
            LOGGER.debug("start queryTaskRelation, taskId={}", taskId);
            ServerQueryTaskRelationResponse.Builder builder = ServerQueryTaskRelationResponse.newBuilder();
            Map<Long, List<Long>> taskRelationMap;
            if (msg.getRelationType() == JobRelationType.PARENT.getValue()) {
                taskRelationMap = taskDependService.loadParent(taskId);
            } else {
                DAGTask dagTask = taskGraph.getTask(taskId);
                if (dagTask != null) {
                    List<DAGTask> childDagTasks = taskGraph.getChildren(taskId);
                    taskRelationMap = TaskGraph.convert2TaskMap(childDagTasks);
                } else {
                    taskRelationMap = taskDependService.loadChild(taskId);
                }
            }
            for (Entry<Long, List<Long>> entry : taskRelationMap.entrySet()) {
                long jobId = entry.getKey();
                List<Long> taskList = entry.getValue();
                TaskMapEntry taskMapEntry = TaskMapEntry.newBuilder().setJobId(jobId).addAllTaskId(taskList).build();
                builder.addTaskRelationMap(taskMapEntry);
            }
            response = builder.setSuccess(true).build();
            getSender().tell(response, getSelf());

        } catch (Exception e) {
            response = ServerQueryTaskRelationResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            throw e;
        }
    }

    /**
     * 查询task的状态
     *
     * @param msg
     */
    private void queryTaskStatus(RestServerQueryTaskStatusRequest msg) throws Exception {
        ServerQueryTaskStatusResponse response;
        try {
            long taskId = msg.getTaskId();
            LOGGER.debug("start queryTaskStatus, taskId={}", taskId);
            Task task = taskService.get(taskId);
            if (task != null) {
                response = ServerQueryTaskStatusResponse.newBuilder().setSuccess(true).setStatus(task.getStatus()).build();
            } else {
                LOGGER.error("task {} is not existed!", taskId);
                response = ServerQueryTaskStatusResponse.newBuilder().setSuccess(false).setMessage("task " + taskId + " is not existed!").build();
            }
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerQueryTaskStatusResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            throw e;
        }
    }

    /**
     * 删除task
     *
     * @param msg
     */
    private void removeTask(RestServerRemoveTaskRequest msg) throws Exception {
        ServerRemoveTaskResponse response;
        try {
            List<Long> taskIds = msg.getTaskIdList();
            LOGGER.debug("start removeTask, taskIds={}", taskIds);
            RemoveTaskEvent removeTaskEvent = new RemoveTaskEvent(taskIds);
            controller.notify(removeTaskEvent);
            response = ServerRemoveTaskResponse.newBuilder().setSuccess(true).setMessage("task has been removed").build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerRemoveTaskResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            throw e;
        }
    }

    /**
     * 查询task
     *
     * @param msg
     */
    private void queryTaskByJobId(RestServerQueryTaskByJobIdRequest msg) throws Exception {
        ServerQueryTaskByJobIdResponse response;
        try {
            long jobId = msg.getJobId();
            LOGGER.debug("start queryTaskByJobId, jobId={}", jobId);
            ServerQueryTaskByJobIdResponse.Builder builder = ServerQueryTaskByJobIdResponse.newBuilder();
            List<Task> tasks = taskService.getTasksByJobId(jobId);
            if (tasks != null) {
                for (Task task : tasks) {
                    TaskEntry taskEntry = TaskEntry.newBuilder().setTaskId(task.getTaskId()).setAttemptId(task.getAttemptId()).build();
                    builder.addTaskEntry(taskEntry);
                }
            }
            response = builder.setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerQueryTaskByJobIdResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            throw e;
        }
    }

    /**
     * 查询关键路径
     *
     * @param msg
     */
    private void queryCriticalPath(RestQueryTaskCriticalPathRequest msg) throws Exception {
        long scheduleDate = msg.getDateTime();
        String jobName = msg.getJobName();
        ServerQueryTaskCriticalPathResponse response;
        try {
            // find task
            Task task = taskService.getTaskByScheduleDateAndJobName(scheduleDate, jobName);
            if (task != null) {
                // get critical path
                List<TaskInfoEntry> taskInfoEntries = new ArrayList<TaskInfoEntry>();
                taskInfoEntries.add(convertTask2TaskInfoEntry(task));
                Map<Long, List<Long>> parentTaskIdMap = taskDependService.loadParent(task.getTaskId());
                while (parentTaskIdMap != null && !parentTaskIdMap.isEmpty()) {
                    long maxEndTime = 0;
                    long maxEndTaskId = 0;
                    for (Entry<Long, List<Long>> entry : parentTaskIdMap.entrySet()) {
                        List<Long> tasks = entry.getValue();
                        for (long taskId : tasks) {
                            if (taskService.get(taskId) != null && taskService.get(taskId).getExecuteEndTime().getTime() > maxEndTime) {
                                maxEndTime = taskService.get(taskId).getExecuteEndTime().getTime();
                                maxEndTaskId = taskId;
                            }
                        }
                    }
                    Task maxEndTask = taskService.get(maxEndTaskId);
                    if (maxEndTask != null) {
                        taskInfoEntries.add(convertTask2TaskInfoEntry(maxEndTask));
                    }
                    parentTaskIdMap = taskDependService.loadParent(maxEndTaskId);
                }
                response = ServerQueryTaskCriticalPathResponse.newBuilder().setSuccess(true).addAllTaskInfo(taskInfoEntries).build();
                getSender().tell(response, getSelf());
            } else {
                String errMsg = "can't find task, scheduleDate=" + new DateTime(scheduleDate).toString("yyyy-MM-dd hh:mm:ss") + ", jobName="
                        + jobName;
                response = ServerQueryTaskCriticalPathResponse.newBuilder().setSuccess(false).setMessage(errMsg).build();
                getSender().tell(response, getSelf());
                LOGGER.error(errMsg);
            }
        } catch (Exception e) {
            response = ServerQueryTaskCriticalPathResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", e);
            throw e;
        }
    }

    private void searchTaskStatusByDataDate(RestSearchTaskStatusRequest msg) throws Exception {
        long jobId = msg.getJobId();
        long dataDate = msg.getDataDate();
        ServerSearchTaskStatusResponse response;
        try {
            List<Task> tasks = taskService.getTasksByJobIdAndDataDate(jobId, dataDate);
            if (tasks == null || tasks.isEmpty()) {
                response = ServerSearchTaskStatusResponse.newBuilder().setSuccess(false)
                        .setMessage("can't find task by jobId=" + jobId + ", dataDate=" + new DateTime(dataDate)).build();
            } else if (tasks.size() > 1) {
                response = ServerSearchTaskStatusResponse.newBuilder().setSuccess(false)
                        .setMessage("find more than 1 tasks by jobId=" + jobId + ", dataDate=" + new DateTime(dataDate)).build();
            } else {
                response = ServerSearchTaskStatusResponse.newBuilder().setSuccess(true).setStatus(tasks.get(0).getStatus()).build();
            }
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerSearchTaskStatusResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", e);
            throw e;
        }
    }

    private TaskInfoEntry convertTask2TaskInfoEntry(Task task) {
        TaskInfoEntry taskInfoEntry = TaskInfoEntry.newBuilder().setTaskId(task.getTaskId()).setJobId(task.getJobId())
                .setJobName(jobService.get(task.getJobId()).getJob().getJobName()).setScheduleTime(task.getScheduleTime().getTime())
                .setDataTime(task.getDataTime().getTime()).setStartTime(task.getExecuteStartTime().getTime())
                .setEndTime(task.getExecuteEndTime().getTime()).setAvgTime(taskService.getAvgExecTime(task.getJobId(), 30))
                .setUseTime(((float) (task.getExecuteEndTime().getTime() - task.getExecuteStartTime().getTime())) / 1000 / 60) //单位分钟
                .setStatus(task.getStatus()).build();
        return taskInfoEntry;
    }

    private void updateTaskAlarmStatus(RestServerUpdateTaskAlarmStatusRequest request) {
        int status = request.getAlarmStatus();
        List<Long> taskIds = request.getTaskIdList();
        ServerUpdateTaskAlarmStatusResponse response = null;
        try {
            taskService.updateAlarmStatus(taskIds, status);
            response = ServerUpdateTaskAlarmStatusResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());
        } catch (Exception e) {
            response = ServerUpdateTaskAlarmStatusResponse.newBuilder().setSuccess(false).setMessage(ExceptionUtil.getErrMsg(e)).build();
            getSender().tell(response, getSelf());
            LOGGER.error("", e);
            throw e;
        }
    }

}
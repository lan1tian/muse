/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 上午10:27:40
 */
package com.mogujie.jarvis.server.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.domain.IdType;
import com.mogujie.jarvis.core.domain.MessageType;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.observer.Event;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.protocol.ReportTaskProtos.ServerReportTaskProgressResponse;
import com.mogujie.jarvis.protocol.ReportTaskProtos.ServerReportTaskStatusResponse;
import com.mogujie.jarvis.protocol.ReportTaskProtos.ServerReportTaskUpdateResponse;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskProgressRequest;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskStatusRequest;
import com.mogujie.jarvis.protocol.ReportTaskProtos.WorkerReportTaskUpdateRequest;
import com.mogujie.jarvis.server.domain.ActorEntry;
import com.mogujie.jarvis.server.domain.JobEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.JobSchedulerController;
import com.mogujie.jarvis.server.scheduler.event.FailedEvent;
import com.mogujie.jarvis.server.scheduler.event.KilledEvent;
import com.mogujie.jarvis.server.scheduler.event.RunningEvent;
import com.mogujie.jarvis.server.scheduler.event.SuccessEvent;
import com.mogujie.jarvis.server.scheduler.event.UnhandleEvent;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.TaskHistoryService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.service.WorkerService;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Actor used to receive task metrics information (e.g. status, process)
 *
 * @author guangming
 *
 */
public class TaskMetricsActor extends UntypedActor {
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
    private TaskHistoryService taskHistoryService = Injectors.getInjector().getInstance(TaskHistoryService.class);
    private WorkerService workerService = Injectors.getInjector().getInstance(WorkerService.class);
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);

    private JobSchedulerController schedulerController = JobSchedulerController.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(TaskMetricsActor.class);
    }

    public static List<ActorEntry> handledMessages() {
        List<ActorEntry> list = new ArrayList<>();
        list.add(new ActorEntry(WorkerReportTaskStatusRequest.class, ServerReportTaskStatusResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(WorkerReportTaskProgressRequest.class, ServerReportTaskProgressResponse.class, MessageType.SYSTEM));
        list.add(new ActorEntry(WorkerReportTaskUpdateRequest.class, ServerReportTaskUpdateResponse.class, MessageType.SYSTEM));
        return list;
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof WorkerReportTaskStatusRequest) {
            WorkerReportTaskStatusRequest msg = (WorkerReportTaskStatusRequest) obj;
            String fullId = msg.getFullId();
            long jobId = IdUtils.parse(fullId, IdType.JOB_ID);
            long taskId = IdUtils.parse(fullId, IdType.TASK_ID);
            TaskStatus status = TaskStatus.parseValue(msg.getStatus());
            LOGGER.info("receive WorkerReportTaskStatusRequest [taskId={},status={}]", taskId, status);

            ServerReportTaskStatusResponse response = ServerReportTaskStatusResponse.newBuilder().setSuccess(true).build();
            getSender().tell(response, getSelf());

            Event event = new UnhandleEvent();
            Address address = getSender().path().address();
            String ip = address.host().get();
            int port = Integer.parseInt(address.port().get().toString());
            Task task = taskService.get(taskId);
            TaskType taskType = TaskType.parseValue(task.getType());
            if (status.equals(TaskStatus.SUCCESS)) {
                String reason = "worker [" + ip + ":" + port + "] report success status.";
                event = new SuccessEvent(jobId, taskId, task.getScheduleTime().getTime(), taskType, reason);
            } else if (status.equals(TaskStatus.FAILED)) {
                String reason = "worker [" + ip + ":" + port + "] report failed status.";
                event = new FailedEvent(jobId, taskId, reason);
            } else if (status.equals(TaskStatus.RUNNING)) {
                int workerId = workerService.getWorkerId(ip, port);
                event = new RunningEvent(jobId, taskId, workerId);
            } else if (status.equals(TaskStatus.KILLED)) {
                event = new KilledEvent(jobId, taskId);
            }
            schedulerController.notify(event);

            // http callback
            JobEntry jobEntry = jobService.get(jobId);
            if (jobEntry != null) {
                String params = jobEntry.getJob().getParams();
                Map<String, Object> map = JsonHelper.fromJson2JobParams(params);
                if (map != null && map.containsKey(JarvisConstants.HTTP_CALLBACK_URL)) {
                    String url = map.get(JarvisConstants.HTTP_CALLBACK_URL).toString();
                    Map<String, Object> postFields = Maps.newHashMap();
                    postFields.put("jobId", jobId);
                    postFields.put("taskId", taskId);
                    postFields.put("status", status);
                    postFields.put("url", url);

                    ActorRef httpCallbackActor = getContext().actorOf(HttpCallbackActor.props());
                    httpCallbackActor.tell(postFields, getSelf());
                }
            }
        } else if (obj instanceof WorkerReportTaskProgressRequest) {
            WorkerReportTaskProgressRequest msg = (WorkerReportTaskProgressRequest) obj;
            String fullId = msg.getFullId();
            long taskId = IdUtils.parse(fullId, IdType.TASK_ID);
            int attemptId = (int) IdUtils.parse(fullId, IdType.ATTEMPT_ID);
            float progress = msg.getProgress();
            LOGGER.info("receive WorkerReportTaskProgressRequest [taskId={},progress={}]", taskId, progress);
            taskService.updateProgress(taskId, progress);
            taskHistoryService.updateProgress(taskId, attemptId, progress);
        } else if (obj instanceof WorkerReportTaskUpdateRequest) {
            WorkerReportTaskUpdateRequest msg = (WorkerReportTaskUpdateRequest) obj;
            String fullId = msg.getFullId();
            long taskId = IdUtils.parse(fullId, IdType.TASK_ID);
            LOGGER.info("receive WorkerReportTaskUpdateRequest [taskId={}]", taskId);
            Task task = new Task();
            task.setTaskId(taskId);
            task.setContent(msg.getContent());
            task.setExecuteUser(msg.getUser());
            task.setUpdateTime(DateTime.now().toDate());
            taskService.updateSelective(task);
        } else {
            unhandled(obj);
        }
    }

}

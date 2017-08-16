/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月7日 下午1:20:15
 */

package com.mogujie.jarvis.worker.actor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Queues;
import com.mogujie.jarvis.core.AbstractLogCollector;
import com.mogujie.jarvis.core.AbstractTask;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.TaskContext.TaskContextBuilder;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.domain.TaskDetail.TaskDetailBuilder;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.protocol.KillTaskProtos.ServerKillTaskRequest;
import com.mogujie.jarvis.protocol.KillTaskProtos.WorkerKillTaskResponse;
import com.mogujie.jarvis.protocol.MapEntryProtos.MapEntry;
import com.mogujie.jarvis.protocol.SubmitTaskProtos.ServerSubmitTaskRequest;
import com.mogujie.jarvis.worker.TaskPool;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.executor.DefaultLogCollector;
import com.mogujie.jarvis.worker.executor.DefaultProgressReporter;
import com.mogujie.jarvis.worker.executor.TaskExecutor;
import com.mogujie.jarvis.worker.executor.TaskUpdateReporter;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class TaskActor extends UntypedActor {

    private TaskPool taskPool = TaskPool.INSTANCE;

    private static Configuration workerConfig = ConfigUtils.getWorkerConfig();
    private static int corePoolSize = workerConfig.getInt(WorkerConfigKeys.WORKER_EXECUTOR_POOL_CORE_SIZE, 5);
    private static int maximumPoolSize = workerConfig.getInt(WorkerConfigKeys.WORKER_EXECUTOR_POOL_MAXIMUM_SIZE, 20);
    private static int keepAliveTime = workerConfig.getInt(WorkerConfigKeys.WORKER_EXECUTOR_POOL_KEEP_ALIVE_SECONDS, 3600);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
            Queues.newArrayBlockingQueue(corePoolSize));
    private static final String SERVER_AKKA_PATH = ConfigUtils.getWorkerConfig().getString(WorkerConfigKeys.SERVER_AKKA_PATH)
            + JarvisConstants.SERVER_AKKA_USER_PATH;
    private static final String LOGSTORAGE_AKKA_PATH = ConfigUtils.getWorkerConfig().getString(WorkerConfigKeys.LOGSTORAGE_AKKA_PATH)
            + JarvisConstants.LOGSTORAGE_AKKA_USER_PATH;

    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(TaskActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof ServerSubmitTaskRequest) {
            ServerSubmitTaskRequest request = (ServerSubmitTaskRequest) obj;
            submitTask(request);
        } else if (obj instanceof ServerKillTaskRequest) {
            ServerKillTaskRequest request = (ServerKillTaskRequest) obj;
            WorkerKillTaskResponse response = killTask(request);
            getSender().tell(response, getSelf());
        } else {
            unhandled(obj);
        }
    }

    private void submitTask(ServerSubmitTaskRequest request) {
        String fullId = request.getFullId();
        String jobType = request.getJobType();
        LOGGER.info("receive ServerSubmitTaskRequest[fullId={},jobType={}]", fullId, jobType);
        TaskDetailBuilder taskBuilder = TaskDetail.newTaskDetailBuilder();
        taskBuilder.setFullId(fullId);
        taskBuilder.setTaskName(request.getTaskName());
        taskBuilder.setAppName(request.getAppName());
        taskBuilder.setUser(request.getUser());
        taskBuilder.setJobType(jobType);
        taskBuilder.setContent(request.getContent());
        taskBuilder.setPriority(request.getPriority());
        taskBuilder.setDataTime(new DateTime(request.getDataTime()));

        Map<String, Object> map = new HashMap<>();
        List<MapEntry> parameters = request.getParametersList();
        for (int i = 0, len = parameters.size(); i < len; i++) {
            MapEntry entry = parameters.get(i);
            map.put(entry.getKey(), entry.getValue());
        }
        taskBuilder.setParameters(map);

        TaskContextBuilder contextBuilder = TaskContext.newBuilder();
        TaskDetail taskDetail = taskBuilder.build();
        contextBuilder.setTaskDetail(taskDetail);

        ActorSelection logActor = getContext().actorSelection(LOGSTORAGE_AKKA_PATH);
        AbstractLogCollector logCollector = new DefaultLogCollector(logActor, fullId);
        contextBuilder.setLogCollector(logCollector);

        ActorSelection serverActor = getContext().actorSelection(SERVER_AKKA_PATH);
        contextBuilder.setProgressReporter(new DefaultProgressReporter(serverActor, getSelf(), fullId));
        contextBuilder.setTaskReporter(new TaskUpdateReporter(serverActor, getSelf()));

        threadPoolExecutor.execute(new TaskExecutor(contextBuilder.build(), getSelf(), getSender(), serverActor));
    }

    private WorkerKillTaskResponse killTask(ServerKillTaskRequest request) {
        String fullId = request.getFullId();
        LOGGER.info("receive ServerKillTaskRequest[fullId={}]", fullId);
        AbstractTask task = taskPool.get(fullId);
        if (task != null) {
            taskPool.remove(fullId);
            try {
                boolean result = task.kill();
                if (result) {
                    taskPool.markTaskKilled(fullId);
                }
                return WorkerKillTaskResponse.newBuilder().setSuccess(result).build();
            } catch (TaskException e) {
                return WorkerKillTaskResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build();
            }
        }

        return WorkerKillTaskResponse.newBuilder().setSuccess(true).build();
    }

}
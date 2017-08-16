/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月22日 下午2:45:50
 */

package com.mogujie.jarvis.server.dispatcher;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.Pair;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.dto.generate.App;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.dto.generate.Worker;
import com.mogujie.jarvis.server.ServerConigKeys;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.AppService;
import com.mogujie.jarvis.server.service.TaskService;
import com.mogujie.jarvis.server.service.WorkerService;

@Singleton
public class TaskManager {

    private Configuration serverConfig = ConfigUtils.getServerConfig();
    private AppService appService = Injectors.getInjector().getInstance(AppService.class);
    private WorkerService workerService = Injectors.getInjector().getInstance(WorkerService.class);
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);

    private Map<String, Pair<WorkerInfo, Integer>> taskMap = Maps.newHashMap();
    private Map<Integer, Integer> maxParallelismMap = Maps.newHashMap();
    private AtomicLongMap<Integer> parallelismCounter = AtomicLongMap.create();
    private static final Logger LOGGER = LogManager.getLogger();

    @Inject
    private void init() {
        LOGGER.debug("init task manager");
        List<App> list = appService.getAppList();
        for (App app : list) {
            maxParallelismMap.put(app.getAppId(), app.getMaxConcurrency());
        }

        List<Task> tasks = taskService.getTasksByStatus(Lists.newArrayList(TaskStatus.RUNNING.getValue()));
        for (Task task : tasks) {
            Worker worker = workerService.getWorkerById(task.getWorkerId());
            if (worker != null) {
                String fullId = IdUtils.getFullId(task.getJobId(), task.getTaskId(), task.getAttemptId());
                WorkerInfo workerInfo = new WorkerInfo(worker.getIp(), worker.getPort());
                taskMap.put(fullId, new Pair<WorkerInfo, Integer>(workerInfo, task.getAppId()));
            }
        }
    }

    public void addApp(int appId, int maxParallelism) {
        maxParallelismMap.put(appId, maxParallelism);
        LOGGER.info("add application: id[{}], parallelism[{}].", appId, maxParallelism);
    }

    public synchronized boolean addTask(String fullId, WorkerInfo workerInfo, int appId) {
        boolean parallelismLimitEnable = serverConfig.getBoolean(ServerConigKeys.APP_MAX_PARALLELISM_LIMIT_ENABLE, false);
        if (parallelismLimitEnable && parallelismCounter.get(appId) >= maxParallelismMap.get(appId)) {
            return false;
        }

        parallelismCounter.getAndIncrement(appId);
        LOGGER.info("add task num, appId={}, num={}", appId, parallelismCounter.get(appId));

        taskMap.put(fullId, new Pair<>(workerInfo, appId));

        return true;
    }

    public synchronized WorkerInfo getWorkerInfo(String fullId) {
        Pair<WorkerInfo, Integer> pair = taskMap.get(fullId);
        if (pair != null) {
            return pair.getFirst();
        }
        return null;
    }

    public synchronized boolean contains(String fullId) {
        return taskMap.containsKey(fullId);
    }

    public void removeTask(String fullId, int appId) {
        if (parallelismCounter.get(appId) > 0) {
            parallelismCounter.getAndDecrement(appId);
            LOGGER.info("reduce task num, appId={}, num={}", appId, parallelismCounter.get(appId));
        }

        taskMap.remove(fullId);
    }

    public void updateAppMaxParallelism(int appId, int maxParallelism) {
        maxParallelismMap.put(appId, maxParallelism);
    }

    public Map<Integer, Long> getAppCounter() {
        return parallelismCounter.asMap();
    }
}

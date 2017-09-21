/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月24日 下午4:24:40
 */

package com.mogujie.jarvis.server.scheduler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.AtomicLongMap;
import com.mogujie.jarvis.core.domain.IdType;
import com.mogujie.jarvis.core.domain.Pair;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.observer.Event;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.core.util.ThreadUtils;
import com.mogujie.jarvis.server.ServerConigKeys;
import com.mogujie.jarvis.server.dispatcher.PriorityTaskQueue;
import com.mogujie.jarvis.server.dispatcher.TaskManager;
import com.mogujie.jarvis.server.domain.RetryType;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.event.FailedEvent;
import com.mogujie.jarvis.server.service.AppService;

import akka.japi.tuple.Tuple3;

/**
 * Task Retry Scheduler
 *
 */
public enum TaskRetryScheduler {
    INSTANCE;

    private volatile boolean running;
    private AppService appService = Injectors.getInjector().getInstance(AppService.class);
    private TaskManager taskManager = Injectors.getInjector().getInstance(TaskManager.class);
    private PriorityTaskQueue taskQueue = Injectors.getInjector().getInstance(PriorityTaskQueue.class);
    private Map<Pair<String, RetryType>, TaskDetail> taskMap = Maps.newConcurrentMap();
    private AtomicLongMap<String> taskFailedRetryCounter = AtomicLongMap.create();
    private int rejectRetryInterval = ConfigUtils.getServerConfig().getInt(ServerConigKeys.TASK_REJECT_INTERVAL, 10);
    private int autoRetryInterval = ConfigUtils.getServerConfig().getInt(ServerConigKeys.TASK_AUTO_INTERVAL, 5);
    private BlockingQueue<Tuple3<String, RetryType, DateTime>> tasks = Queues.newLinkedBlockingQueue(100);
    private JobSchedulerController schedulerController = JobSchedulerController.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();

    public void start() {
        running = true;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new TaskRetryThread());
        executorService.shutdown();
        LOGGER.info("Task retry scheduler started.");
    }

    public void addTask(TaskDetail taskDetail, RetryType retryType) {
        String jobIdWithTaskId = taskDetail.getFullId().replaceAll("_\\d+$", "");

        LOGGER.info("add task to retry, jobIdWithTaskId: {}", jobIdWithTaskId);

        DateTime expiredDateTime = null;
        if (retryType == RetryType.FAILED_RETRY) {
            expiredDateTime = DateTime.now().plusSeconds(taskDetail.getFailedInterval());
            taskMap.putIfAbsent(new Pair<String, RetryType>(jobIdWithTaskId, retryType), taskDetail);
        } else if (retryType == RetryType.REJECT_RETRY) {
            expiredDateTime = DateTime.now().plusSeconds(rejectRetryInterval);
            taskMap.putIfAbsent(new Pair<String, RetryType>(jobIdWithTaskId, retryType), taskDetail);
        } else {
            expiredDateTime = DateTime.now().plusSeconds(autoRetryInterval);
            taskMap.putIfAbsent(new Pair<String, RetryType>(jobIdWithTaskId, retryType), taskDetail);
        }

        try {
            tasks.put(new Tuple3<String, RetryType, DateTime>(jobIdWithTaskId, retryType, expiredDateTime));
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }

    public void remove(String jobIdWithTaskId, RetryType retryType) {
        LOGGER.debug("remove retry task, jobIdWithTaskId: {}", jobIdWithTaskId);
        Pair<String, RetryType> taskKey = new Pair<String, RetryType>(jobIdWithTaskId, retryType);
        taskMap.remove(taskKey);
        taskFailedRetryCounter.remove(jobIdWithTaskId);
        Iterator<Tuple3<String, RetryType, DateTime>> it = tasks.iterator();
        while (it.hasNext()) {
            Tuple3<String, RetryType, DateTime> tuple3 = it.next();
            if (tuple3.t1().equals(jobIdWithTaskId) && tuple3.t2() == retryType) {
                it.remove();
            }
        }
    }

    public void shutdown() {
        running = false;
        LOGGER.info("Task retry scheduler shutdown");
    }

    class TaskRetryThread extends Thread {

        @Override
        public void run() {
            while (running) {
                try {
                    DateTime now = DateTime.now();
                    Iterator<Tuple3<String, RetryType, DateTime>> it = tasks.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, RetryType, DateTime> taskKey = it.next();
                        if (taskKey.t3().isBefore(now)) {
                            String jobIdWithTaskId = taskKey.t1();
                            Pair<String, RetryType> pair = new Pair<String, RetryType>(jobIdWithTaskId, taskKey.t2());
                            TaskDetail taskDetail = taskMap.get(pair);
                            if (taskDetail == null) {
                                it.remove();
                                continue;
                            }

                            int appId = appService.getAppIdByName(taskDetail.getAppName());
                            if (taskKey.t2() == RetryType.FAILED_RETRY) {
                                int retries = taskDetail.getFailedRetries();
                                if (taskFailedRetryCounter.get(jobIdWithTaskId) >= retries) {
                                    taskMap.remove(pair);
                                    taskFailedRetryCounter.remove(jobIdWithTaskId);
                                    long jobId = IdUtils.parse(taskDetail.getFullId(), IdType.JOB_ID);
                                    long taskId = IdUtils.parse(taskDetail.getFullId(), IdType.TASK_ID);
                                    Event event = new FailedEvent(jobId, taskId, "failed retry");
                                    schedulerController.notify(event);
                                } else {
                                    taskManager.removeTask(taskDetail.getFullId(), appId);
                                    taskQueue.put(taskDetail);
                                    taskFailedRetryCounter.getAndIncrement(jobIdWithTaskId);
                                }
                            } else if (taskKey.t2() == RetryType.REJECT_RETRY) {
                                int expiredTime = taskDetail.getExpiredTime();
                                if (expiredTime > 0) {
                                    if (Seconds.secondsBetween(taskDetail.getScheduleTime(), DateTime.now()).getSeconds() > expiredTime) {
                                        taskMap.remove(pair);
                                        long jobId = IdUtils.parse(taskDetail.getFullId(), IdType.JOB_ID);
                                        long taskId = IdUtils.parse(taskDetail.getFullId(), IdType.TASK_ID);
                                        Event event = new FailedEvent(jobId, taskId, "task expired");
                                        schedulerController.notify(event);
                                    } else {
                                        taskManager.removeTask(taskDetail.getFullId(), appId);
                                        taskQueue.put(taskDetail);
                                    }
                                } else {
                                    taskManager.removeTask(taskDetail.getFullId(), appId);
                                    taskQueue.put(taskDetail);
                                }
                            } else {
                                taskManager.removeTask(taskDetail.getFullId(), appId);
                                taskQueue.put(taskDetail);
                            }
                            it.remove();
                        } else {
                            continue;
                        }
                    }

                    ThreadUtils.sleep(1000);
                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }

            LOGGER.info("TaskRetryThread exit");
        }

    }
}

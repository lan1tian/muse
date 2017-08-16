/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月15日 下午3:46:13
 */

package com.mogujie.jarvis.worker;

import com.mogujie.jarvis.worker.status.store.LocalFileSystemStateStore;

public interface WorkerConfigKeys {

    public static final String SERVER_AKKA_PATH = "server.akka.path";
    public static final String WORKER_GROUP_ID = "worker.group.id";
    public static final String WORKER_KEY = "worker.key";
    public static final String WORKER_ACTORS_NUM = "worker.actors.num";
    public static final String WORKER_HEART_BEAT_INTERVAL_SECONDS = "worker.heart.beat.interval.seconds";
    public static final String LOGSTORAGE_AKKA_PATH = "logstorage.akka.path";
    public static final String WORKER_REGISTRY_FAILED_INTERVAL = "worker.registry.failed.interval";
    public static final String WORKER_EXECUTOR_POOL_CORE_SIZE = "worker.executor.pool.core.size";
    public static final String WORKER_EXECUTOR_POOL_MAXIMUM_SIZE = "worker.executor.pool.maximum.size";
    public static final String WORKER_EXECUTOR_POOL_KEEP_ALIVE_SECONDS = "worker.executor.pool.keep.alive.seconds";

    public static final String YARN_MEMORY_USAGE_THRESHOLD = "yarn.memory.usage.threshold";
    public static final String YARN_VCORES_USAGE_THRESHOLD = "yarn.vcores.usage.threshold";
    public static final String YARN_RESOUCEMANAGER_REST_API_URIS = "yarn.resoucemanager.rest.api.uris";
    public static final String WORKER_CPU_USAGE_THRESHOLD = "worker.cpu.usage.threshold";
    public static final String WORKER_CPU_LOAD_AVG_THRESHOLD = "worker.cpu.load.avg.threshold";
    public static final String WORKER_MEMORY_USAGE_THRESHOLD = "worker.memory.usage.threshold";
    public static final String WORKER_JOB_NUM_THRESHOLD = "worker.job.num.threshold";
    public static final String WORKER_TASK_STATE_STORE_CLASS = "worker.task.state.store.class";
    public static final String DEFAULT_WORKER_TASK_STATE_STORE_CLASS = LocalFileSystemStateStore.class.getName();
    public static final String WORKER_TASK_STATE_STORE_SLEEP_INTERVAL = "worker.task.state.store.sleep.interval";

    public static final String LOG_SEND_MAX_BYTES = "log.send.max.bytes";
}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月13日 下午2:14:16
 */

package com.mogujie.jarvis.worker.status;

import org.apache.commons.configuration.Configuration;

import com.google.common.base.Throwables;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.ReflectionUtils;
import com.mogujie.jarvis.worker.WorkerConfigKeys;

public final class TaskStateStoreFactory {

    private static Configuration config = ConfigUtils.getWorkerConfig();
    private static TaskStateStore taskStateStore = null;

    static {
        try {
            String className = config.getString(WorkerConfigKeys.WORKER_TASK_STATE_STORE_CLASS,
                    WorkerConfigKeys.DEFAULT_WORKER_TASK_STATE_STORE_CLASS);
            taskStateStore = ReflectionUtils.getInstanceByClassName(className);
            taskStateStore.init(config);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private TaskStateStoreFactory() {
    }

    public static TaskStateStore getInstance() {
        return taskStateStore;
    }

}

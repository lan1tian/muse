/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午7:57:49
 */

package com.mogujie.jarvis.worker.strategy.impl;

import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.worker.TaskPool;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceResult;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

/**
 * @author wuya
 *
 */
public class TaskNumAcceptanceStrategy implements AcceptanceStrategy {

    public static final int JOB_MAX_THRESHOLD = ConfigUtils.getWorkerConfig().getInt(WorkerConfigKeys.WORKER_JOB_NUM_THRESHOLD, 100);

    @Override
    public AcceptanceResult accept(TaskDetail taskDetail) throws Exception {
        int currentJobNum = TaskPool.INSTANCE.size();
        if (currentJobNum > JOB_MAX_THRESHOLD) {
            return new AcceptanceResult(false, "client当前运行任务数" + currentJobNum + ", 超过阈值" + JOB_MAX_THRESHOLD);
        }

        return new AcceptanceResult(true, "");
    }

}

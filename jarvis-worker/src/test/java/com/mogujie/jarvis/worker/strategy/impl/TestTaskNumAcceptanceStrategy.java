/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月7日 上午11:26:06
 */

package com.mogujie.jarvis.worker.strategy.impl;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.tasks.DummyTask;
import com.mogujie.jarvis.worker.TaskPool;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

public class TestTaskNumAcceptanceStrategy {

    @Test
    public void testAccept() throws Exception {
        TaskPool taskPool = TaskPool.INSTANCE;
        int threshold = ConfigUtils.getWorkerConfig().getInt(WorkerConfigKeys.WORKER_JOB_NUM_THRESHOLD, 100);

        AcceptanceStrategy acceptanceStrategy = new TaskNumAcceptanceStrategy();
        for (int i = 0; i < 300; i++) {
            taskPool.add(i + "+" + i, new DummyTask(null));
        }

        Assert.assertEquals(acceptanceStrategy.accept(null).isAccepted(), taskPool.size() <= threshold);
    }
}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月7日 上午11:18:52
 */

package com.mogujie.jarvis.worker.strategy.impl;

import java.lang.management.ManagementFactory;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.worker.WorkerConfigKeys;
import com.mogujie.jarvis.worker.strategy.AcceptanceStrategy;

public class TestLoadAcceptanceStrategy {

    @Test
    public void testAccept() throws Exception {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        double threshold = ConfigUtils.getWorkerConfig().getDouble(WorkerConfigKeys.WORKER_CPU_LOAD_AVG_THRESHOLD, cpuNum * 1.5);
        AcceptanceStrategy acceptanceStrategy = new LoadAcceptanceStrategy();
        Assert.assertEquals(acceptanceStrategy.accept(null).isAccepted(),
                ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() <= threshold);
    }
}

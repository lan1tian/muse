/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月8日 上午11:43:38
 */

package com.mogujie.jarvis.server.dispatcher.workerselector;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.inject.Injector;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.HeartBeatService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Injectors.class)
@SuppressStaticInitializationFor("com.mogujie.jarvis.server.guice.Injectors")
public class TestRandomWorkerSelector {

    private HeartBeatService heartBeatService = new HeartBeatService();

    @Before
    public void setup() {
        Injector injector = Mockito.mock(Injector.class);
        Mockito.when(injector.getInstance(HeartBeatService.class)).thenReturn(heartBeatService);

        PowerMockito.mockStatic(Injectors.class);
        Mockito.when(Injectors.getInjector()).thenReturn(injector);
    }

    @Test
    public void testSelect() {
        List<WorkerInfo> groupWorkerList1 = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            WorkerInfo workerInfo = new WorkerInfo("10.11.12." + i, 1234);
            groupWorkerList1.add(workerInfo);
            heartBeatService.put(1, workerInfo, 5);
        }

        int total = 10000;
        Multiset<WorkerInfo> multiset = HashMultiset.create();

        WorkerSelector selector = new RandomWorkerSelector();
        for (int i = 0; i < total; i++) {
            WorkerInfo workerInfo = selector.select(1);
            multiset.add(workerInfo);
        }

        double threshold = 0.05;
        for (WorkerInfo workerInfo : multiset) {
            Assert.assertTrue(Math.abs(1.0 / groupWorkerList1.size() - multiset.count(workerInfo) / (double) total) < threshold);
        }
    }
}

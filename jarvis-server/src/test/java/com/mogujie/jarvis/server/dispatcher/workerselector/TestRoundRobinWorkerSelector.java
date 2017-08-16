/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月8日 上午10:39:42
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

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.HeartBeatService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Injectors.class)
@SuppressStaticInitializationFor("com.mogujie.jarvis.server.guice.Injectors")
public class TestRoundRobinWorkerSelector {

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

        WorkerSelector selector = new RoundRobinWorkerSelector();
        for (int i = 0; i < 1000; i++) {
            Assert.assertEquals(selector.select(1), groupWorkerList1.get(i % groupWorkerList1.size()));
        }
    }
}

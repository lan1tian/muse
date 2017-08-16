/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午4:41:03
 */

package com.mogujie.jarvis.server.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mogujie.jarvis.core.domain.WorkerInfo;

public class TestHeartBeatService {

    private HeartBeatService heartBeatService = new HeartBeatService();
    private List<WorkerInfo> workerInfoList = Lists.newArrayList();

    @Before
    public void setup() {
        workerInfoList.add(new WorkerInfo("1.2.3.1", 1234));
        workerInfoList.add(new WorkerInfo("1.2.3.3", 1234));
        workerInfoList.add(new WorkerInfo("1.2.3.2", 1234));
        workerInfoList.add(new WorkerInfo("1.2.3.4", 1234));
        workerInfoList.add(new WorkerInfo("1.2.3.4", 1000));
    }

    @Test
    public void testPut() {
        heartBeatService.put(1, workerInfoList.get(0), 1);
        heartBeatService.put(1, workerInfoList.get(1), 2);
        heartBeatService.put(1, workerInfoList.get(2), 3);
        heartBeatService.put(2, workerInfoList.get(3), 4);
        heartBeatService.put(2, workerInfoList.get(4), 5);
        Assert.assertEquals(heartBeatService.getWorkers(1).size(), 3);
        Assert.assertEquals(heartBeatService.getWorkers(2).size(), 2);
        Assert.assertEquals(heartBeatService.getWorkers(3).size(), 0);
    }

    @Test
    public void testRemove() {
        heartBeatService.remove(2, workerInfoList.get(4));
        Assert.assertEquals(heartBeatService.getWorkers(2).size(), 1);
        Assert.assertEquals(heartBeatService.getWorkers(2).get(0), workerInfoList.get(3));
    }
}

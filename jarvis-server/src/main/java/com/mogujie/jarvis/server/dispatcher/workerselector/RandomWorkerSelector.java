/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午4:05:00
 */

package com.mogujie.jarvis.server.dispatcher.workerselector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.HeartBeatService;

/**
 * Select a worker by random strategy
 */
public class RandomWorkerSelector implements WorkerSelector {

    private HeartBeatService heartBeatService = Injectors.getInjector().getInstance(HeartBeatService.class);

    @Override
    public WorkerInfo select(int workerGroupId) {
        List<WorkerInfo> workers = heartBeatService.getOnlineWorkers(workerGroupId);
        if (workers != null && workers.size() > 0) {
            return workers.get(ThreadLocalRandom.current().nextInt(0, workers.size()));
        }

        return null;
    }

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月24日 下午8:13:57
 */

package com.mogujie.jarvis.server.service;

import java.util.concurrent.atomic.AtomicLong;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.IDMapper;

@Singleton
public class IDService {

    @Inject
    private IDMapper idMapper;

    private AtomicLong atomicJobId;
    private AtomicLong atomicTaskId;

    @Inject
    public void init() {
        Long jobId = idMapper.selectMaxJobId();
        atomicJobId = jobId == null ? new AtomicLong(0) : new AtomicLong(jobId);

        Long taskId = idMapper.selectMaxTaskId();
        atomicTaskId = taskId == null ? new AtomicLong(0) : new AtomicLong(taskId);
    }

    public long getNextJobId() {
        return atomicJobId.incrementAndGet();
    }

    public long getNextTaskId() {
        return atomicTaskId.incrementAndGet();
    }

}

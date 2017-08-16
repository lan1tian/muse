/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月5日 下午3:24:52
 */

package com.mogujie.jarvis.server.scheduler;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.domain.TaskDetail.TaskDetailBuilder;
import com.mogujie.jarvis.core.util.ThreadUtils;
import com.mogujie.jarvis.server.dispatcher.PriorityTaskQueue;
import com.mogujie.jarvis.server.domain.RetryType;
import com.mogujie.jarvis.server.guice.Injectors;

@Ignore
public class TestTaskRetryScheduler {

    private TaskRetryScheduler taskRetryScheduler = TaskRetryScheduler.INSTANCE;
    private PriorityTaskQueue taskQueue = Injectors.getInjector().getInstance(PriorityTaskQueue.class);
    private TaskDetail taskDetail = null;

    @Before
    public void setup() {
        taskRetryScheduler.start();

        TaskDetailBuilder builder = TaskDetail.newTaskDetailBuilder();
        builder.setAppName("testApp");
        builder.setContent("content");
        builder.setDataTime(new DateTime(2016, 1, 5, 1, 2, 3));
        builder.setExpiredTime(5);
        builder.setFailedInterval(3);
        builder.setFailedRetries(3);
        builder.setFullId("123_456_789");
        builder.setGroupId(1);
        builder.setParameters(Maps.newHashMap());
        builder.setPriority(5);
        builder.setTaskName("testTask");
        builder.setJobType("shell");
        builder.setUser("user");
        taskDetail = builder.build();
    }

    @Test
    public void testFailedRetry() {
        taskQueue.clear();
        for (int i = 0; i < 5; i++) {
            taskRetryScheduler.addTask(taskDetail, RetryType.FAILED_RETRY);
        }
        ThreadUtils.sleep(10000);
        Assert.assertEquals(taskQueue.size(), taskDetail.getFailedRetries());
    }

    @Test
    public void testRejectRetry() {
        taskQueue.clear();
        for (int i = 0; i < 5; i++) {
            taskRetryScheduler.addTask(taskDetail, RetryType.REJECT_RETRY);
            ThreadUtils.sleep(2000);
        }
        ThreadUtils.sleep(10000);
        Assert.assertEquals(taskQueue.size(), 3);
    }

    @After
    public void close() {
        taskRetryScheduler.shutdown();
    }
}

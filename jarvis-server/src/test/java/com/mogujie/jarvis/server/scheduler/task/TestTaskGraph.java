/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月8日 上午11:42:18
 */

package com.mogujie.jarvis.server.scheduler.task;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author guangming
 *
 */
public class TestTaskGraph {
    private TaskGraph taskGraph = TaskGraph.INSTANCE;
    private long jobAId = 1;
    private long jobBId = 2;
    private long jobCId = 3;
    private long t1 = 1000;

    @After
    public void teardown() {
        taskGraph.clear();
    }

    /**
     *     task1
     *    /     \
     *  task2   task3
     */
    @Test
    public void testAddTask1() {
        DAGTask task1 = new DAGTask(jobAId, 1, 1, t1);
        DAGTask task2 = new DAGTask(jobBId, 2, 1, t1);
        DAGTask task3 = new DAGTask(jobCId, 3, 1, t1);
        taskGraph.addTask(task1.getTaskId(), task1);
        taskGraph.addTask(task2.getTaskId(), task2);
        taskGraph.addTask(task3.getTaskId(), task3);
        taskGraph.addDependency(task1.getTaskId(), task2.getTaskId());
        taskGraph.addDependency(task1.getTaskId(), task3.getTaskId());

        Assert.assertEquals(2, taskGraph.getChildren(task1.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getParents(task2.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getParents(task3.getTaskId()).size());
    }

    /**
     *  task1  task2
     *    \     /
     *     task3
     */
    @Test
    public void testAddTask2() {
        DAGTask task1 = new DAGTask(jobAId, 1, 1, t1);
        DAGTask task2 = new DAGTask(jobBId, 2, 1, t1);
        DAGTask task3 = new DAGTask(jobCId, 3, 1, t1);
        taskGraph.addTask(task1.getTaskId(), task1);
        taskGraph.addTask(task2.getTaskId(), task2);
        taskGraph.addTask(task3.getTaskId(), task3);
        taskGraph.addDependency(task1.getTaskId(), task3.getTaskId());
        taskGraph.addDependency(task2.getTaskId(), task3.getTaskId());

        Assert.assertEquals(1, taskGraph.getChildren(task1.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getChildren(task2.getTaskId()).size());
        Assert.assertEquals(2, taskGraph.getParents(task3.getTaskId()).size());
    }
}

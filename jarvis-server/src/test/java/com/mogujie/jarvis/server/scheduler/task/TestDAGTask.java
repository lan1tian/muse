/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月8日 下午1:25:06
 */

package com.mogujie.jarvis.server.scheduler.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * @author guangming
 *
 */
public class TestDAGTask {
    private TaskGraph taskGraph = TaskGraph.INSTANCE;
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
    private long jobAId = 1;
    private long jobBId = 2;
    private long jobCId = 3;
    private long t1 = 1000;

    @After
    public void teardown() {
        Set<Long> taskIds = taskGraph.getTaskMap().keySet();
        for (long taskId : taskIds) {
            taskService.deleteTaskAndRelation(taskId);
        }
        taskGraph.clear();
    }

    /**
     *     task1
     *    /     \
     *  task2   task3
     */
    @Test
    public void testCheckStatus1() {
        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskBId = taskService.createTaskByJobId(jobBId, t1, t1, TaskType.SCHEDULE);
        long taskCId = taskService.createTaskByJobId(jobCId, t1, t1, TaskType.SCHEDULE);
        DAGTask task1 = new DAGTask(jobAId, taskAId, 1, t1);
        DAGTask task2 = new DAGTask(jobBId, taskBId, 1, t1);
        DAGTask task3 = new DAGTask(jobCId, taskCId, 1, t1);
        taskGraph.addTask(task1.getTaskId(), task1);
        taskGraph.addTask(task2.getTaskId(), task2);
        taskGraph.addTask(task3.getTaskId(), task3);
        taskGraph.addDependency(task1.getTaskId(), task2.getTaskId());
        taskGraph.addDependency(task1.getTaskId(), task3.getTaskId());

        Assert.assertEquals(2, taskGraph.getChildren(task1.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getParents(task2.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getParents(task3.getTaskId()).size());

        Assert.assertEquals(true, task1.checkStatus());
        Assert.assertEquals(false, task2.checkStatus());
        Assert.assertEquals(false, task3.checkStatus());

        taskService.updateStatus(task1.getTaskId(), TaskStatus.SUCCESS);
        Assert.assertEquals(true, task2.checkStatus());
        Assert.assertEquals(true, task3.checkStatus());
    }

    /**
     *  task1  task2
     *    \     /
     *     task3
     */
    @Test
    public void testCheckStatus2() {
        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskBId = taskService.createTaskByJobId(jobBId, t1, t1, TaskType.SCHEDULE);
        long taskCId = taskService.createTaskByJobId(jobCId, t1, t1, TaskType.SCHEDULE);
        DAGTask task1 = new DAGTask(jobAId, taskAId, 1, t1);
        DAGTask task2 = new DAGTask(jobBId, taskBId, 1, t1);
        DAGTask task3 = new DAGTask(jobCId, taskCId, 1, t1);
        taskGraph.addTask(task1.getTaskId(), task1);
        taskGraph.addTask(task2.getTaskId(), task2);
        taskGraph.addTask(task3.getTaskId(), task3);
        taskGraph.addDependency(task1.getTaskId(), task3.getTaskId());
        taskGraph.addDependency(task2.getTaskId(), task3.getTaskId());

        Assert.assertEquals(1, taskGraph.getChildren(task1.getTaskId()).size());
        Assert.assertEquals(1, taskGraph.getChildren(task2.getTaskId()).size());
        Assert.assertEquals(2, taskGraph.getParents(task3.getTaskId()).size());

        Assert.assertEquals(true, task1.checkStatus());
        Assert.assertEquals(true, task2.checkStatus());
        Assert.assertEquals(false, task3.checkStatus());

        taskService.updateStatus(task1.getTaskId(), TaskStatus.SUCCESS);
        Assert.assertEquals(false, task3.checkStatus());

        taskService.updateStatus(task2.getTaskId(), TaskStatus.SUCCESS);
        Assert.assertEquals(true, task3.checkStatus());
    }

    /**
     *  task1  task2
     *    \     /
     *     task3
     */
    @Test
    public void testGetDependTaskIds() {
        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskBId = taskService.createTaskByJobId(jobBId, t1, t1, TaskType.SCHEDULE);
        long taskCId = taskService.createTaskByJobId(jobBId, t1, t1, TaskType.SCHEDULE);
        Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();
        dependTaskIdMap.put(jobAId, Lists.newArrayList(taskAId));
        dependTaskIdMap.put(jobBId, Lists.newArrayList(taskBId));
        DAGTask task1 = new DAGTask(jobAId, taskAId, 1, t1);
        DAGTask task2 = new DAGTask(jobBId, taskBId, 1, t1);
        DAGTask task3 = new DAGTask(jobCId, taskCId, 1, t1, dependTaskIdMap);
        taskGraph.addTask(task1.getTaskId(), task1);
        taskGraph.addTask(task2.getTaskId(), task2);
        taskGraph.addTask(task3.getTaskId(), task3);

        List<Long> dependIds = task3.getDependTaskIds();
        Assert.assertEquals(2, dependIds.size());
        Assert.assertTrue(dependIds.contains(taskAId));
        Assert.assertTrue(dependIds.contains(taskBId));
    }
}

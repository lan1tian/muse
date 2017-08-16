/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月25日 下午1:19:51
 */

package com.mogujie.jarvis.server.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.scheduler.dag.DAGJob;
import com.mogujie.jarvis.server.scheduler.dag.DAGJobType;
import com.mogujie.jarvis.server.scheduler.event.AddTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.SuccessEvent;

/**
 * @author guangming
 *
 */
public class TestJobSchedulerController extends TestSchedulerBase {
    private long jobAId = 1;
    private long jobBId = 2;
    private long jobCId = 3;
    private long jobDId = 4;
    private long jobEId = 5;
    private long jobFId = 6;
    private long t1 = 1000;

    /**
     *   A
     */
    @Test
    public void testTimeJobWithSuccessEvent() throws Exception {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);

        // jobA time ready
        AddTaskEvent addTaskEvent = new AddTaskEvent(jobAId, t1);
        controller.notify(addTaskEvent);
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(1, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);
        Task task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.READY, TaskStatus.parseValue(task.getStatus()));
        TaskType taskType = TaskType.parseValue(task.getType());

        SuccessEvent successEvent = new SuccessEvent(jobAId, taskAId, task.getScheduleTime().getTime(), taskType, "test");
        controller.notify(successEvent);

        task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(task.getStatus()));
        Assert.assertEquals(0, taskGraph.getTaskMap().size());
    }

    /**
     *   A
     *   |
     *   B
     */
    @Test
    public void testDependJobWithSuccessEvent1() throws Exception {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));

        // jobA time ready
        AddTaskEvent addTaskEvent = new AddTaskEvent(jobAId, t1);
        controller.notify(addTaskEvent);
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(1, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);
        Task task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.READY, TaskStatus.parseValue(task.getStatus()));
        TaskType taskType = TaskType.parseValue(task.getType());

        SuccessEvent successEvent = new SuccessEvent(jobAId, taskAId, task.getScheduleTime().getTime(), taskType, "test");
        controller.notify(successEvent);

        task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(task.getStatus()));

        // start jobB
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(2, taskQueue.size());
    }

    /**
     *     A
     *    / \
     *   B   C
     */
    @Test
    public void testDependJobWithSuccessEvent2() throws Exception {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));

        // jobA time ready
        AddTaskEvent addTaskEvent = new AddTaskEvent(jobAId, t1);
        controller.notify(addTaskEvent);
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(1, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);
        Task task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.READY, TaskStatus.parseValue(task.getStatus()));
        TaskType taskType = TaskType.parseValue(task.getType());

        SuccessEvent successEvent = new SuccessEvent(jobAId, taskAId, task.getScheduleTime().getTime(), taskType, "test");
        controller.notify(successEvent);

        task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(task.getStatus()));

        // start jobB and jobC
        Assert.assertEquals(2, taskGraph.getTaskMap().size());
        Assert.assertEquals(3, taskQueue.size());
    }

    /**
     *   D   E
     *    \ /
     *     F
     */
    @Test
    public void testDependJobWithSuccessEvent3() throws Exception {
        long t1 = new DateTime("2020-10-10T10:10:00").getMillis();
        long t2 = new DateTime("2020-10-10T11:11:00").getMillis();

        jobGraph.addJob(jobDId, new DAGJob(jobDId, DAGJobType.TIME), null);
        jobGraph.addJob(jobEId, new DAGJob(jobEId, DAGJobType.TIME), null);
        jobGraph.addJob(jobFId, new DAGJob(jobFId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobDId, jobEId));
        Assert.assertEquals(1, jobGraph.getChildren(jobDId).size());
        Assert.assertEquals(1, jobGraph.getChildren(jobEId).size());
        Assert.assertEquals(2, jobGraph.getParents(jobFId).size());

        // jobD time ready
        AddTaskEvent addTaskEventD = new AddTaskEvent(jobDId, t1);
        controller.notify(addTaskEventD);
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(1, taskQueue.size());

        // jobE time ready
        AddTaskEvent addTaskEventE = new AddTaskEvent(jobEId, t2);
        controller.notify(addTaskEventE);
        Assert.assertEquals(2, taskGraph.getTaskMap().size());
        Assert.assertEquals(2, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskDId = taskIds.get(0);
        Task taskD = taskService.get(taskDId);
        Assert.assertEquals(TaskStatus.READY, TaskStatus.parseValue(taskD.getStatus()));
        SuccessEvent successEventD = new SuccessEvent(jobDId, taskDId, taskD.getScheduleTime().getTime(),
                TaskType.parseValue(taskD.getType()), "test");
        controller.notify(successEventD);
        taskD = taskService.get(taskDId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(taskD.getStatus()));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        Assert.assertEquals(2, taskQueue.size());

        Assert.assertEquals(0, plan.getPlan().size());

        long taskEId = taskIds.get(1);
        Task taskE = taskService.get(taskEId);
        Assert.assertEquals(TaskStatus.READY, TaskStatus.parseValue(taskE.getStatus()));
        SuccessEvent successEventE = new SuccessEvent(jobEId, taskEId, taskE.getScheduleTime().getTime(),
                TaskType.parseValue(taskE.getType()), "test");
        controller.notify(successEventE);
        taskE = taskService.get(taskEId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(taskE.getStatus()));
        Assert.assertEquals(0, taskGraph.getTaskMap().size());
        Assert.assertEquals(2, taskQueue.size());
        Assert.assertEquals(1, plan.getPlan().size());
        System.out.println(plan.getPlan().peek());
    }

}

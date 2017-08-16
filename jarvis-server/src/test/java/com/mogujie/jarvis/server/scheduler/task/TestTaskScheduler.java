/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月8日 下午1:24:20
 */

package com.mogujie.jarvis.server.scheduler.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.TestSchedulerBase;
import com.mogujie.jarvis.server.scheduler.event.AddTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.SuccessEvent;
import com.mogujie.jarvis.server.service.JobService;

/**
 * @author guangming
 *
 */
public class TestTaskScheduler extends TestSchedulerBase {
    private long jobAId = 1;
    private long jobBId = 2;
    private long jobCId = 3;
    private long t1 = 1000;
    private long t2 = 2000;

    @Test
    public void testHandleSuccessEvent() {
        AddTaskEvent addTaskEvent = new AddTaskEvent(jobAId, t1);
        taskScheduler.handleAddTaskEvent(addTaskEvent);
        Assert.assertEquals(1, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(1, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);
        Task task = taskService.get(taskAId);
        SuccessEvent successEvent = new SuccessEvent(jobAId, task.getTaskId(),
                task.getScheduleTime().getTime(), TaskType.parseValue(task.getType()), "test");
        taskScheduler.handleSuccessEvent(successEvent);

        task = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(task.getStatus()));

        Assert.assertEquals(0, taskGraph.getTaskMap().keySet().size());
    }

    /**
     *     task1
     *    /     \
     *  task2   task3
     */
    @Test
    public void testScheduleTask1() {
        AddTaskEvent addTask1Event = new AddTaskEvent(jobAId, t1);
        taskScheduler.handleAddTaskEvent(addTask1Event);
        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);

        Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();
        dependTaskIdMap.put(jobAId, Lists.newArrayList(taskAId));

        AddTaskEvent addTask2Event = new AddTaskEvent(jobBId, t1, dependTaskIdMap);
        taskScheduler.handleAddTaskEvent(addTask2Event);

        AddTaskEvent addTask3Event = new AddTaskEvent(jobCId, t1, dependTaskIdMap);
        taskScheduler.handleAddTaskEvent(addTask3Event);

        taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        taskAId = taskIds.get(0);
        long taskBId = taskIds.get(1);
        long taskCId = taskIds.get(2);

        Assert.assertEquals(3, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(2, taskGraph.getChildren(taskAId).size());
        Assert.assertEquals(1, taskGraph.getParents(taskBId).size());
        Assert.assertEquals(1, taskGraph.getParents(taskCId).size());
        Assert.assertEquals(1, taskQueue.size());

        Task taskA = taskService.get(taskAId);
        SuccessEvent successEvent = new SuccessEvent(jobAId, taskA.getTaskId(),
                taskA.getScheduleTime().getTime(), TaskType.parseValue(taskA.getType()), "test");
        taskScheduler.handleSuccessEvent(successEvent);
        taskA = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(taskA.getStatus()));

        Assert.assertEquals(2, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(0, taskGraph.getParents(taskBId).size());
        Assert.assertEquals(0, taskGraph.getParents(taskCId).size());
        Assert.assertEquals(3, taskQueue.size());
    }

    /**
     *  task1   task2
     *     \     /
     *      task3
     */
    @Test
    public void testScheduleTask2() {
        AddTaskEvent addTask1Event = new AddTaskEvent(jobAId, t1);
        taskScheduler.handleAddTaskEvent(addTask1Event);
        AddTaskEvent addTask2Event = new AddTaskEvent(jobBId, t1);
        taskScheduler.handleAddTaskEvent(addTask2Event);
        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId = taskIds.get(0);
        long taskBId = taskIds.get(1);

        Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();
        dependTaskIdMap.put(jobAId, Lists.newArrayList(taskAId, taskBId));

        AddTaskEvent addTask3Event = new AddTaskEvent(jobCId, t1, dependTaskIdMap);
        taskScheduler.handleAddTaskEvent(addTask3Event);

        taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskCId = taskIds.get(2);

        Assert.assertEquals(3, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(1, taskGraph.getChildren(taskAId).size());
        Assert.assertEquals(1, taskGraph.getChildren(taskBId).size());
        Assert.assertEquals(2, taskGraph.getParents(taskCId).size());
        Assert.assertEquals(2, taskQueue.size());

        Task taskA = taskService.get(taskAId);
        SuccessEvent successEventA = new SuccessEvent(jobAId, taskA.getTaskId(),
                taskA.getScheduleTime().getTime(), TaskType.parseValue(taskA.getType()), "test");
        taskScheduler.handleSuccessEvent(successEventA);
        taskA = taskService.get(taskAId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(taskA.getStatus()));

        Assert.assertEquals(2, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(1, taskGraph.getParents(taskCId).size());
        Assert.assertEquals(2, taskQueue.size());

        Task taskB = taskService.get(taskBId);
        SuccessEvent successEventB = new SuccessEvent(jobBId, taskB.getTaskId(),
                taskB.getScheduleTime().getTime(), TaskType.parseValue(taskB.getType()), "test");
        taskScheduler.handleSuccessEvent(successEventB);
        taskB = taskService.get(taskBId);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(taskB.getStatus()));

        Assert.assertEquals(1, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(0, taskGraph.getParents(taskCId).size());
        Assert.assertEquals(3, taskQueue.size());
    }

    @Test
    public void testSerialTask() {
        // hook serial flag
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        boolean oldSerialFlag = jobService.get(jobAId).getJob().getIsSerial();
        jobService.get(jobAId).getJob().setIsSerial(true);

        AddTaskEvent addTaskEvent1 = new AddTaskEvent(jobAId, t1);
        taskScheduler.handleAddTaskEvent(addTaskEvent1);
        Assert.assertEquals(1, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(1, taskQueue.size());

        AddTaskEvent addTaskEvent2 = new AddTaskEvent(jobAId, t2);
        taskScheduler.handleAddTaskEvent(addTaskEvent2);
        Assert.assertEquals(2, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(1, taskQueue.size());

        List<Long> taskIds = new ArrayList<Long>(taskGraph.getTaskMap().keySet());
        Collections.sort(taskIds);
        long taskAId1 = taskIds.get(0);

        Task task1 = taskService.get(taskAId1);
        SuccessEvent successEvent = new SuccessEvent(jobAId, task1.getTaskId(),
                task1.getScheduleTime().getTime(), TaskType.parseValue(task1.getType()), "test");
        taskScheduler.handleSuccessEvent(successEvent);

        task1 = taskService.get(taskAId1);
        Assert.assertEquals(TaskStatus.SUCCESS, TaskStatus.parseValue(task1.getStatus()));

        Assert.assertEquals(1, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(2, taskQueue.size());

        jobService.get(jobAId).getJob().setIsSerial(oldSerialFlag);
    }
}

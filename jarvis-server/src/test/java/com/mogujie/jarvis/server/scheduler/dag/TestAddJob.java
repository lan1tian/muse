/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月10日 下午2:13:39
 */

package com.mogujie.jarvis.server.scheduler.dag;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.exception.JobScheduleException;
import com.mogujie.jarvis.server.scheduler.TestSchedulerBase;
import com.mogujie.jarvis.server.scheduler.event.ScheduleEvent;
import com.mogujie.jarvis.server.scheduler.task.DAGTask;

/**
 * @author guangming
 *
 */
public class TestAddJob extends TestSchedulerBase {
    private long jobAId = 4;
    private long jobBId = 5;
    private long jobCId = 6;
    private long taskAId;
    private long taskBId;
    private long t1 = new DateTime("2020-10-10T10:10:00").getMillis();
    private long t2 = new DateTime("2020-10-10T11:11:00").getMillis();

    /**
     *   A   B
     *    \ /
     *     C
     *
     *  C在A和B跑之前加入
     *
     * @throws JobScheduleException
     */
    @Test
    public void testCurrentDay1() throws JobScheduleException {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.TIME), null);
        DateTime dateTime3 = new DateTime("2020-10-10T10:00:00");
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobAId, jobBId), dateTime3);
        Assert.assertEquals(1, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobAId).get(0).getFirst());
        Assert.assertEquals(1, jobGraph.getChildren(jobBId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobBId).get(0).getFirst());
        Assert.assertEquals(2, jobGraph.getParents(jobCId).size());

        // schedule jobA
        taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId, new DAGTask(jobAId, taskAId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        taskService.updateStatus(taskAId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventA = new ScheduleEvent(jobAId, taskAId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventA);
        Assert.assertEquals(0, plan.getPlan().size());

        // schedule jobB
        // pass the dependency check, start to schedule jobC
        taskBId = taskService.createTaskByJobId(jobBId, t2, t2, TaskType.SCHEDULE);
        taskGraph.addTask(taskBId, new DAGTask(jobBId, taskBId, t2, null));
        taskService.updateStatus(taskBId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventB = new ScheduleEvent(jobBId, taskBId, t2);
        dagScheduler.handleScheduleEvent(scheduleEventB);
        Assert.assertEquals(1, plan.getPlan().size());
        System.out.println(plan.getPlan());
    }

    /**
     *   A   B
     *    \ /
     *     C
     *
     *  C在A和B之间加入
     *
     * @throws JobScheduleException
     */
    @Test
    public void testCurrentDay2() throws JobScheduleException {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.TIME), null);

        // schedule jobA
        taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId, new DAGTask(jobAId, taskAId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        taskService.updateStatus(taskAId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventA = new ScheduleEvent(jobAId, taskAId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventA);
        Assert.assertEquals(0, plan.getPlan().size());

        DateTime dateTime3 = new DateTime("2020-10-10T10:30:00");
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobAId, jobBId), dateTime3);
        Assert.assertEquals(1, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobAId).get(0).getFirst());
        Assert.assertEquals(1, jobGraph.getChildren(jobBId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobBId).get(0).getFirst());
        Assert.assertEquals(2, jobGraph.getParents(jobCId).size());

        // schedule jobB
        // pass the dependency check, start to schedule jobC
        taskBId = taskService.createTaskByJobId(jobBId, t2, t2, TaskType.SCHEDULE);
        taskGraph.addTask(taskBId, new DAGTask(jobBId, taskBId, t2, null));
        taskService.updateStatus(taskBId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventB = new ScheduleEvent(jobBId, taskBId, t2);
        dagScheduler.handleScheduleEvent(scheduleEventB);
        Assert.assertEquals(1, plan.getPlan().size());
        System.out.println(plan.getPlan());
    }

    /**
     *   A   B
     *    \ /
     *     C
     *
     *  C在A和B之后加入
     *
     * @throws JobScheduleException
     */
    @Test
    public void testCurrentDay3() throws JobScheduleException {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.TIME), null);

        // schedule jobA
        taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId, new DAGTask(jobAId, taskAId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());
        taskService.updateStatus(taskAId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventA = new ScheduleEvent(jobAId, taskAId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventA);
        Assert.assertEquals(0, plan.getPlan().size());

        // schedule jobB
        // pass the dependency check, start to schedule jobC
        taskBId = taskService.createTaskByJobId(jobBId, t2, t2, TaskType.SCHEDULE);
        taskGraph.addTask(taskBId, new DAGTask(jobBId, taskBId, t2, null));
        taskService.updateStatus(taskBId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventB = new ScheduleEvent(jobBId, taskBId, t2);
        dagScheduler.handleScheduleEvent(scheduleEventB);

        DateTime dateTime3 = new DateTime("2020-10-10T00:00:00");
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobAId, jobBId), dateTime3);
        Assert.assertEquals(1, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobAId).get(0).getFirst());
        Assert.assertEquals(1, jobGraph.getChildren(jobBId).size());
        Assert.assertEquals(jobCId, (long) jobGraph.getChildren(jobBId).get(0).getFirst());
        Assert.assertEquals(2, jobGraph.getParents(jobCId).size());

        Assert.assertEquals(1, plan.getPlan().size());
        System.out.println(plan.getPlan());
    }
}

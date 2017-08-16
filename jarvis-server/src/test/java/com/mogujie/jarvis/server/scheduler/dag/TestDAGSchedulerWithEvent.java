/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月10日 下午5:59:07
 */

package com.mogujie.jarvis.server.scheduler.dag;

import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.expression.CronExpression;
import com.mogujie.jarvis.core.expression.DefaultDependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.TimeOffsetExpression;
import com.mogujie.jarvis.server.domain.JobDependencyEntry;
import com.mogujie.jarvis.server.domain.JobEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.TestSchedulerBase;
import com.mogujie.jarvis.server.scheduler.event.ScheduleEvent;
import com.mogujie.jarvis.server.scheduler.task.DAGTask;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * @author guangming
 *
 */
public class TestDAGSchedulerWithEvent extends TestSchedulerBase {
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
    private long jobAId = 1;
    private long jobBId = 2;
    private long jobCId = 3;
    private long jobDId = 4;
    private long jobEId = 5;
    private long jobFId = 6;
    private long t1 = 1000;

    /**
     *   A
     *   |
     *   B
     */
    @Test
    public void testRunTimeSchedule1() throws Exception {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));
        Assert.assertEquals(1, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(jobBId, (long) jobGraph.getChildren(jobAId).get(0).getFirst());
        Assert.assertEquals(1, jobGraph.getParents(jobBId).size());

        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId, new DAGTask(jobAId, taskAId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());

        // schedule jobA
        ScheduleEvent scheduleEventA = new ScheduleEvent(jobAId, taskAId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventA);
        Assert.assertEquals(2, taskGraph.getTaskMap().size());
    }

    /**
     *     A
     *    / \
     *   B   C
     */
    @Test
    public void testRunTimeSchedule2() throws Exception {
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND), Sets.newHashSet(jobAId));
        Assert.assertEquals(2, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(1, jobGraph.getParents(jobBId).size());
        Assert.assertEquals(1, jobGraph.getParents(jobCId).size());

        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId, new DAGTask(jobAId, taskAId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());

        // schedule jobA
        ScheduleEvent scheduleEventA = new ScheduleEvent(jobAId, taskAId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventA);
        Assert.assertEquals(3, taskGraph.getTaskMap().size());
    }

    /**
     *   D   E
     *    \ /
     *     F
     */
    @Test
    public void testCurrentDaySchedule1() throws Exception {
        long t1 = new DateTime("2020-10-10T10:10:00").getMillis();
        long t2 = new DateTime("2020-10-10T11:11:00").getMillis();

        jobGraph.addJob(jobDId, new DAGJob(jobDId, DAGJobType.TIME), null);
        jobGraph.addJob(jobEId, new DAGJob(jobEId, DAGJobType.TIME), null);
        jobGraph.addJob(jobFId, new DAGJob(jobFId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobDId, jobEId));
        Assert.assertEquals(1, jobGraph.getChildren(jobDId).size());
        Assert.assertEquals(1, jobGraph.getChildren(jobEId).size());
        Assert.assertEquals(2, jobGraph.getParents(jobFId).size());

        long taskDId = taskService.createTaskByJobId(jobDId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskDId, new DAGTask(jobAId, taskDId, t1, null));
        Assert.assertEquals(1, taskGraph.getTaskMap().size());

        long taskEId = taskService.createTaskByJobId(jobEId, t2, t2, TaskType.SCHEDULE);
        taskGraph.addTask(taskEId, new DAGTask(jobAId, taskEId, t2, null));
        Assert.assertEquals(2, taskGraph.getTaskMap().size());

        // schedule jobD
        taskService.updateStatus(taskDId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventD = new ScheduleEvent(jobDId, taskDId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventD);
        Assert.assertEquals(2, taskGraph.getTaskMap().size());

        // schedule jobE
        taskService.updateStatus(taskEId, TaskStatus.SUCCESS);
        ScheduleEvent scheduleEventE = new ScheduleEvent(jobEId, taskDId, t1);
        dagScheduler.handleScheduleEvent(scheduleEventE);
        Assert.assertEquals(1, plan.getPlan().size());
        System.out.println(plan.getPlan().peek());
    }

    /**
     *   A
     *   |
     *   B
     */
    @Test
    public void testLast3DaySchedule1() throws Exception {
        long jobAId = 7;
        long jobBId = 8;
        long t1 = new DateTime("2020-10-10T10:10:00").getMillis();
        long t2 = new DateTime("2020-10-11T10:10:00").getMillis();
        long t3 = new DateTime("2020-10-12T10:10:00").getMillis();

        // init
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        JobEntry jobEntry = jobService.get(jobBId);
        Map<Long, JobDependencyEntry> dependencyEntryMap = jobEntry.getDependencies();
        JobDependencyEntry dependencyEntry = new JobDependencyEntry(new TimeOffsetExpression("d(3)"),
                new DefaultDependencyStrategyExpression("*"));
        dependencyEntryMap.put(jobAId, dependencyEntry);
        jobEntry.addScheduleExpression(1, new CronExpression("0 44 6 * * ?"));

        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.DEPEND_TIME), null);
        jobGraph.addDependency(jobAId, jobBId);
        Assert.assertEquals(1, jobGraph.getChildren(jobAId).size());
        Assert.assertEquals(jobBId, (long) jobGraph.getChildren(jobAId).get(0).getFirst());
        Assert.assertEquals(1, jobGraph.getParents(jobBId).size());

        long taskAId1 = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId1, new DAGTask(jobAId, taskAId1, t1, null));
        long taskAId2 = taskService.createTaskByJobId(jobAId, t2, t2, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId2, new DAGTask(jobAId, taskAId2, t2, null));
        long taskAId3 = taskService.createTaskByJobId(jobAId, t3, t3, TaskType.SCHEDULE);
        taskGraph.addTask(taskAId3, new DAGTask(jobAId, taskAId3, t3, null));
        Assert.assertEquals(3, taskGraph.getTaskMap().size());

        taskService.updateStatus(taskAId1, TaskStatus.SUCCESS);
        taskService.updateStatus(taskAId2, TaskStatus.FAILED);
        taskService.updateStatus(taskAId3, TaskStatus.SUCCESS);

        // schedule jobA
        ScheduleEvent scheduleEventA2 = new ScheduleEvent(jobAId, taskAId2, t2);
        dagScheduler.handleScheduleEvent(scheduleEventA2);
        Assert.assertEquals(0, plan.getPlan().size());
        System.out.println(plan.getPlan().peek());

        // schedule jobA
        taskService.updateStatus(taskAId2, TaskStatus.SUCCESS);
        scheduleEventA2 = new ScheduleEvent(jobAId, taskAId2, t2);
        dagScheduler.handleScheduleEvent(scheduleEventA2);
        Assert.assertEquals(3, plan.getPlan().size());
        System.out.println(plan.getPlan().toString());

        //rollback
        jobEntry.clearScheduleExpressions();
        jobEntry.getDependencies().clear();
    }

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月5日 下午6:57:00
 */

package com.mogujie.jarvis.server.scheduler.dag.checker;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.expression.DefaultDependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.expression.DependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.TimeOffsetExpression;
import com.mogujie.jarvis.core.domain.CommonStrategy;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * @author guangming
 *
 */
public class TestJobDependStatus {
    private long jobAId = 1;
    private long jobBId = 2;
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);

    @Test
    public void testCurrentDayAll() {
        long t1 = new DateTime("2015-10-10T10:10:00").getMillis();
        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);

        DependencyExpression dependencyExpression = new TimeOffsetExpression("cd");
        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ALL.getExpression());
        JobDependStatus status = new JobDependStatus(jobBId, jobAId, dependencyExpression, dependencyStrategy);

        // taskAId is waiting, check failed
        long scheduleTime = new DateTime("2015-10-10T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        // taskAId is finished, check success
        taskService.updateStatus(taskAId, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(scheduleTime));

        // can't find one task on 2015-10-11, check failed
        scheduleTime = new DateTime("2015-10-11T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        taskService.deleteTaskAndRelation(taskAId);
    }

    @Test
    public void testCurrentDayAnyone() {
        long t1 = new DateTime("2015-10-10T10:10:00").getMillis();
        long t11 = new DateTime("2015-10-10T10:15:00").getMillis();
        long taskAId1 = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskAId2 = taskService.createTaskByJobId(jobAId, t11, t11, TaskType.SCHEDULE);

        DependencyExpression dependencyExpression = new TimeOffsetExpression("cd");
        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ANYONE.getExpression());
        JobDependStatus status = new JobDependStatus(jobBId, jobAId, dependencyExpression, dependencyStrategy);

        // taskAId1 and taskAId2 is waiting, check failed
        long scheduleTime = new DateTime("2015-10-10T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        // taskAId1 and taskAId2 failed, check false
        taskService.updateStatus(taskAId1, TaskStatus.FAILED);
        taskService.updateStatus(taskAId2, TaskStatus.FAILED);
        Assert.assertEquals(false, status.check(scheduleTime));

        // taskAId1 success, check success
        taskService.updateStatus(taskAId1, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(scheduleTime));

        // can't find one task on 2015-10-11, check failed
        scheduleTime = new DateTime("2015-10-11T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        taskService.deleteTaskAndRelation(taskAId1);
        taskService.deleteTaskAndRelation(taskAId2);
    }

    @Test
    public void testCurrentDayLastone() {
        long t1 = new DateTime("2015-10-10T10:10:00").getMillis();
        long t11 = new DateTime("2015-10-10T10:15:00").getMillis();
        long taskAId1 = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskAId2 = taskService.createTaskByJobId(jobAId, t11, t11, TaskType.SCHEDULE);

        DependencyExpression dependencyExpression = new TimeOffsetExpression("cd");
        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ANYONE.getExpression());
        JobDependStatus status = new JobDependStatus(jobBId, jobAId, dependencyExpression, dependencyStrategy);

        // taskAId1 and taskAId2 is waiting, check failed
        long scheduleTime = new DateTime("2015-10-10T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        // taskAId1 and taskAId2 failed, check false
        taskService.updateStatus(taskAId1, TaskStatus.FAILED);
        taskService.updateStatus(taskAId2, TaskStatus.FAILED);
        Assert.assertEquals(false, status.check(scheduleTime));

        // taskAId1 success, check false
        taskService.updateStatus(taskAId1, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(scheduleTime));

        // taskAId2 success, check true
        taskService.updateStatus(taskAId2, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(scheduleTime));

        // can't find one task on 2015-10-11, check failed
        scheduleTime = new DateTime("2015-10-11T11:11:00").getMillis();
        Assert.assertEquals(false, status.check(scheduleTime));

        taskService.deleteTaskAndRelation(taskAId1);
        taskService.deleteTaskAndRelation(taskAId2);
    }

    @Test
    public void testCurrentHourAll() {
        long t1 = new DateTime("2015-10-10T10:10:00").getMillis();
        long t11 = new DateTime("2015-10-10T10:15:00").getMillis();
        long t12 = new DateTime("2015-10-10T11:11:00").getMillis();
        long t2 = new DateTime("2015-10-11T11:11:00").getMillis();
        long taskAId = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);

        DependencyExpression dependencyExpression = new TimeOffsetExpression("ch");
        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ALL.getExpression());
        JobDependStatus status = new JobDependStatus(jobBId, jobAId, dependencyExpression, dependencyStrategy);

        // taskAId is waiting, check failed
        Assert.assertEquals(false, status.check(t11));

        // taskAId is finished, check success
        taskService.updateStatus(taskAId, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(t11));

        // can't find one task on 2015-10-10 11:11:00, check failed
        Assert.assertEquals(false, status.check(t12));

        // can't find one task on 2015-10-11, check failed
        Assert.assertEquals(false, status.check(t2));

        taskService.deleteTaskAndRelation(taskAId);
    }

    @Test
    public void testLast3DayAll() {
        long t1 = new DateTime("2020-10-10T10:10:00").getMillis();
        long t2 = new DateTime("2020-10-11T10:10:00").getMillis();
        long t3 = new DateTime("2020-10-12T10:10:00").getMillis();
        long t4 = new DateTime("2020-10-13T10:10:00").getMillis();
        long taskAId1 = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskAId2 = taskService.createTaskByJobId(jobAId, t2, t2, TaskType.SCHEDULE);
        long taskAId3 = taskService.createTaskByJobId(jobAId, t3, t3, TaskType.SCHEDULE);

        DependencyExpression dependencyExpression = new TimeOffsetExpression("d(3)");
        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ALL.getExpression());
        JobDependStatus status = new JobDependStatus(jobBId, jobAId, dependencyExpression, dependencyStrategy);

        // taskAId1,2,3 is waiting, check failed
        Assert.assertEquals(false, status.check(t4));

        // taskAId1 is finished, check false
        taskService.updateStatus(taskAId1, TaskStatus.SUCCESS);
        Assert.assertEquals(false, status.check(t4));

        // taskAId2 is finished, check false
        taskService.updateStatus(taskAId2, TaskStatus.SUCCESS);
        Assert.assertEquals(false, status.check(t4));

        // taskAId3 is finished, check true
        taskService.updateStatus(taskAId3, TaskStatus.SUCCESS);
        Assert.assertEquals(true, status.check(t4));

        taskService.deleteTaskAndRelation(taskAId1);
        taskService.deleteTaskAndRelation(taskAId2);
        taskService.deleteTaskAndRelation(taskAId3);
    }

}

package com.mogujie.jarvis.server.scheduler.dag.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.domain.CommonStrategy;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.expression.DefaultDependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.expression.DependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.TimeOffsetExpression;
import com.mogujie.jarvis.server.scheduler.TestSchedulerBase;
import com.mogujie.jarvis.server.scheduler.dag.DAGJob;
import com.mogujie.jarvis.server.scheduler.dag.DAGJobType;


/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/19.
 * used by jarvis-parent
 */
public class TestJobScheduleWithOffset extends TestSchedulerBase {
    private long jobAId = 4;
    private long jobBId = 5;
    private long jobCId = 6;
    private Map<Long, JobDependStatus> jobDependStatusMap = Maps.newHashMap();

    @Test
    public void testExp() {
        DateTime dateTime = new DateTime("2019-01-19T00:00:00");
        Property dayOfWeek = dateTime.dayOfWeek();
        int todayOfWeek = dayOfWeek.get();
        DependencyExpression expression = new TimeOffsetExpression("cw");
        Range<DateTime> range = expression.getRange(dateTime);
        Range<DateTime> realRange = Range.closedOpen(dateTime.minusDays(todayOfWeek), dateTime.plusDays(7 - todayOfWeek));

        assertEquals(realRange.toString(), range.toString());
    }

    /**
     *  A   B
     *   \ /
     *    C
     */
    @Test
    public void testWeek() throws Exception {
        long scheduleTime = new DateTime("2019-01-19T04:05:00").getMillis();
        long t1 = new DateTime("2019-01-13T00:00:00").getMillis();
        long t2 = new DateTime("2019-01-14T00:00:00").getMillis();
        long t3 = new DateTime("2019-01-15T00:00:00").getMillis();
        long t4 = new DateTime("2019-01-16T00:00:00").getMillis();
        long t5 = new DateTime("2019-01-17T00:00:00").getMillis();
        long t6 = new DateTime("2019-01-18T00:00:00").getMillis();
        long t7 = new DateTime("2019-01-19T23:59:00").getMillis();

        long taskAId1 = taskService.createTaskByJobId(jobAId, t1, t1, TaskType.SCHEDULE);
        long taskAId2 = taskService.createTaskByJobId(jobAId, t2, t2, TaskType.SCHEDULE);
        long taskAId3 = taskService.createTaskByJobId(jobAId, t3, t3, TaskType.SCHEDULE);
        long taskAId4 = taskService.createTaskByJobId(jobAId, t4, t4, TaskType.SCHEDULE);
        long taskAId5 = taskService.createTaskByJobId(jobAId, t5, t5, TaskType.SCHEDULE);
        long taskAId6 = taskService.createTaskByJobId(jobAId, t6, t6, TaskType.SCHEDULE);
        long taskAId7 = taskService.createTaskByJobId(jobAId, t7, t7, TaskType.SCHEDULE);

        long taskBId1 = taskService.createTaskByJobId(jobBId, t1, t1, TaskType.SCHEDULE);
        long taskBId2 = taskService.createTaskByJobId(jobBId, t2, t2, TaskType.SCHEDULE);
        long taskBId3 = taskService.createTaskByJobId(jobBId, t3, t3, TaskType.SCHEDULE);
        long taskBId4 = taskService.createTaskByJobId(jobBId, t4, t4, TaskType.SCHEDULE);
        long taskBId5 = taskService.createTaskByJobId(jobBId, t5, t5, TaskType.SCHEDULE);
        long taskBId6 = taskService.createTaskByJobId(jobBId, t6, t6, TaskType.SCHEDULE);
        long taskBId7 = taskService.createTaskByJobId(jobBId, t7, t7, TaskType.SCHEDULE);
        jobGraph.addJob(jobBId, new DAGJob(jobBId, DAGJobType.TIME), null);
        jobGraph.addJob(jobAId, new DAGJob(jobAId, DAGJobType.TIME), null);
        jobGraph.addJob(jobCId, new DAGJob(jobCId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobAId, jobBId));

        assertEquals(jobGraph.getParents(jobCId).size(), 2);

        DependencyExpression expression = new TimeOffsetExpression("cw");


        DependencyStrategyExpression dependencyStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ALL.getExpression());

        assertTrue(expression.isValid());

        JobDependStatus status_C2A = new JobDependStatus(jobCId, jobAId, expression, dependencyStrategy);
        JobDependStatus status_C2B = new JobDependStatus(jobCId, jobBId, expression, dependencyStrategy);

        jobDependStatusMap.put(jobAId, status_C2A);
        jobDependStatusMap.put(jobBId, status_C2B);

        DAGDependChecker dependChecker = new DAGDependChecker(jobCId);

        dependChecker.setJobDependMap(jobDependStatusMap);

        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId1, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId2, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId3, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId4, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId5, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId6, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskAId7, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));

        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId1, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId2, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId3, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId4, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId5, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId6, TaskStatus.SUCCESS);
        assertFalse(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));
        taskService.updateStatus(taskBId7, TaskStatus.SUCCESS);
        assertTrue(dependChecker.checkDependency(Sets.newHashSet(jobAId, jobBId), scheduleTime));

        taskService.deleteTaskAndRelation(taskAId1);
        taskService.deleteTaskAndRelation(taskAId2);
        taskService.deleteTaskAndRelation(taskAId3);
        taskService.deleteTaskAndRelation(taskAId4);
        taskService.deleteTaskAndRelation(taskAId5);
        taskService.deleteTaskAndRelation(taskAId6);
        taskService.deleteTaskAndRelation(taskAId7);
        taskService.deleteTaskAndRelation(taskBId1);
        taskService.deleteTaskAndRelation(taskBId2);
        taskService.deleteTaskAndRelation(taskBId3);
        taskService.deleteTaskAndRelation(taskBId4);
        taskService.deleteTaskAndRelation(taskBId5);
        taskService.deleteTaskAndRelation(taskBId6);
        taskService.deleteTaskAndRelation(taskBId7);
    }
}

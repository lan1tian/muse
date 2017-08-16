/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年1月11日 上午11:12:03
 */

package com.mogujie.jarvis.server.scheduler.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.core.exception.JobScheduleException;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.server.domain.JobDependencyEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.scheduler.TestSchedulerBase;
import com.mogujie.jarvis.server.scheduler.dag.DAGJob;
import com.mogujie.jarvis.server.scheduler.dag.DAGJobType;
import com.mogujie.jarvis.server.scheduler.event.ManualRerunTaskEvent;
import com.mogujie.jarvis.server.scheduler.event.SuccessEvent;
import com.mogujie.jarvis.server.scheduler.time.TimePlanEntry;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.util.PlanUtil;

/**
 * @author guangming
 *
 */
public class TestRerunTask extends TestSchedulerBase {
    private long jobDId = 4;
    private long jobEId = 5;
    private long jobFId = 6;
    private long t1 = new DateTime("2020-10-10").getMillis();
    private long t2 = new DateTime("2020-10-12").getMillis();
    private JobService jobService = Injectors.getInjector().getInstance(JobService.class);

    @Test
    public void testManualRerunTask() throws JobScheduleException {
        jobGraph.addJob(jobDId, new DAGJob(jobDId, DAGJobType.TIME), null);
        jobGraph.addJob(jobEId, new DAGJob(jobEId, DAGJobType.TIME), null);
        jobGraph.addJob(jobFId, new DAGJob(jobFId, DAGJobType.DEPEND_TIME), Sets.newHashSet(jobDId, jobEId));

        List<Long> jobIdList = Lists.newArrayList(jobDId, jobEId, jobFId);
        List<Long> taskIdList = new ArrayList<Long>();
        DateTime startDate = new DateTime(t1);
        DateTime endDate = new DateTime(t2);
        // 1.生成所有任务的执行计划
        Range<DateTime> range = Range.closed(startDate, endDate);
        Map<Long, List<TimePlanEntry>> planMap = PlanUtil.getReschedulePlan(jobIdList, range);
        // 2.生成新的task
        long scheduleTime = DateTime.now().getMillis();
        for (long jobId : jobIdList) {
            List<TimePlanEntry> planList = planMap.get(jobId);
            for (TimePlanEntry planEntry : planList) {
                // create new task
                long dataTime = planEntry.getDateTime().getMillis();
                long taskId = taskService.createTaskByJobId(jobId, scheduleTime, dataTime, TaskType.RERUN);
                planEntry.setTaskId(taskId);
                taskIdList.add(taskId);
            }
        }
        // 3.确定task依赖关系，添加DAGTask到TaskGraph中
        for (long jobId : jobIdList) {
            List<TimePlanEntry> planList = planMap.get(jobId);
            for (int i = 0; i < planList.size(); i++) {
                TimePlanEntry planEntry = planList.get(i);
                long taskId = planEntry.getTaskId();
                long dataTime = planEntry.getDateTime().getMillis();
                Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();
                Map<Long, JobDependencyEntry> dependencyMap = jobService.get(jobId).getDependencies();
                if (dependencyMap != null) {
                    for (Entry<Long, JobDependencyEntry> entry : dependencyMap.entrySet()) {
                        long preJobId = entry.getKey();
                        if (jobIdList.contains(preJobId)) {
                            JobDependencyEntry dependencyEntry = entry.getValue();
                            DependencyExpression dependencyExpression = dependencyEntry.getDependencyExpression();
                            List<Long> dependTaskIds = getDependTaskIds(planMap.get(preJobId), dataTime, dependencyExpression);
                            dependTaskIdMap.put(preJobId, dependTaskIds);
                        }
                    }
                }
                //如果是串行任务
                if (jobService.get(jobId).getJob().getIsSerial()) {
                    if (i > 0) {
                        // 增加自依赖
                        long preTaskId = planList.get(i - 1).getTaskId();
                        List<Long> dependTaskIds = Lists.newArrayList(preTaskId);
                        dependTaskIdMap.put(jobId, dependTaskIds);
                    }
                }
                // add to taskGraph
                DAGTask dagTask = new DAGTask(jobId, taskId, dataTime, dependTaskIdMap);
                taskGraph.addTask(taskId, dagTask);
            }
        }
        // 4.添加依赖关系
        for (long taskId : taskIdList) {
            DAGTask dagTask = taskGraph.getTask(taskId);
            List<Long> dependTaskIds = dagTask.getDependTaskIds();
            for (long parentId : dependTaskIds) {
                taskGraph.addDependency(parentId, taskId);
            }
        }

        Assert.assertEquals(6, taskGraph.getTaskMap().keySet().size());
        List<TimePlanEntry> planList = planMap.get(jobFId);
        Assert.assertEquals(2, planList.size());
        for (TimePlanEntry entry : planList) {
            long taskId = entry.getTaskId();
            Assert.assertEquals(2, taskGraph.getParents(taskId).size());
        }

        // 5. 重跑任务
        controller.notify(new ManualRerunTaskEvent(taskIdList));
        Assert.assertEquals(4, taskQueue.size());

        List<TimePlanEntry> planDList = planMap.get(jobDId);
        TimePlanEntry entryD1 = planDList.get(0);
        SuccessEvent successEventD1 = new SuccessEvent(entryD1.getJobId(), entryD1.getTaskId(),
                scheduleTime, TaskType.RERUN, "test");
        controller.notify(successEventD1);
        Assert.assertEquals(5, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(4, taskQueue.size());

        List<TimePlanEntry> planEList = planMap.get(jobEId);
        TimePlanEntry entryE1 = planEList.get(0);
        SuccessEvent successEventE1 = new SuccessEvent(entryE1.getJobId(), entryE1.getTaskId(),
                scheduleTime, TaskType.RERUN, "test");
        controller.notify(successEventE1);
        Assert.assertEquals(4, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(5, taskQueue.size());

        TimePlanEntry entryD2 = planDList.get(1);
        SuccessEvent successEventD2 = new SuccessEvent(entryD2.getJobId(), entryD2.getTaskId(),
                scheduleTime, TaskType.RERUN, "test");
        controller.notify(successEventD2);
        Assert.assertEquals(3, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(5, taskQueue.size());

        TimePlanEntry entryE2 = planEList.get(1);
        SuccessEvent successEventE2 = new SuccessEvent(entryE2.getJobId(), entryE2.getTaskId(),
                scheduleTime, TaskType.RERUN, "test");
        controller.notify(successEventE2);
        Assert.assertEquals(2, taskGraph.getTaskMap().keySet().size());
        Assert.assertEquals(6, taskQueue.size());
    }

    private List<Long> getDependTaskIds(List<TimePlanEntry> planList, long dataTime, DependencyExpression dependencyExpression) {
        List<Long> dependTaskIds = new ArrayList<Long>();
        if (dependencyExpression == null) {
            for (TimePlanEntry entry : planList) {
                if (entry.getDateTime().getMillis() == dataTime) {
                    dependTaskIds.add(entry.getTaskId());
                    break;
                }
            }
        } else {
            Range<DateTime> range = dependencyExpression.getRange(new DateTime(dataTime));
            for (TimePlanEntry entry : planList) {
                if (range.contains(entry.getDateTime())) {
                    dependTaskIds.add(entry.getTaskId());
                }
            }
        }
        return dependTaskIds;
    }
}

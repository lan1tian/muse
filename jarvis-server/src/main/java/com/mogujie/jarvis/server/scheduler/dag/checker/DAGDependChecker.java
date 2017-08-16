/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月21日 上午10:50:41
 */

package com.mogujie.jarvis.server.scheduler.dag.checker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.mogujie.jarvis.core.expression.DefaultDependencyStrategyExpression;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.expression.DependencyStrategyExpression;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.core.domain.CommonStrategy;
import com.mogujie.jarvis.server.domain.JobDependencyEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.JobService;

/**
 * 单个任务的依赖检查器，内部维护Map<Long, JobDependStatus> jobScheduleMap进行依赖检查。
 * jobScheduleMap不是线程安全的数据结构，但是当前外部都是同步调用，不会有问题。
 *
 * @author guangming
 *
 */
@NotThreadSafe
public class DAGDependChecker {
    private long myJobId;

    // Map<JobId, JobDependStatus>
    private Map<Long, JobDependStatus> jobDependMap = Maps.newHashMap();

    private static final Logger LOGGER = LogManager.getLogger();

    public DAGDependChecker(long jobId) {
        this.myJobId = jobId;
    }

    public long getMyJobId() {
        return myJobId;
    }

    public void setMyJobId(long myJobId) {
        this.myJobId = myJobId;
    }

    public Map<Long, JobDependStatus> getJobDependMap() {
        return jobDependMap;
    }

    public void setJobDependMap(Map<Long, JobDependStatus> jobDependMap) {
        this.jobDependMap = jobDependMap;
    }

    public boolean checkDependency(Set<Long> needJobs, long scheduleTime) {
        boolean finishDependencies = true;
        for (long jobId : needJobs) {
            JobDependStatus dependStatus = jobDependMap.get(jobId);
            if (dependStatus == null) {
                dependStatus = create(myJobId, jobId);
                LOGGER.info("create JobDependStatus, myJobId={}, preJobId={}, dependencyExpression={},"
                        + "dependencyStrategy={}", myJobId, jobId, dependStatus.getDependencyExpression(),
                        dependStatus.getDependencyStrategy());
                jobDependMap.put(jobId, dependStatus);
            }

            if (!dependStatus.check(scheduleTime)) {
                finishDependencies = false;
                break;
            }
        }

        autoFix(needJobs);

        return finishDependencies;
    }

    /**
     * return Map<JobId, List<preTaskId>>
     *
     */
    public Map<Long, List<Long>> getDependTaskIdMap(long scheduleTime) {
        Map<Long, List<Long>> dependTaskMap = Maps.newHashMap();
        for (Entry<Long, JobDependStatus> entry : jobDependMap.entrySet()) {
            List<Long> preTaskIds = new ArrayList<Long>();
            List<Task> preTasks = entry.getValue().getDependTasks(scheduleTime);
            for (Task task : preTasks) {
                preTaskIds.add(task.getTaskId());
            }
            dependTaskMap.put(entry.getKey(), preTaskIds);
        }
        return dependTaskMap;
    }

    public void updateDependency(long preJobId) {
        JobDependStatus dependStatus = jobDependMap.get(preJobId);
        if (dependStatus != null) {
            dependStatus.updateDependency();
            LOGGER.info("update dependency, new expression={}, new strategy={}",
                    dependStatus.getDependencyExpression(), dependStatus.getDependencyStrategy());
        }
    }

    private void autoFix(Set<Long> needJobs) {
        Iterator<Entry<Long, JobDependStatus>> it = jobDependMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Long, JobDependStatus> entry = it.next();
            long jobId = entry.getKey();
            if (!needJobs.contains(jobId)) {
                it.remove();
                LOGGER.warn("needJobs[{}] not contains JobDependStatus[jobId={}], auto fix to remove it.", needJobs, jobId);
            }
        }
    }

    private JobDependStatus create(long myJobId, long preJobId) {
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        DependencyExpression dependencyExpression = null;
        DependencyStrategyExpression commonStrategy = new DefaultDependencyStrategyExpression(CommonStrategy.ALL.getExpression());
        Map<Long, JobDependencyEntry> dependencyMap = jobService.get(myJobId).getDependencies();
        if (dependencyMap != null && dependencyMap.containsKey(preJobId)) {
            dependencyExpression = dependencyMap.get(preJobId).getDependencyExpression();
            commonStrategy = dependencyMap.get(preJobId).getDependencyStrategyExpression();
        }
        JobDependStatus dependSchedule = new JobDependStatus(myJobId, preJobId, dependencyExpression, commonStrategy);

        return dependSchedule;
    }
}

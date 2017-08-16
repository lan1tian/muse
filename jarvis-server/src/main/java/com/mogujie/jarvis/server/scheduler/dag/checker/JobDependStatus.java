/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月18日 上午11:56:07
 */

package com.mogujie.jarvis.server.scheduler.dag.checker;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Range;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.expression.DependencyExpression;
import com.mogujie.jarvis.core.expression.DependencyStrategyExpression;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.domain.JobDependencyEntry;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.JobService;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * 任务A对任务B的依赖检查器。
 *
 * @author guangming
 *
 */
public class JobDependStatus {
    private long myJobId;
    private long preJobId;
    private DependencyExpression dependencyExpression;
    private DependencyStrategyExpression dependencyStrategy;
    private static final Logger LOGGER = LogManager.getLogger();

    public JobDependStatus(long myJobId, Long preJobId, DependencyExpression dependencyExpression, DependencyStrategyExpression dependencyStrategy) {
        this.myJobId = myJobId;
        this.preJobId = preJobId;
        this.dependencyExpression = dependencyExpression;
        this.dependencyStrategy = dependencyStrategy;
    }

    public long getMyJobId() {
        return myJobId;
    }

    public void seMyJobId(long myJobId) {
        this.myJobId = myJobId;
    }

    public long getPreJobId() {
        return preJobId;
    }

    public void setPreJobId(long preJobId) {
        this.preJobId = preJobId;
    }

    public DependencyExpression getDependencyExpression() {
        return dependencyExpression;
    }

    public DependencyStrategyExpression getDependencyStrategy() {
        return dependencyStrategy;
    }

    public void updateDependency() {
        JobService jobService = Injectors.getInjector().getInstance(JobService.class);
        JobDependencyEntry entry = jobService.get(myJobId).getDependencies().get(preJobId);
        dependencyExpression = entry.getDependencyExpression();
        dependencyStrategy = entry.getDependencyStrategyExpression();
    }

    /**
     * check dependency
     *
     * @param scheduleTime
     */
    public boolean check(long scheduleTime) {
        boolean pass = false;

        // runtime
        if (dependencyExpression == null) {
            // not supported
        } else {
            // offset
            List<Task> tasks = getDependTasks(scheduleTime);
            List<Boolean> taskStatus = new ArrayList<Boolean>();
            for (Task task : tasks) {
                boolean status = (task.getStatus() == TaskStatus.SUCCESS.getValue() ||
                        task.getStatus() == TaskStatus.REMOVED.getValue()) ? true : false;
                taskStatus.add(status);
            }
            pass = dependencyStrategy.check(taskStatus);
            if (!pass) {
                LOGGER.debug("check failed. myJobId={}, preJobId={}, dependencyExpression={}, dependencyStrategy={}, scheduleTime={}",
                        myJobId, preJobId, dependencyExpression, dependencyStrategy, new DateTime(scheduleTime));
            } else {
                LOGGER.info("check success. myJobId={}, preJobId={}, dependencyExpression={}, dependencyStrategy={}, scheduleTime={}",
                        myJobId, preJobId, dependencyExpression, dependencyStrategy, new DateTime(scheduleTime));
            }
        }

        return pass;
    }

    public List<Task> getDependTasks(long scheduleTime) {
        TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);
        DateTime scheduleDate = new DateTime(scheduleTime);
        Range<DateTime> range = dependencyExpression.getRange(scheduleDate);
        return taskService.getTasksBetween(preJobId, range);
    }
}

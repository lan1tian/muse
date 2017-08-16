/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 上午11:46:32
 */

package com.mogujie.jarvis.server.scheduler.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.server.guice.Injectors;
import com.mogujie.jarvis.server.service.TaskDependService;
import com.mogujie.jarvis.server.service.TaskService;

/**
 * @author guangming
 *
 */
public class DAGTask {
    private long jobId;
    private long taskId;
    private int attemptId;
    private long scheduleTime;
    private long dataTime;
    private TaskGraph taskGraph = TaskGraph.INSTANCE;
    private TaskService taskService = Injectors.getInjector().getInstance(TaskService.class);

    // 正常调度逻辑，调度时间和数据时间一样
    public DAGTask(long jobId, long taskId, int attemptId, long scheduleTime) {
        this(jobId, taskId, attemptId, scheduleTime, scheduleTime);
    }

    public DAGTask(long jobId, long taskId, int attemptId, long scheduleTime, long dataTime) {
        this.jobId = jobId;
        this.taskId = taskId;
        this.attemptId = attemptId;
        this.scheduleTime = scheduleTime;
        this.dataTime = dataTime;
    }

    // 正常调度逻辑，调度时间和数据时间一样
    public DAGTask(long jobId, long taskId, long scheduleTime, Map<Long, List<Long>> dependTaskIdMap) {
        this(jobId, taskId, 1, scheduleTime, scheduleTime, dependTaskIdMap);
    }

    public DAGTask(long jobId, long taskId, long scheduletime, long dataTime, Map<Long, List<Long>> dependTaskIdMap) {
        this(jobId, taskId, 1, scheduletime, dataTime, dependTaskIdMap);
    }

    public DAGTask(long jobId, long taskId, int attemptId, long scheduleTime, long dataTime, Map<Long, List<Long>> dependTaskIdMap) {
        this.jobId = jobId;
        this.taskId = taskId;
        this.attemptId = attemptId;
        this.scheduleTime = scheduleTime;
        this.dataTime = dataTime;
        if (dependTaskIdMap != null && !dependTaskIdMap.isEmpty()) {
            // store parent dependency
            TaskDependService taskDependService = Injectors.getInjector().getInstance(TaskDependService.class);
            taskDependService.storeParent(taskId, dependTaskIdMap);
            // add child dependency
            for (List<Long> taskIds : dependTaskIdMap.values()) {
                for (long parentTaskId : taskIds) {
                    taskDependService.addChildDependency(parentTaskId, jobId, taskId);
                }
            }
        }
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public long getDataTime() {
        return dataTime;
    }

    public void setDataTime(long dataTime) {
        this.dataTime = dataTime;
    }

    public boolean checkStatus() {
        boolean pass = true;
        List<DAGTask> parents = taskGraph.getParents(taskId);
        for (DAGTask parent : parents) {
            Task task = taskService.get(parent.getTaskId());
            if (task == null || task.getStatus() != TaskStatus.SUCCESS.getValue()) {
                pass = false;
                break;
            }
        }

        return pass;
    }

    public List<Long> getDependTaskIds() {
        // load dependTaskIdMap from taskDependService
        TaskDependService taskDependService = Injectors.getInjector().getInstance(TaskDependService.class);
        Map<Long, List<Long>> dependTaskIdMap = taskDependService.loadParent(taskId);
        List<Long> dependTaskIds = new ArrayList<Long>();
        for (List<Long> taskIds : dependTaskIdMap.values()) {
            dependTaskIds.addAll(taskIds);
        }

        return dependTaskIds;
    }

    @Override
    public String toString() {
        return "DAGTask [jobId=" + jobId + ", taskId=" + taskId + ", attemptId=" + attemptId + ", scheduleTime=" +  new DateTime(scheduleTime) + ", dataTime="
                + new DateTime(dataTime) + "]";
    }

}

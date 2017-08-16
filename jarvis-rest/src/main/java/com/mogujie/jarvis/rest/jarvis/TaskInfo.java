/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月26日 下午5:49:46
 */

package com.mogujie.jarvis.rest.jarvis;

import org.joda.time.DateTime;

import com.mogujie.jarvis.protocol.TaskInfoEntryProtos.TaskInfoEntry;

/**
 * @author guangming
 *
 */
public class TaskInfo {

    private long taskId;
    private long jobId;
    private String taskTitle;
    private String runDate;
    private String dataDate;
    private String startTime;
    private String endTime;
    private float avgTime;
    private float useTime;
    private int status;

    public TaskInfo(TaskInfoEntry taskInfoEntry) {
        this.taskId = taskInfoEntry.getTaskId();
        this.jobId = taskInfoEntry.getJobId();
        this.taskTitle = taskInfoEntry.getJobName();
        this.runDate = new DateTime(taskInfoEntry.getScheduleTime()).toString("yyyy-MM-dd");
        this.dataDate = new DateTime(taskInfoEntry.getDataTime()).toString("yyyy-MM-dd");
        this.startTime = new DateTime(taskInfoEntry.getStartTime()).toString("yyyy-MM-dd hh:mm:ss");
        this.endTime = new DateTime(taskInfoEntry.getEndTime()).toString("yyyy-MM-dd hh:mm:ss");
        this.avgTime = taskInfoEntry.getAvgTime();
        this.useTime = taskInfoEntry.getUseTime();
        this.status = taskInfoEntry.getStatus();
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(float avgTime) {
        this.avgTime = avgTime;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskInfo [taskId=" + taskId + ", jobId=" + jobId + ", taskTitle=" + taskTitle + ", runDate=" + runDate + ", dataDate=" + dataDate
                + ", startTime=" + startTime + ", endTime=" + endTime + ", avgTime=" + avgTime + ", useTime=" + useTime + ", status=" + status + "]";
    }

}

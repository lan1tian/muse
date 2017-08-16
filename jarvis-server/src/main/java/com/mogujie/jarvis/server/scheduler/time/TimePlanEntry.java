/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月23日 下午2:35:51
 */

package com.mogujie.jarvis.server.scheduler.time;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.joda.time.DateTime;

import com.google.common.collect.Maps;

public class TimePlanEntry  {
    private final long jobId;
    private final DateTime dateTime;
    private long taskId;
    private Map<Long, List<Long>> dependTaskIdMap = Maps.newHashMap();

    public TimePlanEntry(long jobId, DateTime dateTime) {
        this.jobId = jobId;
        this.dateTime = dateTime;
    }

    public TimePlanEntry(long jobId, DateTime dateTime, long taskId) {
        this.jobId = jobId;
        this.dateTime = dateTime;
        this.taskId = taskId;
    }

    public TimePlanEntry(long jobId, DateTime dateTime, Map<Long, List<Long>> dependTaskIdMap) {
        this.jobId = jobId;
        this.dateTime = dateTime;
        this.dependTaskIdMap = dependTaskIdMap;
    }

    public long getJobId() {
        return jobId;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Map<Long, List<Long>> getDependTaskIdMap() {
        return dependTaskIdMap;
    }

    public void setDependTaskIdMap(Map<Long, List<Long>> dependTaskIdMap) {
        this.dependTaskIdMap = dependTaskIdMap;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, dateTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof TimePlanEntry)) {
            return false;
        }

        TimePlanEntry other = (TimePlanEntry) obj;
        if (taskId > 0 && other.getTaskId() > 0 && taskId == other.getTaskId()) {
            return true;
        } else if (jobId == other.getJobId() && dateTime.isEqual(other.getDateTime())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "TimePlanEntry [jobId=" + jobId + ", dateTime=" + dateTime + ", taskId=" + taskId + ", dependTaskIdMap=" + dependTaskIdMap + "]";
    }

}

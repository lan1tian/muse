/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 上午11:34:53
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.mogujie.jarvis.core.domain.JobContentType;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.protocol.JobInfoEntryProtos.JobInfoEntry;

public class JobInfoResult extends Result {
    private static final long serialVersionUID = -3564148230890004211L;
    private long id;
    private String cronExp;
    private String cronExpExplain;
    private Integer scriptId;
    private String title;
    private Integer priority;
    private String publisher;
    private String receiver;
    private Integer status;
    private String publishTime;
    private String editTime;
    private String startDate;
    private String endDate;
    private String pline;
    private String department;
    private List<PreTask> preTasks;

    public JobInfoResult() {}

    public JobInfoResult(JobInfoEntry jobInfo) {
        this.id = jobInfo.getJobId();
        //适配老系统cron表达式
        String exp = jobInfo.getScheduleExpression();
        if (exp != null && exp.length() > 1) {
            this.cronExp = exp.substring(1).trim();
        }

        if (jobInfo.getContentType() == JobContentType.SCRIPT.getValue()) {
            this.scriptId = Integer.valueOf(jobInfo.getContent());
        }
        this.title = jobInfo.getJobName();
        this.priority = jobInfo.getPriority();
        this.publisher = jobInfo.getUser();
        this.receiver = jobInfo.getReceiver();
        this.publishTime = new DateTime(jobInfo.getCreateTime()).toString("yyyy-MM-dd hh:mm:ss");
        this.editTime = new DateTime(jobInfo.getUpdateTime()).toString("yyyy-MM-dd hh:mm:ss");
        this.startDate = new DateTime(jobInfo.getStartDate()).toString("yyyy-MM-dd");
        this.endDate = new DateTime(jobInfo.getEndDate()).toString("yyyy-MM-dd");
        this.pline = jobInfo.getBizName();
        this.department = jobInfo.getDepartment();
        //适配老jarvis状态
        int newStatus = jobInfo.getStatus();
        if (newStatus == JobStatus.ENABLE.getValue()) {
            this.status = TaskStatusEnum.ENABLE.getValue();
        } else {
            this.status = TaskStatusEnum.DISABLE.getValue();
        }
    }

    static class PreTask {
        private long id;
        private String title;

        public PreTask(long id, String title) {
            this.id = id;
            this.title = title;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public String getCronExpExplain() {
        return cronExpExplain;
    }

    public void setCronExpExplain(String cronExpExplain) {
        this.cronExpExplain = cronExpExplain;
    }

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPline() {
        return pline;
    }

    public void setPline(String pline) {
        this.pline = pline;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<PreTask> getPreTasks() {
        return preTasks;
    }

    public void setPreTasks(List<PreTask> preTasks) {
        this.preTasks = preTasks;
    }

    public void setPreJobInfos(List<JobInfoEntry> preJobInfos) {
        this.preTasks = new ArrayList<PreTask>();
        for (JobInfoEntry jobInfo : preJobInfos) {
            preTasks.add(new PreTask(jobInfo.getJobId(), jobInfo.getJobName()));
        }
    }

}

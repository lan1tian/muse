/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午1:16:31
 */

package com.mogujie.jarvis.rest.jarvis;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mogujie.jarvis.core.domain.JobContentType;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.protocol.JobInfoEntryProtos.JobInfoEntry;

public class JobInfo implements Serializable {

    private static final long serialVersionUID = 6129998102397429730L;
    public static final String TARGETMETHOD = "run";

    private Long id;
    private String cronExp;
    private String cronExpExplain;
    private Integer scriptId;
    private String title;
    private Integer priority;
    private String publisher;
    private String receiver;
    private String publishTime;
    private String editTime;
    private String startDate;
    private String endDate;
    private Integer status;

    //脚本所属部门
    private String department;
    //脚本所属生产线
    private String pline;

    // 前置程序IDS
    private String preTaskIds;

    public JobInfo(String username, Integer scriptId, String title,
                String cronExp, String startDate, String endDate, String receiver) {
        this.publisher = username;
        this.scriptId = scriptId;
        this.title = title;
        this.cronExp = cronExp;
        this.startDate = startDate;
        this.endDate = endDate;
        this.receiver = receiver;
        this.status = new Integer(1);
        this.priority = TaskPriorityEnum.MID.getValue();
    }

    public JobInfo(JobInfoEntry jobInfoEntry) {
        this.id = jobInfoEntry.getJobId();
        // 适配老系统cron表达式
        String exp = jobInfoEntry.getScheduleExpression();
        if (exp != null && exp.length() > 1) {
            this.cronExp = exp.substring(1).trim();
        }

        if (jobInfoEntry.getContentType() == JobContentType.SCRIPT.getValue()) {
            this.scriptId = Integer.valueOf(jobInfoEntry.getContent());
        }
        this.title = jobInfoEntry.getJobName();
        this.priority = jobInfoEntry.getPriority();
        this.publisher = jobInfoEntry.getUser();
        this.receiver = jobInfoEntry.getReceiver();
        this.publishTime = new DateTime(jobInfoEntry.getCreateTime()).toString();
        this.editTime = new DateTime(jobInfoEntry.getUpdateTime()).toString();
        this.startDate = new DateTime(jobInfoEntry.getStartDate()).toString();
        this.endDate = new DateTime(jobInfoEntry.getEndDate()).toString();
        this.pline = jobInfoEntry.getBizName();
        this.department = jobInfoEntry.getDepartment();
        //适配老jarvis状态
        int newStatus = jobInfoEntry.getStatus();
        if (newStatus == JobStatus.ENABLE.getValue()) {
            this.status = TaskStatusEnum.ENABLE.getValue();
        } else if (newStatus == JobStatus.DISABLE.getValue() ||
                newStatus == JobStatus.PAUSE.getValue()) {
            this.status = TaskStatusEnum.DISABLE.getValue();
        } else {
            this.status = TaskStatusEnum.DELETE.getValue();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String fetchPriorityDisplay() {
        return TaskPriorityEnum.get(getPriority()).getDescription();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getPreTaskIds() {
        return preTaskIds;
    }

    public void setPreTaskIds(String preTaskIds) {
        this.preTaskIds = preTaskIds;
    }

    public String getPriorityDisplay() {
        return TaskPriorityEnum.get(getPriority()).getDescription();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPline() {
        return pline;
    }

    public void setPline(String pline) {
        this.pline = pline;
    }

    public String toString() {
        return " crontab:"+cronExp+" scriptId:"+scriptId
                +" title:"+title+" priority:"+priority+" receiver:"
                +receiver+" publisher:"+publisher+" status:"+status;
    }
}

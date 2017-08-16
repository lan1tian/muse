package com.mogujie.jarvis.web.entity.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author muming ,hejian
 */
public class TaskDependVo {

    private Long taskId;
    private Integer status;
    private Long jobId;
    private String jobName;
    private Date scheduleTime;
    private Date executeStartTime;
    private Date executeEndTime;
    private Long executeTime;
    private String executeUser;

    private String dependTaskIds;
    private String childTaskIds;
    private List<TaskDependVo> children = new ArrayList<TaskDependVo>();
    private List<TaskDependVo> parents = new ArrayList<TaskDependVo>();

    private boolean parentFlag = false;
    private boolean rootFlag = false;


    public Long getTaskId() {
        return taskId;
    }

    public TaskDependVo setTaskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getDependTaskIds() {
        return dependTaskIds;
    }

    public TaskDependVo setDependTaskIds(String dependTaskIds) {
        this.dependTaskIds = dependTaskIds;
        return this;
    }

    public String getChildTaskIds() {
        return childTaskIds;
    }

    public TaskDependVo setChildTaskIds(String childTaskIds) {
        this.childTaskIds = childTaskIds;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public TaskDependVo setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public TaskDependVo setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TaskDependVo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public TaskDependVo setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
        return this;
    }

    public Date getExecuteStartTime() {
        return executeStartTime;
    }

    public TaskDependVo setExecuteStartTime(Date executeStartTime) {
        this.executeStartTime = executeStartTime;
        return this;
    }

    public Date getExecuteEndTime() {
        return executeEndTime;
    }

    public TaskDependVo setExecuteEndTime(Date executeEndTime) {
        this.executeEndTime = executeEndTime;
        return this;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public TaskDependVo setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public String getExecuteUser() {
        return executeUser;
    }

    public TaskDependVo setExecuteUser(String executeUser) {
        this.executeUser = executeUser;
        return this;
    }

    public List<TaskDependVo> getChildren() {
        return children;
    }

    public TaskDependVo setChildren(List<TaskDependVo> children) {
        this.children = children;
        return this;
    }

    public List<TaskDependVo> getParents() {
        return parents;
    }

    public TaskDependVo setParents(List<TaskDependVo> parents) {
        this.parents = parents;
        return this;
    }

    public boolean isParentFlag() {
        return parentFlag;
    }

    public TaskDependVo setParentFlag(boolean parentFlag) {
        this.parentFlag = parentFlag;
        return this;
    }

    public boolean isRootFlag() {
        return rootFlag;
    }

    public TaskDependVo setRootFlag(boolean rootFlag) {
        this.rootFlag = rootFlag;
        return this;
    }
}
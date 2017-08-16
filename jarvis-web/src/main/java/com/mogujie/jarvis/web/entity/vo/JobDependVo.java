package com.mogujie.jarvis.web.entity.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author muming ,hejian
 */
public class JobDependVo {

    //job——信息
    private Long jobId;
    private String jobName;
    private Integer status;
    private Integer priority;
    private String jobType;
    private String submitUser;
    private String bizGroups;
    private String bizGroupName;
    private Date activeStartDate;
    private Date activeEndDate;

    private String appName;
    private String appKey;
    private Map<String,Object> state;
    private Integer commonStrategy;
    private String offsetStrategy;

    //task——信息
    private List<TaskVo> taskList = new ArrayList<TaskVo>();

    //依赖节点——信息
    private List<JobDependVo> children = new ArrayList<JobDependVo>();
    private List<JobDependVo> parents = new ArrayList<JobDependVo>();
    private boolean parentFlag=false;
    private boolean rootFlag=false;

    //绘图node——信息
    private String text;
    private Map li_attr;
    private Long value;
    private boolean openedFlag;

    public Long getJobId() {
        return jobId;
    }

    public JobDependVo setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public JobDependVo setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JobDependVo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public JobDependVo setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public String getJobType() {
        return jobType;
    }

    public JobDependVo setJobType(String jobType) {
        this.jobType = jobType;
        return this;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public JobDependVo setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
        return this;
    }

    public String getBizGroups() {
        return bizGroups;
    }

    public JobDependVo setBizGroups(String bizGroups) {
        this.bizGroups = bizGroups;
        return this;
    }

    public String getBizGroupName() {
        return bizGroupName;
    }

    public JobDependVo setBizGroupName(String bizGroupName) {
        this.bizGroupName = bizGroupName;
        return this;
    }

    public Date getActiveStartDate() {
        return activeStartDate;
    }

    public JobDependVo setActiveStartDate(Date activeStartDate) {
        this.activeStartDate = activeStartDate;
        return this;
    }

    public Date getActiveEndDate() {
        return activeEndDate;
    }

    public JobDependVo setActiveEndDate(Date activeEndDate) {
        this.activeEndDate = activeEndDate;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public JobDependVo setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public JobDependVo setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public JobDependVo setState(Map<String, Object> state) {
        this.state = state;
        return this;
    }

    public Integer getCommonStrategy() {
        return commonStrategy;
    }

    public JobDependVo setCommonStrategy(Integer commonStrategy) {
        this.commonStrategy = commonStrategy;
        return this;
    }

    public String getOffsetStrategy() {
        return offsetStrategy;
    }

    public JobDependVo setOffsetStrategy(String offsetStrategy) {
        this.offsetStrategy = offsetStrategy;
        return this;
    }

    public List<TaskVo> getTaskList() {
        return taskList;
    }

    public JobDependVo setTaskList(List<TaskVo> taskList) {
        this.taskList = taskList;
        return this;
    }

    public List<JobDependVo> getChildren() {
        return children;
    }

    public JobDependVo setChildren(List<JobDependVo> children) {
        this.children = children;
        return this;
    }

    public List<JobDependVo> getParents() {
        return parents;
    }

    public JobDependVo setParents(List<JobDependVo> parents) {
        this.parents = parents;
        return this;
    }

    public boolean isParentFlag() {
        return parentFlag;
    }

    public JobDependVo setParentFlag(boolean parentFlag) {
        this.parentFlag = parentFlag;
        return this;
    }

    public boolean isRootFlag() {
        return rootFlag;
    }

    public JobDependVo setRootFlag(boolean rootFlag) {
        this.rootFlag = rootFlag;
        return this;
    }

    public String getText() {
        return text;
    }

    public JobDependVo setText(String text) {
        this.text = text;
        return this;
    }

    public Long getValue() {
        return value;
    }

    public JobDependVo setValue(Long value) {
        this.value = value;
        return this;
    }

    public boolean isOpenedFlag() {
        return openedFlag;
    }

    public JobDependVo setOpenedFlag(boolean openedFlag) {
        this.openedFlag = openedFlag;
        return this;
    }

    public Map getLi_attr() {
        return li_attr;
    }

    public void setLi_attr(Map li_attr) {
        this.li_attr = li_attr;
    }
}

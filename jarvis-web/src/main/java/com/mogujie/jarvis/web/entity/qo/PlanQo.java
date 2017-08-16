package com.mogujie.jarvis.web.entity.qo;

import com.mogujie.jarvis.core.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejian on 15/10/21.
 * 备注:setter方法重写过，勿删除
 */
public class PlanQo {
    private List<Long> jobIdList;
    private List<String> jobNameList;
    private List<String> jobTypeList;
    private List<Integer> priorityList;

    private List<String> submitUserList;
    private List<String> executeUserList;
    private List<String> taskStatusList;

    private String scheduleDate;
    private int scheduleStartTime;
    private int scheduleEndTime;


    private Boolean unInitial;
    private String dataTime;
    private Integer offset;
    private Integer limit;
    private String sort;
    private String order;

    public List<Long> getJobIdList() {
        return jobIdList;
    }

    public void setJobIdList(String jobIdList) {
        if (StringUtils.isNotBlank(jobIdList)) {
            List<Long> list = JsonHelper.fromJson(jobIdList, List.class);
            if (list.size() > 0) {
                this.jobIdList = list;
            }
        }

    }

    public List<String> getJobNameList() {
        return jobNameList;
    }

    public void setJobNameList(String jobNameList) {
        if (StringUtils.isNotBlank(jobNameList)) {
            List<String> list = JsonHelper.fromJson(jobNameList, List.class);
            if (list.size() > 0) {
                this.jobNameList = list;
            }
        }

    }

    public List<String> getJobTypeList() {
        return jobTypeList;
    }

    public void setJobTypeList(String jobTypeList) {
        if (StringUtils.isNotBlank(jobTypeList)) {
            List<String> list = JsonHelper.fromJson(jobTypeList, List.class);
            if (list.size() > 0) {
                this.jobTypeList = list;
            }
        }
    }

    public List<Integer> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(String priorityList) {
        if (StringUtils.isNotBlank(priorityList)) {
            List<Integer> list = JsonHelper.fromJson(priorityList, List.class);
            if (list.size() > 0) {
                this.priorityList = list;
            }
        }
    }

    public List<String> getSubmitUserList() {
        return submitUserList;
    }

    public void setSubmitUserList(String submitUserList) {
        if (StringUtils.isNotBlank(submitUserList)) {
            List<String> list = JsonHelper.fromJson(submitUserList, List.class);
            if (list.size() > 0) {
                this.submitUserList = list;
            }
        }
    }

    public List<String> getExecuteUserList() {
        return executeUserList;
    }

    public void setExecuteUserList(String executeUserList) {
        if (StringUtils.isNotBlank(executeUserList)) {
            List<String> list = JsonHelper.fromJson(executeUserList, List.class);
            if (list.size() > 0) {
                this.executeUserList = list;
            }
        }
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public PlanQo setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public int getScheduleStartTime() {
        return scheduleStartTime;
    }

    public PlanQo setScheduleStartTime(int scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
        return this;
    }

    public int getScheduleEndTime() {
        return scheduleEndTime;
    }

    public PlanQo setScheduleEndTime(int scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
        return this;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(String taskStatusList) {
        if (StringUtils.isNotBlank(taskStatusList)) {
            List<String> list = JsonHelper.fromJson(taskStatusList, List.class);
            this.taskStatusList = list;
        } else {
            this.taskStatusList = new ArrayList<>();
        }
    }

    public Boolean isUnInitial() {
        return unInitial;
    }

    public void setUnInitial(Boolean unInitial) {
        this.unInitial = unInitial;
    }
}

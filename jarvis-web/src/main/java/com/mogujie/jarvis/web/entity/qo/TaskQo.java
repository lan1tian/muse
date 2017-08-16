package com.mogujie.jarvis.web.entity.qo;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.dto.generate.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by hejian on 15/9/17.
 * 备注:setter方法重写过，勿删除
 */
public class TaskQo {
    private String scheduleDate;
    private String executeDate;
    private String startDate;
    private String endDate;
    private List<String> taskIdList;
    private List<String> jobIdList;
    private List<String> jobNameList;
    private List<String> jobTypeList;
    private List<String> executeUserList;
    private String taskStatusArrStr;
    private List<Integer> taskStatus;

    private List<String> isTemp;
    private String order;
    private Integer offset;
    private Integer limit;
    private String sort;


    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
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

    public List<String> getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(String isTemp) {
        if (StringUtils.isNotBlank(isTemp)) {
            List<String> list = JsonHelper.fromJson(isTemp, List.class);
            if (list.size() > 0) {
                this.isTemp = list;
            }
        }
    }

    public List<String> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(String taskIdList) {
        if (StringUtils.isNotBlank(taskIdList)) {
            List<String> list = JsonHelper.fromJson(taskIdList, List.class);
            if (list.size() > 0) {
                this.taskIdList = list;
            }
        }
    }

    public List<String> getJobIdList() {
        return jobIdList;
    }

    public void setJobIdList(String jobIdList) {
        if (StringUtils.isNotBlank(jobIdList)) {
            List<String> list = JsonHelper.fromJson(jobIdList, List.class);
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

    public String getTaskStatusArrStr() {
        return taskStatusArrStr;
    }

    public void setTaskStatusArrStr(String taskStatusArrStr) {
        this.taskStatusArrStr = taskStatusArrStr;
    }

    public List<Integer> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(List<Integer> taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
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
}

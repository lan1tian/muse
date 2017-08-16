package com.mogujie.jarvis.web.entity.qo;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.dto.generate.Job;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by hejian on 15/9/15.
 * 备注:setter方法重写过，勿删除
 */
public class JobQo {
    private Long jobId;
    private List<String> jobIdList;
    private List<String> jobNameList;
    private List<String> jobTypeList;
    private List<String> statusList;
    private List<String> submitUserList;
    private List<String> priorityList;
    private List<String> appIdList;
    private List<String> workerGroupIdList;

    private List<String> isTempList;

    private Integer offset;
    private Integer limit;
    private String order;
    private String sort;

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

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(String statusList) {
        if (StringUtils.isNotBlank(statusList)) {
            List<String> list = JsonHelper.fromJson(statusList, List.class);
            if (list.size() > 0) {
                this.statusList = list;
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

    public List<String> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(String priorityList) {
        if (StringUtils.isNotBlank(priorityList)) {
            List<String> list = JsonHelper.fromJson(priorityList, List.class);
            if (list.size() > 0) {
                this.priorityList = list;
            }
        }
    }

    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(String appIdList) {
        if (StringUtils.isNotBlank(appIdList)) {
            List<String> list = JsonHelper.fromJson(appIdList, List.class);
            if (list.size() > 0) {
                this.appIdList = list;
            }
        }
    }

    public List<String> getWorkerGroupIdList() {
        return workerGroupIdList;
    }

    public void setWorkerGroupIdList(String workerGroupIdList) {
        if (StringUtils.isNotBlank(workerGroupIdList)) {
            List<String> list = JsonHelper.fromJson(workerGroupIdList, List.class);
            if (list.size() > 0) {
                this.workerGroupIdList = list;
            }
        }
    }

    public List<String> getIsTempList() {
        return isTempList;
    }

    public void setIsTempList(String isTempList) {
        if (StringUtils.isNotBlank(isTempList)) {
            List<String> list = JsonHelper.fromJson(isTempList, List.class);
            if (list.size() > 0) {
                this.isTempList = list;
            }
        }
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月3日 上午11:34:33
 */

package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * @author guangming
 *
 */
public class TaskRerunVo extends  AbstractVo {
    private List<Long> jobIdList;
    private Long startDate;
    private Long endDate;

    public List<Long> getJobIdList() {
        return jobIdList;
    }
    public void setJobIdList(List<Long> jobIdList) {
        this.jobIdList = jobIdList;
    }
    public Long getStartDate() {
        return startDate;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }
    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}

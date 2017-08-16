package com.mogujie.jarvis.dto.generate;

import java.util.Date;

public class PlanKey {
    private Long jobId;

    private Date planTime;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }
}
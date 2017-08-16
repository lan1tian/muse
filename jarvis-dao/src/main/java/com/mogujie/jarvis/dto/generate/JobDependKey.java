package com.mogujie.jarvis.dto.generate;

public class JobDependKey {
    private Long jobId;

    private Long preJobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getPreJobId() {
        return preJobId;
    }

    public void setPreJobId(Long preJobId) {
        this.preJobId = preJobId;
    }
}
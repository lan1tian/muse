package com.mogujie.jarvis.web.entity.qo;

/**
 * Created by hejian on 16/1/12.
 */
public class AlarmQo {
    private Long id;
    private Long jobId;
    private String alarmType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
}

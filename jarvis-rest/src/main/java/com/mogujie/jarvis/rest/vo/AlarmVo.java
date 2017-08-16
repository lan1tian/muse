package com.mogujie.jarvis.rest.vo;

/**
 * AlarmVo类
 * @author muming
 */
public class AlarmVo extends  AbstractVo{

    private Long jobId;

    private String alarmType;

    private String receiver;

    private Integer status;

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

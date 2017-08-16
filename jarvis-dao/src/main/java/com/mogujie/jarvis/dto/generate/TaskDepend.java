package com.mogujie.jarvis.dto.generate;

import java.util.Date;

public class TaskDepend {
    private Long taskId;

    private Date createTime;

    private String dependTaskIds;

    private String childTaskIds;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDependTaskIds() {
        return dependTaskIds;
    }

    public void setDependTaskIds(String dependTaskIds) {
        this.dependTaskIds = dependTaskIds;
    }

    public String getChildTaskIds() {
        return childTaskIds;
    }

    public void setChildTaskIds(String childTaskIds) {
        this.childTaskIds = childTaskIds;
    }
}
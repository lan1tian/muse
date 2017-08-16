package com.mogujie.jarvis.dto.generate;

import java.util.Date;

public class JobDepend extends JobDependKey {
    private Integer commonStrategy;

    private String offsetStrategy;

    private Date createTime;

    private Date updateTime;

    private String updateUser;

    public Integer getCommonStrategy() {
        return commonStrategy;
    }

    public void setCommonStrategy(Integer commonStrategy) {
        this.commonStrategy = commonStrategy;
    }

    public String getOffsetStrategy() {
        return offsetStrategy;
    }

    public void setOffsetStrategy(String offsetStrategy) {
        this.offsetStrategy = offsetStrategy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
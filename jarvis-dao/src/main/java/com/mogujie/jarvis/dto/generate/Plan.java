package com.mogujie.jarvis.dto.generate;

import java.util.Date;

public class Plan extends PlanKey {
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
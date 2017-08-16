/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年12月3日 下午1:46:01
 */

package com.mogujie.jarvis.rest.vo;

public class SystemStatusResultVo extends  AbstractVo{
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public SystemStatusResultVo setStatus(Integer status) {
        this.status = status;
        return this;
    }
}

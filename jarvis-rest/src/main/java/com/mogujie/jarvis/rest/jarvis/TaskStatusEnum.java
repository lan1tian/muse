/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午8:25:03
 */

package com.mogujie.jarvis.rest.jarvis;

/**
 * @author yinxiu
 * @version $Id: ScriptTypeEnum.java,v 0.1 2013-7-17 下午4:00:25 yinxiu Exp $
 */
public enum TaskStatusEnum {
    ENABLE(new Integer(1), "启用"), DISABLE(new Integer(0), "禁用"), DELETE(
            new Integer(2), "已删除");

    private TaskStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    private Integer value;
    private String description;

    public Integer getValue() {
        return value;
    }


    public String getDescription() {
        return description;
    }


}


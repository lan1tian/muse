/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月22日 下午4:49:51
 */

package com.mogujie.jarvis.core.domain;

/**
 * @author guangming
 *
 */
public enum JobContentType {
    UNKNOWN(0, "未知"),
    TEXT(1, "文本"),
    SCRIPT(2, "脚本"),
    JAR(3, "jar包");

    private int value;
    private String description;

    JobContentType(int value, String description) {
        this.value = value;
        this.description=description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static JobContentType parseValue(int value) {
        JobContentType[] statusList = JobContentType.values();
        for (JobContentType s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("JobContentType value is invalid. value:" + value);
    }

}

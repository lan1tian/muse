/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年7月9日 下午16:55:35
 */
package com.mogujie.jarvis.core.domain;

/**
 * @author muming
 */
public enum WorkerStatus {
    ONLINE(1, "上线"), //下线
    OFFLINE(2, "下线");  //上线

    private int value;
    private String description;

    WorkerStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Boolean isValid(int value) {
        WorkerStatus[] values = WorkerStatus.values();
        for (WorkerStatus s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public static WorkerStatus parseValue(int value) {
        WorkerStatus[] statusList = WorkerStatus.values();
        for (WorkerStatus s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("WorkerStatus value is invalid. value:" + value);
    }

}

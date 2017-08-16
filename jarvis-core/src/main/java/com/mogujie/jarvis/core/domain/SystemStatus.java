/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月29日 上午11:10:39
 */

package com.mogujie.jarvis.core.domain;

/**
 *
 *
 */
public enum SystemStatus {

    RUNNING(1, "运行"),   //运行
    PAUSE(2, "停止");    //停止

    private int value;
    private String description;

    SystemStatus(int value, String description) {
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
        SystemStatus[] values = SystemStatus.values();
        for (SystemStatus s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public static SystemStatus parseValue(int value) throws IllegalArgumentException {
        SystemStatus[] statusList = SystemStatus.values();
        for (SystemStatus s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("SystemStatus value is invalid. value:" + value);
    }

}

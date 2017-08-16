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
public enum AppStatus {

    ENABLE(1, "启用"),      //启用
    DISABLED(2, "禁用"),    //禁用
    DELETE(3, "删除");      //删除

    private int value;
    private String description;

    AppStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public static Boolean isValid(int value) {
        AppStatus[] values = AppStatus.values();
        for (AppStatus s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
}

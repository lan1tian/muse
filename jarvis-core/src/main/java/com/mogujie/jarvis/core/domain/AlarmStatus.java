/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月29日 上午11:10:39
 */

package com.mogujie.jarvis.core.domain;

/**
 * 报警状态
 */
public enum AlarmStatus {

    ENABLE(1),      //有效
    DISABLED(2);    //无效

    private  int value;

    AlarmStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Boolean isValid(int value) {
        AlarmStatus[] values = AlarmStatus.values();
        for (AlarmStatus s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}

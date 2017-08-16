/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月12日 下午4:56:39
 */
package com.mogujie.jarvis.core.domain;

/**
 * @author muming
 */
public enum JobPriority {

    LOW(1), //低
    NORMAL(2), //正常
    HIGH(3),   //高
    VERY_HIGH(4); //很高

    private int value;

    JobPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static JobPriority parseValue(int value) {
        JobPriority[] all = JobPriority.values();
        for (JobPriority s : all) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("JobPriority value is invalid. value:" + value);
    }
}

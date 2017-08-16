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
public enum JobRelationType {

    PARENT(1),
    CHILD(2);

    private int value;

    JobRelationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Boolean isValid(int value) {
        JobRelationType[] values = JobRelationType.values();
        for (JobRelationType s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public static JobRelationType parseValue(int value) {
        JobRelationType[] all = JobRelationType.values();
        for (JobRelationType s : all) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("JobRelationType value is invalid. value:" + value);
    }

}

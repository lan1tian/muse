/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月29日 下午10:46:12
 */

package com.mogujie.jarvis.core.expression;

public enum ScheduleExpressionType {

    CRON(1,"Cron表达式"),
    FIXED_RATE(2,"固定频率"),
    FIXED_DELAY(3,"固定时延"),
    ISO8601(4,"ISO8601");

    private int value;
    private String description;

    ScheduleExpressionType(int value,String description) {
        this.value = value;
        this.description=description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ScheduleExpressionType parseValue(int value) throws IllegalArgumentException {
        ScheduleExpressionType[] typeList = ScheduleExpressionType.values();
        for (ScheduleExpressionType t : typeList) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new IllegalArgumentException("ScheduleExpressionType value is invalid. value:" + value);
    }

    public static Boolean isValid(int value) {
        ScheduleExpressionType[] values = ScheduleExpressionType.values();
        for (ScheduleExpressionType s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}

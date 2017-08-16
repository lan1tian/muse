/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年12月31日 下午3:51:19
 */

package com.mogujie.jarvis.core.domain;

/**
 * @author guangming
 *
 */
public enum TaskType {

    SCHEDULE(1,"自动调度"), //调度系统自动调度的task
    RERUN(2,"手动重跑"),    //手动重跑的task
    TEMP(3,"临时调度");     //一次性的临时task

    private int value;
    private String description;

    TaskType(int value,String description) {
        this.value = value;
        this.description=description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static TaskType parseValue(int value) {
        TaskType[] typeList = TaskType.values();
        for (TaskType s : typeList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("TaskType value is invalid. value:" + value);
    }

}

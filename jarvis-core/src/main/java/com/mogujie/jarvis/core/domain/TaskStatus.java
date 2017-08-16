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
public enum TaskStatus {

    UNKNOWN(0, "未知"), //未知
    WAITING(1, "等待"), //等待（条件未满足）
    READY(2, "准备"),   //准备（分发中）
    RUNNING(3, "运行"), //执行中
    SUCCESS(4, "成功"), //成功
    FAILED(5, "失败"),  //失败
    KILLED(6, "killed"),  //killed
    REMOVED(99, "删除"); //removed

    private int value;
    private String description;

    TaskStatus(int value, String description) {
        this.value = value;
        this.description=description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static TaskStatus parseValue(int value) {
        TaskStatus[] statusList = TaskStatus.values();
        for (TaskStatus s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("TaskStatus value is invalid. value:" + value);
    }
}

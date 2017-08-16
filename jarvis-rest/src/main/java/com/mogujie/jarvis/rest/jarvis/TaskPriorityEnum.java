/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午7:06:45
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gongye on 15-9-16.
 */

public enum TaskPriorityEnum {

    UNKNOW(new Integer(1), "UNKNOWN"),
    LOW(new Integer(0), "LOW"), MID(new Integer(5), "NORMAL"), HIGH(
            new Integer(10), "HIGH"), VERY_HIGH(new Integer(15), "VERY_HIGH"), RERUN(
            new Integer(9), "重跑");
    static Map<Integer, TaskPriorityEnum> pool = new HashMap<Integer, TaskPriorityEnum>();

    static {
        pool.put(new Integer(0), LOW);
        pool.put(new Integer(5), MID);
        pool.put(new Integer(10), HIGH);
        pool.put(new Integer(15), VERY_HIGH);
        pool.put(new Integer(9), RERUN);
    }

    private Integer value;
    private String description;

    private TaskPriorityEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TaskPriorityEnum get(Integer key) {

        if (null == key) {

            return UNKNOW;
        }
        for (TaskPriorityEnum taskPriorityEnum : TaskPriorityEnum.values()) {

            if (key.equals(taskPriorityEnum.getValue())) {

                return taskPriorityEnum;
            }
        }

        return UNKNOW;
    }

    public Integer getValue() {
        return value;
    }


    public String getDescription() {
        return description;
    }


}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月10日 下午3:50:06
 */
package com.mogujie.jarvis.core.domain;

/**
 * @author wuya
 */
public enum StreamType {
    STD_OUT(1,"OUT"), STD_ERR(2,"ERR");

    private int value;
    private String description;

    StreamType(int value,String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static StreamType parseValue(int value) throws IllegalArgumentException{
        StreamType[] all = StreamType.values();
        for (StreamType s : all) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("StreamType value is invalid. value:" + value);
    }

}

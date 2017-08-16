/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年10月16日 下午1:55:37
 */

package com.mogujie.jarvis.core.domain;

public enum MessageType {

    GENERAL(1), SYSTEM(2);

    private int type;

    private MessageType(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}

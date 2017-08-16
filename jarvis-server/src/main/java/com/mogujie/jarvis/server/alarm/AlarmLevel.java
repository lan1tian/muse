/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya Create Date: 2015年11月25日 下午1:20:18
 */

package com.mogujie.jarvis.server.alarm;

public enum AlarmLevel {
    ERROR(0), INFO(1), WARN(2), FATAL_ERROR(3);

    private int level;

    AlarmLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}

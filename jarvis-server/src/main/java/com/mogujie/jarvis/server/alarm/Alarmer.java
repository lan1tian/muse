/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya Create Date: 2015年11月25日 下午1:16:24
 */

package com.mogujie.jarvis.server.alarm;

import java.util.List;

import com.google.common.collect.Lists;
import com.mogujie.jarvis.core.domain.AlarmType;

public abstract class Alarmer {

    abstract public boolean alarm(AlarmLevel alarmLevel, List<AlarmType> alarmTypes, List<String> receiver, String message);

    public boolean alarm(List<AlarmType> alarmTypes, List<String> receiver, String message) {
        return alarm(AlarmLevel.ERROR, alarmTypes, receiver, message);
    }

    public boolean alarm(List<String> receiver, String message) {
        return alarm(AlarmLevel.ERROR, Lists.newArrayList(AlarmType.SMS), receiver, message);
    }
}

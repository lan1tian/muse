/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月2日 下午8:42:29
 */

package com.mogujie.jarvis.core.util;

import org.joda.time.DurationFieldType;

public class DurationFieldTypes {

    public static DurationFieldType valueOf(char c) {
        switch (c) {
            case 's':
                return DurationFieldType.seconds();
            case 'm':
                return DurationFieldType.minutes();
            case 'h':
                return DurationFieldType.hours();
            case 'd':
                return DurationFieldType.days();
            case 'w':
                return DurationFieldType.weeks();
            case 'M':
                return DurationFieldType.months();
            case 'y':
                return DurationFieldType.years();
            default:
                break;
        }

        return null;
    }

}

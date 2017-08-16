/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月12日 下午7:14:18
 */
package com.mogujie.jarvis.core.util;

import com.google.common.base.Throwables;

public class ThreadUtils {

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }
}

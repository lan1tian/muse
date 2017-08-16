/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月26日 上午9:56:18
 */

package com.mogujie.jarvis.core.util;

/**
 * @author guangming
 *
 */
public class ExceptionUtil {
    public static String getErrMsg(Exception e) {
        return (e.getMessage() == null ? e.toString() : e.getMessage());
    }
}

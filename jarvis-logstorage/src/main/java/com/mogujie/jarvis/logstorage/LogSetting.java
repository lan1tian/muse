/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月7日 上午10:13:42
 */

package com.mogujie.jarvis.logstorage;

import com.mogujie.jarvis.core.util.ConfigUtils;

public class LogSetting {

    public static final String LOG_LOCAL_PATH = ConfigUtils.getLogstorageConfig().getString("log.path.local", "/tmp/logs");
    public static final int LOG_READ_MAX_SIZE = ConfigUtils.getLogstorageConfig().getInt("log.read.size.max", 1024 * 1024);

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午10:05:41
 */

package com.mogujie.jarvis.tasks.jdbc;

import org.apache.commons.configuration.Configuration;

import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.tasks.domain.HiveTaskEntity;
import com.mogujie.jarvis.tasks.util.HiveConfigUtils;

/**
 * @author guangming
 *
 */
public class HiveJdbcTask extends JdbcTask {

    public HiveJdbcTask(TaskContext jobContext) {
        super(jobContext);
    }

    @Override
    protected String getTaskType() {
        return "hiveserver2";
    }

    @Override
    protected String getDriverName() {
        return "org.apache.hive.jdbc.HiveDriver";
    }

    @Override
    protected String getJdbcUrl(Configuration conf) {
        return conf.getString("hiveserver2.jdbc.url");
    }

    @Override
    protected int getMaxQueryRows(Configuration conf) {
        return conf.getInt("hiveserver2.max.query.rows", DEFAULT_MAX_QUERY_ROWS);
    }

    @Override
    protected String getUser() {
        String user = getTaskContext().getTaskDetail().getUser();
        if (user == null || user.isEmpty()) {
            HiveTaskEntity entity = HiveConfigUtils.getHiveJobEntry(getTaskContext().getTaskDetail().getAppName());
            user = entity.getUser();
        }
        return user;
    }

    @Override
    protected String getPasswd() {
        return getUser();
    }
}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午10:06:30
 */

package com.mogujie.jarvis.tasks.jdbc;

import org.apache.commons.configuration.Configuration;

import com.mogujie.jarvis.core.TaskContext;

/**
 * @author guangming
 *
 */
public class PrestoJdbcTask extends JdbcTask {

    public PrestoJdbcTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    protected String getTaskType() {
        return "presto";
    }

    @Override
    protected String getDriverName() {
        return "com.facebook.presto.jdbc.PrestoDriver";
    }

    @Override
    protected String getJdbcUrl(Configuration conf) {
        return conf.getString("presto.jdbc.url");
    }

    @Override
    protected int getMaxQueryRows(Configuration conf) {
        return conf.getInt("presto.max.query.rows", DEFAULT_MAX_QUERY_ROWS);
    }

    @Override
    protected String getUser() {
        String user = getTaskContext().getTaskDetail().getUser();
        if (user == null || user.isEmpty()) {
            user = getTaskContext().getTaskDetail().getAppName();
        }
        return user;
    }

    @Override
    protected String getPasswd() {
        return getUser();
    }
}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午10:07:19
 */

package com.mogujie.jarvis.tasks.jdbc;

import org.apache.commons.configuration.Configuration;

import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.util.ConfigUtils;

/**
 * @author guangming
 *
 */
public class MysqlJdbcTask extends JdbcTask {
    private static String USER = ConfigUtils.getWorkerConfig().getString("mysql.user");
    private static String PASSWD = ConfigUtils.getWorkerConfig().getString("mysql.passwd");

    public MysqlJdbcTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    protected String getTaskType() {
        return "mysql";
    }

    @Override
    protected String getDriverName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    protected String getJdbcUrl(Configuration conf) {
        return conf.getString("mysql.jdbc.url");
    }

    @Override
    protected int getMaxQueryRows(Configuration conf) {
        return conf.getInt("mysql.max.query.rows", DEFAULT_MAX_QUERY_ROWS);
    }

    @Override
    protected String getUser() {
        return USER;
    }

    @Override
    protected String getPasswd() {
        return PASSWD;
    }

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午9:55:32
 */
package com.mogujie.jarvis.tasks.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.mogujie.jarvis.core.AbstractLogCollector;
import com.mogujie.jarvis.core.AbstractTask;
import com.mogujie.jarvis.core.TaskContext;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.exception.TaskException;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.tasks.util.HiveQLUtil;

/**
 * @author guangming
 *
 */
public abstract class JdbcTask extends AbstractTask {
    protected static final String COLUMNS_SEPARATOR = "\001";
    protected static final int DEFAULT_MAX_QUERY_ROWS = 10000;
    private Connection connection;
    private Statement statement;
    private static final Logger LOGGER = LogManager.getLogger("worker");

    public JdbcTask(TaskContext taskContext) {
        super(taskContext);
    }

    @Override
    public boolean execute() throws TaskException {
        TaskDetail task = getTaskContext().getTaskDetail();
        Configuration config = ConfigUtils.getWorkerConfig();
        AbstractLogCollector collector = getTaskContext().getLogCollector();

        try {
            Class.forName(getDriverName());
            String user = getUser();
            String passwd = getPasswd();

            connection = DriverManager.getConnection(getJdbcUrl(config), user, passwd);
            statement = connection.createStatement();
            final long startTime = System.currentTimeMillis();
            collector.collectStderr("Querying " + getTaskType() + "...");

            String hql = task.getContent().trim();
            String[] cmds = HiveQLUtil.splitHiveScript(hql);
            for (String sql : cmds) {
                boolean hasResults = statement.execute(sql.trim());
                // some sql has no results,
                // e.g. "use db;", "set hive.cli.print.header=true;"...
                if (hasResults) {
                    ResultSet rs = statement.getResultSet();
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    List<String> columns = new ArrayList<String>(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(metaData.getColumnName(i));
                    }

                    collector.collectStdout(Joiner.on(COLUMNS_SEPARATOR).join(columns));
                    for (int i = 0; i < getMaxQueryRows(config) && rs.next(); i++) {
                        columns.clear();
                        for (int j = 1; j <= columnCount; j++) {
                            columns.add(rs.getString(j));
                        }
                        collector.collectStdout(Joiner.on(COLUMNS_SEPARATOR).useForNull("NULL").join(columns));
                    }
                }
            }

            final long endTime = System.currentTimeMillis();
            collector.collectStderr("Finished, time taken: " + (endTime - startTime) / 1000F + " seconds");

            return true;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            collector.collectStderr(Throwables.getStackTraceAsString(e));
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                LOGGER.warn("Error when close jdbc connection, caused by {}", e.getMessage());
            }
        }
    }

    @Override
    public boolean kill() throws TaskException {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
            getTaskContext().getLogCollector().collectStderr("Task killed!");
        } catch (SQLException e) {
            LOGGER.warn("Error when close jdbc connection, caused by {}", e.getMessage());
            return false;
        }

        return true;
    }

    protected abstract String getTaskType();

    protected abstract String getDriverName();

    protected abstract String getJdbcUrl(Configuration conf);

    protected abstract int getMaxQueryRows(Configuration conf);

    protected abstract String getUser();

    protected abstract String getPasswd();

}

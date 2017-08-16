/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月6日 下午5:06:51
 */

package com.mogujie.jarvis.worker.status.lookup;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.worker.status.TaskStatusLookup;

public class ShellStatusLookup implements TaskStatusLookup {

    private String dir = null;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init(Configuration conf) {
        dir = conf.getString("shell.status.data.dir", "/tmp/jarvis_shell_status");
    }

    @Override
    public int lookup(TaskDetail taskDetail) {
        String fullId = taskDetail.getFullId();
        try {
            File file = new File(dir + "/" + fullId + ".status");
            if (file.exists()) {
                String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
                switch (line.trim()) {
                    case "0":
                        return TaskStatus.SUCCESS.getValue();
                    default:
                        return TaskStatus.FAILED.getValue();
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        return TaskStatus.UNKNOWN.getValue();
    }

    @Override
    public void close() {
        // ignore
    }

}

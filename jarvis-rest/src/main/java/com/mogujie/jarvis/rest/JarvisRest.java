/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年9月14日 上午10:32:59
 */

package com.mogujie.jarvis.rest;

import java.io.IOException;

import com.google.common.base.Throwables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

import com.mogujie.jarvis.core.metrics.Metrics;
import com.mogujie.jarvis.core.util.ConfigUtils;

/**
 * 启动RestServer
 */
public class JarvisRest {

    static {
        //FOR GrizzlyHttpServer`logger change
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting Jarvis rest server ...");
        try {
            HttpServer server = RestServerFactory.createHttpServer();
            server.start();
            Metrics.start(ConfigUtils.getRestConfig());
            LOGGER.info("Rest server started.");
        } catch (Exception ex) {
            LOGGER.error("Jarvis rest server start error", ex);
            Throwables.propagate(ex);
        }
    }

}
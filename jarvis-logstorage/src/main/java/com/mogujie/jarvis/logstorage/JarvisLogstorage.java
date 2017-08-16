/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月7日 上午10:13:42
 */

package com.mogujie.jarvis.logstorage;

import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Throwables;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.metrics.Metrics;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.logstorage.actor.LogRoutingActor;
import com.typesafe.config.Config;

import akka.actor.ActorSystem;

public class JarvisLogstorage {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        LOGGER.info("Starting jarvis logStorage...");
        try {
            Config akkaConfig = ConfigUtils.getAkkaConfig("akka-logstorage.conf");
            ActorSystem system = ActorSystem.create(JarvisConstants.LOGSTORAGE_AKKA_SYSTEM_NAME, akkaConfig);

            Configuration logConfig = ConfigUtils.getLogstorageConfig();
            int actorNum = logConfig.getInt("logstorage.actors.num", 100);
            system.actorOf(LogRoutingActor.props(actorNum), JarvisConstants.LOGSTORAGE_AKKA_SYSTEM_NAME);

            Metrics.start(ConfigUtils.getLogstorageConfig());

            LOGGER.info("Jarvis logStorage started.");

        } catch (Exception ex) {
            LOGGER.error("Jarvis LogStorage start error", ex);
            Throwables.propagate(ex);
        }
    }

}

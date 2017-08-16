/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月21日 下午3:55:58
 */

package com.mogujie.jarvis.rest;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.mogujie.jarvis.core.JarvisConstants;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.core.util.ConfigUtils;
import org.apache.commons.configuration.Configuration;

/**
 *
 *
 */
public class RestAkka {

    private static ActorSystem system = ActorSystem.create(JarvisConstants.REST_AKKA_SYSTEM_NAME, ConfigUtils.getAkkaConfig("akka-rest.conf"));

    public static ActorSystem getSystem() {
        return system;
    }

    public static ActorSelection getActor(AkkaType akkaType) {

        String userPath;
        Configuration config = ConfigUtils.getRestConfig();

        if (akkaType == AkkaType.SERVER) {
            userPath = config.getString("server.akka.path") + JarvisConstants.SERVER_AKKA_USER_PATH;

        } else if (akkaType == AkkaType.WORKER) {
            userPath = config.getString("worker.akka.path") + JarvisConstants.WORKER_AKKA_USER_PATH;

        } else if (akkaType == AkkaType.LOGSTORAGE) {
            userPath = config.getString("logstorage.akka.path") + JarvisConstants.LOGSTORAGE_AKKA_USER_PATH;
        } else {
            return null;
        }

        return system.actorSelection(userPath);

    }

}

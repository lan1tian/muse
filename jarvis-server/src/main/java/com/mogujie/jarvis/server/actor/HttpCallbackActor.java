/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月23日 下午5:38:45
 */

package com.mogujie.jarvis.server.actor;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class HttpCallbackActor extends UntypedActor {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Props props() {
        return Props.create(HttpCallbackActor.class);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            String url = map.remove("url").toString();
            try {
                Unirest.post(url).fields(map).asString();
            } catch (UnirestException e) {
                LOGGER.error(e);
            }

            getContext().stop(getSelf());
        } else {
            unhandled(obj);
        }
    }

}

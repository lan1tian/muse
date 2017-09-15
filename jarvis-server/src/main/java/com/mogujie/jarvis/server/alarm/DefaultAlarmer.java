/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年11月25日 下午1:30:06
 */

package com.mogujie.jarvis.server.alarm;

import java.util.List;
import java.util.Map;

import com.mogujie.jarvis.server.util.EmailUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.domain.AlarmType;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.server.ServerConigKeys;

public class DefaultAlarmer extends Alarmer {

    private static Gson gson = new Gson();
    private Configuration serverConfig = ConfigUtils.getServerConfig();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public boolean alarm(AlarmLevel alarmLevel, List<AlarmType> alarmTypes, List<String> receiver, String message) {
        Map<String, Object> fields = Maps.newHashMap();
        fields.put("appName", serverConfig.getString(ServerConigKeys.ALARM_APP_NAME));
        fields.put("errorLevel", alarmLevel.name());
        fields.put("errorMsg", message);
        Map<String, Object> ext = Maps.newHashMap();
        ext.put("nickname", receiver);
        fields.put("ext", gson.toJson(ext));

        try {
//            HttpResponse<JsonNode> response = Unirest.post(serverConfig.getString(ServerConigKeys.ALARM_SERVICE_URL)).fields(fields).asJson();
//            JSONObject json = response.getBody().getObject();
            EmailUtil.send(fields);
            JSONObject json = new JSONObject();
            json.put("success", true);

            if (json.has("success")) {
                return json.getBoolean("success");
            } else {
                LOGGER.error("Alarm error: {}", json.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Alarm error", e);
        }

        return false;
    }
}

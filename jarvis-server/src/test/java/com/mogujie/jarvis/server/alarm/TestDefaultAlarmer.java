/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月13日 下午1:25:16
 */

package com.mogujie.jarvis.server.alarm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Unirest.class })
@PowerMockIgnore("javax.management.*")
public class TestDefaultAlarmer {

    private JsonNode jsonNode = new JsonNode("{\"success\":\"true\"}");

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws UnirestException {
        HttpResponse<JsonNode> response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.getBody()).thenReturn(jsonNode);

        MultipartBody multipartBody = Mockito.mock(MultipartBody.class);
        Mockito.when(multipartBody.asJson()).thenReturn(response);

        HttpRequestWithBody httpRequestWithBody = Mockito.mock(HttpRequestWithBody.class);
        Mockito.when(httpRequestWithBody.fields(Mockito.anyMap())).thenReturn(multipartBody);

        PowerMockito.mockStatic(Unirest.class);
        Mockito.when(Unirest.post(Mockito.anyString())).thenReturn(httpRequestWithBody);
    }

    @Test
    public void testAlarm() {
        Alarmer alarmer = new DefaultAlarmer();
        Assert.assertTrue(alarmer.alarm(Lists.newArrayList("user1", "user2"), "test msg"));
    }
}

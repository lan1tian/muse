package com.mogujie.jarvis.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import com.mogujie.jarvis.rest.vo.AbstractVo;
import org.junit.Ignore;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestAlarmRest extends AbstractTestRest {

    public void test() throws UnirestException {

        long jobId = 12;
        alarmDelete(jobId);
        alarmAdd(jobId);
        alarmSet(jobId);
        alarmDelete(jobId);

    }

    private void alarmAdd(long jobId) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("jobId", jobId);
        params.put("alarmType", "1,2,3");
        params.put("receiver", "mingren,muming");
        params.put("status", 1);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/alarm/add").field("appName", "jarvis-web").field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AbstractVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }

    private void alarmSet(long jobId) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("jobId", jobId);
        params.put("alarmType", "1,2,3,4");
        params.put("receiver", "muming");
        params.put("status", 2);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/alarm/edit").field("appName", "jarvis-web").field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AbstractVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }

    private void alarmDelete(long jobId) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("jobId", jobId);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/alarm/delete").field("appName", "jarvis-web").field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AbstractVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }

}

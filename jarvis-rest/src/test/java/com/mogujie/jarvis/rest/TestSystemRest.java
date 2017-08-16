package com.mogujie.jarvis.rest;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.domain.SystemStatus;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import com.mogujie.jarvis.rest.vo.AbstractVo;
import com.mogujie.jarvis.rest.vo.SystemStatusResultVo;
import org.junit.Assert;
import org.junit.Ignore;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestSystemRest extends AbstractTestRest {

    public void test() throws UnirestException {
        Integer status;
        status = getSystemStatus();
        System.out.print(status);
        setSystemStatus(SystemStatus.PAUSE);
        status = getSystemStatus();
        Assert.assertEquals(status.intValue(),SystemStatus.PAUSE.getValue());
        setSystemStatus(SystemStatus.RUNNING);
        status = getSystemStatus();
        Assert.assertEquals(status.intValue(),SystemStatus.RUNNING.getValue());
    }

    private void setSystemStatus(SystemStatus status ) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("status", status.getValue());
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/system/status")
                .field("appName", "jarvis-web")
                .field("appToken", "123")
                .field("user", "muming")
                .field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AbstractVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }

    private Integer getSystemStatus() throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/system/status/get")
                .field("appName", "jarvis-web")
                .field("appToken", "123")
                .field("user", "muming")
                .field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<SystemStatusResultVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<SystemStatusResultVo> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
        return result.getData().getStatus();

    }


}

package com.mogujie.jarvis.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import com.mogujie.jarvis.rest.vo.AppResultVo;
import org.junit.Assert;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.vo.AbstractVo;
import org.junit.Ignore;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestAppRest extends AbstractTestRest {

    public void testAppWorkerGroup() throws UnirestException {
        appWorkerGroupSet(OperationMode.DELETE, 26, 8);
        appWorkerGroupSet(OperationMode.ADD, 25, 8);
        appWorkerGroupSet(OperationMode.DELETE, 25, 8);
    }

    public void testApp()throws UnirestException{
        Integer appId = addApp();
        System.out.print("appId:" + appId);
    }

    private Integer addApp() throws UnirestException {
        Map<String, Object> params = new HashMap<>();
        params.put("applicationName", "mumingTest4");
        params.put("status", 1);
        params.put("owner", "tianhuo");
        params.put("maxConcurrency", 20);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/app/add")
                .field("appName", "jarvis-web")
                .field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AppResultVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<AppResultVo> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
        return result.getData().getAppId();

    }

    private void appWorkerGroupSet(OperationMode mode, Integer appId, Integer workerGroupId) throws UnirestException {

        List<Map<String, Object>> params = new ArrayList<>();
        Map<String, Object> entry;

        entry = new HashMap<>();
        entry.put("appId", appId);
        entry.put("workerGroupId", workerGroupId);
        params.add(entry);

        String paramsJson = JsonHelper.toJson(params);

        String path = "";
        if (mode == OperationMode.ADD) {
            path = "/api/app/workerGroup/add";
        } else if (mode == OperationMode.DELETE) {
            path = "/api/app/workerGroup/delete";
        }

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + path)
                .field("appName", "jarvis-web")
                .field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<AbstractVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }


}

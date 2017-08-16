package com.mogujie.jarvis.rest;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import com.mogujie.jarvis.rest.vo.AbstractVo;
import com.mogujie.jarvis.rest.vo.BizGroupResultVo;
import org.junit.Assert;
import org.junit.Ignore;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestBizGroupRest extends AbstractTestRest {

    public void test() throws UnirestException {

        int id;
        id = addBizGroup();
        editBizGroup(id);
        deleteBizGroup(id);

    }


    private int addBizGroup() throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "数据组1");
        params.put("status", 1);
        params.put("owner", "xiaohai");
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/bizGroup/add")
                .field("appName", "jarvis-web")
                .field("appToken", "123")
                .field("user", "muming")
                .field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<BizGroupResultVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<BizGroupResultVo> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
        return result.getData().getId();

    }

    private void editBizGroup(int id) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", "数据组2");
        params.put("status",2);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/bizGroup/edit")
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

    private void deleteBizGroup(int id) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/bizGroup/delete")
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

}

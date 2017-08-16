package com.mogujie.jarvis.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import com.mogujie.jarvis.rest.vo.WorkerHeartbeatVo;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestWorkerRest extends AbstractTestRest {

    @Test
    public void testAppWorkerGroup() throws UnirestException {
        queryWorkerHeartbeatInfo(0);
        queryWorkerHeartbeatInfo(2);
    }

    private void queryWorkerHeartbeatInfo(int groupId) throws UnirestException {
        Map<String, Object> params = new HashMap<>();
        params.put("workerGroupId", groupId);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/worker/heartbeat/get").field("appName", "jarvis-web")
                .field("appToken", "123").field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<WorkerHeartbeatVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<WorkerHeartbeatVo> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);

    }

}

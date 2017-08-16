package com.mogujie.jarvis.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import org.junit.Assert;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.vo.LogResultVo;
import org.junit.Ignore;

/**
 * Created by muming on 15/12/1.
 */
@Ignore
public class TestLogRest extends AbstractTestRest {

    public void TestLog() throws UnirestException{
        readLog(1001,1002,1);
    }

    private void readLog(long jobId,long taskId,int attemptId) throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("fullId", IdUtils.getFullId(jobId,taskId,attemptId));
        params.put("size", 10);
        params.put("offset", 0);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        while (true) {
            HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/log/readResult")
                    .field("appName", "jarvis-web")
                    .field("appToken", "123")
                    .field("user", "muming")
                    .field("parameters", paramsJson).asString();

            Assert.assertEquals(jsonResponse.getStatus(), 200);
            Type type = new TypeToken<RestResult4TestEntity<LogResultVo>>() {
            }.getType();
            RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), type);
            Assert.assertEquals(result.getCode(), 0);
            LogResultVo log = (LogResultVo) result.getData();
            System.out.print(log.getLog());
            if (log.isEnd()) {
                break;
            }
            params.put("offset", log.getOffset());
            paramsJson = JsonHelper.toJson(params, Map.class);
        }
    }

}
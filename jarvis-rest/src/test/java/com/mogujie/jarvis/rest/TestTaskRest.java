package com.mogujie.jarvis.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mogujie.jarvis.rest.domain.RestResult4TestEntity;
import org.junit.Assert;

import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.vo.JobResultVo;
import com.mogujie.jarvis.rest.vo.TaskEntryVo;
import com.mogujie.jarvis.rest.vo.TaskRelationsVo;
import org.junit.Test;

/**
 * Created by muming on 15/12/1.
 */
public class TestTaskRest {

    private String baseUrl = "http://127.0.0.1:8080";

    @Test
    public void temp(){

        Map<String, Object> jobPrams = new HashMap<>();
        jobPrams.put("name", "muming");
        jobPrams.put("age", 18);
        jobPrams.put("isMail", false);
        String json = JsonHelper.toJson(jobPrams);
        json = json.replaceAll("\"","\\\\\"");
        String cmd =  "sparkLauncher.sh \""  +  JsonHelper.toJson(jobPrams).replaceAll("\"","\\\\\"") +"\"";

        String akkaPath = "akka.tcp://logstorage@127.0.0.1:10002";
        int i = akkaPath.indexOf("muming");
        String address = akkaPath.substring(i+1);
        System.out.println(address);

        String key = UUID.randomUUID().toString().replace("-", "");
        System.out.println(key);
    }

    public void taskSubmit() throws UnirestException {

        TaskEntryVo task = new TaskEntryVo();
        task.setTaskName("mmTest");
        task.setUser("muming");
        task.setTaskType("dummy");
        task.setContent("welcome to dummy");
        task.setGroupId(1);

        //任务参数
        Map<String, Object> jobPrams = new HashMap<>();
        jobPrams.put("name", "muming");
        jobPrams.put("age", 18);
        jobPrams.put("isMail", false);
        task.setParams(jobPrams);

        // 任务参数
        String paramsJson = JsonHelper.toJson(task, TaskEntryVo.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/task/submit").field("appName", "jarvis-web").field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<JobResultVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);
    }

    public void queryRelations() throws UnirestException {

        Map<String, Object> params = new HashMap<>();
        params.put("taskId", 4);
        params.put("relationType", 2);
        String paramsJson = JsonHelper.toJson(params, Map.class);

        HttpResponse<String> jsonResponse = Unirest.post(baseUrl + "/api/task/queryRelation").field("appName", "jarvis-web").field("appToken", "123")
                .field("user", "muming").field("parameters", paramsJson).asString();

        Type restType = new TypeToken<RestResult4TestEntity<TaskRelationsVo>>() {
        }.getType();

        Assert.assertEquals(jsonResponse.getStatus(), 200);
        RestResult4TestEntity<?> result = JsonHelper.fromJson(jsonResponse.getBody(), restType);
        Assert.assertEquals(result.getCode(), 0);

    }

}
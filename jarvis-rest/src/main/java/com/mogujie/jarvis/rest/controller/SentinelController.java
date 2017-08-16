/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月15日 上午10:45:07
 */

package com.mogujie.jarvis.rest.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.core.domain.JobContentType;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.JobProtos.RestSubmitJobRequest;
import com.mogujie.jarvis.protocol.JobProtos.ServerSubmitJobResponse;
import com.mogujie.jarvis.protocol.KillTaskProtos.RestServerKillTaskRequest;
import com.mogujie.jarvis.protocol.KillTaskProtos.ServerKillTaskResponse;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageReadLogResponse;
import com.mogujie.jarvis.protocol.LogProtos.RestReadLogRequest;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.RestServerQueryTaskByJobIdRequest;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.ServerQueryTaskByJobIdResponse;
import com.mogujie.jarvis.protocol.QueryTaskByJobIdProtos.TaskEntry;
import com.mogujie.jarvis.protocol.QueryTaskStatusProtos.RestServerQueryTaskStatusRequest;
import com.mogujie.jarvis.protocol.QueryTaskStatusProtos.ServerQueryTaskStatusResponse;
import com.mogujie.jarvis.rest.sentinel.BaseRet;
import com.mogujie.jarvis.rest.sentinel.JobStatusEnum;
import com.mogujie.jarvis.rest.sentinel.LogQueryRet;
import com.mogujie.jarvis.rest.sentinel.ResponseCodeEnum;
import com.mogujie.jarvis.rest.sentinel.ResponseParams;
import com.mogujie.jarvis.rest.utils.ValidUtils;
import com.mogujie.jarvis.rest.utils.ValidUtils.CheckMode;
import com.mogujie.jarvis.rest.vo.JobVo;

/**
 * 兼容sentinel rest接口
 *
 * @author guangming
 *
 */
@Deprecated
@Path("server")
public class SentinelController extends AbstractController {

    private final static int DEFAULT_LOG_SIZE = ConfigUtils.getRestConfig().getInt("read.log.size", 1000);
    private final static int DEFAULT_RESULT_SIZE = ConfigUtils.getRestConfig().getInt("read.result.size", 10000);
    private static Cache<String, Long> logOffsetCache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(5, TimeUnit.MINUTES) //过期时间为5分钟
            .build();

    /**
     * @param appToken
     * @param appName 业务系统名称
     * @param time
     * @param content
     * @param user
     * @param jobName
     * @param jobType
     * @param groupId
     * @return RestResult
     */

    @POST
    @Path("execute")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseParams executeSql(@FormParam("token") String appToken, @FormParam("name") String appName, @FormParam("time") long time,
            @FormParam("content") String content, @FormParam("executor") String user, @FormParam("jobName") String jobName,
            @FormParam("jobType") @DefaultValue("hive") String jobType, @FormParam("groupId") @DefaultValue("1") Integer groupId) {
        LOGGER.debug("提交job任务");
        try {
            Preconditions.checkNotNull(appToken, "token不能为null");
            Preconditions.checkNotNull(appName, "name不能为null");
            Preconditions.checkNotNull(content, "content不能为null");
            Preconditions.checkNotNull(jobName, "jobName不能为null");

            if (user == null) {
                user = "";
            }

            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JobVo jobVo = new JobVo();
            jobVo.setJobName(jobName);
            jobVo.setJobType(jobType);
            jobVo.setWorkerGroupId(groupId);
            jobVo.setContent(content);
            jobVo.setTemp(true);
            jobVo.setContentType(JobContentType.TEXT.getValue()); //一次性任务都是文本格式
            ValidUtils.checkJob(CheckMode.ADD, jobVo);
            RestSubmitJobRequest request = vo2RequestByAdd(jobVo, appAuth, user);

            // 发送请求到server
            ServerSubmitJobResponse response = (ServerSubmitJobResponse) callActor(AkkaType.SERVER, request);
            BaseRet result;
            if (response.getSuccess()) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("jobId", String.valueOf(response.getJobId()));
                result = new BaseRet(ResponseCodeEnum.SUCCESS, "任务提交成功", params);
            } else {
                result = new BaseRet(ResponseCodeEnum.FAILED, "任务添加失败:" + response.getMessage());
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("", e);
            return new BaseRet(ResponseCodeEnum.FAILED, "任务添加失败:" + e.getMessage());
        }
    }

    @POST
    @Path("killjob")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseParams killJob(@FormParam("token") String appToken, @FormParam("name") String appName, @FormParam("time") long time,
            @FormParam("jobId") long jobId) {
        LOGGER.debug("kill task");
        try {
            Preconditions.checkNotNull(appToken, "token不能为null");
            Preconditions.checkNotNull(appName, "name不能为null");

            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestServerQueryTaskByJobIdRequest queryTaskRequest = RestServerQueryTaskByJobIdRequest.newBuilder().setAppAuth(appAuth).setJobId(jobId)
                    .build();
            ServerQueryTaskByJobIdResponse queryTaskReponse = (ServerQueryTaskByJobIdResponse) callActor(AkkaType.SERVER, queryTaskRequest);
            if (queryTaskReponse.getSuccess()) {
                List<TaskEntry> taskEntryList = queryTaskReponse.getTaskEntryList();
                if (taskEntryList == null || taskEntryList.size() != 1) {
                    String err = "job[" + jobId + "] 尚未调度起来";
                    return new BaseRet(ResponseCodeEnum.FAILED, err);
                } else {
                    long taskId = taskEntryList.get(0).getTaskId();
                    RestServerKillTaskRequest request = RestServerKillTaskRequest.newBuilder().setAppAuth(appAuth).addTaskId(taskId).build();
                    ServerKillTaskResponse response = (ServerKillTaskResponse) callActor(AkkaType.SERVER, request);
                    if (response.getSuccess()) {
                        return new BaseRet(ResponseCodeEnum.SUCCESS, "jobId: " + jobId + " 任务删除成功");
                    } else {
                        return new BaseRet(ResponseCodeEnum.FAILED, response.getMessage());
                    }
                }
            } else {
                return new BaseRet(ResponseCodeEnum.FAILED, queryTaskReponse.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return new BaseRet(ResponseCodeEnum.FAILED, e.getMessage());
        }
    }

    @POST
    @Path("jobstatus")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseParams queryJobStatus(@FormParam("token") String appToken, @FormParam("name") String appName, @FormParam("time") long time,
            @FormParam("jobId") long jobId) {
        LOGGER.debug("query job status");
        try {
            Preconditions.checkNotNull(appToken, "token不能为null");
            Preconditions.checkNotNull(appName, "name不能为null");

            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestServerQueryTaskByJobIdRequest queryTaskRequest = RestServerQueryTaskByJobIdRequest.newBuilder().setAppAuth(appAuth).setJobId(jobId)
                    .build();
            ServerQueryTaskByJobIdResponse queryTaskReponse = (ServerQueryTaskByJobIdResponse) callActor(AkkaType.SERVER, queryTaskRequest);
            if (queryTaskReponse.getSuccess()) {
                List<TaskEntry> taskEntryList = queryTaskReponse.getTaskEntryList();
                if (taskEntryList == null || taskEntryList.size() != 1) {
                    String err = "job[" + jobId + "] 尚未调度起来";
                    return new BaseRet(ResponseCodeEnum.FAILED, err);
                } else {
                    long taskId = taskEntryList.get(0).getTaskId();
                    RestServerQueryTaskStatusRequest request = RestServerQueryTaskStatusRequest.newBuilder().setAppAuth(appAuth).setTaskId(taskId)
                            .build();

                    ServerQueryTaskStatusResponse response = (ServerQueryTaskStatusResponse) callActor(AkkaType.SERVER, request);
                    if (response.getSuccess()) {
                        TaskStatus jarvisStatus = TaskStatus.parseValue(response.getStatus());
                        JobStatusEnum sentinelStatus = convert2SentinelStatus(jarvisStatus);
                        if (sentinelStatus == null) {
                            return new BaseRet(ResponseCodeEnum.FAILED, "无法转换到sentinel的状态，jarvis状态为: " + jarvisStatus);
                        } else {
                            BaseRet ret = new BaseRet(ResponseCodeEnum.SUCCESS);
                            ret.put("jobStatus", String.valueOf(sentinelStatus.getValue()));
                            ret.put("message", "查询状态成功");
                            return ret;
                        }
                    } else {
                        return new BaseRet(ResponseCodeEnum.FAILED, response.getMessage());
                    }
                }
            } else {
                return new BaseRet(ResponseCodeEnum.FAILED, queryTaskReponse.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return new BaseRet(ResponseCodeEnum.FAILED, e.getMessage());
        }
    }

    @POST
    @Path("result")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseParams queryResult(@FormParam("token") String appToken, @FormParam("name") String appName, @FormParam("time") long time,
            @FormParam("jobId") Integer jobId) {
        LOGGER.debug("query result");
        try {
            Preconditions.checkNotNull(appToken, "token不能为null");
            Preconditions.checkNotNull(appName, "name不能为null");

            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestServerQueryTaskByJobIdRequest queryTaskRequest = RestServerQueryTaskByJobIdRequest.newBuilder().setAppAuth(appAuth).setJobId(jobId)
                    .build();
            ServerQueryTaskByJobIdResponse queryTaskReponse = (ServerQueryTaskByJobIdResponse) callActor(AkkaType.SERVER, queryTaskRequest);
            if (queryTaskReponse.getSuccess()) {
                List<TaskEntry> taskEntryList = queryTaskReponse.getTaskEntryList();
                if (taskEntryList == null || taskEntryList.size() != 1) {
                    String err = "job[" + jobId + "] 尚未调度起来";
                    return new BaseRet(ResponseCodeEnum.FAILED, err);
                } else {
                    long taskId = taskEntryList.get(0).getTaskId();
                    int attemptId = taskEntryList.get(0).getAttemptId();
                    String fullId = IdUtils.getFullId(jobId, taskId, attemptId);
                    long offset = 0;
                    int size = DEFAULT_RESULT_SIZE;

                    RestReadLogRequest request = RestReadLogRequest.newBuilder().setAppAuth(appAuth).setFullId(fullId)
                            .setType(StreamType.STD_OUT.getValue()).setOffset(offset).setSize(size).build();

                    LogStorageReadLogResponse response = (LogStorageReadLogResponse) callActor(AkkaType.LOGSTORAGE, request);
                    if (response.getSuccess()) {
                        String result = response.getLog();
                        while (!response.getIsEnd()) {
                            offset = response.getOffset();
                            request = RestReadLogRequest.newBuilder().setAppAuth(appAuth).setFullId(fullId).setType(StreamType.STD_OUT.getValue())
                                    .setOffset(offset).setSize(size).build();
                            response = (LogStorageReadLogResponse) callActor(AkkaType.LOGSTORAGE, request);
                            if (!response.getSuccess()) {
                                return new BaseRet(ResponseCodeEnum.FAILED, response.getMessage());
                            } else {
                                result += response.getLog();
                            }
                        }
                        String[] rows = result.split("\n", -1);
                        rows = Arrays.copyOf(rows, rows.length - 1);
                        ArrayList<String> rowList = Lists.newArrayList(rows);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("data", rowList);
                        map.put("isEnd", true);
                        map.put("rowCnt", rows.length);
                        map.put("volume", "");
                        return new BaseRet(ResponseCodeEnum.SUCCESS, "成功", map);
                    } else {
                        return new BaseRet(ResponseCodeEnum.FAILED, response.getMessage());
                    }
                }
            } else {
                return new BaseRet(ResponseCodeEnum.FAILED, queryTaskReponse.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return new BaseRet(ResponseCodeEnum.FAILED, e.getMessage());
        }
    }

    @POST
    @Path("querylog")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseParams queryLog(@FormParam("token") String appToken, @FormParam("name") String appName, @FormParam("time") long time,
            @FormParam("jobId") long jobId) {
        LOGGER.debug("query job log");
        try {
            Preconditions.checkNotNull(appToken, "token不能为null");
            Preconditions.checkNotNull(appName, "name不能为null");

            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestServerQueryTaskByJobIdRequest queryTaskRequest = RestServerQueryTaskByJobIdRequest.newBuilder().setAppAuth(appAuth).setJobId(jobId)
                    .build();
            ServerQueryTaskByJobIdResponse queryTaskReponse = (ServerQueryTaskByJobIdResponse) callActor(AkkaType.SERVER, queryTaskRequest);
            if (queryTaskReponse.getSuccess()) {
                List<TaskEntry> taskEntryList = queryTaskReponse.getTaskEntryList();
                if (taskEntryList == null || taskEntryList.size() != 1) {
                    String err = "job[" + jobId + "] 尚未调度起来";
                    return new LogQueryRet(ResponseCodeEnum.FAILED, err);
                } else {
                    long taskId = taskEntryList.get(0).getTaskId();
                    int attemptId = taskEntryList.get(0).getAttemptId();
                    String fullId = IdUtils.getFullId(jobId, taskId, attemptId);
                    Long offset = logOffsetCache.getIfPresent(fullId);
                    if (offset == null) {
                        offset = 0L;
                    }
                    int size = DEFAULT_LOG_SIZE;

                    RestReadLogRequest request = RestReadLogRequest.newBuilder().setAppAuth(appAuth).setFullId(fullId)
                            .setType(StreamType.STD_ERR.getValue()).setOffset(offset).setSize(size).build();

                    LogStorageReadLogResponse response = (LogStorageReadLogResponse) callActor(AkkaType.LOGSTORAGE, request);
                    if (response.getSuccess()) {
                        LogQueryRet ret = new LogQueryRet(ResponseCodeEnum.SUCCESS, "success");
                        String log = response.getLog();
                        if (logOffsetCache.getIfPresent(fullId) == null) {
                            log = "jobId is " + jobId + "\n" + log;
                        }

                        ret.setLog(log);
                        if (response.getIsEnd()) {
                            ret.setIsEnd(true);
                            logOffsetCache.invalidate(fullId);
                        } else {
                            logOffsetCache.put(fullId, response.getOffset());
                        }
                        return ret;
                    } else {
                        return new LogQueryRet(ResponseCodeEnum.FAILED, response.getMessage());
                    }
                }
            } else {
                return new LogQueryRet(ResponseCodeEnum.FAILED, queryTaskReponse.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return new LogQueryRet(ResponseCodeEnum.FAILED, e.getMessage());
        }
    }

    @GET
    @Path("result/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("token") String token, @QueryParam("time") long timestamp, @QueryParam("name") String appName,
            @QueryParam("jobId") final long jobId) {
        LOGGER.debug("download log");
        AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(token).build();

        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    RestServerQueryTaskByJobIdRequest queryTaskRequest = RestServerQueryTaskByJobIdRequest.newBuilder().setAppAuth(appAuth)
                            .setJobId(jobId).build();
                    ServerQueryTaskByJobIdResponse queryTaskReponse = (ServerQueryTaskByJobIdResponse) callActor(AkkaType.SERVER, queryTaskRequest);
                    if (queryTaskReponse.getSuccess()) {
                        List<TaskEntry> taskEntryList = queryTaskReponse.getTaskEntryList();
                        if (taskEntryList == null || taskEntryList.size() != 1) {
                            LOGGER.error("jobId={} 尚未调度起来", jobId);
                            return;
                        } else {
                            long taskId = taskEntryList.get(0).getTaskId();
                            int attemptId = taskEntryList.get(0).getAttemptId();
                            String fullId = IdUtils.getFullId(jobId, taskId, attemptId);
                            long offset = 0;
                            final int size = 10000;
                            boolean isEnd = false;

                            while (!isEnd) {
                                RestReadLogRequest request = RestReadLogRequest.newBuilder().setAppAuth(appAuth).setFullId(fullId)
                                        .setType(StreamType.STD_ERR.getValue()).setOffset(offset).setSize(size).build();
                                LogStorageReadLogResponse response = (LogStorageReadLogResponse) callActor(AkkaType.LOGSTORAGE, request);
                                if (response.getSuccess()) {
                                    output.write(response.getLogBytes().toByteArray());
                                    offset = response.getOffset();
                                    isEnd = response.getIsEnd();
                                } else {
                                    LOGGER.error("获取日志失败:" + response.getMessage());
                                    return;
                                }
                            }
                        }
                    } else {
                        LOGGER.error("通过jobId={}查询task失败!", jobId);
                        return;
                    }
                } catch (Exception e) {
                    LOGGER.error("", e);
                } finally {
                    output.close();
                }
            }
        };

        return Response.ok(streamingOutput).header("content-disposition", "attachment;filename=result_job_" + jobId + ".txt").build();
    }

    /**
     * jobVo转换为request——增加
     */
    private RestSubmitJobRequest vo2RequestByAdd(JobVo vo, AppAuth appAuth, String user) {
        // 构造请求
        RestSubmitJobRequest.Builder builder = RestSubmitJobRequest.newBuilder().setAppAuth(appAuth).setUser(user).setJobName(vo.getJobName())
                .setJobType(vo.getJobType()).setStatus(vo.getStatus(JobStatus.ENABLE.getValue())).setContentType(vo.getContentType())
                .setContent(vo.getContent()).setParameters(vo.getParams("{}")).setAppName(vo.getAppName(appAuth.getName()))
                .setWorkerGroupId(vo.getWorkerGroupId()).setDepartmentId(vo.getDepartmentId(0)).setBizGroups(vo.getBizGroups(""))
                .setPriority(vo.getPriority(1)).setIsTemp(vo.isTemp()).setActiveStartDate(vo.getActiveStartDate(0L))
                .setActiveEndDate(vo.getActiveEndDate(0L)).setExpiredTime(vo.getExpiredTime(60 * 10)) //临时任务默认十分钟
                .setFailedAttempts(vo.getFailedAttempts(0)).setFailedInterval(vo.getFailedInterval(3));

        return builder.build();
    }

    private JobStatusEnum convert2SentinelStatus(TaskStatus jarvisStatus) {
        JobStatusEnum sentinelStatus = null;
        if (jarvisStatus.equals(TaskStatus.SUCCESS)) {
            sentinelStatus = JobStatusEnum.SUCCESS;
        } else if (jarvisStatus.equals(TaskStatus.WAITING)) {
            sentinelStatus = JobStatusEnum.WAIT;
        } else if (jarvisStatus.equals(TaskStatus.READY)) {
            sentinelStatus = JobStatusEnum.WAIT;
        } else if (jarvisStatus.equals(TaskStatus.RUNNING)) {
            sentinelStatus = JobStatusEnum.RUNNING;
        } else if (jarvisStatus.equals(TaskStatus.FAILED)) {
            sentinelStatus = JobStatusEnum.FAIL;
        } else if (jarvisStatus.equals(TaskStatus.KILLED)) {
            sentinelStatus = JobStatusEnum.KILLED;
        } else if (jarvisStatus.equals(TaskStatus.REMOVED)) {
            sentinelStatus = JobStatusEnum.EXCEPTION;
        }
        return sentinelStatus;
    }
}

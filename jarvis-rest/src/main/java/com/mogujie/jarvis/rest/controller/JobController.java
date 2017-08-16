/*
 * 蘑菇街 Inc. Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming Create Date: 2015年10月08日 下午3:19:28
 */
package com.mogujie.jarvis.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Preconditions;
import com.google.gson.reflect.TypeToken;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.core.domain.JobRelationType;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.JobProtos.JobStatusEntry;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobDependRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobScheduleExpRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobStatusRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestQueryJobRelationRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestSubmitJobRequest;
import com.mogujie.jarvis.protocol.JobProtos.ServerModifyJobDependResponse;
import com.mogujie.jarvis.protocol.JobProtos.ServerModifyJobResponse;
import com.mogujie.jarvis.protocol.JobProtos.ServerModifyJobScheduleExpResponse;
import com.mogujie.jarvis.protocol.JobProtos.ServerModifyJobStatusResponse;
import com.mogujie.jarvis.protocol.JobProtos.ServerQueryJobRelationResponse;
import com.mogujie.jarvis.protocol.JobProtos.ServerSubmitJobResponse;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.utils.JsonParameters;
import com.mogujie.jarvis.rest.utils.ValidUtils;
import com.mogujie.jarvis.rest.utils.ValidUtils.CheckMode;
import com.mogujie.jarvis.rest.vo.JobDependencyVo;
import com.mogujie.jarvis.rest.vo.JobRelationsVo;
import com.mogujie.jarvis.rest.vo.JobResultVo;
import com.mogujie.jarvis.rest.vo.JobScheduleExpVo;
import com.mogujie.jarvis.rest.vo.JobVo;

/**
 * @author muming
 */
@Path("api/job")
public class JobController extends AbstractController {

    /**
     * 提交job任务
     *
     * @throws Exception
     */
    @POST
    @Path("submit")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult submit(@FormParam("appName") String appName, @FormParam("appToken") String appToken, @FormParam("user") String user,
                             @FormParam("parameters") String parameters) {
        LOGGER.debug("提交job任务");
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JobVo jobVo = JsonHelper.fromJson(parameters, JobVo.class);
            ValidUtils.checkJob(CheckMode.ADD, jobVo);
            RestSubmitJobRequest request = vo2RequestByAdd(jobVo, appAuth, user);

            // 发送请求到server
            ServerSubmitJobResponse response = (ServerSubmitJobResponse) callActor(AkkaType.SERVER, request);

            return response.getSuccess() ? successResult(new JobResultVo().setJobId(response.getJobId()))
                    : errorResult(response.getMessage());

        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 修改job任务
     *
     * @throws Exception
     */
    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult edit(@FormParam("appName") String appName, @FormParam("appToken") String appToken, @FormParam("user") String user,
                           @FormParam("parameters") String parameters) {

        LOGGER.debug("更新job任务");
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JobVo jobVo = JsonHelper.fromJson(parameters, JobVo.class);
            ValidUtils.checkJob(CheckMode.EDIT, jobVo);
            RestModifyJobRequest request = vo2RequestByEdit(jobVo, appAuth, user);

            // 发送请求到server
            ServerModifyJobResponse response = (ServerModifyJobResponse) callActor(AkkaType.SERVER, request);

            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("edit job error", e);
            return errorResult(e);
        }
    }

    /**
     * 修改job任务依赖
     *
     * @throws Exception
     */
    @POST
    @Path("dependency/set")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult dependencySet(@FormParam("appName") String appName,
                                    @FormParam("appToken") String appToken,
                                    @FormParam("user") String user,
                                    @FormParam("parameters") String parameters) {

        LOGGER.debug("更新job依赖");
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JobDependencyVo jobVo = JsonHelper.fromJson(parameters, JobDependencyVo.class);
            // 构造请求
            RestModifyJobDependRequest.Builder builder = RestModifyJobDependRequest.newBuilder()
                    .setAppAuth(appAuth).setUser(user).setJobId(jobVo.getJobId());

            Preconditions.checkArgument(jobVo.getDependencyList() != null && jobVo.getDependencyList().size() > 0, "任务依赖为空");

            for (JobDependencyVo.DependencyEntry entryInput : jobVo.getDependencyList()) {
                builder.addDependencyEntry(ValidUtils.convertDependencyEntry(entryInput));
            }

            // 发送请求到server
            ServerModifyJobDependResponse response = (ServerModifyJobDependResponse) callActor(AkkaType.SERVER, builder.build());

            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("edit job error", e);
            return errorResult(e);
        }
    }


    /**
     * 修改job计划表达式
     *
     * @throws Exception
     */
    @POST
    @Path("scheduleExp/set")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult scheduleExpSet(@FormParam("appName") String appName,
                                     @FormParam("appToken") String appToken,
                                     @FormParam("user") String user,
                                     @FormParam("parameters") String parameters) {

        LOGGER.debug("更新job时间表达式");
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JobScheduleExpVo jobVo = JsonHelper.fromJson(parameters, JobScheduleExpVo.class);
            // 构造请求
            RestModifyJobScheduleExpRequest.Builder builder = RestModifyJobScheduleExpRequest.newBuilder().setAppAuth(appAuth).setUser(user).setJobId(jobVo.getJobId());

            Preconditions.checkArgument(jobVo.getScheduleExpressionList() != null && jobVo.getScheduleExpressionList().size() > 0, "计划表达式为空");

            if (jobVo.getScheduleExpressionList() != null && jobVo.getScheduleExpressionList().size() > 0) {
                for (JobScheduleExpVo.ScheduleExpressionEntry entryInput : jobVo.getScheduleExpressionList()) {
                    builder.addExpressionEntry(ValidUtils.convertScheduleExpressionEntry(entryInput));
                }
            }

            // 发送请求到server
            ServerModifyJobScheduleExpResponse response = (ServerModifyJobScheduleExpResponse) callActor(AkkaType.SERVER, builder.build());

            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("edit job error", e);
            return errorResult(e);
        }
    }

    /**
     * 修改job任务状态
     *
     * @throws Exception
     */
    @POST
    @Path("status/set")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult statusSet(@FormParam("appName") String appName, @FormParam("appToken") String appToken, @FormParam("user") String user,
                                @FormParam("parameters") String parameters) {

        LOGGER.debug("更新job任务标志");
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JsonParameters para = new JsonParameters(parameters);
            List<Long> jobIds = para.getList("jobIds", new TypeToken<List<Long>>(){}.getType());
            int status = para.getIntegerNotNull("status");

            // 构造请求
            RestModifyJobStatusRequest.Builder builder = RestModifyJobStatusRequest.newBuilder().setAppAuth(appAuth).setUser(user).setStatus(status);
            if (jobIds != null && !jobIds.isEmpty()) {
                for (long jobId : jobIds) {
                    builder.addJobId(jobId);
                }
                // 发送请求到server
                ServerModifyJobStatusResponse response = (ServerModifyJobStatusResponse) callActor(AkkaType.SERVER, builder.build());
                return response.getSuccess() ? successResult() : errorResult(response.getMessage());
            } else {
                return errorResult("jobIds不能为空");
            }
        } catch (Exception e) {
            LOGGER.error("edit job error", e);
            return errorResult(e);
        }
    }

    /**
     * 查找Job关系
     */
    @POST
    @Path("queryRelation")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult queryRelation(@FormParam("user") String user, @FormParam("appToken") String appToken, @FormParam("appName") String appName,
                                    @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JsonParameters paras = new JsonParameters(parameters);
            Long jobId = paras.getLongNotNull("jobId");
            Integer relationType = paras.getIntegerNotNull("relationType");
            if (!JobRelationType.isValid(relationType)) {
                throw new IllegalArgumentException("参数不对。key='relationType',value=" + relationType.toString());
            }

            RestQueryJobRelationRequest request = RestQueryJobRelationRequest.newBuilder().setAppAuth(appAuth).setUser(user).setJobId(jobId)
                    .setRelationType(relationType).build();

            ServerQueryJobRelationResponse response = (ServerQueryJobRelationResponse) callActor(AkkaType.SERVER, request);
            if (response.getSuccess()) {
                JobRelationsVo vo = new JobRelationsVo();
                if (response.getJobStatusEntryOrBuilderList() != null) {
                    List<JobRelationsVo.JobStatusEntry> list = new ArrayList<>();
                    for (JobStatusEntry entry : response.getJobStatusEntryList()) {
                        list.add(new JobRelationsVo.JobStatusEntry().setJobId(entry.getJobId()).setStatus(entry.getStatus()));
                    }
                    vo.setList(list);
                }
                return successResult(vo);
            } else {
                return errorResult(response.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * jobVo转换为request——增加
     */
    public static RestSubmitJobRequest vo2RequestByAdd(JobVo vo, AppAuth appAuth, String user) {
        // 构造请求
        RestSubmitJobRequest.Builder builder = RestSubmitJobRequest.newBuilder().setAppAuth(appAuth).setUser(user)
                .setJobName(vo.getJobName())
                .setJobType(vo.getJobType())
                .setStatus(vo.getStatus(JobStatus.ENABLE.getValue()))
                .setContentType(vo.getContentType())
                .setContent(vo.getContent())
                .setParameters(vo.getParams("{}"))
                .setAppName(vo.getAppName(appAuth.getName()))
                .setWorkerGroupId(vo.getWorkerGroupId())
                .setDepartmentId(vo.getDepartmentId(0))
                .setBizGroups(vo.getBizGroups(""))
                .setPriority(vo.getPriority(1))
                .setIsTemp(vo.isTemp())
                .setActiveStartDate(vo.getActiveStartDate(0L))
                .setActiveEndDate(vo.getActiveEndDate(0L))
                .setExpiredTime(vo.getExpiredTime(86400)) //默认24小时
                .setFailedAttempts(vo.getFailedAttempts(0))
                .setFailedInterval(vo.getFailedInterval(3));

        if (vo.getScheduleExpressionList() != null && vo.getScheduleExpressionList().size() > 0) {
            for (JobScheduleExpVo.ScheduleExpressionEntry entryInput : vo.getScheduleExpressionList()) {
                builder.addExpressionEntry(ValidUtils.convertScheduleExpressionEntry(entryInput));
            }
        }
        if (vo.getDependencyList() != null && vo.getDependencyList().size() > 0) {
            for (JobDependencyVo.DependencyEntry entryInput : vo.getDependencyList()) {
                builder.addDependencyEntry(ValidUtils.convertDependencyEntry(entryInput));
            }
        }
        return builder.build();
    }

    /**
     * jobVo转换为request——修改
     */
    private RestModifyJobRequest vo2RequestByEdit(JobVo vo, AppAuth appAuth, String user) {

        // 构造请求
        RestModifyJobRequest.Builder builder = RestModifyJobRequest.newBuilder().setAppAuth(appAuth).setUser(user);
        builder.setJobId(vo.getJobId());

        if (vo.getJobName() != null && !vo.getJobName().equals("")) {
            builder.setJobName(vo.getJobName());
        }
        if (vo.getJobType() != null) {
            builder.setJobType(vo.getJobType());
        }
        if (vo.getContentType() != null) {
            builder.setContentType(vo.getContentType());
        }
        if (vo.getContent() != null) {
            builder.setContent(vo.getContent());
        }
        if (vo.getParams() != null && !vo.getParams().equals("")) {
            builder.setParameters(vo.getParams());
        }
        if (vo.getPriority() != null) {
            builder.setPriority(vo.getPriority());
        }
        if (vo.getAppName() != null) {
            builder.setAppName(vo.getAppName());
        }
        if (vo.getWorkerGroupId() != null) {
            builder.setWorkerGroupId(vo.getWorkerGroupId());
        }
        if (vo.getBizGroups() != null) {
            builder.setBizGroups(vo.getBizGroups());
        }
        if (vo.getDepartmentId() != null) {
            builder.setDepartmentId(vo.getDepartmentId());
        }
        if (vo.getActiveStartDate() != null) {
            builder.setActiveStartDate(vo.getActiveStartDate());
        }
        if (vo.getActiveEndDate() != null) {
            builder.setActiveEndDate(vo.getActiveEndDate());
        }
        if (vo.getExpiredTime() != null) {
            builder.setExpiredTime(vo.getExpiredTime());
        }
        if (vo.getFailedAttempts() != null) {
            builder.setFailedAttempts(vo.getFailedAttempts());
        }
        if (vo.getFailedInterval() != null) {
            builder.setFailedInterval(vo.getFailedInterval());
        }
        return builder.build();
    }


    /**
     * 测试
     *
     * @throws Exception
     */
    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult test() throws Exception {
        JobResultVo vo = new JobResultVo();
        vo.setJobId(123456);
        return successResult(vo);

    }

}

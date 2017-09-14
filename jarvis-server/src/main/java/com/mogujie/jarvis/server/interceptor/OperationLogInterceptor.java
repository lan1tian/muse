package com.mogujie.jarvis.server.interceptor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.mogujie.jarvis.core.domain.JobContentType;
import com.mogujie.jarvis.core.domain.JobPriority;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.OperationInfo;
import com.mogujie.jarvis.dao.generate.JobDependMapper;
import com.mogujie.jarvis.dao.generate.JobMapper;
import com.mogujie.jarvis.dao.generate.JobScheduleExpressionMapper;
import com.mogujie.jarvis.dao.generate.OperationLogMapper;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.dto.generate.JobDepend;
import com.mogujie.jarvis.dto.generate.JobDependExample;
import com.mogujie.jarvis.dto.generate.JobScheduleExpression;
import com.mogujie.jarvis.dto.generate.JobScheduleExpressionExample;
import com.mogujie.jarvis.protocol.JobDependencyEntryProtos;
import com.mogujie.jarvis.protocol.JobProtos;
import com.mogujie.jarvis.protocol.JobScheduleExpressionEntryProtos;
import com.mogujie.jarvis.protocol.ManualRerunTaskProtos;
import com.mogujie.jarvis.server.service.JobActorLogService;
import com.mogujie.jarvis.server.service.TaskActorLogService;

/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/2/29
 * time: 上午10:48
 */

public class OperationLogInterceptor implements MethodInterceptor {

    @Inject
    private OperationLogMapper operationLogMapper;
    @Inject
    private JobMapper jobMapper;
    @Inject
    private JobDependMapper jobDependMapper;
    @Inject
    private JobScheduleExpressionMapper jobScheduleExpressionMapper;
    private static final Logger LOGGER = LogManager.getLogger("operation");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.nanoTime();
        try {

            // add job actor interceptor
            if (invocation.getStaticPart().toString().indexOf(JobActorLogService.class.getCanonicalName()) != -1) {
                // add job actor log
                this.handleJobActorLog(invocation);
            } else if (invocation.getStaticPart().toString().indexOf(TaskActorLogService.class.getCanonicalName()) != -1) {
                this.handleTaskActorLog(invocation);
            }

            Object result = invocation.proceed();

            return result;

        } finally {
            LOGGER.info(String.format("Invocation of method %s () with parameters %s took %s ms.", invocation.getMethod().getName(),
                    Arrays.toString(invocation.getArguments()), (System.nanoTime() - start) / 1000000.0));
        }
    }

    /**
     * taskActor 操作记录
     *
     * @param invocation
     */
    private void handleTaskActorLog(MethodInvocation invocation) {
        com.mogujie.jarvis.dto.generate.OperationLog operationLog = new com.mogujie.jarvis.dto.generate.OperationLog();

        Object obj = invocation.getArguments()[0];

        if (obj instanceof ManualRerunTaskProtos.RestServerManualRerunTaskRequest) {
            ManualRerunTaskProtos.RestServerManualRerunTaskRequest msg = (ManualRerunTaskProtos.RestServerManualRerunTaskRequest) obj;
            operationLog.setOperator(msg.getUser());
            operationLog.setOperationType(OperationInfo.MANUALRERUNTASK.getName());
            operationLog.setType("task");
            DateTime now = DateTime.now();
            operationLog.setOpeDate(now.toDate());
            for (long jobId : msg.getJobIdList()) {
                Job preJob = this.jobMapper.selectByPrimaryKey(jobId);
                operationLog.setTitle(preJob.getJobName());
                operationLog.setRefer(String.valueOf(jobId));
                String format = "";
                try {
                    format = String.format("%s 重跑任务 %s, 重跑起始时间为%s,重跑截止时间为%s", msg.getUser(), preJob.getJobName(), msg.getStartTime(), msg.getEndTime());
                    LOGGER.info("operationLog:"+operationLog);
                } catch (Exception e) {
                    LOGGER.error("error: ", e);
                }
                operationLog.setAfterOperationContent(format);
            }
        }

    }

    /**
     * jobActor 操作记录
     *
     * @param invocation
     */
    private void handleJobActorLog(MethodInvocation invocation) {
        com.mogujie.jarvis.dto.generate.OperationLog operationLog = new com.mogujie.jarvis.dto.generate.OperationLog();

        Object obj = invocation.getArguments()[0];

        if (obj instanceof JobProtos.RestSubmitJobRequest) {
            JobProtos.RestSubmitJobRequest msg = (JobProtos.RestSubmitJobRequest) obj;
            // temp job not record
            if(msg.getIsTemp()) {
                return;
            }
            operationLog.setOperationType(OperationInfo.SUBMITJOB.getName());
            if (msg.getContentType() == JobContentType.TEXT.getValue()) {
                operationLog.setAfterOperationContent(msg.getContent());
            } else if (msg.getContentType() == JobContentType.SCRIPT.getValue()) {
                operationLog.setAfterOperationContent(String.format("add script Job. scriptId=%s", msg.getContent()));
            }
            operationLog.setOperator(msg.getUser());
            operationLog.setTitle(msg.getJobName());
        } else if (obj instanceof JobProtos.RestModifyJobRequest) {
            JobProtos.RestModifyJobRequest msg = (JobProtos.RestModifyJobRequest) obj;
            Job preJob = this.jobMapper.selectByPrimaryKey(((JobProtos.RestModifyJobRequest) obj).getJobId());
            operationLog.setOperationType(OperationInfo.MODIFYJOB.getName());
            operationLog.setPreOperationContent(this.compare(preJob, msg).get("preJob"));
            operationLog.setAfterOperationContent(this.compare(preJob, msg).get("afterJob"));
            operationLog.setOperator(msg.getUser());
            operationLog.setTitle(msg.getJobName());
            operationLog.setRefer(String.valueOf(msg.getJobId()));
        } else if (obj instanceof JobProtos.RestModifyJobDependRequest) {
            JobProtos.RestModifyJobDependRequest msg = (JobProtos.RestModifyJobDependRequest) obj;
            Job preJob = this.jobMapper.selectByPrimaryKey(msg.getJobId());

            StringBuilder afterDependency = new StringBuilder();
            for (JobDependencyEntryProtos.DependencyEntry entry : msg.getDependencyEntryList()) {
                afterDependency.append(entry.getJobId()).append(",");
            }

            StringBuilder preJobDependency = new StringBuilder();
            List<JobDepend> JobDependList = this.selectPreDependency(preJob.getJobId());
            for (JobDepend jobDepend : JobDependList) {
                preJobDependency.append(jobDepend.getJobId()).append(",");
            }

            operationLog.setPreOperationContent(preJobDependency.toString());
            operationLog.setAfterOperationContent(afterDependency.toString());
            operationLog.setOperationType(OperationInfo.MODIFYJOBDEPENDENCY.getName());
            operationLog.setOperator(msg.getUser());
            operationLog.setTitle(preJob.getJobName());
            operationLog.setRefer(String.valueOf(msg.getJobId()));

        } else if (obj instanceof JobProtos.RestModifyJobScheduleExpRequest) {
            JobProtos.RestModifyJobScheduleExpRequest msg = (JobProtos.RestModifyJobScheduleExpRequest) obj;
            Job preJob = this.jobMapper.selectByPrimaryKey(msg.getJobId());

            StringBuilder afterJobExpress = new StringBuilder();
            for (JobScheduleExpressionEntryProtos.ScheduleExpressionEntry expressEntry : msg.getExpressionEntryList()) {
                afterJobExpress.append(expressEntry.getScheduleExpression()).append("\t");
            }

            operationLog.setOperationType(OperationInfo.MODIFYJOBSCHEDULEEXP.getName());
            operationLog.setPreOperationContent(this.selectJobScheduleExpress(preJob.getJobId()));
            operationLog.setAfterOperationContent(afterJobExpress.toString());
            operationLog.setOperator(msg.getUser());
            operationLog.setTitle(preJob.getJobName());
            operationLog.setRefer(String.valueOf(msg.getJobId()));

        } else if (obj instanceof JobProtos.RestModifyJobStatusRequest) {
            JobProtos.RestModifyJobStatusRequest msg = (JobProtos.RestModifyJobStatusRequest) obj;
            for (long jobId : msg.getJobIdList()) {
                Job job = this.jobMapper.selectByPrimaryKey(jobId);
                this.batchModifyJob(job, msg);
                return;
            }
        }

        operationLog.setType("job");
        DateTime now = DateTime.now();
        operationLog.setOpeDate(now.toDate());

        this.operationLogMapper.insert(operationLog);
    }

    /**
     * batch update job status
     *
     * @param job
     * @param msg
     */
    private void batchModifyJob(Job job, JobProtos.RestModifyJobStatusRequest msg) {
        // TODO add batch operation
        com.mogujie.jarvis.dto.generate.OperationLog operationLog = new com.mogujie.jarvis.dto.generate.OperationLog();

        operationLog.setOperator(msg.getUser());
        operationLog.setOperationType(OperationInfo.MODIFYJOBSTATUS.getName());
        operationLog.setPreOperationContent(JobStatus.parseValue(job.getStatus()).getDescription());
        operationLog.setAfterOperationContent(JobStatus.parseValue(msg.getStatus()).getDescription());
        operationLog.setTitle(job.getJobName());
        operationLog.setRefer(String.valueOf(job.getJobId()));
        operationLog.setType("job");
        DateTime now = DateTime.now();
        operationLog.setOpeDate(now.toDate());

        this.operationLogMapper.insert(operationLog);
    }

    private String selectJobScheduleExpress(Long jobId) {
        JobScheduleExpressionExample jobScheduleExpressionExample = new JobScheduleExpressionExample();
        jobScheduleExpressionExample.createCriteria().andJobIdEqualTo(jobId);
        List<JobScheduleExpression> jobScheduleExpressions = this.jobScheduleExpressionMapper.selectByExample(jobScheduleExpressionExample);

        StringBuilder builder = new StringBuilder();
        if (jobScheduleExpressions.size() == 0) {
            return null;
        } else {
            for (JobScheduleExpression expression : jobScheduleExpressions) {
                builder.append(expression.getExpression()).append("\t");
            }
        }

        return builder.toString();
    }

    private List<JobDepend> selectPreDependency(Long jobId) {
        JobDependExample jobDependExample = new JobDependExample();
        jobDependExample.createCriteria().andJobIdEqualTo(jobId);
        List<JobDepend> jobDependList = jobDependMapper.selectByExample(jobDependExample);

        return jobDependList;
    }

    /**
     * compare JOB info before modified and after modified
     *
     * @param preJob
     * @param afterJob
     * @return
     */
    private Map<String, String> compare(Job preJob, JobProtos.RestModifyJobRequest afterJob) {
        Map<String, String> comparemap = new HashMap<>();
        StringBuilder preJobBuilder = new StringBuilder();
        StringBuilder afterJobBuilder = new StringBuilder();

        if (!preJob.getJobName().equals(afterJob.getJobName())) {
            preJobBuilder.append("任务名称:").append(preJob.getJobName()).append("\t");
            afterJobBuilder.append("任务名称:").append(afterJob.getJobId()).append("\t");
        }
        if (!preJob.getJobType().equals(afterJob.getJobType())) {
            preJobBuilder.append("任务类型:").append(preJob.getJobType()).append("\t");
            afterJobBuilder.append("任务类型:").append(afterJob.getJobType()).append("\t");
        }
        if (preJob.getContentType() != afterJob.getContentType()) {
            preJobBuilder.append("执行内容类型:").append(JobContentType.parseValue(preJob.getContentType()).getDescription()).append("\t");
            afterJobBuilder.append("执行内容类型:").append(JobContentType.parseValue(afterJob.getContentType()).getDescription()).append("\t");
        }
        if (!preJob.getContent().equals(afterJob.getContent())) {
            preJobBuilder.append("执行内容:").append(preJob.getContent()).append("\t");
            afterJobBuilder.append("执行内容:").append(afterJob.getContent()).append("\t");
        }
        if (!preJob.getParams().equals(afterJob.getParameters())) {
            preJobBuilder.append("任务参数:").append(preJob.getParams()).append("\t");
            afterJobBuilder.append("任务参数:").append(afterJob.getParameters()).append("\t");
        }
        if (!preJob.getSubmitUser().equals(afterJob.getUser())) {
            preJobBuilder.append("提交用户:").append(preJob.getSubmitUser()).append("\t");
            afterJobBuilder.append("提交用户:").append(afterJob.getUser()).append("\t");
        }
        if (preJob.getPriority() != afterJob.getPriority()) {
            preJobBuilder.append("优先级:").append(JobPriority.parseValue(preJob.getPriority()).getValue()).append("\t");
            afterJobBuilder.append("优限级:").append(afterJob.getPriority()).append("\t");
        }
        if (preJob.getIsSerial() != afterJob.getIsSerial()) {
            preJobBuilder.append("是否串行:").append(preJob.getIsSerial() ? "串行" : "并行").append("\t");
            afterJobBuilder.append("是否串行:").append(afterJob.getIsSerial() ? "串行" : "并行").append("\t");
        }
        if (preJob.getIsTemp() != afterJob.getIsTemp()) {
            preJobBuilder.append("是否为临时任务:").append(preJob.getIsTemp() ? "是" : "否").append("\t");
            afterJobBuilder.append("是否为临时任务:").append(afterJob.getIsTemp() ? "是" : "否").append("\t");
        }
        if (preJob.getWorkerGroupId() != afterJob.getWorkerGroupId()) {
            preJobBuilder.append("worker组ID:").append(preJob.getWorkerGroupId()).append("\t");
            afterJobBuilder.append("worker组ID:").append(afterJob.getWorkerGroupId()).append("\t");
        }
        if (!preJob.getDepartmentId().equals(afterJob.getDepartmentId())) {
            preJobBuilder.append("部门ID:").append(preJob.getDepartmentId()).append("\t");
            afterJobBuilder.append("部门ID:").append(afterJob.getDepartmentId()).append("\t");
        }
        if (!preJob.getBizGroups().equals(afterJob.getBizGroups())) {
            preJobBuilder.append("业务组名称:").append(preJob.getBizGroups());
            afterJobBuilder.append("业务组名称:").append(afterJob.getBizGroups()).append("\t");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        if (!sdf.format(preJob.getActiveStartDate()).equals(sdf.format(new Date(afterJob.getActiveStartDate())))) {
            preJobBuilder.append("有效开始时间:").append(sdf.format(preJob.getActiveStartDate())).append("\t");
            afterJobBuilder.append("有效开始时间:").append(sdf.format(afterJob.getActiveStartDate())).append("\t");
        }
        if (!sdf.format(preJob.getActiveEndDate()).equals(sdf.format(new Date(afterJob.getActiveEndDate())))) {
            preJobBuilder.append("有效结束时间:").append(sdf.format(preJob.getActiveEndDate())).append("\t");
            afterJobBuilder.append("有效结束时间:").append(sdf.format(afterJob.getActiveEndDate())).append("\t");
        }
        if (preJob.getExpiredTime() != afterJob.getExpiredTime()) {
            preJobBuilder.append("失效时间(s)").append(preJob.getExpiredTime()).append("\t");
            afterJobBuilder.append("失效时间(s)").append(afterJob.getExpiredTime()).append("\t");
        }
        if (preJob.getFailedAttempts() != afterJob.getFailedAttempts()) {
            preJobBuilder.append("任务运行失败时的重试次数:").append(preJob.getFailedAttempts()).append("\t");
            afterJobBuilder.append("任务运行失败时的重试次数:").append(afterJob.getFailedAttempts()).append("\t");
        }
        if (preJob.getFailedInterval() != afterJob.getFailedInterval()) {
            preJobBuilder.append("任务运行失败时重试的间隔(秒):").append(preJob.getFailedInterval()).append("\t");
            afterJobBuilder.append("任务运行失败时重试的间隔(秒):").append(afterJob.getFailedInterval()).append("\t");
        }

        comparemap.put("preJob", preJobBuilder.toString());
        comparemap.put("afterJob", afterJobBuilder.toString());

        return comparemap;
    }

}

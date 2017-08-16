/*
 * 蘑菇街 Inc. Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年9月29日 下午4:42:28
 */

package com.mogujie.jarvis.server.service;

import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.dto.generate.DepartmentBizMap;
import java.util.Date;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.AlarmStatus;
import com.mogujie.jarvis.core.domain.AlarmType;
import com.mogujie.jarvis.core.domain.AppStatus;
import com.mogujie.jarvis.core.domain.BizGroupStatus;
import com.mogujie.jarvis.core.domain.CommonStrategy;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.core.expression.TimeOffsetExpression;
import com.mogujie.jarvis.core.util.ExpressionUtils;
import com.mogujie.jarvis.dto.generate.Alarm;
import com.mogujie.jarvis.dto.generate.App;
import com.mogujie.jarvis.dto.generate.AppWorkerGroup;
import com.mogujie.jarvis.dto.generate.BizGroup;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.protocol.JobDependencyEntryProtos.DependencyEntry;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobDependRequest;
import com.mogujie.jarvis.protocol.JobProtos.RestModifyJobScheduleExpRequest;
import com.mogujie.jarvis.protocol.JobScheduleExpressionEntryProtos.ScheduleExpressionEntry;
import com.mogujie.jarvis.server.domain.JobEntry;

/**
 * @author muming
 */

@Singleton
public class ValidService {

    public enum CheckMode {
        ADD, //追加
        EDIT, //修改
        EDIT_STATUS, //修改_状态
        DELETE; //删除

        /**
         * 是否在scope中
         */
        public Boolean isIn(CheckMode... scope) {
            for (CheckMode member : scope) {
                if (ordinal() == member.ordinal()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Inject
    private AppService appService;
    @Inject
    private JobService jobService;
    @Inject
    private AlarmService alarmService;
    @Inject
    private WorkerGroupService workerGroupService;
    @Inject
    private AppWorkerGroupService appWorkerGroupService;
    @Inject
    private BizGroupService bizGroupService;
    @Inject
    private DepartmentService departmentService;

    //--------------------------------------- job ---------------------------------

    /**
     * 检查——job任务
     */
    public void checkJob(CheckMode mode, Job job) throws NotFoundException, IllegalArgumentException {

        Long jobId = job.getJobId();
        Job oldJob = null;
        if (mode == CheckMode.EDIT || mode == CheckMode.EDIT_STATUS) {
            Preconditions.checkArgument(jobId != null && jobId > 0, "jobId不能为空");
            JobEntry oldJobEntry = jobService.get(jobId);
            Preconditions.checkArgument(oldJobEntry != null, "job不存在");
            oldJob = oldJobEntry.getJob();
        }
        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.EDIT_STATUS) || jobId != null, "jobId不能为空");
        Preconditions.checkArgument(jobId == null || jobId > 0, "jobId不能为空");

        String name = job.getJobName();
        Preconditions.checkArgument(mode != CheckMode.ADD || name != null, "jobName不能为空");
        Preconditions.checkArgument(name == null || !name.trim().equals(""), "jobName不能为空");

        String jobType = job.getJobType();
        Preconditions.checkArgument(mode != CheckMode.ADD || jobType != null, "jobType不能为空");
        Preconditions.checkArgument(jobType == null || !jobType.trim().equals(""), "jobType不能为空");

        Integer workerGroupId = job.getWorkerGroupId();
        Preconditions.checkArgument(mode != CheckMode.ADD || workerGroupId != null, "workGroupId不能为空");
        Preconditions.checkArgument(workerGroupId == null || workerGroupId > 0, "workGroupId不能为空");

        String content = job.getContent();
        Preconditions.checkArgument(mode != CheckMode.ADD || content != null, "job内容不能为空");
//        Preconditions.checkArgument(content == null || !content.trim().equals(""), "job内容不能为空");

        Integer status = job.getStatus();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD, CheckMode.EDIT_STATUS) || status != null, "status不能为空");
        Preconditions.checkArgument(status == null || JobStatus.isValid(status), "status内容不正确。value:" + status);

        Date start = job.getActiveStartDate();
        Date end = job.getActiveEndDate();
        if (start != null && end != null) {
            Preconditions.checkArgument(start.getTime() <= end.getTime(),
                    "有效开始时间不能大于有效结束时间. start:" + start.toString() + "; end:" + end.toString());
        } else if (start != null && mode.isIn(CheckMode.EDIT)) {
            Preconditions.checkArgument(start.getTime() <= oldJob.getActiveEndDate().getTime(),
                    "有效开始时间不能大于有效结束时间. start:" + start.toString() + "; end:" + oldJob.getActiveEndDate().toString());
        } else if (end != null && mode.isIn(CheckMode.EDIT)) {
            Preconditions.checkArgument(oldJob.getActiveStartDate().getTime() <= end.getTime(),
                    "有效开始时间不能大于有效结束时间. start:" + oldJob.getActiveStartDate().toString() + "; end:" + end.toString());
        }

    }

    /**
     * 检查-job依赖
     */
    public void checkJobDependency(RestModifyJobDependRequest msg) throws IllegalArgumentException {
        Preconditions.checkArgument(msg.getDependencyEntryList() != null && !msg.getDependencyEntryList().isEmpty()
                , "依赖对象不能为空");
        long jobId = msg.getJobId();
        JobEntry job = jobService.get(jobId);
        Preconditions.checkArgument(job != null, "jobId对象不存在");
        for (DependencyEntry entry : msg.getDependencyEntryList()) {
            int mode = entry.getOperator();
            Preconditions.checkArgument(OperationMode.isValid(mode), "操作模式不对");
            long preJobId = entry.getJobId();
            Preconditions.checkArgument(preJobId != 0, "依赖JobId不能为空");
            if (mode == OperationMode.ADD.getValue() || mode == OperationMode.EDIT.getValue()) {
                int commonStrategy = entry.getCommonDependStrategy();
                Preconditions.checkArgument(CommonStrategy.isValid(commonStrategy), "依赖的通用策略不对");
                //偏移策略可以为空，表示runtime模式。
                String offsetStrategy = entry.getOffsetDependStrategy();
                if (offsetStrategy == null || offsetStrategy.equals("")) {
                    // TODO: 16/1/8
                } else {
                    Preconditions.checkArgument(new TimeOffsetExpression(offsetStrategy).isValid(), "依赖的偏移策略不对");
                }
            }
        }
    }

    /**
     * 检查-job计划表达式
     */
    public void check2JobScheduleExp(RestModifyJobScheduleExpRequest msg) {
        Preconditions.checkArgument(msg.getExpressionEntryList() != null && !msg.getExpressionEntryList().isEmpty()
                , "计划表达式不能为空");
        long jobId = msg.getJobId();
        JobEntry job = jobService.get(jobId);
        Preconditions.checkArgument(job != null, "jobId对象不存在");
        for (ScheduleExpressionEntry entry : msg.getExpressionEntryList()) {
            int mode = entry.getOperator();
            Preconditions.checkArgument(OperationMode.isValid(mode), "操作模式不对. mode:" + mode);
            //追加与新建模式做检查,删除模式不做检查
            if (mode == OperationMode.ADD.getValue() || mode == OperationMode.EDIT.getValue()) {
                ExpressionUtils.checkExpression(entry.getExpressionType(), entry.getScheduleExpression());
            }
        }
    }

    /**
     * APP内容检查
     */
    public void checkApp(CheckMode mode, App app) throws IllegalArgumentException {

        Integer id = app.getAppId();
        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.DELETE) || id != null , "appId不能为空");
        Preconditions.checkArgument(id == null || id > 0 , "appId不能为零");

        String appName = app.getAppName();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || appName != null, "appName不能为空");
        if (appName != null) {
            Preconditions.checkArgument(!appName.trim().equals(""), "appName不能为空。");
            appService.checkDuplicateName(appName, app.getAppId());
        }

        String owner = app.getOwner();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || owner != null, "owner不能为空");
        Preconditions.checkArgument(owner == null || !owner.trim().equals(""), "owner不能为空");

        Integer status = app.getStatus();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || status != null, "status不能为空");
        Preconditions.checkArgument(status == null || AppStatus.isValid(status), "status内容不对。value:" + status);
    }

    /**
     * 检查 ——BizGroup
     */
    public void checkBizGroup(CheckMode mode, BizGroup bg) throws IllegalArgumentException, NotFoundException {
        Integer id = bg.getId();
        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.DELETE)
                || (id != null && id != 0), "id is empty。 id:" + id);

        if (mode.isIn(CheckMode.DELETE)) {
            bizGroupService.checkDeletable(id);
        }

        String name = bg.getName();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || name != null, "name不能为空。");
        if (name != null) {
            Preconditions.checkArgument(!name.trim().equals(""), "name不能为空。");
            bizGroupService.checkDuplicateName(name, bg.getId());
        }

        Integer status = bg.getStatus();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || status != null, "status不能为空。");
        Preconditions.checkArgument(status == null || BizGroupStatus.isValid(status), "status类型不对。value:" + status);

        String owner = bg.getOwner();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || owner != null, "owner不能为空。");
        Preconditions.checkArgument(owner == null || !owner.trim().equals(""), "owner不能为空。");
    }

    /**
     * 检查——Alarm报警
     */
    public void checkAlarm(CheckMode mode, Alarm alarm) {
        Long jobId = alarm.getJobId();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD, CheckMode.EDIT, CheckMode.DELETE)
                || (jobId != null && jobId != 0), "jobId不能为空。jobId:" + jobId);

        if (mode.isIn(CheckMode.ADD, CheckMode.EDIT)) {
            JobEntry job = jobService.get(jobId);
            Preconditions.checkNotNull(job, "job对象不存在。jobId:" + jobId);
        }
        if (mode.isIn(CheckMode.ADD)) {
            Alarm cur = alarmService.getAlarmByJobId(jobId);
            Preconditions.checkArgument(cur == null, "alarm对象已经存在,不能增加。jobId:" + jobId);
        }

        String type = alarm.getAlarmType();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || type != null, "alarmType不能为空。");
        Preconditions.checkArgument(type == null || AlarmType.isValid(type), "alarmType不对。 value:" + type);

        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || alarm.getReceiver() != null, "receiver不能为空。");

        Integer status = alarm.getStatus();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || status != null, "status不能为空。");
        Preconditions.checkArgument(status == null || AlarmStatus.isValid(status), "status类型不对。value:" + status);

    }

    /**
     * 检查——AppWorkerGroup
     */
    public void checkAppWorkerGroup(Integer modeVal, List<AppWorkerGroup> list) throws NotFoundException, IllegalArgumentException {
        OperationMode mode = OperationMode.parseValue(modeVal);
        Preconditions.checkArgument(mode.isIn(OperationMode.ADD, OperationMode.DELETE), "mode必须是增加或者删除. mode:" + modeVal);
        Preconditions.checkArgument(list != null && list.size() > 0, "数组对象为空");
        for (AppWorkerGroup entry : list) {
            int appId = entry.getAppId();
            appService.getAppById(appId);
            int workerGroupId = entry.getWorkerGroupId();
            workerGroupService.getGroupByGroupId(workerGroupId);
            AppWorkerGroup appWorkerGroup = appWorkerGroupService.query(appId, workerGroupId);
            Preconditions.checkArgument(mode != OperationMode.ADD || appWorkerGroup == null
                    , "AppWorkerGroup对象已经存在,不能插入. appID:" + appId + "; workerGroupId:" + workerGroupId);
        }
    }

  /**
   * 检查部门
   */
    public void checkDepartment(CheckMode mode, Department department) {
        Integer id = department.getId();
        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.DELETE)
            || (id != null && id != 0), "id is empty。 id:" + id);

        String name = department.getName();
        Preconditions.checkArgument(!mode.isIn(CheckMode.ADD) || name != null, "name不能为空。");
        if (name != null) {
            Preconditions.checkArgument(!name.trim().equals(""), "name不能为空。");
            departmentService.checkDuplicateName(name, department.getId());

        }
    }

  /**
   * 检查部门和产品线的映射关系
   */
    public void checkDepartmentBizMap(CheckMode mode, DepartmentBizMap departmentBizMap) {
        Integer bizId = departmentBizMap.getBizId();
        Integer departmentId = departmentBizMap.getDepartmentId();

        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.DELETE)
            || (bizId != null && bizId != 0), "bizId is empty。 bizId:" + bizId);
        Preconditions.checkArgument(!mode.isIn(CheckMode.EDIT, CheckMode.DELETE)
            || (departmentId != null && departmentId != 0), "departmentId is empty。 departmentId:" + departmentId);

        if(mode.isIn(CheckMode.ADD)) {
            departmentService.checkDuplicateMap(bizId, departmentId);
        }

    }

}

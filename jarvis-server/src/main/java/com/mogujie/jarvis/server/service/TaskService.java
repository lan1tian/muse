/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月10日 上午11:24:33
 */

package com.mogujie.jarvis.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.core.domain.TaskType;
import com.mogujie.jarvis.dao.generate.TaskHistoryMapper;
import com.mogujie.jarvis.dao.generate.TaskMapper;
import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.dto.generate.Task;
import com.mogujie.jarvis.dto.generate.TaskExample;
import com.mogujie.jarvis.dto.generate.TaskHistory;
import com.mogujie.jarvis.dto.generate.TaskHistoryExample;

/**
 * @author guangming
 */
@Singleton
public class TaskService {
    @Inject
    private TaskMapper taskMapper;

    @Inject
    private TaskHistoryMapper taskHistoryMapper;

    @Inject
    private TaskDependService taskDependService;

    @Inject
    private TaskHistoryService taskHistoryService;

    @Inject
    private JobService jobService;

    public Task get(long taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }

    public void update(Task task) {
        taskMapper.updateByPrimaryKey(task);
    }

    public Task getTaskByScheduleDateAndJobName(long scheduleDate, String jobName) {
        Job job = jobService.searchJobByName(jobName);
        if (job != null) {
            DateTime scheduleDateTime = new DateTime(scheduleDate).withTimeAtStartOfDay();
            TaskExample example = new TaskExample();
            example.createCriteria().andJobIdEqualTo(job.getJobId()).andScheduleTimeBetween(scheduleDateTime.toDate(),
                    scheduleDateTime.plusDays(1).toDate());
            List<Task> tasks = taskMapper.selectByExample(example);
            if (tasks != null && !tasks.isEmpty()) {
                return tasks.get(0);
            }
        }
        return null;
    }

    public float getAvgExecTime(long jobId, int times) {
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId).andStatusEqualTo(TaskStatus.SUCCESS.getValue());
        example.setOrderByClause("executeStartTime desc");
        List<Task> tasks = taskMapper.selectByExample(example);
        float avgTime = 0;
        if (tasks != null && !tasks.isEmpty()) {
            int size = tasks.size() > times ? times : tasks.size();
            float totalUseTime = 0;
            for (int i = 0; i < size; i++) {
                Task task = tasks.get(i);
                totalUseTime += ((float) (task.getExecuteEndTime().getTime() - task.getExecuteStartTime().getTime()) / 1000 / 60); //单位分
            }
            avgTime = totalUseTime / size;
        }
        return avgTime;
    }

    public List<Task> getTasksByJobIdAndDataDate(long jobId, long dataDate) {
        DateTime dataTime = new DateTime(dataDate).withTimeAtStartOfDay();
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId).andDataTimeBetween(dataTime.toDate(), dataTime.plusDays(1).toDate());
        return taskMapper.selectByExample(example);
    }

    public List<Task> getTasksByJobId(long jobId) {
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId);
        return taskMapper.selectByExample(example);
    }

    public Task getTaskByJobIdAndScheduleTime(long jobId, long scheduleTime) {
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId).andScheduleTimeEqualTo(new Date(scheduleTime));
        List<Task> tasks = taskMapper.selectByExample(example);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0);
        } else {
            return null;
        }
    }

    public long insert(Task record) {
        taskMapper.insert(record);
        return record.getTaskId();
    }

    public long insertSelective(Task record) {
        taskMapper.insertSelective(record);
        return record.getTaskId();
    }

    public long createTaskByJobId(long jobId, long scheduleTime, long dataTime, TaskType taskType, String user) {
        Task record = new Task();
        record.setJobId(jobId);
        record.setAttemptId(1);
        DateTime dt = DateTime.now();
        Date currentTime = dt.toDate();
        record.setCreateTime(currentTime);
        record.setUpdateTime(currentTime);
        record.setScheduleTime(new Date(scheduleTime));
        record.setDataTime(new Date(dataTime));
        record.setStatus(TaskStatus.WAITING.getValue());
        record.setProgress(0F);
        record.setAlarmEnable(1);
        Job job = jobService.get(jobId).getJob();
        if (job.getIsTemp()) {
            //如果是临时任务，设置task类型为TEMP
            record.setType(TaskType.TEMP.getValue());
        } else {
            record.setType(taskType.getValue());
        }
        if (user == null) {
            record.setExecuteUser(job.getSubmitUser());
        } else {
            record.setExecuteUser(user);
        }
        record.setContent(job.getContent());
        record.setParams(job.getParams());
        record.setAppId(job.getAppId());
        return insertSelective(record);
    }

    public long createTaskByJobId(long jobId, long scheduleTime, long dataTime, TaskType taskType) {
        return createTaskByJobId(jobId, scheduleTime, dataTime, taskType, null);
    }

    public void updateSelective(Task record) {
        taskMapper.updateByPrimaryKeySelective(record);
    }

    public void updateProgress(long taskId, float progress) {
        Task record = new Task();
        record.setTaskId(taskId);
        record.setProgress(progress);
        record.setUpdateTime(DateTime.now().toDate());
        taskMapper.updateByPrimaryKeySelective(record);
    }

    public void updateStatus(long taskId, TaskStatus status) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(status.getValue());
        task.setUpdateTime(DateTime.now().toDate());
        taskMapper.updateByPrimaryKeySelective(task);
    }

    public void updateStatusWithStart(long taskId, TaskStatus status, int workerId) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(status.getValue());
        task.setWorkerId(workerId);
        Date currentTime = DateTime.now().toDate();
        task.setExecuteStartTime(currentTime);
        task.setUpdateTime(currentTime);
        taskMapper.updateByPrimaryKeySelective(task);
    }

    public void updateStatusWithEnd(long taskId, TaskStatus status) {
        updateStatusWithEnd(taskId, status, null);
    }

    public void updateStatusWithEnd(long taskId, TaskStatus status, String reason) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(status.getValue());
        if (reason != null) {
            task.setFinishReason(reason);
        }
        Date currentTime = DateTime.now().toDate();
        task.setExecuteEndTime(currentTime);
        task.setUpdateTime(currentTime);
        taskMapper.updateByPrimaryKeySelective(task);

        insertHistory(taskId);
    }

    public void insertHistory(long taskId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        TaskHistory history = new TaskHistory();
        history.setTaskId(task.getTaskId());
        history.setAttemptId(task.getAttemptId());
        history.setJobId(task.getJobId());
        history.setContent(task.getContent());
        history.setParams(task.getParams());
        history.setScheduleTime(task.getScheduleTime());
        history.setDataTime(task.getDataTime());
        history.setProgress(task.getProgress());
        history.setType(task.getType());
        history.setStatus(task.getStatus());
        history.setFinishReason(task.getFinishReason());
        history.setAppId(task.getAppId());
        history.setWorkerId(task.getWorkerId());
        history.setExecuteUser(task.getExecuteUser());
        history.setExecuteStartTime(task.getExecuteStartTime());
        history.setExecuteEndTime(task.getExecuteEndTime());
        history.setAlarmEnable(1);
        Date currentTime = DateTime.now().toDate();
        history.setCreateTime(currentTime);
        history.setUpdateTime(currentTime);
        taskHistoryService.insertOrUpdate(history);
    }

    public List<Task> getTasksByStatusNotIn(List<Integer> statusList) {
        TaskExample example = new TaskExample();
        example.createCriteria().andStatusNotIn(statusList);
        List<Task> tasks = taskMapper.selectByExample(example);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        return tasks;
    }

    public List<Task> getTasksByStatus(List<Integer> statusList) {
        TaskExample example = new TaskExample();
        example.createCriteria().andStatusIn(statusList);
        List<Task> tasks = taskMapper.selectByExample(example);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        return tasks;
    }

    public List<Task> getTasksBetween(long jobId, Range<DateTime> range) {
        return getTasksBetween(jobId, range, TaskType.SCHEDULE);
    }

    public List<Task> getTasksBetween(long jobId, Range<DateTime> range, TaskType taskType) {
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId).andScheduleTimeBetween(range.lowerEndpoint().toDate(), range.upperEndpoint().toDate())
                .andTypeEqualTo(taskType.getValue());
        List<Task> tasks = taskMapper.selectByExample(example);
        if (tasks == null) {
            tasks = Lists.newArrayList();
        }
        return tasks;
    }

    public Task getLastTask(long jobId, long scheduleTime, TaskType taskType) {
        TaskExample example = new TaskExample();
        example.createCriteria().andJobIdEqualTo(jobId).andScheduleTimeLessThan(new DateTime(scheduleTime).toDate())
                .andTypeEqualTo(taskType.getValue());
        example.setOrderByClause("scheduleTime desc");
        List<Task> taskList = taskMapper.selectByExample(example);
        if (taskList != null && !taskList.isEmpty()) {
            return taskList.get(0);
        }
        return null;
    }

    public void updateAlarmStatus(List<Long> taskIds, int status) {
        Task task = new Task();
        task.setAlarmEnable(status);
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andTaskIdIn(taskIds);
        taskMapper.updateByExampleSelective(task, taskExample);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setAlarmEnable(status);
        TaskHistoryExample taskHistoryExample = new TaskHistoryExample();
        taskExample.createCriteria().andTaskIdIn(taskIds);
        taskHistoryMapper.updateByExampleSelective(taskHistory, taskHistoryExample);
    }

    @VisibleForTesting
    public void deleteTaskAndRelation(long taskId) {
        taskMapper.deleteByPrimaryKey(taskId);
        taskDependService.remove(taskId);
        taskHistoryService.deleteByTaskId(taskId);
    }

}

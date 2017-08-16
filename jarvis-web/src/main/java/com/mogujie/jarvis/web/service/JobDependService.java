package com.mogujie.jarvis.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mogujie.jarvis.web.entity.vo.JobDependVo;
import com.mogujie.jarvis.web.entity.vo.TaskVo;
import com.mogujie.jarvis.web.entity.qo.JobDependQo;
import com.mogujie.jarvis.web.mapper.JobDependMapper;


/**
 * @author muming, hejian
 */
@Service
public class JobDependService {
    @Autowired
    JobDependMapper jobDependMapper;

    @Autowired
    TaskService taskService;

    private static Logger logger = LogManager.getLogger();

    /**
     * 获取最近父节点
     */
    public List<JobDependVo> getParentById(Long jobId,List<Integer> statusList) {
        JobDependQo query= new JobDependQo();
        query.setJobId(jobId);

        query.setStatusList(statusList);

        return jobDependMapper.getParentById(query);
    }

    /**
     * 获取——自身与所有子节点.
     */
    public JobDependVo getSubTree(JobDependQo query) {
        if (null == query || 0 == query.getJobId()) {
            return null;
        }
        JobDependVo jobDependVo = jobDependMapper.getJobById(query.getJobId());
        if (null == jobDependVo) {
            return null;
        }
        jobDependVo.setRootFlag(true);

        List<JobDependVo> jobDependVoChildrenList = getChildren(jobDependVo, true);
        jobDependVo.setChildren(jobDependVoChildrenList);

        return jobDependVo;

    }

    //获取目录树的根节点
    public JobDependVo getRoot(JobDependQo query) {
        if (null == query || 0 == query.getJobId()) {
            return null;
        }
        JobDependVo jobDependVo = jobDependMapper.getJobById(query.getJobId());
        if (null == jobDependVo) {
            return null;
        }

        return jobDependVo;
    }

    //获取目录树的直接子节点
    public List getDirectChildren(JobDependQo query) {
        if (null == query || 0 == query.getJobId()) {
            return null;
        }
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        query.setStatusList(statusList);
        List<JobDependVo> jobChildren = jobDependMapper.getChildrenById(query);

        return jobChildren;
    }

    //判断是否有children
    public Map<Long, Boolean> hasChildren(List<Long> jobIdList) {
        Map<Long, Boolean> map = new HashMap<>();
        if (null == jobIdList || 0 == jobIdList.size()) {
            return map;
        }
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);

        JobDependQo query = new JobDependQo();
        query.setJobIdList(jobIdList);
        query.setStatusList(statusList);
        List<Map> list = jobDependMapper.getChildrenCount(query);

        for (Map item : list) {

            if (null == item.get("cnt") || 0 == Long.valueOf(item.get("cnt").toString())) {
                map.put(Long.valueOf(item.get("jobId").toString()), false);
            } else {
                map.put(Long.valueOf(item.get("jobId").toString()), true);
            }
        }

        return map;
    }

    /**
     * 获取——依赖(自身,父节点,子节点,共三层节点)
     */
    public JobDependVo getDepended(JobDependQo query) {
        if (null == query || 0 == query.getJobId()) {
            return null;
        }
        JobDependVo jobDependVo = jobDependMapper.getJobById(query.getJobId());
        if (null == jobDependVo) {
            return null;
        }
        jobDependVo.setRootFlag(true);

        List<JobDependVo> jobDependVoParentList = getParents(jobDependVo, false);
        jobDependVo.setParents(jobDependVoParentList);

        List<JobDependVo> jobDependVoChildrenList = getChildren(jobDependVo, false);
        jobDependVo.setChildren(jobDependVoChildrenList);

        generateTaskList4Depend(jobDependVo, query);

        return jobDependVo;
    }

    /**
     * 递归获取所有子节点
     */
    private List<JobDependVo> getChildren(JobDependVo jobDependVo, boolean all) {
        JobDependQo qo = new JobDependQo();
        qo.setJobId(jobDependVo.getJobId());

        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        statusList.add(3);
        statusList.add(5);

        qo.setStatusList(statusList);

        List<JobDependVo> jobChildren = jobDependMapper.getChildrenById(qo);
        if (jobChildren == null) {
            jobChildren = new ArrayList<JobDependVo>();
        }
        for (JobDependVo childJob : jobChildren) {
            if (all) {
                childJob.setChildren(getChildren(childJob, all));
            }
        }
        return jobChildren;
    }

    /**
     * 递归获取所有父节点
     */
    private List<JobDependVo> getParents(JobDependVo jobDependVo, boolean all) {
        JobDependQo query = new JobDependQo();
        query.setJobId(jobDependVo.getJobId());

        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        statusList.add(3);
        statusList.add(5);

        query.setStatusList(statusList);

        List<JobDependVo> jobParents = jobDependMapper.getParentById(query);
        if (jobParents == null) {
            jobParents = new ArrayList<JobDependVo>();
        }
        for (JobDependVo parentJob : jobParents) {
            parentJob.setParentFlag(true);
            if (all) {
                parentJob.setParents(getParents(parentJob, all));
            }
        }

        return jobParents;
    }

    /**
     * 获取——TaskList
     */
    private void generateTaskList4Depend(JobDependVo jobRoot, JobDependQo query) {

        //做成JobIds
        List<Long> jobIds = new ArrayList<Long>();
        jobIds.add(jobRoot.getJobId());
        for (JobDependVo parent : jobRoot.getParents()) {
            jobIds.add(parent.getJobId());
        }
        for (JobDependVo child : jobRoot.getChildren()) {
            jobIds.add(child.getJobId());
        }

        //获取taskList
        List<TaskVo> taskList = null;
        try {
            taskList = taskService.getTaskByJobIdBetweenTime(jobIds, query.getShowTaskStartTime(), query.getShowTaskEndTime());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        Map<Long, List<TaskVo>> taskMap = new HashMap<Long, List<TaskVo>>();
        for (TaskVo item : taskList) {
            Long jobId = item.getJobId();
            List<TaskVo> value;
            if (taskMap.containsKey(jobId)) {
                value = taskMap.get(jobId);
            } else {
                value = new ArrayList<TaskVo>();
                taskMap.put(jobId, value);
            }
            value.add(item);
        }

        //设置——taskList
        long findJobId = jobRoot.getJobId();
        if (taskMap.containsKey(findJobId)) {
            jobRoot.setTaskList(taskMap.get(findJobId));
        }
        for (JobDependVo parent : jobRoot.getParents()) {
            findJobId = parent.getJobId();
            if (taskMap.containsKey(findJobId)) {
                parent.setTaskList(taskMap.get(findJobId));
            }
        }
        for (JobDependVo child : jobRoot.getChildren()) {
            findJobId = child.getJobId();
            if (taskMap.containsKey(findJobId)) {
                child.setTaskList(taskMap.get(findJobId));
            }
        }
    }

}

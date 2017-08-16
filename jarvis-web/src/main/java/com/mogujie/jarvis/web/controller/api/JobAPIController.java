package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.web.entity.vo.JSTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mogujie.jarvis.core.domain.CommonStrategy;
import com.mogujie.jarvis.core.domain.JobStatus;
import com.mogujie.jarvis.core.expression.ScheduleExpressionType;
import com.mogujie.jarvis.web.entity.qo.JobQo;
import com.mogujie.jarvis.web.entity.vo.JobDependVo;
import com.mogujie.jarvis.web.entity.vo.JobVo;
import com.mogujie.jarvis.web.service.JobDependService;
import com.mogujie.jarvis.web.service.JobService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import com.mogujie.jarvis.web.entity.qo.JobDependQo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/15.
 */
@Controller
@RequestMapping(value = "/api/job")
public class JobAPIController {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobDependService jobDependService;

    /*
    * 根据id获取job的详细信息
    * */
    @RequestMapping("getById")
    @ResponseBody
    public Object getById(JobQo jobQo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == jobQo.getJobId()) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", "jobId必须传入");
            return map;
        }
        try {
            JobVo jobVo = jobService.getJobById(jobQo.getJobId());
            if (jobVo == null) {
                throw new NotFoundException("job不存在. jobId:" + jobQo.getJobId());
            }
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", jobVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /*
    * 根据id获取job的详细信息
    * */
    @RequestMapping("getByName")
    @ResponseBody
    public Object getByName(String jobName) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == jobName) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", "jobId必须传入");
            return map;
        }
        try {
            JobVo jobVo = jobService.getJobByName(jobName);
            if (jobVo == null) {
                throw new NotFoundException("job不存在. jobName:" + jobName);
            }
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", jobVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }


    /*
    * 根据id获取job的父任务
    * */
    @RequestMapping("getParentsById")
    @ResponseBody
    public Object getParentsById(JobQo jobQo, Boolean all) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == jobQo.getJobId()) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", "jobId必须传入");
            return map;
        }
        try {
            List<Integer> statusList = new ArrayList<>();
            statusList.add(1);
            statusList.add(2);
            statusList.add(3);
            statusList.add(5);
            if (all != null && all) {
                statusList.add(4);
            }
            List<JobDependVo> list = jobDependService.getParentById(jobQo.getJobId(), statusList);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }


    @RequestMapping("/getJobs")
    @ResponseBody
    public Object getJobs(JobQo jobQo) {

        Map<String, Object> map = jobService.getJobs(jobQo);
        return map;
    }

    /*
    * 提交过job的用户
    * */
    @RequestMapping(value = "getSubmitUsers")
    @ResponseBody
    public Object getSubmitUsers() {
        List<String> submitUsers = jobService.getSubmitUsers();
        return submitUsers;
    }

    @RequestMapping(value = "getAllJobIdAndName")
    @ResponseBody
    public Object getAllJobs() {
        List<Integer> statusList = new ArrayList<Integer>();
        List<Map> list = jobService.getAllJobIdAndName(statusList);
        return list;
    }

    /**
     * 获取任务依赖
     */
    @RequestMapping(value = "/getDepend")
    @ResponseBody
    public Object getDepend(JobDependQo jobQo) {
        return jobDependService.getDepended(jobQo);
    }

    /**
     * 单向依赖树
     */
    @RequestMapping("/getTreeDependedOnJob")
    @ResponseBody
    public Object getTreeDependedOnJob(JobDependQo jobQo) {
        return jobDependService.getSubTree(jobQo);
    }

    /**
     * 获取root节点
     */
    @RequestMapping("/getRoot")
    @ResponseBody
    public Object getRoot(JobDependQo jobQo) {
        JobDependVo jobDependVo = jobDependService.getRoot(jobQo);

        JSTreeNode jsTreeNode = new JSTreeNode();
        jsTreeNode.setText(jobDependVo.getJobName());
        Map li_attr = new HashMap();
        li_attr.put("jobId", jobDependVo.getJobId());
        jsTreeNode.setLi_attr(li_attr);

        List<Long> jobIdList = new ArrayList<>();
        jobIdList.add(jobDependVo.getJobId());
        Map<Long, Boolean> hasChildren = jobDependService.hasChildren(jobIdList);

        jsTreeNode.setChildren(hasChildren.get(jobDependVo.getJobId()));
        return jsTreeNode;
    }

    /**
     * 获取直接子节点节点
     */
    @RequestMapping("/getDirectChildren")
    @ResponseBody
    public Object getDirectChildren(JobDependQo jobQo) {
        List<JobDependVo> jobDependVoList = jobDependService.getDirectChildren(jobQo);

        List<Long> jobIdList = new ArrayList<>();

        List<JSTreeNode> jsTreeNodeList = new ArrayList<>();
        for (JobDependVo item : jobDependVoList) {
            jobIdList.add(item.getJobId());
        }
        Map<Long, Boolean> hasChildren = jobDependService.hasChildren(jobIdList);

        for (JobDependVo item : jobDependVoList) {
            JSTreeNode jsTreeNode = new JSTreeNode();

            jsTreeNode.setText(item.getJobName());
            Map li_attr = new HashMap();
            li_attr.put("jobId", item.getJobId());
            jsTreeNode.setLi_attr(li_attr);

            jsTreeNode.setChildren(hasChildren.get(item.getJobId()));

            jsTreeNodeList.add(jsTreeNode);
        }


        return jsTreeNodeList;
    }

    /**
     * 相似jobId
     */
    @RequestMapping("/getSimilarJobIds")
    @ResponseBody
    public Object getSimilarJobIds(Long q) {
        Map<String, Object> result = jobService.getSimilarJobIds(q);
        return result;
    }

    /**
     * 相似jobName
     */
    @RequestMapping("/getSimilarJobNames")
    @ResponseBody
    public Object getSimilarJobNames(String q) {
        Map<String, Object> result = jobService.getSimilarJobNames(q);
        return result;
    }

    /**
     * 相似jobName
     */
    @RequestMapping("/getJobBySimilarNames")
    @ResponseBody
    public Object getJobBySimilarNames(String q) {
        Map<String, Object> result = jobService.getJobBySimilarNames(q);
        return result;
    }


    @RequestMapping(value = "getJobStatus")
    @ResponseBody
    public Object getJobStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JobStatus[] jobStatuses = JobStatus.values();
        for (JobStatus jobStatus : jobStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", jobStatus.getValue());
            map.put("text", jobStatus.getDescription());
            list.add(map);
        }

        return list;
    }

    @RequestMapping(value = "getExpressionType")
    @ResponseBody
    public Object getExpressionType() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        ScheduleExpressionType[] scheduleExpressionTypes = ScheduleExpressionType.values();
        for (ScheduleExpressionType scheduleExpressionType : scheduleExpressionTypes) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", scheduleExpressionType.getValue());
            map.put("text", scheduleExpressionType.getDescription());
            list.add(map);
        }

        return list;
    }

    @RequestMapping(value = "getCommonStrategy")
    @ResponseBody
    public Object getCommonStrategy() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        CommonStrategy[] commonStrategies = CommonStrategy.values();
        for (CommonStrategy commonStrategy : commonStrategies) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", commonStrategy.getValue());
            map.put("text", commonStrategy.getDescription());
            list.add(map);
        }

        return list;
    }

}

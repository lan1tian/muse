package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.entity.qo.JobQo;
import com.mogujie.jarvis.web.entity.vo.JobVo;
import com.mogujie.jarvis.web.mapper.JobMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/17.
 */
@Service
public class JobService {
    @Autowired
    private JobMapper jobMapper;

    Logger logger = Logger.getLogger(this.getClass());

    public List<JobVo> getAllJobs(Integer status) {
        JobQo jobQo = new JobQo();
        List<String> statusList = new ArrayList<String>();
        if (null != status) {
            statusList.add(status.toString());
        }
        jobQo.setStatusList(JsonHelper.toJson(statusList));
        List<JobVo> jobVoList = jobMapper.getJobsByCondition(jobQo);
        return jobVoList;
    }

    public Map<String, Object> getJobs(JobQo jobQo) {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer count = jobMapper.getCountByCondition(jobQo);
        count = count == null ? 0 : count;

        List<JobVo> jobList = jobMapper.getJobsByCondition(jobQo);

        result.put("total", count);
        result.put("rows", jobList);

        return result;
    }

    public Map<String, Object> getSimilarJobIds(Long jobId) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Long> jobList = jobMapper.getSimilarJobIds(jobId);
        List<Map> list = new ArrayList<Map>();
        for (int i = 0; i < jobList.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", jobList.get(i));
            item.put("text", jobList.get(i));
            list.add(item);
        }

        result.put("total", jobList.size());
        result.put("items", list);

        return result;
    }

    public Map<String, Object> getSimilarJobNames(String jobName) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> jobList = jobMapper.getSimilarJobNames(jobName);

        List<Map> list = new ArrayList<Map>();
        for (int i = 0; i < jobList.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", jobList.get(i));
            item.put("text", jobList.get(i));
            list.add(item);
        }

        result.put("total", jobList.size());
        result.put("items", list);

        return result;
    }

    public Map<String, Object> getJobBySimilarNames(String jobName) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<JobVo> jobList = jobMapper.getJobBySimilarNames(jobName);

        List<Map> list = new ArrayList<Map>();
        for (JobVo jobVo : jobList) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", jobVo.getJobId());
            item.put("text", jobVo.getJobName());
            list.add(item);
        }
        result.put("total", jobList.size());
        result.put("items", list);

        return result;
    }


    public JobVo getJobById(Long jobId) {
        return jobMapper.getJobById(jobId);
    }

    public JobVo getJobByName(String jobName) {
        return jobMapper.getJobByName(jobName);
    }

    public List<Long> getJobIds() {
        return jobMapper.getJobIds();
    }

    public List<String> getJobNames() {
        return jobMapper.getJobNames();
    }

    public List<String> getSubmitUsers() {
        return jobMapper.getSubmitUsers();
    }
    public List<Map> getAllJobIdAndName(List<Integer> statusList){
        return jobMapper.getAllJobIdAndName(statusList);
    }

}

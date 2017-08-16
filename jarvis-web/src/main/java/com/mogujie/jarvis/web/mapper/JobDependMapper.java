package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.qo.JobDependQo;
import com.mogujie.jarvis.web.entity.vo.JobDependVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/22.
 */
public interface JobDependMapper {
    JobDependVo getJobById(Long jobId);

    List<JobDependVo> getChildrenById(JobDependQo qo);

    List<JobDependVo> getParentById(JobDependQo qo);
    List<Map> getChildrenCount(JobDependQo qo);
}

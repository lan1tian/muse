package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.Job;
import com.mogujie.jarvis.dto.generate.JobExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobMapper {
    int countByExample(JobExample example);

    int deleteByExample(JobExample example);

    int deleteByPrimaryKey(Long jobId);

    int insert(Job record);

    int insertSelective(Job record);

    java.util.List<com.mogujie.jarvis.dto.generate.Job> selectByExampleWithBLOBs(JobExample example);

    java.util.List<com.mogujie.jarvis.dto.generate.Job> selectByExample(JobExample example);

    Job selectByPrimaryKey(Long jobId);

    int updateByExampleSelective(@Param("record") Job record, @Param("example") JobExample example);

    int updateByExampleWithBLOBs(@Param("record") Job record, @Param("example") JobExample example);

    int updateByExample(@Param("record") Job record, @Param("example") JobExample example);

    int updateByPrimaryKeySelective(Job record);

    int updateByPrimaryKeyWithBLOBs(Job record);

    int updateByPrimaryKey(Job record);
}
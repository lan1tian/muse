package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.TaskDepend;
import com.mogujie.jarvis.dto.generate.TaskDependExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskDependMapper {
    int countByExample(TaskDependExample example);

    int deleteByExample(TaskDependExample example);

    int deleteByPrimaryKey(Long taskId);

    int insert(TaskDepend record);

    int insertSelective(TaskDepend record);

    java.util.List<com.mogujie.jarvis.dto.generate.TaskDepend> selectByExampleWithBLOBs(TaskDependExample example);

    java.util.List<com.mogujie.jarvis.dto.generate.TaskDepend> selectByExample(TaskDependExample example);

    TaskDepend selectByPrimaryKey(Long taskId);

    int updateByExampleSelective(@Param("record") TaskDepend record, @Param("example") TaskDependExample example);

    int updateByExampleWithBLOBs(@Param("record") TaskDepend record, @Param("example") TaskDependExample example);

    int updateByExample(@Param("record") TaskDepend record, @Param("example") TaskDependExample example);

    int updateByPrimaryKeySelective(TaskDepend record);

    int updateByPrimaryKeyWithBLOBs(TaskDepend record);

    int updateByPrimaryKey(TaskDepend record);
}
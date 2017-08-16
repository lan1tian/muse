package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.TaskHistory;
import com.mogujie.jarvis.dto.generate.TaskHistoryExample;
import com.mogujie.jarvis.dto.generate.TaskHistoryKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskHistoryMapper {
    int countByExample(TaskHistoryExample example);

    int deleteByExample(TaskHistoryExample example);

    int deleteByPrimaryKey(TaskHistoryKey key);

    int insert(TaskHistory record);

    int insertSelective(TaskHistory record);

    java.util.List<com.mogujie.jarvis.dto.generate.TaskHistory> selectByExampleWithBLOBs(TaskHistoryExample example);

    java.util.List<com.mogujie.jarvis.dto.generate.TaskHistory> selectByExample(TaskHistoryExample example);

    TaskHistory selectByPrimaryKey(TaskHistoryKey key);

    int updateByExampleSelective(@Param("record") TaskHistory record, @Param("example") TaskHistoryExample example);

    int updateByExampleWithBLOBs(@Param("record") TaskHistory record, @Param("example") TaskHistoryExample example);

    int updateByExample(@Param("record") TaskHistory record, @Param("example") TaskHistoryExample example);

    int updateByPrimaryKeySelective(TaskHistory record);

    int updateByPrimaryKeyWithBLOBs(TaskHistory record);

    int updateByPrimaryKey(TaskHistory record);
}
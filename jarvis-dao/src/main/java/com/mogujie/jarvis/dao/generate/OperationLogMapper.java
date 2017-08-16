package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.OperationLog;
import com.mogujie.jarvis.dto.generate.OperationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperationLogMapper {
    int countByExample(OperationLogExample example);

    int deleteByExample(OperationLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    java.util.List<com.mogujie.jarvis.dto.generate.OperationLog> selectByExampleWithBLOBs(OperationLogExample example);

    java.util.List<com.mogujie.jarvis.dto.generate.OperationLog> selectByExample(OperationLogExample example);

    OperationLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    int updateByExampleWithBLOBs(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    int updateByExample(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    int updateByPrimaryKeySelective(OperationLog record);

    int updateByPrimaryKeyWithBLOBs(OperationLog record);

    int updateByPrimaryKey(OperationLog record);
}
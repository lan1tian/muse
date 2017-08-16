package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.AppWorkerGroup;
import com.mogujie.jarvis.dto.generate.AppWorkerGroupExample;
import com.mogujie.jarvis.dto.generate.AppWorkerGroupKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppWorkerGroupMapper {
    int countByExample(AppWorkerGroupExample example);

    int deleteByExample(AppWorkerGroupExample example);

    int deleteByPrimaryKey(AppWorkerGroupKey key);

    int insert(AppWorkerGroup record);

    int insertSelective(AppWorkerGroup record);

    java.util.List<com.mogujie.jarvis.dto.generate.AppWorkerGroup> selectByExample(AppWorkerGroupExample example);

    AppWorkerGroup selectByPrimaryKey(AppWorkerGroupKey key);

    int updateByExampleSelective(@Param("record") AppWorkerGroup record, @Param("example") AppWorkerGroupExample example);

    int updateByExample(@Param("record") AppWorkerGroup record, @Param("example") AppWorkerGroupExample example);

    int updateByPrimaryKeySelective(AppWorkerGroup record);

    int updateByPrimaryKey(AppWorkerGroup record);
}
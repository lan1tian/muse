package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.Plan;
import com.mogujie.jarvis.dto.generate.PlanExample;
import com.mogujie.jarvis.dto.generate.PlanKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlanMapper {
    int countByExample(PlanExample example);

    int deleteByExample(PlanExample example);

    int deleteByPrimaryKey(PlanKey key);

    int insert(Plan record);

    int insertSelective(Plan record);

    java.util.List<com.mogujie.jarvis.dto.generate.Plan> selectByExample(PlanExample example);

    Plan selectByPrimaryKey(PlanKey key);

    int updateByExampleSelective(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByExample(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByPrimaryKeySelective(Plan record);

    int updateByPrimaryKey(Plan record);
}
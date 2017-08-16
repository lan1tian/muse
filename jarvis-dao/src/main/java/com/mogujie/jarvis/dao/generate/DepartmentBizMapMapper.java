package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.DepartmentBizMap;
import com.mogujie.jarvis.dto.generate.DepartmentBizMapExample;
import com.mogujie.jarvis.dto.generate.DepartmentBizMapKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DepartmentBizMapMapper {
    int countByExample(DepartmentBizMapExample example);

    int deleteByExample(DepartmentBizMapExample example);

    int deleteByPrimaryKey(DepartmentBizMapKey key);

    int insert(DepartmentBizMap record);

    int insertSelective(DepartmentBizMap record);

    java.util.List<com.mogujie.jarvis.dto.generate.DepartmentBizMap> selectByExample(DepartmentBizMapExample example);

    DepartmentBizMap selectByPrimaryKey(DepartmentBizMapKey key);

    int updateByExampleSelective(@Param("record") DepartmentBizMap record, @Param("example") DepartmentBizMapExample example);

    int updateByExample(@Param("record") DepartmentBizMap record, @Param("example") DepartmentBizMapExample example);

    int updateByPrimaryKeySelective(DepartmentBizMap record);

    int updateByPrimaryKey(DepartmentBizMap record);
}
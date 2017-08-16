package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.BizGroup;
import com.mogujie.jarvis.dto.generate.BizGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BizGroupMapper {
    int countByExample(BizGroupExample example);

    int deleteByExample(BizGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BizGroup record);

    int insertSelective(BizGroup record);

    java.util.List<com.mogujie.jarvis.dto.generate.BizGroup> selectByExample(BizGroupExample example);

    BizGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BizGroup record, @Param("example") BizGroupExample example);

    int updateByExample(@Param("record") BizGroup record, @Param("example") BizGroupExample example);

    int updateByPrimaryKeySelective(BizGroup record);

    int updateByPrimaryKey(BizGroup record);
}
package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.Script;
import com.mogujie.jarvis.dto.generate.ScriptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScriptMapper {
    int countByExample(ScriptExample example);

    int deleteByExample(ScriptExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Script record);

    int insertSelective(Script record);

    java.util.List<com.mogujie.jarvis.dto.generate.Script> selectByExampleWithBLOBs(ScriptExample example);

    java.util.List<com.mogujie.jarvis.dto.generate.Script> selectByExample(ScriptExample example);

    Script selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Script record, @Param("example") ScriptExample example);

    int updateByExampleWithBLOBs(@Param("record") Script record, @Param("example") ScriptExample example);

    int updateByExample(@Param("record") Script record, @Param("example") ScriptExample example);

    int updateByPrimaryKeySelective(Script record);

    int updateByPrimaryKeyWithBLOBs(Script record);

    int updateByPrimaryKey(Script record);
}
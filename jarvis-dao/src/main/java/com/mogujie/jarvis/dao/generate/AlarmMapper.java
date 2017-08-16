package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.Alarm;
import com.mogujie.jarvis.dto.generate.AlarmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlarmMapper {
    int countByExample(AlarmExample example);

    int deleteByExample(AlarmExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Alarm record);

    int insertSelective(Alarm record);

    java.util.List<com.mogujie.jarvis.dto.generate.Alarm> selectByExample(AlarmExample example);

    Alarm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Alarm record, @Param("example") AlarmExample example);

    int updateByExample(@Param("record") Alarm record, @Param("example") AlarmExample example);

    int updateByPrimaryKeySelective(Alarm record);

    int updateByPrimaryKey(Alarm record);
}
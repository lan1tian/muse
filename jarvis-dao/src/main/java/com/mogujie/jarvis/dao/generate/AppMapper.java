package com.mogujie.jarvis.dao.generate;

import com.mogujie.jarvis.dto.generate.App;
import com.mogujie.jarvis.dto.generate.AppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppMapper {
    int countByExample(AppExample example);

    int deleteByExample(AppExample example);

    int deleteByPrimaryKey(Integer appId);

    int insert(App record);

    int insertSelective(App record);

    java.util.List<com.mogujie.jarvis.dto.generate.App> selectByExample(AppExample example);

    App selectByPrimaryKey(Integer appId);

    int updateByExampleSelective(@Param("record") App record, @Param("example") AppExample example);

    int updateByExample(@Param("record") App record, @Param("example") AppExample example);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);
}
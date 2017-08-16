package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.vo.ScriptVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hejian on 15/9/17.
 */
public interface ScriptMapper {

    List<ScriptVo> queryScript(String scriptName);
    ScriptVo getScriptById( @Param("id") Integer id);

}

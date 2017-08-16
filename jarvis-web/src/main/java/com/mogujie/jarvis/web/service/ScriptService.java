package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.web.entity.vo.ScriptVo;
import com.mogujie.jarvis.web.mapper.ScriptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muming
 */
@Service
public class ScriptService {
    @Autowired
    ScriptMapper scriptMapper;

    /**
     * 查询脚本
     */
    public List<ScriptVo> queryScript(String title) {
        return scriptMapper.queryScript(title);
    }

    /**
     * 查询脚本
     */
    public ScriptVo getScriptById(Integer id) throws NotFoundException,IllegalArgumentException {
        if (id == null || id == 0) {
            throw new IllegalArgumentException("id 不能为空");
        }
        ScriptVo vo = scriptMapper.getScriptById(id);
        if (vo == null) {
            throw new NotFoundException("脚本不存在");
        }
        return vo;
    }


}

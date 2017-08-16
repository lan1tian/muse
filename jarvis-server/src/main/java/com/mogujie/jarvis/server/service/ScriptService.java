/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月22日 下午5:11:26
 */

package com.mogujie.jarvis.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.generate.ScriptMapper;
import com.mogujie.jarvis.dto.generate.Script;

/**
 * @author guangming
 *
 */
@Singleton
public class ScriptService {

    @Inject
    private ScriptMapper mapper;

    public String getContentById(int id) {
        Script script = mapper.selectByPrimaryKey(id);
        if (script != null) {
            return script.getContent();
        } else {
            return null;
        }
    }

    public String getTypeById(int id) {
        Script script = mapper.selectByPrimaryKey(id);
        if (script != null) {
            return script.getType();
        } else {
            return null;
        }
    }
}

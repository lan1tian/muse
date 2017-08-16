/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月15日 下午3:22:06
 */

package com.mogujie.jarvis.rest.sentinel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ResponseParams implements Serializable {

    private static final long serialVersionUID = 3481797831998852487L;
    public Map<String, Object> params = new HashMap<>();

    public ResponseParams() {

    }

    public ResponseParams(Map<String, Object> params) {
        if (params != null) {
            this.params = params;
        }
    }

    public void put(String key, Object value) {
        params.put(key, value);
    }

    public Object get(String key) {
        return params.get(key);
    }

    public String toString() {
        return new Gson().toJson(params);
    }

}

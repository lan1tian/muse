package com.mogujie.jarvis.rest;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.rest.vo.BizGroupVo;

/**
 * Created by lixun on 2017/9/20.
 */
public class TestJson {
    public static void main(String[] args) {
        String parameters = "{\"departmentName\":\"1\",\"status\":\"1\",\"bizGroupName\":\"dt1\",\"departmentNameR\":\"\",\"bizGroupNameR\":\"\",\"owner\":[\"lisi\",\"wangwu\"]}";
        parameters = "{\"id\":1,\"status\":1,\"name\":\"dt1\",\"owner\":\"wangwu\"}";
        BizGroupVo vo = JsonHelper.fromJson(parameters, BizGroupVo.class);

    }
}

/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.mock;

import com.mogu.bigdata.admin.client.entity.Result;
import com.mogu.bigdata.admin.client.service.RbacService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Mock权限检查
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @16-3-21 上午10:04
 */

public class MockRbacService implements RbacService {

    private static final Result<Boolean> result = new Result<Boolean>();

    private static final Result<List<String>> resultLst = new Result<List<String>>();

    private static final Result<Set<String>> resultSet = new Result<Set<String>>();

    private static final Result<Set<Long>> resultSetLong = new Result<Set<Long>>();

    static {

        result.setResult(true);

        resultLst.setResult(Collections.<String>emptyList());

        resultSet.setResult(Collections.<String>emptySet());

        resultSetLong.setResult(Collections.<Long>emptySet());
    }

    @Override
    public Result<Boolean> checkByUname(String uname, String permission, String app, String secret) {

        return result;
    }

    @Override
    public Result<Boolean> checkByUid(Long uid, String permission, String app, String secret) {
        return result;
    }

    @Override
    public Result<List<String>> filterByUname(String uname, List<String> permissions, String app, String secret) {
        return resultLst;
    }

    @Override
    public Result<List<String>> filterByUid(Long uid, List<String> permissions, String app, String secret) {
        return resultLst;
    }

    @Override
    public Result<Set<String>> getUnamesByPermission(String permission, String app, String secret) {
        return resultSet;
    }

    @Override
    public Result<Set<Long>> getUidsByPermission(String permission, String app, String secret) {
        return resultSetLong;
    }
}

package com.mogu.bigdata.admin.client.service;

import com.mogu.bigdata.admin.client.entity.Result;

import java.util.List;
import java.util.Set;

/**
 * Created by lixun on 2017/7/25.
 */
public interface RbacService {
    public Result<Boolean> checkByUname(String uname, String permission, String app, String secret) ;
    public Result<Boolean> checkByUid(Long uid, String permission, String app, String secret);

    public Result<List<String>> filterByUname(String uname, List<String> permissions, String app, String secret) ;

    public Result<List<String>> filterByUid(Long uid, List<String> permissions, String app, String secret) ;

    public Result<Set<String>> getUnamesByPermission(String permission, String app, String secret);

    public Result<Set<Long>> getUidsByPermission(String permission, String app, String secret) ;
}

package com.mogu.bigdata.admin.client.service;

import com.mogu.bigdata.admin.client.entity.Result;
import com.mogu.bigdata.admin.core.entity.vo.Menu;
import com.mogu.bigdata.admin.core.entity.vo.PlatformVo;

import java.util.List;

/**
 * Created by lixun on 2017/7/25.
 */
public interface MenuService {

    public Result<List<Menu>> getByUname(String uname, String app, String secret);

    public Result<List<Menu>> getByUid(Long uid, String app, String secret);

    public Result<List<Menu>> get(String app, String secret);

    public Result<List<Menu>> getCurrentByUname(String uname, List<String> permissions, String currentUrl, String app, String secret) ;

    public Result<List<Menu>> getCurrentByUid(Long uid, List<String> permissions, String currentUrl, String app, String secret);

    public Result<List<Menu>> getAllCurrentByUname(String uname, List<String> permissions, String currentUrl, String app, String secret);

    public Result<List<Menu>> getAllCurrentByUid(Long uid, List<String> permissions, String currentUrl, String app, String secret) ;

    public Result<List<PlatformVo>> getPlatformsByUname(String uname, String app, String secret);

    public Result<List<PlatformVo>> getPlatformsByUid(Long uid, String app, String secret);

}

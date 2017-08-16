/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.mock;

import com.mogu.bigdata.admin.client.entity.Consts;
import com.mogu.bigdata.admin.client.entity.Result;
import com.mogu.bigdata.admin.client.service.MenuService;
import com.mogu.bigdata.admin.core.entity.vo.Menu;
import com.mogu.bigdata.admin.core.entity.vo.PlatformVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock菜单服务
 *
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @16-3-21 上午11:57
 */

public class MockMenuService implements MenuService {

    private static final Result<List<Menu>> lstMenu = new Result<>();

    static {

        List<Menu> menus = new ArrayList<>();

        Menu m=new Menu();

        lstMenu.setCode(Consts.SUCCESS);
        lstMenu.setMsg("success");
        lstMenu.setResult(menus);


    }

    @Override
    public Result<List<Menu>> getByUname(String uname, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> getByUid(Long uid, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> get(String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> getCurrentByUname(String uname, List<String> permissions, String currentUrl, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> getCurrentByUid(Long uid, List<String> permissions, String currentUrl, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> getAllCurrentByUname(String uname, List<String> permissions, String currentUrl, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<Menu>> getAllCurrentByUid(Long uid, List<String> permissions, String currentUrl, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<PlatformVo>> getPlatformsByUname(String uname, String app, String secret) {
        return null;
    }

    @Override
    public Result<List<PlatformVo>> getPlatformsByUid(Long uid, String app, String secret) {
        return null;
    }
}

/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.mock;

import com.mogu.bigdata.admin.client.entity.Consts;
import com.mogu.bigdata.admin.client.entity.Result;
import com.mogu.bigdata.admin.client.service.UserService;
import com.mogu.bigdata.admin.core.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @16-3-21 上午11:16
 */

public class MockUserService implements UserService {

    public static final Result<User> mockUser = new Result<User>();

    static {

        mockUser.setCode(Consts.SUCCESS);
        mockUser.setMsg("success");
        mockUser.setResult(MockUuapService.user);
    }

    @Override
    public Result<User> get(String uname, String app, String secret) {
        return mockUser;
    }

    @Override
    public Result<User> add(User user, String app, String secret) {
        return mockUser;
    }
}

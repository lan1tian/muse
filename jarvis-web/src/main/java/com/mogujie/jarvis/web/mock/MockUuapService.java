/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.mock;

import com.mogu.bigdata.admin.client.entity.Result;
import com.mogu.bigdata.admin.core.entity.User;
import com.mogu.bigdata.admin.passport.exception.PassportException;
import com.mogu.bigdata.admin.passport.user.UuapService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Mock单点登录
 *
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @16-3-21 上午11:41
 */
public class MockUuapService /*implements UuapService*/ {

    public static final Result<User> mockUser = new Result<User>();

    public static final User user = new User();

    static {

        user.setEmail("test@mogujie.com");
        user.setId(9999L);
        user.setNick("测试");
        user.setUname("ceshi");
    }

//    @Override
    public User authenticate(HttpServletRequest request, HttpServletResponse response, String uname, String password) throws PassportException {

        return user;
    }

//    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws PassportException {

    }
}

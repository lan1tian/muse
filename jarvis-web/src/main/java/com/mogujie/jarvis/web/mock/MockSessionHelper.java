/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.mock;

import com.mogu.bigdata.admin.core.entity.User;
import com.mogu.bigdata.admin.passport.user.session.SessionHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @16-3-21 上午11:51
 */

public class MockSessionHelper implements SessionHelper {
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        return MockUuapService.user;
    }

    @Override
    public void updateSession(User user, HttpServletRequest request, HttpServletResponse response) {

    }
}

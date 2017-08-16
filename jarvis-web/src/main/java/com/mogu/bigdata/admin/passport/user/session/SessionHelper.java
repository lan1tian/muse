package com.mogu.bigdata.admin.passport.user.session;

import com.mogu.bigdata.admin.core.entity.User;
import com.mogujie.jarvis.web.mock.MockUuapService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lixun on 2017/7/25.
 */
public interface SessionHelper {
    public User getCurrentUser(HttpServletRequest request);

    public void updateSession(User user, HttpServletRequest request, HttpServletResponse response);
}

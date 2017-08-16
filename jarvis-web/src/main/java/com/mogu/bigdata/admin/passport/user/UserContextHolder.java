package com.mogu.bigdata.admin.passport.user;

import com.mogu.bigdata.admin.core.entity.User;

/**
 * Created by lixun on 2017/7/25.
 */
public class UserContextHolder {

    public static User getUser() {
        return new User();
    }
}

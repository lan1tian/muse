package com.mogujie.jarvis.server.actor.base;

import com.mogujie.jarvis.core.util.AppTokenUtils;
import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;

import org.apache.commons.configuration.Configuration;

import java.util.Date;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/12.
 * used by jarvis-parent
 */
public class TestAppActorBase {
    protected AppAuth appAuth=AppAuth.newBuilder()
            .setName("myServerAppTest")
            .setToken(AppTokenUtils.generateToken(new Date().getTime(), "myServerAppTest")).build();
    protected static Configuration conf = ConfigUtils.getServerConfig();

}

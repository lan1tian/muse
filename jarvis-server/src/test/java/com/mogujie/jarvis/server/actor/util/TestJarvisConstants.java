package com.mogujie.jarvis.server.actor.util;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/2/2.
 * used by jarvis-parent
 */
public class TestJarvisConstants {
    public static final String TEST_SERVER_HOST = "10.11.6.129";
    public static final String TEST_AKKA_SYSTEM_NAME = "test_qh";
    public static final String TEST_SERVER_PORT = "10000";
    public static final String TEST_SERVER_ACTOR_PATH = "akka.tcp://server@"
            + TEST_SERVER_HOST + ":" + TEST_SERVER_PORT + "/user/server";
}

package com.mogujie.jarvis.core;

import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author wuya
 */
public interface JarvisConstants {

    String SERVER_AKKA_SYSTEM_NAME = "server";
    String SERVER_AKKA_USER_NAME = "server";
    String SERVER_AKKA_USER_PATH = "/user/" + SERVER_AKKA_USER_NAME;

    String WORKER_AKKA_SYSTEM_NAME = "worker";
    String WORKER_AKKA_USER_NAME = "worker";
    String WORKER_AKKA_USER_PATH = "/user/" + WORKER_AKKA_USER_NAME;

    String REST_AKKA_SYSTEM_NAME = "rest";
    String REST_AKKA_USER_NAME = "rest";
    String REST_AKKA_USER_PATH = "/user/" + REST_AKKA_USER_NAME;

    String LOGSTORAGE_AKKA_SYSTEM_NAME = "logstorage";
    String LOGSTORAGE_AKKA_USER_NAME = "logstorage";
    String LOGSTORAGE_AKKA_USER_PATH = "/user/" + LOGSTORAGE_AKKA_USER_NAME;

    String EMPTY_STRING = "";
    String LINE_SEPARATOR = System.getProperty("line.separator");
    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String HTTP_CALLBACK_URL = "httpCallbackUrl";

    DateTime DATETIME_MIN = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeZone.forOffsetHours(0));
    DateTime DATETIME_MAX = new DateTime(9999, 1, 1, 0, 0, 0, DateTimeZone.forOffsetHours(0));

    int BIZ_GROUP_ID_UNKNOWN = 0;
    String BIZ_GROUP_NAME_UNKNOWN = "";

    Pattern IP_PATTERN = Pattern
            .compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

//    String JOB_PARAMS_KEY_4SCRIPT_ID = "_jarvis_script_id";
    String JOB_PARAMS_KEY_4JAR_URL = "_jarvis_jar_url";

}

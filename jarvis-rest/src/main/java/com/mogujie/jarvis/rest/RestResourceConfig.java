/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月7日 下午8:19:45
 */

package com.mogujie.jarvis.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class RestResourceConfig extends ResourceConfig {

    public RestResourceConfig() {
        packages("com.mogujie.jarvis.rest.controller");
    }

}

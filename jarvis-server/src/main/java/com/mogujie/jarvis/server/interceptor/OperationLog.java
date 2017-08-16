package com.mogujie.jarvis.server.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/2
 * time: 下午5:15
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface OperationLog {
}

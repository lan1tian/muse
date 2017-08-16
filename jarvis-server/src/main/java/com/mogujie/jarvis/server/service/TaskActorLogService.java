package com.mogujie.jarvis.server.service;

import com.google.inject.Singleton;
import com.mogujie.jarvis.server.interceptor.OperationLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/15
 * time: 下午3:56
 */
@Singleton
public class TaskActorLogService {
  private static final Logger LOGGER = LogManager.getLogger();

  @OperationLog
  public Object handleLog(Object obj) {
    return null;
  }

}

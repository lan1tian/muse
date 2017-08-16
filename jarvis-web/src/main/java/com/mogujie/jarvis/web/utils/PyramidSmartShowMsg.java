package com.mogujie.jarvis.web.utils;

import com.google.common.base.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 15/12/25
 * time: 下午6:11
 */
public class PyramidSmartShowMsg {

  private static final String ERR_TEMPLATE = "推测原因:";

  public static String getPyramidSmartShow(String msg, String defaultErrMsg) {
    Preconditions.checkNotNull(msg, "jarvis error msg is empty");

    Pattern pattern = Pattern.compile(".*" + ERR_TEMPLATE + "(.*)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(msg);
    if (matcher.matches()) {
      String smartShowMsg = matcher.group(1).split("\n")[0];
      return smartShowMsg;
    } else {
      return defaultErrMsg;
    }

  }

}

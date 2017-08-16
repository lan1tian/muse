/**
 * 蘑菇街 Inc.
 * <p/>
 * Copyright (c) 2010-2015 All Rights Reserved.
 */
package com.mogujie.jarvis.web.utils;

/**
 * @author 鸣人(mingren@mogujie.com)
 * @version 1.0 @15-11-27 上午10:31
 */
public enum EnumErrorType {

    TECH("技术原因"),
    NOT_TECH("非技术原因"),
    UNKNOWN("未知");

    String msg;

    EnumErrorType(String msg) {

        this.msg = msg;
    }

}

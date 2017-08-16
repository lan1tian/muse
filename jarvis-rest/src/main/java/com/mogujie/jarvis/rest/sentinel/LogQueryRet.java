/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月16日 下午2:47:27
 */

package com.mogujie.jarvis.rest.sentinel;

public class LogQueryRet extends BaseRet {


    @Override
    public String toString() {
        return "LogQueryRet [log=" + log + ", isEnd=" + isEnd + ", toString()="
                + super.toString() + "]";
    }

    private static final long serialVersionUID = 1937946404822259179L;

    private String log;
    private boolean isEnd=false;

    /**
     * @param responseCode
     * @param responseMsg
     */
    public LogQueryRet(Integer responseCode, String responseMsg) {
        super(responseCode, responseMsg);
        isEnd = false;
    }

    /**
     * @param success
     * @param responseMsg
     */
    public LogQueryRet(ResponseCodeEnum success, String responseMsg) {
        super(success, responseMsg);
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

}

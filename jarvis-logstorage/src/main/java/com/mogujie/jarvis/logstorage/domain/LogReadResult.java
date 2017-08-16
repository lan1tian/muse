package com.mogujie.jarvis.logstorage.domain;

/**
 * @author 牧名
 */
public class LogReadResult {

    private String log = "";
    private boolean isEnd = false;
    private long offset = 0;

    public LogReadResult(boolean isEnd, String log, long offset) {

        this.log = log;
        this.isEnd = isEnd;
        this.offset = offset;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}

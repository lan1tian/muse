package com.mogujie.jarvis.rest.vo;

/**
 * Created by hejian on 15/10/21.
 */
public class LogResultVo extends AbstractVo {
    private String log;
    private Long offset;
    private boolean isEnd;

    public String getLog() {
        return log;
    }
    public LogResultVo setLog(String log) {
        this.log = log;
        return this;
    }
    public Long getOffset() {
        return offset;
    }
    public LogResultVo setOffset(Long offset) {
        this.offset = offset;
        return this;
    }
    public boolean isEnd() {
        return isEnd;
    }
    public LogResultVo setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
        return this;
    }
}

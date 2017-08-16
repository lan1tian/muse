package com.mogujie.jarvis.logstorage.logStream;

import com.mogujie.jarvis.logstorage.domain.LogReadResult;

import java.io.IOException;

/**
 * Created by muming on 15/10/15.
 */
public interface LogStream {

    //写日志——一行
    public void writeText(String log) throws IOException;

    //写日志——结束标志
    public void writeEndFlag() throws IOException;

    /**
     * 读取日志
     *
     * @param offset        ：偏移量
     * @param size          ：字节数
     * @return              ：读取内容返回
     * @throws java.io.IOException
     */
    public LogReadResult readText(long offset, int size) throws IOException;

}

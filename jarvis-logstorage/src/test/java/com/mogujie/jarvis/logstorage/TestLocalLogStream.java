/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月14日 下午11:06:53
 */
package com.mogujie.jarvis.logstorage;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mogujie.jarvis.logstorage.logStream.LocalLogStream;
import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.logstorage.domain.LogReadResult;

/**
 * @author 牧名
 */
public class TestLocalLogStream {

    @Test
    public void testLog() throws ParseException, IOException {

        String testFile = "test_log_hello";
        LocalLogStream localLogStream = new LocalLogStream(testFile, StreamType.STD_OUT);

        //clear log
        localLogStream.clearLog();

        //write log
        List<String> logsWrite = new ArrayList<>();
        String text;
        for (Integer i = 1; i < 100; i++) {
            text = "hello_测试大法好,棒棒哒!" + i.toString() + "\n";
            logsWrite.add(text);
            localLogStream.writeText(text);
        }
        String LOG_END_LINE = "end_line";
        localLogStream.writeText(LOG_END_LINE);
        localLogStream.writeEndFlag();

        //read log
        LogReadResult result;
        long offset = 0;
        int size;
        for (String logExpect : logsWrite) {
            size = logExpect.length();
            int readLen = 0;
            String logRead = "";
            while (size - readLen > 0) {
                size = size - readLen;
                if (size > LogSetting.LOG_READ_MAX_SIZE) {
                    readLen = LogSetting.LOG_READ_MAX_SIZE;
                } else {
                    readLen = size;
                }
                result = localLogStream.readText(offset, readLen);
                offset = result.getOffset();
                Assert.assertEquals(result.isEnd(), false);
                logRead = logRead + result.getLog();
            }

            Assert.assertEquals(logExpect,logRead);
        }
        result = localLogStream.readText(offset, LogSetting.LOG_READ_MAX_SIZE);
        Assert.assertEquals(LOG_END_LINE,result.getLog());
        Assert.assertEquals(true,result.isEnd());

    }


}

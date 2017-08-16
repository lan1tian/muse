package com.mogujie.jarvis.logstorage.logStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.google.common.annotations.VisibleForTesting;
import com.mogujie.jarvis.core.domain.StreamType;
import com.mogujie.jarvis.core.util.IdUtils;
import com.mogujie.jarvis.logstorage.LogConstants;
import com.mogujie.jarvis.logstorage.LogSetting;
import com.mogujie.jarvis.logstorage.domain.LogReadResult;

/**
 * @author muming
 */
public class LocalLogStream implements LogStream {

    private String logFile;

    public LocalLogStream(String fullId, StreamType streamType) {
        String type = (streamType == StreamType.STD_OUT) ? ".out" : ".err";
        String logId = IdUtils.getLogIdFromFullId(fullId);
        logFile = LogSetting.LOG_LOCAL_PATH + "/" + logId + type;
    }

    /**
     * 写入日志
     *
     * @param text
     * @throws java.io.IOException
     */
    @Override
    public void writeText(String text) throws IOException {
        if (text == null || text.length() == 0) {
            return;
        }
        //写文件
        FileUtils.writeStringToFile(new File(logFile), text, StandardCharsets.UTF_8, true);
    }

    /**
     * 写入END标记
     *
     * @throws java.io.IOException
     */
    @Override
    public void writeEndFlag() throws IOException {
        writeText(String.valueOf(LogConstants.END_OF_LOG));
    }

    /**
     * 读取日志
     *
     * @param offset ：偏移量
     * @param size   ：字符数
     * @return ：读取内容返回
     * @throws java.io.IOException
     */
    @Override
    public LogReadResult readText(long offset, int maxCharSize) throws IOException {
        if (offset < 0) {
            offset = 0;
        }

        if (maxCharSize <= 0 || maxCharSize > LogSetting.LOG_READ_MAX_SIZE) {
            maxCharSize = LogSetting.LOG_READ_MAX_SIZE;
        }

        try (RandomAccessFile raf = new RandomAccessFile(logFile, "r")) {
            if (offset > raf.length()) {
                raf.seek(raf.length() - 1);
                int c = raf.read();
                if (c == LogConstants.END_OF_LOG) {
                    return new LogReadResult(true, "", raf.length());
                } else {
                    return new LogReadResult(false, "", offset);
                }
            }
            raf.seek(offset);

            int readCharSize = 0;
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Channels.newInputStream(raf.getChannel()), StandardCharsets.UTF_8))) {
                char[] charBuffer = new char[1024];
                int readCount = 0;
                int highSurrogateCount = 0;
                while ((readCount = br.read(charBuffer)) > -1) {
                    char[] readChars = readCount == charBuffer.length ? charBuffer : Arrays.copyOf(charBuffer, readCount);
                    for (char c : readChars) {
                        if (c == LogConstants.END_OF_LOG) {
                            return new LogReadResult(true, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
                        } else {
                            if (readCharSize < maxCharSize) {
                                sb.append(c);

                                // handle emoji
                                boolean isHighSurrogate = Character.isHighSurrogate(c);
                                if (!isHighSurrogate && highSurrogateCount == 0) {
                                    readCharSize++;
                                } else {
                                    if (!isHighSurrogate && highSurrogateCount == 2) {
                                        readCharSize++;
                                        highSurrogateCount = 0;
                                    }

                                    if (isHighSurrogate) {
                                        highSurrogateCount++;
                                    }
                                }
                            } else {
                                return new LogReadResult(false, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
                            }
                        }
                    }
                }
                return new LogReadResult(false, sb.toString(), offset + sb.toString().getBytes(StandardCharsets.UTF_8).length);
            }
        } catch (FileNotFoundException ex) {
            return new LogReadResult(false, "", 0);
        }
    }

    @VisibleForTesting
    public void clearLog() throws IOException {
        try {
            FileUtils.write(new File(logFile), "");
        } catch (IOException ex) {

        }
    }

}

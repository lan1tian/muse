package com.mogujie.jarvis.tasks;

import com.mogujie.jarvis.core.AbstractLogCollector;
import com.mogujie.jarvis.core.domain.StreamType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * Created by muming on 16/1/22.
 */

/**
 *   本地日志接口
 */
public class LocalLogCollector extends AbstractLogCollector {

    private static Logger logger = LogManager.getLogger();

    private String fullId;

    public LocalLogCollector(String fullId) {
        this.fullId = fullId;
    }

    private void write(String text, StreamType streamType) throws IOException {
        String type = (streamType == StreamType.STD_OUT) ? ".out" : ".err";
        String logFile = "/tmp/logs/" + fullId + type;
        Writer writer = null;
        try {
            writer = new FileWriter(logFile, true);
            writer.write(text);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    public void collectStdout(String line, boolean isEnd) {
        try {
            write(line, StreamType.STD_OUT);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void collectStderr(String line, boolean isEnd) {
        try {
            write(line, StreamType.STD_ERR);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


}

package com.mogujie.bigdata.base;

import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;

/**
 * Created by lixun on 2017/7/25.
 */
public class IOUtils {
    public static void closeQuietly(FileSystem fs) {
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

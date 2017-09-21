package com.mogujie.jarvis.worker;

import java.io.File;

/**
 * Created by lixun on 2017/9/20.
 */
public class Test {
    public static void main(String[] args) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String defaultDir = tmpDir.endsWith(File.separator) ? tmpDir + "jarvis_state_store" : tmpDir + File.separator + "jarvis_state_store";

        System.out.println(defaultDir);
    }
}

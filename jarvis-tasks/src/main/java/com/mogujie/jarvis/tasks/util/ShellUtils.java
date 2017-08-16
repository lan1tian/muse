/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月9日 下午1:18:35
 */
package com.mogujie.jarvis.tasks.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.mogujie.jarvis.core.exception.ShellException;

/**
 * @author wuya
 *
 */
public class ShellUtils {

    public static ProcessBuilder createProcessBuilder(String cmd) {
        return new ProcessBuilder("/bin/sh", "-c", cmd);
    }

    /**
     * 运行shell命令 命令执行错误时（exit_code !=0），会返回shellException，
     *
     * @param cmd
     *            : shell命令
     * @return : 命令执行反馈(stdErr)
     * @throws ShellException
     */
    public static BufferedReader executeShell(String cmd) throws ShellException {

        try {

            ProcessBuilder pb = ShellUtils.createProcessBuilder(cmd);

            Process p = pb.start();
            int exitCode = p.waitFor();

            InputStream inputStream = p.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            if (exitCode != 0) {

                StringBuilder stdError = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {

                    stdError.append(line);
                }
                throw new ShellException(stdError.toString());

            }

            return br;

        } catch (IOException | InterruptedException e) {
            throw new ShellException(e);
        }

    }

    // public static boolean executeShell(String cmd) throws IOException,InterruptedException {
    //
    // ProcessBuilder processBuilder = ShellUtils.createProcessBuilder(cmd);
    // Process process = processBuilder.start();
    // return (process.waitFor() == 0);
    // }

}

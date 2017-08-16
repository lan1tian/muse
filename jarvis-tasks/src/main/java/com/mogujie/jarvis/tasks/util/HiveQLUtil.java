/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午10:00:11
 */

package com.mogujie.jarvis.tasks.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guangming
 *
 */
public class HiveQLUtil {
    /**
     * 提取HiveQL脚本中的单条语句
     *
     * @param hql
     * @return
     */
    public static String[] splitHiveScript(String hql) {
        if (hql == null || hql.trim().isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String[] lines = hql.split("\n");
        for (String line : lines) {
            line = line.trim();
            // 去除注释语句（以#开头并已#结尾）
            if (!line.isEmpty() && !line.startsWith("#") && !line.endsWith("#") && !line.startsWith("--")) {
                sb.append(line);
                sb.append("\n");
            }
        }

        // 块注释处理_追加(muming 2014.08.28)
        String sql = sb.toString().replaceAll("(?s)/\\*.*?\\*/", "");
        // 追加结束

        String replacedHql = sql;
        List<String> list = new ArrayList<String>();
        int cursor = 0;
        int flag = 0;
        char mask = '0';
        for (int index = 0, len = replacedHql.length(); index < len; index++) {
            char c = replacedHql.charAt(index);
            if (c == ';') {
                if (flag == 0) {
                    String cmd = replacedHql.substring(cursor, index).trim();
                    cursor = index + 1;
                    if (!isBlank(cmd) && !cmd.startsWith("--") && !cmd.toUpperCase().replace("\t", " ").startsWith("SET ")) {
                        list.add(cmd);
                    }
                }
            } else if (c == '"' || c == '\'') {
                if (flag == 0) {
                    mask = c;
                    flag++;
                } else if (mask == c && (index == 0 || (index - 1 >= 0 && replacedHql.charAt(index - 1) != '\\'))) {
                    flag = 0;
                }
            }
        }

        if (cursor < replacedHql.length()) {
            String cmd = replacedHql.substring(cursor, replacedHql.length()).trim();
            if (!cmd.isEmpty() && !cmd.startsWith("--") && !cmd.toUpperCase().replace("\t", " ").startsWith("SET ")) {
                list.add(cmd);
            }
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * 提取出set命令
     *
     * @param @param hql
     * @param @return
     * @return String[]
     */
    public static String[] splitScriptForExec(String hql) {
        if (hql == null || hql.trim().isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String[] lines = hql.split("\n");
        for (String line : lines) {
            line = line.trim();
            // 去除注释语句（以#开头并已#结尾）
            if (!line.isEmpty() && !line.startsWith("#") && !line.endsWith("#") && !line.startsWith("--")) {
                sb.append(line);
                sb.append("\n");
            }
        }

        // 块注释处理_追加(muming 2014.08.28)
        String sql = sb.toString().replaceAll("(?s)/\\*.*?\\*/", "");
        // 追加结束

        String replacedHql = sql;
        List<String> list = new ArrayList<String>();
        int cursor = 0;
        int flag = 0;
        char mask = '0';
        for (int index = 0, len = replacedHql.length(); index < len; index++) {
            char c = replacedHql.charAt(index);
            if (c == ';') {
                if (flag == 0) {
                    String cmd = replacedHql.substring(cursor, index).trim();
                    cursor = index + 1;
                    if (!isBlank(cmd) && !cmd.startsWith("--")) {
                        list.add(cmd);
                    }
                }
            } else if (c == '"' || c == '\'') {
                if (flag == 0) {
                    mask = c;
                    flag++;
                } else if (mask == c && (index == 0 || (index - 1 >= 0 && replacedHql.charAt(index - 1) != '\\'))) {
                    flag = 0;
                }
            }
        }

        if (cursor < replacedHql.length()) {
            String cmd = replacedHql.substring(cursor, replacedHql.length()).trim();
            if (!cmd.isEmpty() && !cmd.startsWith("--")) {
                list.add(cmd);
            }
        }

        return list.toArray(new String[list.size()]);
    }

    public static String filterSetOper(String hql) {
        if (hql == null || hql.trim().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] lines = hql.split("\n");
        for (String line : lines) {
            line = line.trim();
            // 去除注释语句（以#开头并已#结尾）
            if (!line.isEmpty() && !line.startsWith("#") && !line.endsWith("#") && !line.startsWith("--")) {
                sb.append(line);
                sb.append("\n");
            }
        }

        // 块注释处理_追加(muming 2014.08.28)
        String sql = sb.toString().replaceAll("(?s)/\\*.*?\\*/", "");
        // 追加结束

        String replacedHql = sql;
        StringBuilder hqlBuilder = new StringBuilder();
        int cursor = 0;
        int flag = 0;
        char mask = '0';
        for (int index = 0, len = replacedHql.length(); index < len; index++) {
            char c = replacedHql.charAt(index);
            if (c == ';') {
                if (flag == 0) {
                    String cmd = replacedHql.substring(cursor, index).trim();
                    cursor = index + 1;
                    if (!isBlank(cmd) && !cmd.startsWith("--") && !cmd.toUpperCase().replace("\t", " ").startsWith("SET ")) {
                        hqlBuilder.append(cmd + ";");
                    }
                }
            } else if (c == '"' || c == '\'') {
                if (flag == 0) {
                    mask = c;
                    flag++;
                } else if (mask == c && (index == 0 || (index - 1 >= 0 && replacedHql.charAt(index - 1) != '\\'))) {
                    flag = 0;
                }
            }
        }

        if (cursor < replacedHql.length()) {
            String cmd = replacedHql.substring(cursor, replacedHql.length()).trim();
            if (!cmd.isEmpty() && !cmd.startsWith("--") && !cmd.toUpperCase().replace("\t", " ").startsWith("SET ")) {
                hqlBuilder.append(cmd + ";");
            }
        }

        return hqlBuilder.toString();
    }

    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

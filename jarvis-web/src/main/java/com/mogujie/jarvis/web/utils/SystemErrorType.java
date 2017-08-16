package com.mogujie.jarvis.web.utils;

//import com.mogujie.bigdata.base.StringUtils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * author:鸣人
 * mail: mingren@mogujie.com
 * date: 15-9-22 下午4:29
 */
public enum SystemErrorType {


    //语法错误
    SEMANTIC_ERROR(
            "您的脚本存在语法错误,请修改后重试",
            EnumErrorType.NOT_TECH, new String[]{

            "FAILED: ParseException",
    }),

    //表不存在
    TABLE_NOTFOUND(
            "您的脚本存在语法错误,请修改后重试",
            EnumErrorType.NOT_TECH, new String[]{

            "FAILED: SemanticException",
    }

    ),

    //内存错误
    OUT_OF_MEMEORY("" +
            "任务内存配置不合理,请使用Hive参数:" +
            "\nset set mapreduce.map.memory.mb=MAP阶段内存;" +
            "\nset mapred.map.child.java.opts=MAP阶段内存;" +
            "\nset mapreduce.reduce.memory.mb=REDUCE阶段内存;" +
            "\nset mapred.reduce.child.java.opts=REDUCE阶段内存;" +
            "\n进行调整",
            EnumErrorType.NOT_TECH,
            new String[]{
                    "Error: GC overhead limit exceeded",
                    "Error: Java heap space"
            }
    ),

    TASK_RUN_ERROR(

            "执行时遇到错误",
            EnumErrorType.TECH,
            new String[]{

                    "AlreadyExistsException",
                    "Caused by: java.io.FileNotFoundException"
            }
    ),

    HIVE_RUNTIME_ERROR(
            "HIVE执行出错",
            EnumErrorType.TECH,
            new String[]{
                    "Error: java.lang.RuntimeException: org.apache.hadoop.hive.ql.metadata.HiveException"
            }),

    CLUSTER_ERROR1(
            "集群错误",
            EnumErrorType.TECH,
            new String[]{
                    "FAILED: Execution Error",
                    "java.io.IOException: Could not find status of job",
                    "AttemptID.*Timed out after 600 secs"
            }

    ),
    

    PYRAMID_ERROR(

            "pyramid错误",
            EnumErrorType.TECH,
            new String[]{

                    "ERROR - mr_handle",
                    "ERROR - dump_data",
                    "ERROR - output_data",
                    "ERROR - hdata_run",
                    "ERROR - metadata_prepare ",
            }),

    UNKNOWN(
            "未知错误",
            EnumErrorType.UNKNOWN,
            new String[]{
                    "",
            }
    );

    private String notifyMsg;
    private String[] errorTemplate;
    private EnumErrorType enumErrorType;


    SystemErrorType(String notifyMsg, EnumErrorType enumErrorType, String[] errorTemplate) {

        this.errorTemplate = errorTemplate;

        this.enumErrorType = enumErrorType;

        this.notifyMsg = notifyMsg;
    }


    public static SystemErrorType autoInferErrorType(String msg) {

        if (StringUtils.isEmpty(msg)) {

            return SystemErrorType.UNKNOWN;
        }

        boolean found = false;

        SystemErrorType currentError = SystemErrorType.UNKNOWN;

        for (SystemErrorType errorType : SystemErrorType.values()) {

            if (errorType != SystemErrorType.UNKNOWN) {

                found = false;
                String errorTemplates[] = errorType.getErrorTemplate();
                for (String errTemplate : errorTemplates) {

                    Pattern p = Pattern.compile(".*" + errTemplate + ".*", Pattern.DOTALL);
                    Matcher m = p.matcher(msg);
                    if (m.matches()) {

                        found = true;

                        break;
                    }

                }

                if (found) {

                    currentError = errorType;
                    break;
                }
            }
        }

        if (SystemErrorType.PYRAMID_ERROR.name().equals(currentError.name())) {
            currentError.setNotifyMsg(PyramidSmartShowMsg.getPyramidSmartShow(msg, currentError.getNotifyMsg()));
        }

        return currentError;
    }

    public String getNotifyMsg() {
        return notifyMsg;
    }

    public void setNotifyMsg(String notifyMsg) {
        this.notifyMsg = notifyMsg;
    }

    public String[] getErrorTemplate() {
        return errorTemplate;
    }

    public EnumErrorType getEnumErrorType() {
        return enumErrorType;
    }

    public void setEnumErrorType(EnumErrorType enumErrorType) {
        this.enumErrorType = enumErrorType;
    }

    @Override
    public String toString() {
        return "SystemErrorType{" +
                "notifyMsg='" + notifyMsg + '\'' +
                ", enumErrorType=" + enumErrorType +
                '}';
    }
}


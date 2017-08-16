const CONST = {
    MSG_CODE: { //消息Code
        SUCCESS: 1000,
        FAILED: 1001
    },
    OPERATE_MODE: { //操作模式
        ADD: 1,
        EDIT: 2,
        DELETE: 3
    },
    WORKER_STATUS: {
        ONLINE: 1,
        OFFLINE: 2
    },
    WORKER_GROUP_STATUS: {
        ENABLE: 1,
        DISABLE: 2,
        DELETED: 3
    },
    JOB_TYPE: { //job类型
        DUMMY: 'dummy',
        MAPREDUCE: 'mapreduce',
        HIVE: 'hive',
        JAVA: 'java',
        SHELL: 'shell',
        SPARK_LAUNCHER: 'sparkLauncher'
    },
    CONTENT_TYPE: { //content类型
        TEXT: 1,
        SCRIPT: 2,
        JAR: 3,
        EMPTY: 0
    },
    JOB_ACTIVE_DATE: {  //任务有效日期
        MIN_DATE: 0,
        MAX_DATE: 253370764800000
    },
    SPARK_LAUNCHER_JOB: { //sparkLauncher任务
        PARAMS_KEY: {
            taskName: 'taskName',
            mainClass: 'mainClass',
            taskJar: 'taskJar',
            applicationArguments: 'applicationArguments',
            driverCores: 'driverCores',
            driverMemory: 'driverMemory',
            executorCores: 'executorCores',
            executorMemory: 'executorMemory',
            executorNum: 'executorNum',
            sparkSubmitProperties: 'sparkSubmitProperties',
            sparkVersion: 'sparkVersion'
        }
    },
    JAVA_JOB: {  //java任务
        PARAMS_KEY: {
            mainClass: 'mainClass',
            jar: 'jar',
            classpath: 'classpath',
            arguments: 'arguments',
        }
    },
    MAPREDUCE_JOB: {  //java任务
    },
    SCHEDULE_CIRCLE_TYPE: { //计划类型
        NONE: 0,        //未知
        PER_DAY: 1,     //每天
        PER_HOUR: 2,    //每小时
        PER_WEEK: 3,    //每周
        PER_MONTH: 4,   //每月
        PER_YEAR: 5     //每年
    },
    SCHEDULE_EXP_TYPE: { //表达式类型
        CRON: 1,            //Cron表达式
        FIXED_RATE: 2,      //固定频率
        FIXED_DELAY: 3,     //固定时延
        ISO8601: 4          //ISO8601
    }

};



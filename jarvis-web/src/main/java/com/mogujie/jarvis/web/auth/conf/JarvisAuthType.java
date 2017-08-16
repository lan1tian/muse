package com.mogujie.jarvis.web.auth.conf;


public interface JarvisAuthType {

    String plan = "1001"; //执行计划
    String task = "2001"; //执行流水
    String job = "3001"; //任务管理
    String trigger = "4001"; //重跑任务
    String manage_system = "5001"; //调度系统管理
    String manage_app = "5002"; // 应用管理
    String manage_worker = "5003"; // worker管理
    String manage_biz = "5004"; //业务类型管理
    String help = "6001"; //使用帮助手册
    String operation = "7001"; // 操作记录
    String department = "8001"; //部门管理
}

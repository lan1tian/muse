package com.mogujie.jarvis.core.domain;

/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/3
 * time: 下午12:08
 */
public enum OperationInfo {

  // job actor
  SUBMITJOB("submitJob", "提交任务"),
  MODIFYJOB("modifyJob", "修改任务"),
  MODIFYJOBDEPENDENCY("modifyJobDependency", "修改任务依赖"),
  MODIFYJOBSCHEDULEEXP("modifyJobScheduleExp", "修改任务计划表达式"),
  MODIFYJOBSTATUS("modifyJobStatus", "修改任务状态"),

  // task actor
  MANUALRERUNTASK("manualRerunTask", "手动重跑");

  OperationInfo(String name, String description) {
    this.name = name;
    this.description = description;
  }

  private String name;
  private String description;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}

package com.mogujie.jarvis.web.entity.vo;

/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午7:51
 */
public class DepartmentVo {

  private String departmentName;
  private String bizGroupName;
  private String bizGroupOwner;
  private String updateTime;
  private Integer id;

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public String getBizGroupName() {
    return bizGroupName;
  }

  public void setBizGroupName(String bizGroupName) {
    this.bizGroupName = bizGroupName;
  }

  public String getBizGroupOwner() {
    return bizGroupOwner;
  }

  public void setBizGroupOwner(String bizGroupOwner) {
    this.bizGroupOwner = bizGroupOwner;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}

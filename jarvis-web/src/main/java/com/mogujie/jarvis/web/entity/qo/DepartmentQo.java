package com.mogujie.jarvis.web.entity.qo;

import com.mogujie.jarvis.core.util.JsonHelper;
import java.util.List;
import org.apache.commons.lang3.StringUtils;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午7:24
 */
public class DepartmentQo {
  private List<String> nameList;
  private List<String> bizGroupList;
  private List<String> ownerList;
  private String name;
  private String bizGroup;
  private String owner;

  private Integer offset;
  private Integer limit;
  private String order;

  public List<String> getNameList() {
    return nameList;
  }

  public void setNameList(String nameList) {
    if(StringUtils.isNotBlank(nameList)) {
      List<String> list = JsonHelper.fromJson(nameList, List.class);
      if (list.size() > 0) {
        this.nameList = list;
      }
    }
  }

  public List<String> getBizGroupList() {
    return bizGroupList;
  }

  public void setBizGroupList(String bizGroupList) {
    if(StringUtils.isNotBlank(bizGroupList)) {
      List<String> list = JsonHelper.fromJson(bizGroupList, List.class);
      if(list.size() > 0) {
        this.bizGroupList = list;
      }
    }
  }

  public List<String> getOwnerList() {
    return ownerList;
  }

  public void setOwnerList(String ownerList) {
    if (StringUtils.isNotBlank(ownerList)) {
      List<String> list = JsonHelper.fromJson(ownerList, List.class);
      if (list.size() > 0) {
        this.ownerList = list;
      }
    }
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBizGroup() {
    return bizGroup;
  }

  public void setBizGroup(String bizGroup) {
    this.bizGroup = bizGroup;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}

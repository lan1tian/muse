/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月17日 下午1:05:45
 */

package com.mogujie.jarvis.tasks.domain;

/**
 * @author wuya
 */
public class HiveTaskEntity {

    private String name;
    private String user;
    private boolean isAdmin;
    private int maxResultRows;
    private int maxMapperNum;

    public HiveTaskEntity(String name, String user, boolean isAdmin, int maxResultRows, int maxMapperNum) {
        this.name = name;
        this.user = user;
        this.isAdmin = isAdmin;
        this.maxResultRows = maxResultRows;
        this.maxMapperNum = maxMapperNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getMaxResultRows() {
        return maxResultRows;
    }

    public void setMaxResultRows(int maxResultRows) {
        this.maxResultRows = maxResultRows;
    }

    public int getMaxMapperNum() {
        return maxMapperNum;
    }

    public void setMaxMapperNum(int maxMapperNum) {
        this.maxMapperNum = maxMapperNum;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "HiveJobEntry [name=" + name + ", isAdmin=" + isAdmin + ", user=" + user + ", maxResultRows="
                + maxResultRows + ", maxMapperNum=" + maxMapperNum + "]";
    }

}

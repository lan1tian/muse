/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午6:58:32
 */

package com.mogujie.jarvis.rest.jarvis;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author yinxiu
 * @version $Id: User.java,v 0.1 2013-6-6 上午9:17:02 yinxiu Exp $
 */
public class User implements Serializable, SelfDependency {
    private static final long serialVersionUID = 9054794938270174666L;
    private String name;
    private String menuId;
    private BigInteger functions; // 以2进制位纪录管理员用户的权限
    private String pinYin;

    public static final String USER_DEFAULT_PERMISSION = "0";

    public User() {
        // 给一个默认的权限
        this.functions = new BigInteger("0");
        this.name = "未知";
        this.pinYin = "unknown";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setFunctions(int pos) {
        if (this.functions == null) {
            this.functions = new BigInteger("0");
        }
        this.functions = this.functions.setBit(pos);
    }

    public void setFunctions(String userPermission) {
        this.functions = new BigInteger(userPermission, 10);
    }

    public boolean isAdmin() {

        if (name.equals("etlprd") || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdminOrAuthor(String name) {

        if ("etlprd".equalsIgnoreCase(this.name) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        // 判断用户是否是任务发布者
        return this.name.equals(name);
    }

    /**
     * 在指定的2进制位上是否有权限
     *
     * @param index
     * @return
     */
    public boolean haveFunction(int index) {
        // 超级管理员 或者 具有超级管理员权限的账号 没有权限限制
        if ("etlprd".equalsIgnoreCase(this.name) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        if (functions == null) {
            return false;
        }
        return this.functions.testBit(index);
    }

    /**
     * 是否拥有权限
     *
     * @param fe
     * @return
     */
    public boolean haveFunction(PermissionEnum fe) {
        return haveFunction(fe.getCode());
    }

    /**
     * 是否有传入权限枚举名字其中之一的权限
     *
     * @param permissionName
     * @return
     */
    public boolean haveFunction(String... permissionName) {
        // 超级管理员 或者 具有超级管理员权限的账号 没有权限限制
        if ("etlprd".equalsIgnoreCase(this.name) || this.functions.testBit(PermissionEnum.SUPER_ADMIN.getCode())) {
            return true;
        }
        for (String pName : permissionName) {
            PermissionEnum p = PermissionEnum.indexOf(pName);
            if (null == p) {
                return false;
            }
            if (this.functions.testBit(p.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String lieDown() {
        return SelfUtil.format(this.name, this.menuId, this.functions.toString(36));
    }

    @Override
    public User riseUp(String value) {
        String[] values = SelfUtil.recover(value);
        this.name = values[0];
        this.menuId = values[1];
        this.functions = new BigInteger(values[2], 36);
        return this;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", menuId='" + menuId + '\'' + ", functions=" + functions + ", pinYin='" + pinYin + '\'' + '}';
    }
}

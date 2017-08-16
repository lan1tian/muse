/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月23日 下午7:03:20
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限枚举
 * <p/>
 * <pre>
 * 1.权限值由0开始，0分配为超级管理员
 * 2.模块值，由两位数值组成（超级管理员0除外），取值范围10-99
 * 3.枚举名称定义遵循规则
 *     BIZ开头代表业务相关模块，后面跟CRUD之一，后面是页面或者是模块对应的缩写
 *     SYS开头代表与系统相关的模块，后面跟CRUD之一，后面是页面或者是模块对应的缩写
 * </pre>
 *
 * @author yinxiu
 * @version $Id: PermissionEnum.java,v 0.1 2014年1月2日 下午1:28:03 yinxiu Exp $
 */
public enum PermissionEnum {
    SUPER_ADMIN(169, 0, "超级管理员");

    private static Map<Object, PermissionEnum> pool = new HashMap<Object, PermissionEnum>();
    private static Map<Integer, List<PermissionEnum>> module_pool = new HashMap<Integer, List<PermissionEnum>>();

    static {
        for (PermissionEnum each : PermissionEnum.values()) {
            PermissionEnum defined = pool.get(each.code);
            // 有相同权限代码定义抛出例外
            if (null != defined) {
                throw new java.lang.IllegalArgumentException(defined.toString()
                        + " defined as same code with " + each.toString());
            }
            // 权限代码值压入缓存池
            pool.put(each.code, each);
            // 权限枚举名压入缓存池
            pool.put(each.name(), each);
            // 权限功能模块值压入缓存
            List<PermissionEnum> listPermissionEnum = module_pool
                    .get(each.module);
            if (null == listPermissionEnum) {
                listPermissionEnum = new ArrayList<PermissionEnum>();
                module_pool.put(each.module, listPermissionEnum);
            }
            listPermissionEnum.add(each);
        }
    }

    PermissionEnum(int code, int module, String desc) {
        this.code = code;
        this.module = module;
        this.desc = desc;
    }

    // 权限代码值
    private int code;
    // 功能模块
    private int module;
    // 描述
    private String desc;

    public int getCode() {
        return code;
    }


    public int getModule() {
        return module;
    }


    public String getDesc() {
        return desc;
    }


    public static PermissionEnum indexOf(int code) {
        return pool.get(code);
    }

    public static PermissionEnum indexOf(String name) {
        return pool.get(name);
    }

    /**
     * 根据功能模块索引权限
     *
     * @param model
     * @return
     */
    public static List<PermissionEnum> indexModule(int model) {
        List<PermissionEnum> list = new ArrayList<PermissionEnum>();
        // 功能模块定义2位数，范围10-99
        if (model > 99 || model < 10) {
            return list;
        }
        List<PermissionEnum> fromPool = module_pool.get(model);
        if (null == fromPool) {
            return list;
        } else {
            return fromPool;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getName()).append("[code:")
                .append(code).append(",desc:").append(desc).append("]")
                .toString();
    }

}

/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月12日 下午4:56:39
 */
package com.mogujie.jarvis.core.domain;

/**
 * @author muming
 */
public enum OperationMode {

    ADD(1),         //追加
    EDIT(2),        //修改
    DELETE(3);      //删除

    private int value;

    OperationMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Boolean isValid(int value) {
        OperationMode[] values = OperationMode.values();
        for (OperationMode s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public static OperationMode parseValue(int value) {
        OperationMode[] all = OperationMode.values();
        for (OperationMode s : all) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("OperationMode value is invalid. value:" + value);
    }

    /**
     * 是否在scope中
     * @param scope
     * @return
     */
    public Boolean isIn(OperationMode ... scope){

        for(OperationMode member : scope){
            if(ordinal() == member.ordinal()){
                return true;
            }
        }
        return false;
    }


}

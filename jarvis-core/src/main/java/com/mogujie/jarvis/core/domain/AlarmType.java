package com.mogujie.jarvis.core.domain;

import com.google.common.primitives.Ints;

/**
 * Created by hejian on 16/1/8.
 */
public enum AlarmType {

    SMS(1, "短信"),      //短信
    TT(2, "TT"),        //TT
    EMAIL(3, "邮件"),    //邮件
    WEIXIN(4, "微信");   //微信

    private int value;
    private String description;

    AlarmType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public static AlarmType parseValue(int value) {
        AlarmType[] statusList = AlarmType.values();
        for (AlarmType s : statusList) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("AlarmType value is invalid. value:" + value);
    }


    public String getDescription() {
        return description;
    }

    public static Boolean isValid(int value) {
        AlarmType[] values = AlarmType.values();
        for (AlarmType s : values) {
            if (s.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查字符串(用逗号或者分号间隔的值)
     *
     * @param values
     * @return
     */
    public static Boolean isValid(String values){
        if (values == null) {
            return false;
        }
        String[] valueArray = values.split("[;,]");
        for (String strValue : valueArray) {
            Integer intValue = Ints.tryParse(strValue);
            if (intValue == null || !isValid(intValue)) {
                return false;
            }
        }
        return true;
    }

}

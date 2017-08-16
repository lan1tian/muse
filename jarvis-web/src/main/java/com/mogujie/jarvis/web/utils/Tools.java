package com.mogujie.jarvis.web.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejian on 16/1/13.
 */
public class Tools {

    /*
    * 取出某个类型的所有字段名
    * */
    public static <T> List<String> getObjectField(Class<T> objectClass) {
        List<String> list = new ArrayList<String>();

        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }
}

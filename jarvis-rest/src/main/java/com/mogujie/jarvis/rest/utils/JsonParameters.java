package com.mogujie.jarvis.rest.utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.mogujie.jarvis.core.util.JsonHelper;

/**
 * Created by muming on 15/11/26.
 */

public class JsonParameters {

    static Type mapType = new TypeToken<Map<String, Object>>() {
    }.getType();

    private Map<String, Object> data;

    public JsonParameters(String jsonString) {
        this(jsonString, mapType);
    }

    public JsonParameters(String jsonString, Type typeOfT) {
        data = JsonHelper.fromJson(jsonString, typeOfT);
        if (data == null) {
            data = new HashMap<>();
        }
    }

    public Object getObject(String key, Object defaultVal) {
        if (data.containsKey(key)) {
            return data.get(key).toString();
        } else {
            return defaultVal;
        }
    }

    public Object getObject(String key) {
        return getObject(key, null);
    }

    public String getString(String key, String defaultVal) {
        if (data.containsKey(key)) {
            return data.get(key).toString();
        } else {
            return defaultVal;
        }
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultVal) {
        if (data.containsKey(key)) {
            return Boolean.parseBoolean(data.get(key).toString());
        } else {
            return defaultVal;
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getStringNotEmpty(String key) throws IllegalArgumentException {
        String value = getString(key, null);
        if (value == null || value.equals("")) {
            throw new IllegalArgumentException(key + "不能为空");
        }
        return value;
    }

    public Integer getInteger(String key, Integer defaultVal) {
        if (data.containsKey(key)) {
            try {
                return convert2Integer(data.get(key), defaultVal);
            } catch (NumberFormatException Ex) {
                throw new IllegalArgumentException("获取Integer值失败；key='" + key + "';value=" + data.get(key).toString());
            }
        } else {
            return defaultVal;
        }
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getIntegerNotNull(String key) {
        Integer value = getInteger(key, null);
        if (value == null) {
            throw new IllegalArgumentException(key + "不能为空");
        }
        return value;
    }

    public Long getLong(String key, Long defaultVal) {
        if (data.containsKey(key)) {
            try {
                return convert2Long(data.get(key), defaultVal);
            } catch (NumberFormatException Ex) {
                throw new IllegalArgumentException("获取Long值失败；key='" + key + "';value=" + data.get(key).toString());
            }
        } else {
            return defaultVal;
        }
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLongNotNull(String key) {
        Long value = getLong(key, null);
        if (value == null) {
            throw new IllegalArgumentException(key + "不能为空");
        }
        return value;
    }

    public Double getDouble(String key, Double defaultVal) {
        if (data.containsKey(key)) {
            try {
                return Double.parseDouble(data.get(key).toString());
            } catch (NumberFormatException Ex) {
                throw new IllegalArgumentException("获取Double值失败；key='" + key + "';value=" + data.get(key).toString());
            }
        } else {
            return defaultVal;
        }
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public <T> List<T> getList(String key, Type typeOfT) {
        return JsonHelper.fromJson(getString(key), typeOfT);
    }

    private Long convert2Long(Object value, Long defaultVal) throws NumberFormatException {
        if (value.getClass() == Long.class) {
            return (Long) value;
        }
        if (value.getClass() == String.class) {
            if (value.equals("")) {
                return defaultVal;
            }
            return Long.parseLong((String) value);
        }

        if (value.getClass() != Double.class) {
            throw new NumberFormatException();
        }
        Double d = (Double) value;
        if (d - d.longValue() != 0) {
            throw new NumberFormatException();
        }
        return d.longValue();
    }

    private Integer convert2Integer(Object value, Integer defaultVal) throws NumberFormatException {
        if (value.getClass() == Integer.class) {
            return (Integer) value;
        }
        if (value.getClass() == String.class) {
            if (value.equals("")) {
                return defaultVal;
            }
            return Integer.parseInt((String) value);
        }
        if (value.getClass() != Double.class) {
            throw new NumberFormatException();
        }
        Double d = (Double) value;
        if (d - d.longValue() != 0 || d.intValue() != d.longValue()) {
            throw new NumberFormatException();
        }
        return d.intValue();
    }

}

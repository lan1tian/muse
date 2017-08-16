/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月12日 下午2:15:30
 */

package com.mogujie.jarvis.worker.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.joda.time.DateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mogujie.jarvis.worker.misc.JodaDateTimeSerializer;

public class KryoUtils {

    private static ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(DateTime.class, new JodaDateTimeSerializer());
            return kryo;
        };
    };

    public static byte[] toBytes(Object obj) {
        Kryo kryo = kryos.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream, 4096);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object toObject(byte[] bytes) {
        Kryo kryo = kryos.get();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream, 4096);
        return kryo.readClassAndObject(input);
    }
}

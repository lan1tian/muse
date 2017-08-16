/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月12日 下午3:49:33
 */

package com.mogujie.jarvis.worker.misc;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class JodaDateTimeSerializer extends Serializer<DateTime> {

    public JodaDateTimeSerializer() {
        setImmutable(true);
    }

    @Override
    public DateTime read(final Kryo kryo, final Input input, final Class<DateTime> type) {
        final long millis = input.readLong(true);
        final Chronology chronology = IdentifiableChronology.readChronology(input);
        final DateTimeZone tz = readTimeZone(input);
        return new DateTime(millis, chronology.withZone(tz));
    }

    @Override
    public void write(final Kryo kryo, final Output output, final DateTime obj) {
        output.writeLong(obj.getMillis(), true);

        final String chronologyId = IdentifiableChronology.getChronologyId(obj.getChronology());
        output.writeString(chronologyId == null ? "" : chronologyId);

        output.writeString(obj.getZone().getID());
    }

    private DateTimeZone readTimeZone(final Input input) {
        final String tz = input.readString();

        if ("".equals(tz)) {
            return DateTimeZone.getDefault();
        }

        return DateTimeZone.forID(tz);
    }

}

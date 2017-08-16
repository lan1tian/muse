/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月6日 下午4:57:59
 */

package com.mogujie.jarvis.worker.status.store;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.mogujie.jarvis.core.domain.TaskDetail;
import com.mogujie.jarvis.worker.status.TaskStateStore;
import com.mogujie.jarvis.worker.util.KryoUtils;

public class LocalFileSystemStateStore implements TaskStateStore {

    private DB db = null;

    @Override
    public void init(Configuration conf) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String defaultDir = tmpDir.endsWith(File.separator) ? tmpDir + "jarvis_state_store" : tmpDir + File.separator + "jarvis_state_store";
        File file = new File(conf.getString("local.filesystem.statestore.dir", defaultDir));
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = Iq80DBFactory.factory.open(file, options);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void write(TaskDetail taskDetail, int status) {
        db.put(KryoUtils.toBytes(taskDetail), Ints.toByteArray(status));
    }

    @Override
    public void delete(String fullId) {
        DBIterator iterator = db.iterator();
        try {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                byte[] key = iterator.peekNext().getKey();
                TaskDetail taskDetail = (TaskDetail) KryoUtils.toObject(key);
                if (fullId.equals(taskDetail.getFullId())) {
                    db.delete(key);
                }
            }
        } finally {
            try {
                iterator.close();
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }

    @Override
    public Map<TaskDetail, Integer> restore() {
        Map<TaskDetail, Integer> result = Maps.newHashMap();
        DBIterator iterator = db.iterator();
        try {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                TaskDetail taskDetail = (TaskDetail) KryoUtils.toObject(iterator.peekNext().getKey());
                int status = Ints.fromByteArray(iterator.peekNext().getValue());
                result.put(taskDetail, status);
            }
        } finally {
            try {
                iterator.close();
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }

        return result;
    }

    @Override
    public void close() {
        if (db != null) {
            try {
                db.close();
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }
}

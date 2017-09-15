package com.mogujie.jarvis.tasks;

import org.junit.Assert;
import org.junit.Test;

import com.mogujie.jarvis.tasks.domain.HiveTaskEntity;
import com.mogujie.jarvis.tasks.util.HiveConfigUtils;

/**
 * Created by muming on 15/12/7.
 */
public class TestHiveConfigUtils {

    @Test
    public void read(){

        String app = "ironman";
        HiveTaskEntity hive = HiveConfigUtils.getHiveJobEntry(app);
        System.out.println("HiveTaskEntity="+hive);
        Assert.assertNotNull(hive);
    }

}

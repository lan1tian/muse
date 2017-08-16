package com.mogujie.jarvis.web.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午8:53
 */
public class CommonUtils {

  public static Map<String, Object> getSimilarMsg(List<String> lists) {
    Map<String, Object> result = new HashMap();
    List<Map> list = new ArrayList<>();
    for(int i=0; i<lists.size(); i++) {
      Map<String, Object> item = new HashMap<String, Object>();
      item.put("id", lists.get(i));
      item.put("text", lists.get(i));
      list.add(item);
    }

    result.put("total", lists.size());
    result.put("items", list);

    return result;
  }

}

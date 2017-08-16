/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2013 All Rights Reserved.
 *
 * Author     :yinxiu
 * Version    :1.0
 * Create Date:2013年9月22日
 */
package com.mogujie.jarvis.tasks.util;

/**
 * HiveQL注释去除工具
 * 
 * @author yinxiu
 * @version $Id: MoguAnnotationUtil.java,v 0.1 2013年9月22日 上午10:01:51 yinxiu Exp
 *          $
 */
public class MoguAnnotationUtils {
	/**
	 * 去除注释
	 * 
	 * @return String
	 */
	public static String removeAnnotation(String content) {
		StringBuilder builder = new StringBuilder();
		if (content == null) {
			return "";
		}
		String[] lines = content.split("\n");
		for (int i = 0; i < lines.length; i++) {
			// 这行内容不是注释，添加到结果中
			String line = lines[i].trim();
			if (!(line.startsWith("#") && line.endsWith("#"))) {
				builder.append(lines[i] + "\n");
			}
		}
		return builder.toString();
	}
}

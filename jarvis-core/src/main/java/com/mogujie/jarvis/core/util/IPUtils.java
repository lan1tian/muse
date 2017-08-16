/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月11日 下午2:01:02
 */

package com.mogujie.jarvis.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;

import com.mogujie.jarvis.core.JarvisConstants;

public class IPUtils {

    public static String getIPV4Address() throws UnknownHostException, SocketException {
        String ip = Inet4Address.getLocalHost().getHostName();
        Matcher m = JarvisConstants.IP_PATTERN.matcher(ip);
        if (m.matches() && !ip.equals("127.0.0.1")) {
            return ip;
        }

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return null;

    }
}

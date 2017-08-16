package com.mogujie.jarvis.server.actor.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Location www.mogujie.com
 * Created by qinghuo on 16/1/15.
 * used by jarvis-parent
 */
public class TestUtil {
    public static boolean isPortHasBeenUse(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket();
            InetSocketAddress inetAddress = new InetSocketAddress(host, port);
            socket.bind(inetAddress);
            return true;
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                System.err.println("socket 关闭异常");
            }
            System.err.println("Address " + host + " on " + port + " already in use ");
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

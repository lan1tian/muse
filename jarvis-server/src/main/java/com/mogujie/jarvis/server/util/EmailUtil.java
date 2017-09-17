package com.mogujie.jarvis.server.util;

import com.google.common.collect.Maps;
import com.mogujie.jarvis.server.ServerConigKeys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ruson on 2017/9/15.
 */
public class EmailUtil {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void send( Map<String, Object> fields) {
        LOGGER.info("email alerm:"+fields);
        String to = "test02@yg.com";
        String from = "jarvis@alert.com";
        String host = "172.17.1.222";
        String protocol = "smtp";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail." + protocol + ".auth", "false");
        properties.put("mail.smtp.ssl.trust", host);
//        Session session = Session.getDefaultInstance(properties);
//        try{
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            String msg = fields.get("errorMsg")+"";
//            String appName = fields.get("appName")+"";
//            message.setSubject(appName);
//            message.setText(msg);
//            Transport.send(message);
//        }catch (Exception mex) {
//            LOGGER.warn("error send email: ", mex);
//        }
    }

    public static void main(String[] args) {
        Map<String, Object> fields = Maps.newHashMap();
        fields.put("appName", "test");
        fields.put("errorLevel","test");
        fields.put("errorMsg", "teste");
        send(fields);
    }
}

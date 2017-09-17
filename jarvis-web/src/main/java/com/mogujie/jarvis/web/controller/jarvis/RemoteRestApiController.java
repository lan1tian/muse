package com.mogujie.jarvis.web.controller.jarvis;

import com.google.common.hash.Hashing;
//import com.mogu.bigdata.admin.core.entity.User;
//import com.mogu.bigdata.admin.passport.user.UserContextHolder;
//import com.mogujie.bigdata.base.StringUtils;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.entity.vo.AppVo;
import com.mogujie.jarvis.web.utils.SystemErrorType;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by hejian on 15/10/12.
 */
@Controller
@RequestMapping("/remote")
public class RemoteRestApiController {
    static String domain = "";
    static AppVo app = new AppVo();

    static Logger logger = Logger.getLogger("jarvisLogger");

    /*
    * 读取配置文件
    * */
    static {
        try {
            InputStream inputStream = RemoteRestApiController.class.getClassLoader().getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            domain = properties.getProperty("rest.domain");

            InputStream appInputStream = RemoteRestApiController.class.getClassLoader().getResourceAsStream("app.properties");
            Properties appProperties = new Properties();
            appProperties.load(appInputStream);

            app.setAppId(Integer.parseInt(appProperties.getProperty("app.id")));
            app.setAppName(appProperties.getProperty("app.name"));
            app.setAppKey(appProperties.getProperty("app.key"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    enum EnumAPIType {

        GetLog("/api/log/readExecuteLog"),
        UNKNOWN("");

        private String url;

        EnumAPIType(String url) {

            this.url = url;
        }

        public static EnumAPIType getAPIType(String url) {

//            if (StringUtils.isEmpty(url)) {
            if (url != null) {

                return UNKNOWN;
            } else {


                for (EnumAPIType enumApiType : EnumAPIType.values()) {

                    if (enumApiType.url.toLowerCase().equals(url.trim().toLowerCase())) {

                        return enumApiType;
                    }
                }
                return UNKNOWN;
            }

        }
    }

    /*
    * 执行远程请求的方法，作为一个client，需要传入参数url与para
    * @param url,远程rest url，必须
    * @param para,json格式的字符串，请求rest url
    * @author hejian
    * */
    @RequestMapping("/request")
    @ResponseBody
    public Map<String, Object> restApi(ModelMap modelMap, String url, String para) {
        Map<String, Object> result = new HashMap<String, Object>();
        String originUrl = url;
        url = domain + url;

        Map<String, String> data = new HashMap<String, String>();
        data.put("parameters", para);
        try {
//            User currentUser = UserContextHolder.getUser();
//            String nick = currentUser.getNick();
            String nick = "ruson";
            data.put("user", nick);

            DateTime dateTime = new DateTime();
            Long timeStamp = dateTime.getMillis() / 1000;
            String token = generateToken(timeStamp, app.getAppKey());

            data.put("appToken", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put("appName", app.getAppName());
        data.put("appKey", app.getAppKey());

        //请求远程REST服务器。
        try {
            logger.info("remote url:" + url);
            logger.info("para:" + para);
            logger.info("data: "+data.toString());
            Connection connection = Jsoup.connect(url)
                    .data(data)
                    .postDataCharset("UTF-8")
                    .ignoreContentType(true)
                    .timeout(15000)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.POST);

            Connection.Response response = connection.execute();
            logger.info("request url:" + response.url());
            String resultBody = response.body();
            logger.info("返回结果:" + resultBody);
            result = JsonHelper.fromJson(resultBody, Map.class);

            result = this.postHandle(originUrl, result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 1);
            result.put("msg", e.getLocalizedMessage());
        }
        return result;
    }

    //todo 结构定义-硬编码
    private Map<String, Object> postHandle(String url, Map<String, Object> result) {


        EnumAPIType enumApiType = EnumAPIType.getAPIType(url);


        switch (enumApiType) {

            case GetLog:

                Map<String, String> resultBody = (Map<String, String>) result.get("data");

                SystemErrorType systemErrorType = SystemErrorType.autoInferErrorType(resultBody.get("log"));

                result.put("errorNotify", systemErrorType.getNotifyMsg());

                return result;

            default:

                return result;
        }

    }

    public static String generateToken(long timestamp, String appKey) {
        return timestamp + Hashing.md5().hashString(appKey + timestamp, StandardCharsets.UTF_8).toString();
    }

}

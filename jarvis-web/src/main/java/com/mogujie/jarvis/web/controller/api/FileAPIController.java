package com.mogujie.jarvis.web.controller.api;

import com.mogu.bigdata.admin.core.entity.User;
import com.mogu.bigdata.admin.passport.user.UserContextHolder;
import com.mogujie.jarvis.core.exception.JarvisException;
import com.mogujie.jarvis.core.util.ExceptionUtil;
import com.mogujie.jarvis.web.utils.HdfsUtil;
import com.mogujie.jarvis.web.utils.MessageStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author muming
 */
@Controller
@RequestMapping(value = "/api/file")
public class FileAPIController{

    Logger logger = Logger.getLogger(FileAPIController.class);

    /**
     * 上传jar文件
     *
     * @return
     */
    @RequestMapping("/uploadJar")
    @ResponseBody
    public Object uploadJar(String title, HttpServletRequest request) {
        User user = UserContextHolder.getUser();
        String userName = (user != null) ? user.getUname() : "unknown";
        Map<String, Object> map = new HashMap<>();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile jarFile = multipartRequest.getFile("file");
            if (jarFile == null) {
                throw new JarvisException("上传文件不能为空");
            }
            String fileName =  ((CommonsMultipartFile) jarFile).getFileItem().getName();
            if (title != null && !title.trim().isEmpty()) {
                fileName = title + ".jar";
            }
            String url = HdfsUtil.uploadFile2Hdfs(jarFile, fileName, userName, false);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", url);
        } catch (Exception e) {
            if (!(e instanceof JarvisException)) {
                logger.error("上传jar文件出错", e);
            }
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", ExceptionUtil.getErrMsg(e));
        }
        return map;
    }

    /**
     * jar文件改名
     *
     * @return
     */
    @RequestMapping("/renameJar")
    @ResponseBody
    public Object renameJar(String newTitle, String curUrl, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        try {
            if (newTitle == null || newTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("标题不能为空");
            }
            if (curUrl == null || curUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("文件url不能为空");
            }
            String url = HdfsUtil.renameFile4Hdfs(curUrl, newTitle, true);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", url);
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                logger.error("jar文件改名", e);
            }
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", ExceptionUtil.getErrMsg(e));
        }
        return map;
    }

}

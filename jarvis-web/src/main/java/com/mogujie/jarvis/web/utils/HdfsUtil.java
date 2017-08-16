package com.mogujie.jarvis.web.utils;

import org.apache.hadoop.fs.FileStatus;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.mogujie.bigdata.base.IOUtils;
import com.mogujie.bigdata.base.PropUtils;

/**
 * Created by muming on 16/2/23.
 */
public class HdfsUtil {

    static final Logger logger = Logger.getLogger(HdfsUtil.class);

    public static String uploadFile2Hdfs(MultipartFile file, String fileName, String userName, boolean isDebugLocal) throws IOException {
        FileSystem fs = null;
        try {

            File tmpDirectory = null;
            File localFile = null;

            //拷贝到本地临时目录
            String localTmpDirectory = getLocalJarDir();
            tmpDirectory = new File(localTmpDirectory);
            if (!tmpDirectory.exists()) {
                tmpDirectory.mkdirs();
            }
            localFile = new File(tmpDirectory, fileName);
            localFile.delete();
            file.transferTo(localFile);
            if (isDebugLocal) {
                return localTmpDirectory + fileName;
            }

            //上传到HDFS
            fs = initHadoopFileSystem();

            String distString = getHdfsJarDir(userName);
            Path distPath = new Path(distString);
            logger.info("distPath:" + distString);
            if (!fs.exists(distPath)) {
                logger.info("distPath not exist. path:" + distString);
                fs.mkdirs(distPath);
            }
            fs.copyFromLocalFile(new Path(localFile.getAbsolutePath()), distPath);

            // 4. 删除本地临时文件
            localFile.delete();
            return distString + fileName;
        }catch(Exception ex){
            throw  ex;
        } finally {
            if(fs != null){
                IOUtils.closeQuietly(fs);
            }
        }
    }

    /**
     * HDFS文件改名
     */
    public static String renameFile4Hdfs(String curUrl, String newTitle, boolean isDebugLocal) throws IOException {
        FileSystem fs = null;
        try {
            String dirPrefix = curUrl.substring(0, curUrl.lastIndexOf('/'));
            String newUrl = dirPrefix + newTitle + ".jar";

            if (isDebugLocal) {
                return newUrl;
            }

            //上传到HDFS
            fs = initHadoopFileSystem();
            fs.rename(new Path(curUrl), new Path(newUrl));
            return newUrl;
        } finally {
            IOUtils.closeQuietly(fs);
        }
    }

    private static FileSystem initHadoopFileSystem() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs;
        //设置Hadoop用户
        System.setProperty("HADOOP_USER_NAME", PropUtils.getProp("config.properties", "hdfs.super.account", false));
        conf.addResource(HdfsUtil.class.getResourceAsStream("/hdfs-site.xml"));
//        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        fs = FileSystem.get(conf);
        return fs;
    }

    private static String getHdfsJarDir(String userName) {
        String uploadDir = PropUtils.getProp("config.properties", "upload.hdfs.dir", false);
        return (uploadDir.endsWith("/") ? uploadDir : uploadDir + "/") + userName + "/";
    }

    private static String getLocalJarDir() {
        return PropUtils.getProp("config.properties", "local.temp.jar.dir", "/tmp/jar", false);
    }

    public static List<String> getAllFilePath(Path filePath, FileSystem fs) throws IOException {
        List<String> fileList = new ArrayList<String>();
        FileStatus[] fileStatus = fs.listStatus(filePath);
        for (FileStatus fileStat : fileStatus) {
            if (fileStat.isDirectory()) {
                fileList.addAll(getAllFilePath(fileStat.getPath(), fs));
            } else {
                fileList.add(fileStat.getPath().toString());
            }
        }

        for (String a : fileList) {
            logger.info("dir:" + a);
        }
        return fileList;
    }

}


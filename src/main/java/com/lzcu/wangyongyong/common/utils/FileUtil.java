package com.lzcu.wangyongyong.common.utils;
import com.lzcu.wangyongyong.common.enums.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Random;

@Slf4j
public class FileUtil implements Serializable {

    /**
     * @param fileUrl 资源地址
     * @Description: 网络资源转file, 用完以后必须删除该临时文件
     * @return: 返回值
     */
    public static File urlToFile(String fileUrl) {
        String path = System.getProperty("user.dir");
        File tmpFile = new File(path, "tmp");
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        return urlToFile(fileUrl, tmpFile);
    }
    public static String urlToFilePath(String fileUrl) {
        String path = System.getProperty("user.dir");
        File tmpFile = new File(path, "tmp");
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        File file = urlToFile(fileUrl, tmpFile);
        return file.getAbsolutePath();
    }
    /**
     * @param fileUrl 资源地址
     * @param tmpFile  临时文件
     * @Description: 网络资源转file, 用完以后必须删除该临时文件
     * @return: 返回值
     */
    public static File urlToFile(String fileUrl, File tmpFile) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
        FileOutputStream downloadFile = null;
        InputStream openStream = null;
        File savedFile = null;
        try {
            savedFile = new File(tmpFile.getAbsolutePath() + fileName);
            URL url = new URL(fileUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            openStream = connection.getInputStream();
            int index;
            byte[] bytes = new byte[1024];
            downloadFile = new FileOutputStream(savedFile);
            while ((index = openStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (openStream != null) {
                    openStream.close();
                }
                if (downloadFile != null) {
                    downloadFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        savedFile.getAbsolutePath();
        return savedFile;
    }
    /**
     * @param fileUrl 资源地址
     * @param tmpFile  临时文件
     * @Description: 网络资源转file, 用完以后必须删除该临时文件
     * @return: 返回值
     */
    public static String urlToFilePath(String fileUrl, File tmpFile) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
        FileOutputStream downloadFile = null;
        InputStream openStream = null;
        File savedFile = null;
        try {
            savedFile = new File(tmpFile.getAbsolutePath() + fileName);
            URL url = new URL(fileUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            openStream = connection.getInputStream();
            int index;
            byte[] bytes = new byte[1024];
            downloadFile = new FileOutputStream(savedFile);
            while ((index = openStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (openStream != null) {
                    openStream.close();
                }
                if (downloadFile != null) {
                    downloadFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        assert savedFile != null;
        return savedFile.getAbsolutePath();
    }

    public static String getNetUrlHttp(String path){
        //每次数据抓取前随机等待5秒至20秒
        try {
            Thread.sleep((new Random()).nextInt(Constant.MAX)%(Constant.MIN) + Constant.MIN);
        } catch (InterruptedException e) {
            log.error("随机睡眠时间出错，请检查！");
            throw new RuntimeException(e);
        }
        //对本地文件命名，path是http的完整路径，主要得到资源的名字
        String fileName = path.split("[?]")[1];

        //保存到本地的路径
        String filePath="D:\\code\\java\\ESG信息管理系统\\AirDataAnalysisPlatform\\images\\"+fileName+".png";

        File file = null;

        URL urlfile;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            //判断文件的父级目录是否存在，不存在则创建
            file = new File(filePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try{
                //创建文件
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
            //下载
            urlfile = new URL(path);
            inputStream = urlfile.openStream();
            outputStream = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead=inputStream.read(buffer,0,8192))!=-1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }
}
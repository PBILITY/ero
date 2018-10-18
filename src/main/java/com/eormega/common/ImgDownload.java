package com.eormega.common;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImgDownload {
    public static void httpGetImg(CloseableHttpClient client, List<String> imgs, String savePath, String zipPtah, String zipTitel) {
        HttpGet request = new HttpGet();
        try {
            List<File> fileList = new ArrayList<>();
            //创建文件夹
            File file = new File(savePath);
            if(!file.exists()){
                file.mkdir();
            }
            File zipDir = new File(zipPtah);
            if(!zipDir.exists()){
                zipDir.mkdir();
            }
            //创建解压文件
            File zip = new File(zipPtah+zipTitel);
            if(!zip.exists()){
                zip.createNewFile();
            }
            for (String imgUrl : imgs ) {
                String suffix = imgUrl.substring(imgUrl.lastIndexOf("/"));
                // 发送get请求
                request = new HttpGet(imgUrl);
                // 设置请求和传输超时时间
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(50000).setConnectTimeout(50000).build();
                //设置请求头
                request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
                request.setConfig(requestConfig);
                CloseableHttpResponse response = client.execute(request);
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    InputStream in = entity.getContent();
                    File img = new File(savePath+suffix);
                    FileUtils.copyInputStreamToFile(in, img);
                    System.out.println("下载图片成功:" + imgUrl);
                    fileList.add(img);
                }
            }
            ZipUtils.zipFile(zipPtah+zipTitel,fileList);
            System.out.println("删除文件");
            for (File f : fileList) {
                f.delete();
            }
            System.out.println("删除完毕");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            request.releaseConnection();
        }
    }
}

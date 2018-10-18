package com.xxl.job.executor.service.jobhandler;

import com.eormega.common.ImgDownload;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@JobHandler(value = "erobroadway")
public class Erobroadway extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ReturnT result = new ReturnT<String>("SUCCESS");
            //首页开始
            String url = "http://erobroadway.com/?tag=%E3%83%95%E3%83%AB%E3%82%AB%E3%83%A9%E3%83%BC";
            Erobroadway erobroadway = new Erobroadway();
            Document doc = Jsoup.connect(url).get();
            Integer total = Integer.valueOf(doc.select(".pagination span").text().split(" ")[3]);//总页数
            for (int i = total ; i >= 1 ; i--){
                String page = "http://erobroadway.com/?tag=%E3%83%95%E3%83%AB%E3%82%AB%E3%83%A9%E3%83%BC&paged="+i;
                Document docPage = Jsoup.connect(page).get();
                Elements links = docPage.select(".grid-box-img a");
                String webs = null;
                CloseableHttpClient client = HttpClients.createDefault();
                List<String> urlList = new ArrayList<>();
                for (Element link : links) {
                    webs = link.attr("href");
                    urlList.add(webs);
                }
                //首页结束
                //子页开始
                for (String tempUrl:urlList){
                    Document docc = Jsoup.connect(tempUrl).get();
                    System.out.println(docc.body());
                    Elements linkss = docc.select(".alignnone");

                    List<String> tempUrlList = new ArrayList<>();
                    for (Element e : linkss) {
                        webs = e.attr("src");
                        tempUrlList.add(webs);
                    }
                    String fileOldName = docc.select(".entry-title").text();
                    String zipTitel = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                    ImgDownload.httpGetImg(client,tempUrlList,"D:\\ero","D:\\erozip\\",zipTitel+".zip");
                }
            }
        return result;
    }
}

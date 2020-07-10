package tg.bot.rssgo.controller;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import tg.bot.rssgo.util.html2md.HTML2Md;

import java.net.URL;
import java.util.Date;

/**
 * @author: HIBO
 * @date: 2020-07-06 16:31
 * @description:
 */
public class FeedReader {

    FeedSender feedSender = new FeedSender();

    public void read(int waitTime, Date botStartDate){
        String url = "https://rsshub.app/solidot/www";

        Long beginWaitTime = System.nanoTime()-300000000000l;
        Long endWaitTime;
        boolean isFirstRun = true;
        Date lastPostDate = new Date();

        while (true){

            endWaitTime = System.nanoTime();

            // 刷新间隔时间到
            if ((endWaitTime - beginWaitTime) / 1000000000.0 >= waitTime){
                beginWaitTime = endWaitTime;
                System.out.println("时间到");
                try (XmlReader reader = new XmlReader(new URL(url))){
                    SyndFeed feed = new SyndFeedInput().build(reader);
                    if (isFirstRun){
                        lastPostDate = feed.getPublishedDate();
                        //lastPostDate = new Date("Mon, 06 Jul 2020 13:19:00 GMT");
                        isFirstRun = false;
                        if (lastPostDate.before(botStartDate)) {
                            // 服务运行前的内容不再推送
                            System.out.println("过去的就让它过去吧");
                            feedSender.sendMessage("过去的就让它过去吧");
                        }
                    }

                    if (lastPostDate.before(feed.getPublishedDate())){
                        System.out.println("****************开始*****************");
                        for (SyndEntry entry: feed.getEntries()){
                            if (entry.getPublishedDate().before(lastPostDate)){
                                System.out.println(entry.getPublishedDate().toString()+"\nand lastPostDate:\n"+lastPostDate.toString());
                                break;
                            }

                            System.out.println("***********************************");
                            String s= (entry.getDescription().getValue());
                            //System.out.println(s);
                            String parsedText = HTML2Md.convert(s, "UTF-8");
                            if (feed.getTitle().equals("奇客的资讯，重要的东西")) {
                                System.out.println("#Solidot" + " \n " + "*【" + entry.getTitle() + "】*" + " \n\n " + parsedText.replace("![](https://img.solidot.org//0/446/liiLIZF8Uh6yM.jpg)","") + "\n" + "[原文]("+entry.getLink()+")");
                                feedSender.sendMessage("#Solidot" + " \n " + "*【" + entry.getTitle() + "】*" + " \n\n " + parsedText.replace("![](https://img.solidot.org//0/446/liiLIZF8Uh6yM.jpg)","") + "\n" + "[原文]("+entry.getLink()+")");
                            }else {
                                System.out.println("#"+ feed.getTitle() + " \n " + "*【" + entry.getTitle() + "】*" + " \n\n " + parsedText + " \n\n " + "[原文]("+entry.getLink()+")");
                                feedSender.sendMessage("#"+ feed.getTitle() + " \n " + "*【" + entry.getTitle() + "】*" + " \n\n " + parsedText + " \n\n " + "[原文]("+entry.getLink()+")");
                            }
                        }
                        System.out.println("****************结束*****************");
                        lastPostDate = feed.getPublishedDate();
                        System.out.println(lastPostDate.toString());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}

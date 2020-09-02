package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tg.bot.rssgo.util.html2md.HTML2Md;

import java.time.LocalDateTime;

/**
 * @author: HIBO
 * @date: 2020-07-10 13:51
 * @description:
 */

@Setter
@Getter
@AllArgsConstructor
public class ItemPostVO {
    // RSS 源的标题
    private String sourceTitle;
    // 单条更新的链接
    private String contentLink;
    // 单条更新的标题
    private String contentTitle;
    // 单条更新的内容
    private String contentDescription;
    // 单条更新的发布时间
    private LocalDateTime itemPublishedTime;
    // RSS最近更新时间
    private LocalDateTime sourcePublishedTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //System.out.println(s);
        String parsedText = HTML2Md.convert(contentDescription, "UTF-8");
        if (sourceTitle.equals("奇客的资讯，重要的东西")) {
            sb.append("#Solidot" + " \n " + "*【" + contentTitle + "】*" + " \n\n " + parsedText.replace("![](https://img.solidot.org//0/446/liiLIZF8Uh6yM.jpg)","") + "\n" + "[原文]("+contentLink+")");
        }else {
            //sb.append("#"+ sourceTitle + " \n " + "*【" + contentTitle + "】*" + " \n\n " + parsedText + " \n\n " + "[原文]("+contentLink+")");
            sb.append("#"+ sourceTitle + " \n\n" + "[原文]("+contentLink+")");
        }

        //System.out.println("#"+ sourceTitle + " \n " + "*【" + contentTitle + "】*" + " \n\n " + parsedText + " \n\n " + "[原文]("+contentLink+")");
        return sb.toString();
    }
}

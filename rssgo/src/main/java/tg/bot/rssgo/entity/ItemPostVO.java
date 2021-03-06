package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tg.bot.rssgo.service.impl.RssHandleServiceImpl;
import tg.bot.rssgo.util.EmojiUtil;
import tg.bot.rssgo.util.WordCountUtil;
import tg.bot.rssgo.util.html2md.HTML2Md;

import java.time.LocalDateTime;

/**
 * @author HAIBO
 * @date 2020-07-10 13:51
 * @description
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

        String parsedText = HTML2Md.convert(contentDescription, "UTF-8");

        if (sourceTitle.equals("奇客Solidot–传递最新科技情报")) {
            sb.append("#Solidot" + " \n " + "*【" + contentTitle + "】*" + "\n\n " + parsedText.replace("![](https://img.solidot.org//0/446/liiLIZF8Uh6yM.jpg)","")  + "["+contentTitle+"]"+"("+contentLink+")");
        }else if (contentLink.startsWith("https://weibo.com") || contentLink.startsWith("http://weibo.com")||contentLink.startsWith("https://m.okjike.com") || contentLink.startsWith("http://m.okjike.com")){
            for (String s: EmojiUtil.emojiMap.keySet()) {
                if (parsedText.contains("["+s+"]")){
                    parsedText = parsedText.replace("["+s+"]", EmojiUtil.emojiMap.get(s));
                }
            }
            sb.append((parsedText  + " \n\n "+"#"+ sourceTitle + "  " + "[原文]("+contentLink+")"));
        }else{
            sb.append("#"+ sourceTitle + " \n " + "*【" + contentTitle + "】*" + "\n\n " + parsedText + " \n\n " + "["+contentTitle+"]"+"("+contentLink+")");
        }

        return sb.toString();
    }
}

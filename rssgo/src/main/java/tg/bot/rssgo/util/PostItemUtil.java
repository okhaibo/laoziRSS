package tg.bot.rssgo.util;

import cn.hutool.core.util.ReUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.MediaGroupPostVO;
import tg.bot.rssgo.entity.PhotoPostVO;
import tg.bot.rssgo.util.html2md.HTML2Md;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author HAIBO
 * @date 2021-11-07 1:08
 * @description telegram bot 消息生成工具
 */
public class PostItemUtil {

    // 用于匹配内容的正则表达式
    static String RE_IMG_PATTERN = "<img.*?(?:>|\\/>)";
    static String RE_IMG_SRC_LINK = "src=[\\'\\\"]?([^\\'\\\"]*)[\\'\\\"]";

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成纯文字消息
     */
    public static void createTextMessagesByList(List<String> chatIds, ItemPostVO post, Stack<SendMessage> textMessageStack) {
        for (String id : chatIds) {
            SendMessage msg = new SendMessage(id, post.toString().replace("* ", "\\* "));
            msg.enableMarkdown(true);
            msg.disableWebPagePreview();
            textMessageStack.push(msg);
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成单图消息
     */
    public static  void createPhotoMessagesByList(List<String> chatIds, ItemPostVO post, Stack<SendPhoto> photoMessageStack) {
        PhotoPostVO photo = parsePhotoPost(post);
        for (String id : chatIds) {
            SendPhoto msg = new SendPhoto();
            msg.setChatId(id);
            msg.setPhoto(new InputFile(photo.getLink()));
            msg.setCaption(photo.getCaption());
            msg.setParseMode(ParseMode.MARKDOWN);
            photoMessageStack.push(msg);
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成多图消息
     */
    public static  void createMediaGroupMessagesByList(List<String> chatIds, ItemPostVO post, Stack<SendMediaGroup> mediaGroupMessageStack) {

        MediaGroupPostVO mediaGroup = parseMediaPost(post);

        LinkedList<InputMedia> photos = new LinkedList<>();
        String lastLink = "";
        for (String link: mediaGroup.getLinks()) {
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
            inputMediaPhoto.setMedia(link);
            inputMediaPhoto.setParseMode(ParseMode.MARKDOWN);
            photos.add(inputMediaPhoto);
            lastLink = link;
        }
        photos.removeLast();
        InputMediaPhoto lastMedia = new InputMediaPhoto(lastLink);
        lastMedia.setCaption(mediaGroup.getCaption());
        lastMedia.setParseMode(ParseMode.MARKDOWN);
        photos.add(lastMedia);

        for (String id : chatIds) {
            SendMediaGroup myMediaGroup = new SendMediaGroup();
            myMediaGroup.setChatId(id);
            myMediaGroup.setMedias(photos);
            mediaGroupMessageStack.push(myMediaGroup);
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:34
     * @description 解析单图消息，获取其中的图片链接和去除图片后的文字消息
     */
    private static PhotoPostVO parsePhotoPost(ItemPostVO post) {

        String content = post.getContentDescription();
        List<String> imgPatterns = ReUtil.findAll(RE_IMG_PATTERN, content, 0, new LinkedList<>());
        List<String> srcPatterns =ReUtil.findAll(RE_IMG_SRC_LINK, imgPatterns.get(0), 0, new LinkedList<>());

        String link = srcPatterns.get(0).substring(5,srcPatterns.get(0).length()-1);
        String caption = content.replace(imgPatterns.get(0), "");
        String parsedText = HTML2Md.convert(caption, "UTF-8");

        StringBuilder sb = new StringBuilder();
        if (post.getContentLink().startsWith("https://weibo.com") || post.getContentLink().startsWith("http://weibo.com")||post.getContentLink().startsWith("https://m.okjike.com") || post.getContentLink().startsWith("http://m.okjike.com")){
            for (String s: EmojiUtil.emojiMap.keySet()) {
                if (parsedText.contains("["+s+"]")){
                    parsedText = parsedText.replace("["+s+"]", EmojiUtil.emojiMap.get(s));
                }
            }
            sb.append((parsedText  + " \n\n "+"#"+ post.getSourceTitle() + "  " + "[原文]("+post.getContentLink()+")"));
        }else {
            sb.append((parsedText + " \n\n " + "*【" + post.getContentTitle() + "】*" + "\n\n " + "#" + post.getSourceTitle() + "  " + "[原文](" + post.getContentLink() + ")"));
        }

        return new PhotoPostVO(link, sb.toString().replace("* ","\\* "));
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:34
     * @description 解析多图消息，获取其中的图片链接和去除图片后的文字消息
     */
    private static MediaGroupPostVO parseMediaPost(ItemPostVO post) {

        String content = post.getContentDescription();
        List<String> imgPatterns = ReUtil.findAll(RE_IMG_PATTERN, content, 0, new LinkedList<>());
        List<String> srcPatterns = new LinkedList<>();
        String caption = content;
        for (String imgPattern: imgPatterns) {
            srcPatterns.addAll(ReUtil.findAll(RE_IMG_SRC_LINK, imgPattern, 0, new LinkedList<>()));
            caption = caption.replace(imgPattern, "");
        }

        List<String> links = new LinkedList<>();
        for (String srcPattern: srcPatterns) {
            links.add(srcPattern.substring(5,srcPattern.length()-1));
        }

        String parsedText = HTML2Md.convert(caption, "UTF-8");

        StringBuilder sb = new StringBuilder();
        if (post.getContentLink().startsWith("https://weibo.com") || post.getContentLink().startsWith("http://weibo.com")||post.getContentLink().startsWith("https://m.okjike.com") || post.getContentLink().startsWith("http://m.okjike.com")){
            for (String s: EmojiUtil.emojiMap.keySet()) {
                if (parsedText.contains("["+s+"]")){
                    parsedText = parsedText.replace("["+s+"]", EmojiUtil.emojiMap.get(s));
                }
            }
            sb.append((parsedText  + " \n\n "+"#"+ post.getSourceTitle() + "  " + "[原文]("+post.getContentLink()+")"));
        }else{
            sb.append((parsedText + " \n\n " +  "*【" + post.getContentTitle() + "】*" + "\n\n "+"#"+ post.getSourceTitle() + "  "   + "[原文]("+post.getContentLink()+")"));
        }

        return new MediaGroupPostVO(links, sb.toString().replace("* ","\\* "));
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:35
     * @description 根据post中图片的数量，确定post的消息类型
     */
    public static String getPostType(ItemPostVO post) {
        if (post.getSourceTitle().equals("奇客Solidot–传递最新科技情报")) {
            return "SendMessage";
        }

        List<String> result = ReUtil.findAll(RE_IMG_PATTERN, post.getContentDescription(), 0, new LinkedList<>());
        if (result.size() == 0){
            return "SendMessage";
        }else if (result.size() == 1){
            return "SendPhoto";
        }else{
            return "SendMediaGroup";
        }
    }
}

package tg.bot.rssgo.service.impl;

import cn.hutool.core.util.ReUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.MediaGroupPostVO;
import tg.bot.rssgo.entity.PhotoPostVO;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.service.IContentsService;
import tg.bot.rssgo.service.ISourcesService;
import tg.bot.rssgo.service.ISubscribesService;
import tg.bot.rssgo.util.RssUtil;
import tg.bot.rssgo.util.html2md.HTML2Md;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HAIBO
 * @date 2020-09-30 19:12
 * @description 检测和获取RSS更新内容
 */
@Log4j2
@Component
public class RssHandleServiceImpl {
    @Value("${bot.errorcount}")
    private int ERRORCOUNT;

    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    ISourcesService sourcesService;
    @Autowired
    IContentsService contentsService;

    // 用于匹配内容的正则表达式
    String RE_IMG_PATTERN = "<img.*?(?:>|\\/>)";
    String RE_IMG_SRC_LINK = "src=[\\'\\\"]?([^\\'\\\"]*)[\\'\\\"]";

    // 不同类型的消息
    List<SendMessage> textMessageList = new LinkedList<>();
    List<SendPhoto> photoMessageList = new LinkedList<>();
    List<SendMediaGroup> mediaGroupMessageList =new LinkedList<>();

    /**
     * @author HAIBO
     * @date 2020-10-01 15:36
     * @description 更新订阅源，获取上次更新后的新内容，并生成对应类型的消息，添加到消息List中
     */
    public void updateAllMessagesForRss(){
        List<Sources> sourcesList = sourcesService.list();

        for (Sources source : sourcesList) {
            if (source.getErrorCount() <= ERRORCOUNT && checkConnection(source)) {
                // 获取所有订阅者的chatId，只给订阅了当前RSS源的用户推送更新
                List<Long> chatIds = subscribesService.getChatIDsBySourceId(source.getId());

                List<ItemPostVO> posts = getAllPostAfterLastUpdate(source);
                for (ItemPostVO post: posts) {
                    switch (getPostType(post)){
                        case "SendPhoto":
                            createPhotoMessagesByList(chatIds, post);
                            break;
                        case "SendMediaGroup":
                            createMediaGroupMessagesByList(chatIds, post);
                            break;
                        default:
                            createTextMessagesByList(chatIds, post);
                    }
                }
            }
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成纯文字消息
     */
    private void createTextMessagesByList(List<Long> chatIds, ItemPostVO post) {
        for (Long id : chatIds) {
            textMessageList.add(new SendMessage(id, post.toString()).enableMarkdown(true).disableWebPagePreview());
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成单图消息
     */
    private void createPhotoMessagesByList(List<Long> chatIds, ItemPostVO post) {
        PhotoPostVO photo = parsePhotoPost(post);
        for (Long id : chatIds) {
            photoMessageList.add(new SendPhoto().setChatId(id)
                                                .setPhoto(photo.getLink())
                                                .setCaption(photo.getCaption())
                                                .setParseMode(ParseMode.MARKDOWN));
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:33
     * @description 生成多图消息
     */
    private void createMediaGroupMessagesByList(List<Long> chatIds, ItemPostVO post) {
        MediaGroupPostVO mediaGroup = parseMediaPost(post);

        LinkedList<InputMedia> photos = new LinkedList<>();
        String lastLink = "";
        for (String link: mediaGroup.getLinks()) {
            photos.add(new InputMediaPhoto(link, "").setParseMode(ParseMode.MARKDOWN));
            lastLink = link;
        }
        photos.removeLast();
        photos.add(new InputMediaPhoto(lastLink, mediaGroup.getCaption()).setParseMode(ParseMode.MARKDOWN));

        for (Long id : chatIds) {
            SendMediaGroup myMediaGroup = new SendMediaGroup();
            myMediaGroup.setChatId(id).setMedia(photos);
            mediaGroupMessageList.add(myMediaGroup);
        }
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:34
     * @description 解析单图消息，获取其中的图片链接和去除图片后的文字消息
     */
    private PhotoPostVO parsePhotoPost(ItemPostVO post) {

        String content = post.getContentDescription();
        List<String> imgPatterns = ReUtil.findAll(RE_IMG_PATTERN, content, 0, new LinkedList<>());
        List<String> srcPatterns =ReUtil.findAll(RE_IMG_SRC_LINK, imgPatterns.get(0), 0, new LinkedList<>());

        String link = srcPatterns.get(0).substring(5,srcPatterns.get(0).length()-1);
        String caption = content.replace(imgPatterns.get(0), "");
        String parsedText = HTML2Md.convert(caption, "UTF-8");

        StringBuilder sb = new StringBuilder();
        if (post.getContentLink().startsWith("https://weibo.com") || post.getContentLink().startsWith("http://weibo.com")){
            sb.append((parsedText  + " \n\n "+"#"+ post.getSourceTitle() + "  " + "[原文]("+post.getContentLink()+")"));
        }else {
            sb.append((parsedText + " \n\n " + "*【" + post.getContentTitle() + "】*" + " \n\n " + "#" + post.getSourceTitle() + "  " + "[原文](" + post.getContentLink() + ")"));
        }

        return new PhotoPostVO(link, sb.toString());
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:34
     * @description 解析多图消息，获取其中的图片链接和去除图片后的文字消息
     */
    private MediaGroupPostVO parseMediaPost(ItemPostVO post) {

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
        if (post.getContentLink().startsWith("https://weibo.com") || post.getContentLink().startsWith("http://weibo.com")){
            sb.append((parsedText  + " \n\n "+"#"+ post.getSourceTitle() + "  " + "[原文]("+post.getContentLink()+")"));
        }else{
            sb.append((parsedText + " \n\n " +  "*【" + post.getContentTitle() + "】*" + " \n\n "+"#"+ post.getSourceTitle() + "  "   + "[原文]("+post.getContentLink()+")"));
        }

        return new MediaGroupPostVO(links, sb.toString());
    }

    /**
     * @author HAIBO
     * @date 2020-10-01 15:35
     * @description 根据post中图片的数量，确定post的消息类型
     */
    private String getPostType(ItemPostVO post) {
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


    private List<ItemPostVO> getAllPostAfterLastUpdate(Sources source){
        log.info("获取更新："+source.getLink());
        List<ItemPostVO> allPosts = RssUtil.getAllPost(source.getLink());
        List<ItemPostVO> result = new LinkedList<>();

        // 获取所有新发布的内容
        for (ItemPostVO item : allPosts) {
            if (item.getItemPublishedTime().isBefore(source.getLastUpdatetime())) {
                break;
            }
            result.add(item);
        }
        // 有更新内容，则刷新数据库中的是最近更新时间
        if(result.size()!= 0){
            sourcesService.updateLastUpdateTimeById(source.getId(), allPosts.get(0).getSourcePublishedTime());
        }
        return result;
    }

    private boolean checkConnection(Sources source) {
        String link = source.getLink();
        SyndFeed syndFeed = new SyndFeedImpl();
        try{
            URL feedSource = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            syndFeed = input.build(new XmlReader(feedSource));
            return true;
        }catch (Exception e){
            log.info("链接更新获取失败: "+link);
            sourcesService.updateErrorCountById(source.getId());
            if(source.getErrorCount() > ERRORCOUNT){

                log.info("错误次数达到上限，请检查订阅连接是否存在问题: "+link);
            }
            return false;
        }
    }


    public List<SendMessage> getTextMessageList() {
        return textMessageList;
    }

    public List<SendPhoto> getPhotoMessageList() {
        return photoMessageList;
    }

    public List<SendMediaGroup> getMediaGroupMessageList() {
        return mediaGroupMessageList;
    }

    public void clearMessageList(){
        textMessageList.clear();
        photoMessageList.clear();
        mediaGroupMessageList.clear();
    }
}

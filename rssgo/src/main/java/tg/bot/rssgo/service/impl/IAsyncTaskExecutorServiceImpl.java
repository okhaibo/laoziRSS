package tg.bot.rssgo.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.multipart.MultipartFormData;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.service.IAsyncTaskExecutorService;
import tg.bot.rssgo.service.ISourcesService;
import tg.bot.rssgo.service.ISubscribesService;
import tg.bot.rssgo.util.PostItemUtil;
import tg.bot.rssgo.util.RssUtil;
import tg.bot.rssgo.util.WordCountUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author HAIBO
 * @date 2021-11-06 23:54
 */
@Slf4j
@Service
public class IAsyncTaskExecutorServiceImpl implements IAsyncTaskExecutorService {

    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    ISourcesService sourcesService;
    @Autowired
    BotServiceImpl botService;

    /**
     * 执行异步任务
     * @param source
     */
    @Override
    @Async("asyncServiceExecutor")
    public void asyncUpdateMessage(Sources source, int errorCount, int maxWordCount) {
        // 不同类型的消息
        Stack<SendMessage> text = new Stack<>();
        Stack<SendPhoto> photo = new Stack<>();
        Stack<SendMediaGroup> mediaGroup = new Stack<>();

        if (source.getErrorCount() <= errorCount && checkConnection(source, errorCount)) {
            List<ItemPostVO> posts = getAllPostAfterLastUpdate(source);
            if (posts.size() == 0){
                return;
            }
            // 获取所有订阅者的chatId，只给订阅了当前RSS源的用户推送更新
            List<String> chatIds = subscribesService.getChatIDsBySourceId(source.getId());
            for (ItemPostVO post: posts) {
                if (WordCountUtil.count(post.getContentDescription()) > maxWordCount){
                    post.setContentDescription(post.getContentDescription().substring(0,maxWordCount)+"===内容长度超限，完整内容请看原文===");
                }
                switch (PostItemUtil.getPostType(post)) {
                    case "SendPhoto":
                        PostItemUtil.createPhotoMessagesByList(chatIds, post, photo);
                        break;
                    case "SendMediaGroup":
                        PostItemUtil.createMediaGroupMessagesByList(chatIds, post, mediaGroup);
                        break;
                    default:
                        PostItemUtil.createTextMessagesByList(chatIds, post, text);
                }
            }
        }

        sendMessage(text, photo, mediaGroup);
    }

    private void sendMessage(Stack<SendMessage> textMessageStack, Stack<SendPhoto> photoMessageStack, Stack<SendMediaGroup> mediaGroupMessageStack) {
        log.info("【开始发送消息】");
        while (!textMessageStack.empty()) {
            SendMessage msg = textMessageStack.pop();
            log.info("发送文字消息 ==> ");
            try {
                botService.execute(msg);
                //Thread.sleep(100);
            } catch (TelegramApiException e) {
                log.error(e.getMessage()+"原文为："+msg.getText(), e);
            }
        }
        while (!photoMessageStack.empty()) {
            SendPhoto msg = photoMessageStack.pop();
            log.info("发送单图消息 ==> ");
            try {
                botService.execute(msg);
                //Thread.sleep(100);
            } catch (TelegramApiException e) {
                try {
                    handleMediaError(msg.getChatId(), msg.getCaption());
                }catch (TelegramApiException ex) {
                    log.error(e.getMessage()+"原文为："+ msg.getPhoto() +msg.getCaption(),e);
                }
            }
        }
        while (!mediaGroupMessageStack.empty()) {
            SendMediaGroup msg = mediaGroupMessageStack.pop();
            log.info("发送多图消息 ==> ");
            try {
                botService.execute(msg);
                //Thread.sleep(100);
            } catch (TelegramApiException e) {
                int size = msg.getMedias().size();
                String caption  = msg.getMedias().get(size-1).getCaption();
                try {
                    handleMediaError(msg.getChatId(), caption);
                }catch (TelegramApiException ex) {
                    log.error(e.getMessage()+"原文为："+ msg.getMedias(),e);
                }
            }
        }
        // 所有消息执行完后，清空待发送消息列表
        log.info("【结束消息发送】");
    }

    private void handleMediaError(String chatId, String caption) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile("https://github.com/okhaibo/laoziRSS/raw/master/beauty.jpg"));
        sendPhoto.setCaption("【图片发送出现问题，请查看原文图片】\n\n"+caption);
        botService.execute(sendPhoto);
    }

    private List<ItemPostVO> getAllPostAfterLastUpdate(Sources source){
        log.info(" >>> 获取更新=="+source.getTitle()+"："+source.getLink());
        List<ItemPostVO> allPosts = RssUtil.getAllPost(source.getLink());
        List<ItemPostVO> result = new LinkedList<>();

        if (allPosts==null || allPosts.size()==0){
            return result;
        }
        // 获取所有新发布的内容
        for (ItemPostVO item : allPosts) {
            if (item.getItemPublishedTime().isBefore(source.getLastUpdatetime())) {
                break;
            }

            result.add(item);
        }
        // 有更新内容，则刷新数据库中的最近更新时间
        if(result.size()!= 0){
            log.info(" <<< 【有更新:】 == "+source.getTitle()+"有"+result.size()+"条更新内容");
            sourcesService.updateLastUpdateTimeById(source.getId(), allPosts.get(0).getSourcePublishedTime().plusSeconds(50));
        }else{
            log.info(" <<< "+source.getTitle()+"== 没有更新内容");
        }
        return result;
    }

    private boolean checkConnection(Sources source, int errorCount) {
        String link = source.getLink();
        SyndFeed syndFeed = new SyndFeedImpl();
        try{
            URL feedSource = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            syndFeed = input.build(new XmlReader(feedSource));
            return true;
        }catch (Exception e){
            log.info(" ERROR <<< 链接更新获取失败: "+link);
            sourcesService.updateErrorCountById(source.getId());
            if(source.getErrorCount() > errorCount){
                log.info("错误次数达到上限，请检查订阅连接是否存在问题: "+link);
            }
            return false;
        }
    }


}

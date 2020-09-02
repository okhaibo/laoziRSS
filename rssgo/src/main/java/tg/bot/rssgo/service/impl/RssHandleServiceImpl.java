package tg.bot.rssgo.service.impl;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.service.IContentsService;
import tg.bot.rssgo.service.ISourcesService;
import tg.bot.rssgo.service.ISubscribesService;
import tg.bot.rssgo.util.RssUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: HIBO
 * @date: 2020-07-10 21:17
 * @description: 检测和获取RSS更新内容
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


    public List<SendMessage> getAllMessagesForRss(){
        List<Sources> sourcesList = sourcesService.list();
        List<SendMessage> messageList = new ArrayList<>();

        for (Iterator<Sources> iter = sourcesList.iterator(); iter.hasNext();){
            Sources tempSource = iter.next();
            if (tempSource.getErrorCount() <= ERRORCOUNT && checkConnection(tempSource)){
                messageList.addAll(createMessagesByList(subscribesService.getChatIDsBySouceId(tempSource.getId()) ,
                        getAllPostAfterLastUpdate(tempSource)));
            }
        }

        return messageList;
    }


    private List<ItemPostVO> getAllPostAfterLastUpdate(Sources source){
        List<ItemPostVO> allPosts = RssUtil.getAllPost(source.getLink());
        List<ItemPostVO> result = new LinkedList<>();

        // 获取所有新发布的内容
        for (ItemPostVO item : allPosts) {
            if (item.getItemPublishedTime().isBefore(source.getLastUpdatetime())) {
                break;
            }
            result.add(item);
        }

        sourcesService.updateLastUpdateTimeById(source.getId(), allPosts.get(0).getSourcePublishedTime());
        return result;
    }

    private List<SendMessage> createMessagesByList(List<Long> chatIds, List<ItemPostVO> posts){
        List<SendMessage> messageList = new ArrayList<>();
        for (Iterator<Long> idIter = chatIds.iterator(); idIter.hasNext();){
            Long id = idIter.next();
            for (Iterator<ItemPostVO> postIter = posts.iterator(); postIter.hasNext();){
                messageList.add(new SendMessage(id, postIter.next().toString()));
            }
        }
        return messageList;
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
            log.info("网址: "+link + " 获取失败");
            sourcesService.updateErrorCountById(source.getId());
            return false;
        }
    }
}

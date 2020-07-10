package tg.bot.rssgo.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import tg.bot.rssgo.entity.ItemVO;
import tg.bot.rssgo.entity.Sources;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HIBO
 * @date: 2020-07-10 11:26
 * @description:
 */
@Slf4j
public class RssUtil {

    public static ItemVO getLastestPost(String link){
        SyndFeed feed = getSyndFeed(link);
        SyndEntry lastestEntry = feed.getEntries().get(0);
        return new ItemVO(lastestEntry.getLink(),lastestEntry.getTitle());
    }


    public static List<ItemVO> getAllNews(String link) {
        SyndFeed feed = getSyndFeed(link);
        return feed.getEntries().stream()
                .map(entry -> new ItemVO(entry.getLink(), entry.getTitle()))
                .collect(Collectors.toList());
    }


    private static SyndFeed getSyndFeed(String link){
        SyndFeed syndFeed = new SyndFeedImpl();
        try{
            URL feedSource = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            syndFeed = input.build(new XmlReader(feedSource));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return syndFeed;
    }

    public static Sources getSourceByLink(String link){
        SyndFeed feed = getSyndFeed(link);

        Sources source = new Sources();
        source.setTitle(feed.getTitle());
        source.setLink(feed.getLink());
        source.setErrorCount(0);
        source.setCreatedAt(LocalDateTime.now());
        source.setUpdatedAt(LocalDateTime.now());

        return source;
    }

}

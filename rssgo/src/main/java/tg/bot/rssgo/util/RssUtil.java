package tg.bot.rssgo.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.log4j.Log4j2;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.Sources;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HIBO
 * @date 2020-07-10 11:26
 * @description
 */
@Log4j2
public class RssUtil {
    public static final String LINK_PATTERN = "[a-zA-z]+://[^\\s]*";

    public static ItemPostVO getLatestPost(String link){
        SyndFeed feed = getSyndFeed(link);
        SyndEntry latestEntry = feed.getEntries().get(0);
        return new ItemPostVO(feed.getTitle(), latestEntry.getLink(), latestEntry.getTitle(), latestEntry.getDescription().getValue(),latestEntry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime(), feed.getPublishedDate().toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime());
    }


    public static List<ItemPostVO> getAllPost(String link) {
        SyndFeed feed = getSyndFeed(link);
        List<ItemPostVO> postVOList = null;
        try{
            postVOList = feed.getEntries().stream().limit(50)
                    .map(entry -> new ItemPostVO(feed.getTitle(), entry.getLink(), entry.getTitle(), entry.getDescription().getValue(), entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime(), feed.getPublishedDate().toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime()))
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage()+"网址: "+link + "获取更新失败",e);
        }
        return postVOList;
    }


    private static SyndFeed getSyndFeed(String link){
        SyndFeed syndFeed = new SyndFeedImpl();
        try{
            URL feedSource = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            syndFeed = input.build(new XmlReader(feedSource));
        }catch (Exception e){
            log.error(e.getMessage()+"网址: "+link + "解析失败", e);
        }
        return syndFeed;
    }

    public static Sources getSourceByLink(String link){
        SyndFeed feed = getSyndFeed(link);
        Date lastUpdateTime = feed.getEntries().get(0).getPublishedDate();

        Sources source = new Sources();
        source.setTitle(feed.getTitle());
        source.setLink(link);
        source.setErrorCount(0);
        source.setUserCount(0L);
        source.setLastUpdatetime(lastUpdateTime.toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime());
        source.setCreatedAt(LocalDateTime.now());
        source.setUpdatedAt(LocalDateTime.now());

        return source;
    }

}

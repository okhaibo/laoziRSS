package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.service.IAsyncTaskExecutorService;
import tg.bot.rssgo.service.ISourcesService;

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
    private int ERROR_COUNT;
    @Value("${bot.maxword}")
    private int MAX_WORD_COUNT;

    @Autowired
    ISourcesService sourcesService;
    @Autowired
    IAsyncTaskExecutorService asyncTaskExecutorService;

    /**
     * @author HAIBO
     * @date 2020-10-01 15:36
     * @description 更新订阅源，并推送消息
     */
    public void updateAllMessagesForRss(){
        List<Sources> sourcesList = sourcesService.list();

        for (Sources source : sourcesList) {
            asyncTaskExecutorService.asyncUpdateMessage(source,  ERROR_COUNT, MAX_WORD_COUNT);
        }
    }
}

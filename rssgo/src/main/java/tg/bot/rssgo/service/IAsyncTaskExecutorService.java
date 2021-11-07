package tg.bot.rssgo.service;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import tg.bot.rssgo.entity.ItemPostVO;
import tg.bot.rssgo.entity.Sources;

import java.util.List;
import java.util.Stack;

/**
 * @Description : 执行异步任务的接口
 */
public interface IAsyncTaskExecutorService {

        /**
         * 异步获取rss更新
         * @param source
         * @return
         */
        void asyncUpdateMessage(Sources source, int errorCount, int maxWordCount);
}

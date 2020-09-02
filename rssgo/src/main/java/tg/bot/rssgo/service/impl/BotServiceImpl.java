package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.entity.TgBot;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;

/**
 * @author: HIBO
 * @date: 2020-07-09 22:35
 * @description: bot 服务，实现注册bot，定时任务和发送消息
 */
@Log4j2
@Component
public class BotServiceImpl {
    @Autowired
    UpdateHandleServiceImpl updateHandleService;
    @Autowired
    RssHandleServiceImpl rssHandleService;

    @Autowired
    TgBot bot;

    public void excute(SendMessage msg){
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void excute(SendPhoto photo){
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void registerBot(){
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(bot);
            log.info("============== Telegram Bot 注册完成 ==============");
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Transactional
    @Scheduled(fixedDelayString = "${bot.delay}") // 1000 = 1s
    public void sendRssToUsers() {
        List<SendMessage> msgs = rssHandleService.getAllMessagesForRss();
        for(Iterator<SendMessage> iter = msgs.iterator(); iter.hasNext();){
            SendMessage sendMessage = iter.next();
            sendMessage.enableMarkdown(true).disableWebPagePreview();
            excute(sendMessage);
        }
    }
}

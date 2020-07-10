package tg.bot.rssgo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.entity.TgBot;

import javax.annotation.PostConstruct;

/**
 * @author: HIBO
 * @date: 2020-07-09 22:35
 * @description:
 */
@Slf4j
@Component
public class BotServiceImpl {
    @Autowired
    UpdateHandleServiceImpl updateHandleService;

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
}

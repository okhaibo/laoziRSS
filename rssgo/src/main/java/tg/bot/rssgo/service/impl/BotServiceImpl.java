package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.entity.TgBot;

import javax.annotation.PostConstruct;

/**
 * @author HIBO
 * @date 2020-07-09 22:35
 * @description bot 服务，实现注册bot，发送消息
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

    public void execute(SendMessage msg){
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void execute(SendPhoto photo){
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void execute(SendMediaGroup mediaGroup){
        try {
            bot.execute(mediaGroup);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void execute(SendVideo video){
        try {
            bot.execute(video);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void execute(SendDocument document){
        try {
            bot.execute(document);
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

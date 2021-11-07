package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bot.rssgo.entity.TgBot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    public void execute(SendMessage msg) throws TelegramApiException {
        bot.execute(msg);


    }

    public void execute(SendPhoto photo) throws TelegramApiException {
        bot.execute(photo);
    }

    public void execute(SendMediaGroup mediaGroup) throws TelegramApiException {
        bot.execute(mediaGroup);
    }

    public void execute(SendVideo video) throws TelegramApiException {
        bot.execute(video);
    }

    public void execute(SendDocument document) throws TelegramApiException {
        bot.execute(document);
    }

    public boolean isGroupAdmin(String groupChatId, String userId) {
        GetChatMember getAdmins = GetChatMember.builder().chatId(groupChatId).userId(Long.parseLong(userId)).build();
        ChatMember memberResult = null;
        try {
            memberResult = bot.execute(getAdmins);
        } catch (TelegramApiException e) {
            return false;
        }
        String adminStatus = memberResult.getStatus();
        return (adminStatus.equals("creator") || adminStatus.equals("administrator"));
    }
    
    @PostConstruct
    public void registerBot(){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            log.info("============== Telegram Bot 注册完成 ==============");
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }
}

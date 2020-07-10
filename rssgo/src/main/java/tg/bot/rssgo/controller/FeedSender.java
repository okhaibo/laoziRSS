package tg.bot.rssgo.controller;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import tg.bot.rssgo.entity.TgBot;

import java.io.File;

/**
 * @author: HIBO
 * @date: 2020-07-06 19:39
 * @description:
 */
public class FeedSender {
    TgBot bot;
    public FeedSender(){
        //代理
        HttpHost proxy = new HttpHost("127.0.0.1", Integer.parseInt("7890"));
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        DefaultBotOptions options = new DefaultBotOptions();
        options.setRequestConfig(config);

        // 初始化
        ApiContextInitializer.init();
        //实例化 bot
        /*TelegramBotsApi botsApi = new TelegramBotsApi();
        bot = new TgBot(options);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }*/
    }

    public void sendFile(){
        File file = new File("./1.jpg");

        SendPhoto photo = new SendPhoto().setChatId("528303388")
                                         .setPhoto(file)
                                         .setCaption("来了，老弟");
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text){
        SendMessage msg = new SendMessage() .setChatId("528303388")
                                            .setParseMode(ParseMode.MARKDOWN)
                                            .setText(text)
                                            .disableWebPagePreview();
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

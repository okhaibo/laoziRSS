package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.config.TimerConfig;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;

/**
 * @author HAIBO
 * @date 2020-09-29 16:48
 * @description 更新间隔时间设置，对应命令为 /timer 5
 */
@Log4j2
@Service
public class CommandTimerImpl implements ICommandService {

    @Override
    public SendMessage execute(TgUpdate update) {

        String text = update.getText();
        if (text.length() < 8) {
            return new SendMessage(update.getChatId(), "快告诉老子隔多久更新，单位是分钟，必须是整数");
        }else {
            try{
                TimerConfig.timerId = Integer.parseInt(text.substring(7));
            }catch (Exception e){
                log.error("定时器设置出错", e);
                return new SendMessage(update.getChatId(), "兄弟，讲点道理好不好，单位是分钟，必须是整数");
            }
        }

        String resMsg =  "知道了，老子每隔"+TimerConfig.timerId+"分钟会帮你看看有没有更新";

        return new SendMessage(update.getChatId(), resMsg).enableMarkdown(true)
                                                          .disableWebPagePreview();
    }

    @Override
    public boolean isNeeded(TgUpdate update) {
        String text = update.getText();
        if (text.length() < 6) {
            return false;
        }
        return text.startsWith("/timer");
    }
}

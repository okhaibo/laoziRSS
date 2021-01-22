package tg.bot.rssgo.service.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;

import java.util.List;

/**
 * @author HAIBO
 * @date 2020-07-09 18:09
 * @description
 */

@Service
@Data
@Log4j2
public class UpdateHandleServiceImpl {
    private final List<ICommandService> commandServices;

    @Autowired
    BotServiceImpl botService;


    private SendMessage resolve(TgUpdate tgUpdate){
        return commandServices.stream()
                                .filter(commandServices -> commandServices.isNeeded(tgUpdate))
                                .findFirst()
                                .map(commandServices -> commandServices.execute(tgUpdate))
                                .orElse(new SendMessage(tgUpdate.getChatId(), "什么狗屁指令，老子不听"));
    }

    public void handle(TgUpdate tgUpdate){
        try {
            botService.execute(resolve(tgUpdate));
        } catch (TelegramApiException e) {
            log.error(e.getMessage(),e);
        }
    }
}

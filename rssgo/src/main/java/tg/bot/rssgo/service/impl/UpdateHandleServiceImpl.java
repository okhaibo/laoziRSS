package tg.bot.rssgo.service.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;

import java.util.List;

/**
 * @author: HIBO
 * @date: 2020-07-09 18:09
 * @description:
 */

@Service
@Data
public class UpdateHandleServiceImpl {
    private final List<ICommandService> commandServices;

    @Autowired
    BotServiceImpl botService;


    private SendMessage resolve(TgUpdate tgUpdate){
        return commandServices.stream()
                                .filter(commandServices -> commandServices.isNeeded(tgUpdate))
                                .findFirst()
                                .map(commandServices -> commandServices.execute(tgUpdate))
                                .orElse(new SendMessage(tgUpdate.getChatId(), "老弟，来了"));
    }

    public void handle(TgUpdate tgUpdate){
        botService.excute(resolve(tgUpdate));
    }
}

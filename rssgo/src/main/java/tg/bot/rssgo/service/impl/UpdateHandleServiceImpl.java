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
        String text = tgUpdate.getText();
        String[] args = text.split(" ");
        String personalChatId = tgUpdate.getChatId();
        if (args.length >= 2 && args[1].startsWith("@") && (!botService.isGroupAdmin(args[1], personalChatId))){
            // 非管理员角色操作频道
            SendMessage msg = new SendMessage(personalChatId, "兄弟别搞事，你不是管理员." );
            msg.enableMarkdown(true);
            msg.disableWebPagePreview();
            return msg;
        }
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

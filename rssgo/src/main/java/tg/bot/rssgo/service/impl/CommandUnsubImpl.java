package tg.bot.rssgo.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;

/**
 * @author: HIBO
 * @date: 2020-07-09 17:03
 * @description:
 */
@Service
public class CommandUnsubImpl  implements ICommandService {
    @Override
    public SendMessage execute(TgUpdate tgUpdate) {
        return new SendMessage(tgUpdate.getChatId(), "退订成功");
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        return tgUpdate.getText().equals("/unsub");
    }
}

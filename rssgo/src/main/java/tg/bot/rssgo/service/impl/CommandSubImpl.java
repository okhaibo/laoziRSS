package tg.bot.rssgo.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;

/**
 * @author: HIBO
 * @date: 2020-07-09 17:02
 * @description:
 */
@Service
public class CommandSubImpl implements ICommandService {
    @Override
    public SendMessage execute(TgUpdate tgUpdate) {
        return new SendMessage(tgUpdate.getChatId(), "订阅成功");
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        return tgUpdate.getText().equals("/sub");
    }
}

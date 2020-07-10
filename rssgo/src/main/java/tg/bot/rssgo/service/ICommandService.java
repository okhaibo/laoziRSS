package tg.bot.rssgo.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.TgUpdate;

public interface ICommandService {
    SendMessage execute(TgUpdate tgUpdate);
    boolean isNeeded(TgUpdate tgUpdate);
}

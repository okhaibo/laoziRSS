package tg.bot.rssgo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.entity.Subscribes;
import tg.bot.rssgo.entity.TgBot;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;
import tg.bot.rssgo.service.ISourcesService;
import tg.bot.rssgo.service.ISubscribesService;
import tg.bot.rssgo.util.RssUtil;

import java.util.List;

/**
 * @author HIBO
 * @date 2020-07-09 17:04
 * @description
 */
@Service
public class CommandListImpl implements ICommandService {

    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    ISourcesService sourcesService;

    @Override
    public SendMessage execute(TgUpdate tgUpdate) {

        String text = tgUpdate.getText();
        String[] args = text.split(" ");
        String personalChatId = tgUpdate.getChatId();
        if (args.length == 2 && args[1].startsWith("@")){
            // 频道订阅
            tgUpdate.setChatId(args[1]);
        }

        List<Subscribes> subscribesList = subscribesService.list(Wrappers.<Subscribes>lambdaQuery().eq(Subscribes::getChatId, tgUpdate.getChatId()));
        StringBuilder subs = new StringBuilder();
        int i = 1;
        for (Subscribes subscribes : subscribesList) {
            Sources source = sourcesService.getById(subscribes.getSourceId());
            subs.append("[[" + i + "]] [" + source.getTitle() + "](" + source.getLink() + ")\n");
            i++;
        }
        SendMessage msg = new SendMessage(personalChatId, "老子的订阅列表如下：\n\n" + subs);
        msg.enableMarkdown(true);
        msg.disableWebPagePreview();
        return msg;
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        return tgUpdate.getText().startsWith("/list");
    }
}

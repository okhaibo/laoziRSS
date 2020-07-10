package tg.bot.rssgo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.entity.Subscribes;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.service.ICommandService;
import tg.bot.rssgo.service.ISourcesService;
import tg.bot.rssgo.service.ISubscribesService;

import java.util.Iterator;
import java.util.List;

/**
 * @author: HIBO
 * @date: 2020-07-09 17:04
 * @description:
 */
@Service
public class CommandListImpl implements ICommandService {

    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    ISourcesService sourcesService;

    @Override
    public SendMessage execute(TgUpdate tgUpdate) {

        List<Subscribes> subscribesList = subscribesService.list(Wrappers.<Subscribes>lambdaQuery().eq(Subscribes::getChatId, tgUpdate.getChatId()));
        StringBuilder subs = new StringBuilder();
        int i = 1;
        for (Iterator<Subscribes> iter = subscribesList.iterator(); iter.hasNext();){
            Sources source = sourcesService.getById(iter.next().getSourceId());
            subs.append("[[" +i+ "]] ["+ source.getTitle() +"]("+source.getLink()+")\n");
            i++;
        }
        return new SendMessage(tgUpdate.getChatId(), "老子的订阅列表如下：\n\n" + subs)
                .enableMarkdown(true)
                .disableWebPagePreview();
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        return tgUpdate.getText().equals("/list");
    }
}

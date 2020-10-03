package tg.bot.rssgo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import tg.bot.rssgo.util.RssUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HIBO
 * @date 2020-07-09 17:03
 * @description
 */
@Service
public class CommandUnsubImpl  implements ICommandService {

    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    ISourcesService sourcesService;

    @Override
    public SendMessage execute(TgUpdate tgUpdate) {

        String link = getLink(tgUpdate);
        if (link == null) {
            return new SendMessage(tgUpdate.getChatId(), "老子不知道退订哪个，重发");
        }

        Sources source = sourcesService.getOne(Wrappers.<Sources>lambdaQuery().eq(Sources::getLink, link));
        if (source == null){
            return new SendMessage(tgUpdate.getChatId(), "你都没订阅，老子要怎么退订");
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("chat_id",tgUpdate.getChatId());
        map.put("source_id",source.getId());

        Subscribes subscribe = subscribesService.getOne(new QueryWrapper<Subscribes>().allEq(map));
        if (subscribe == null){
            return new SendMessage(tgUpdate.getChatId(), "你都没订阅，老子要怎么退订");
        }
        subscribesService.removeById(subscribe.getId());

        if (source.getUserCount() == 1){
            sourcesService.removeById(source.getId());
        }else {
            sourcesService.delUserCountById(source.getId());
        }

        return new SendMessage(tgUpdate.getChatId(), "["+source.getTitle()+"]("+ source.getLink()+")"+" 老子帮你退订成功了")
                .enableMarkdown(true)
                .disableWebPagePreview();
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        String text = tgUpdate.getText();
        if (text.length() < 6) {
            return false;
        }
        return text.startsWith("/unsub");
    }

    private String getLink(TgUpdate update){
        String text = update.getText();
        if (text.matches(RssUtil.LINK_PATTERN)){
            return text;
        }else if (text.length() < 8){
            return null;
        }else {
            return text.substring(7);
        }
    }
}

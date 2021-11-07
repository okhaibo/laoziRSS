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

        String text = tgUpdate.getText();
        String personalChatId = tgUpdate.getChatId();
        String link;
        String[] args = text.split(" ");
        if (args.length<=1){
            return new SendMessage(personalChatId, "老子不知道退订哪个，重发");
        }else if (args.length==2){
            link = args[1].matches(RssUtil.LINK_PATTERN)? args[1]: null;
        }else if (args.length ==3 && args[1].startsWith("@")){
            // 频道订阅
            tgUpdate.setChatId(args[1]);
            link = args[2].matches(RssUtil.LINK_PATTERN)? args[2]: null;
        }else {
            link = text;
        }

        if (link == null) {
            return new SendMessage(personalChatId, "老子不知道退订哪个，重发");
        }

        Sources source = sourcesService.getOne(Wrappers.<Sources>lambdaQuery().eq(Sources::getLink, link));
        if (source == null){
            return new SendMessage(personalChatId, "你都没订阅，老子要怎么退订");
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("chat_id",tgUpdate.getChatId());
        map.put("source_id",source.getId());

        Subscribes subscribe = subscribesService.getOne(new QueryWrapper<Subscribes>().allEq(map));
        if (subscribe == null){
            return new SendMessage(personalChatId, "你都没订阅，老子要怎么退订");
        }
        subscribesService.removeById(subscribe.getId());

        if (source.getUserCount() <= 1){
            sourcesService.removeById(source.getId());
        }else {
            sourcesService.delUserCountById(source.getId());
        }
        SendMessage msg = new SendMessage(personalChatId, "["+source.getTitle()+"]("+ source.getLink()+")"+" 老子帮你退订成功了");
        msg.enableMarkdown(true);
        msg.disableWebPagePreview();
        return msg;
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        String text = tgUpdate.getText();
        if (text.length() < 6) {
            return false;
        }
        return text.startsWith("/unsub");
    }
}

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
import tg.bot.rssgo.service.IUsersService;
import tg.bot.rssgo.util.RssUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HIBO
 * @date 2020-07-09 17:02
 * @description
 */
@Service
public class CommandSubImpl implements ICommandService {

    @Autowired
    ISourcesService sourcesService;
    @Autowired
    ISubscribesService subscribesService;
    @Autowired
    IUsersService usersService;


    @Override
    public SendMessage execute(TgUpdate update) {

        String text = update.getText();
        String link;
        if (text.matches(RssUtil.LINK_PATTERN)){
            link = text;
        }else if (text.length() < 6) {
            return new SendMessage(update.getChatId(), "快告诉老子要订阅什么网址");
        }else {
            link = text.substring(5);
        }

        String resMsg;
        if (link.matches(RssUtil.LINK_PATTERN)){
            Sources source = sourcesService.getOne(Wrappers.<Sources>lambdaQuery().eq(Sources::getLink, link));
            if (source == null){
                source = RssUtil.getSourceByLink(link);
                sourcesService.save(source);
            }else {
                sourcesService.addUserCountById(source.getId());
            }


            Map<String,Object> map = new HashMap<>(2);
            map.put("chat_id",update.getChatId());
            map.put("source_id",source.getId());

            Subscribes subscribe = subscribesService.getOne(new QueryWrapper<Subscribes>().allEq(map));
            if (subscribe == null){

                subscribe = new Subscribes().setChatId(update.getChatId())
                                             .setSourceId(source.getId())
                                             .setEnableNotification(1)
                                             .setEnableTelegraph(0)
                                             .setEnableWebPagePreview(0)
                                             .setCreatedAt(LocalDateTime.now())
                                             .setUpdatedAt(LocalDateTime.now());

                subscribesService.save(subscribe);
            }

            resMsg = "["+source.getTitle()+"]("+ source.getLink()+")"+" 订阅成功，有新内容老子会告诉你的";
        }else {
            resMsg = "这是什么网址，老子不认识";
        }

        return new SendMessage(update.getChatId(), resMsg).enableMarkdown(true)
                                                          .disableWebPagePreview();
    }

    @Override
    public boolean isNeeded(TgUpdate update) {
        String text = update.getText();
        if (text.length() < 4) {
            return false;
        }
        return text.startsWith("/sub") || text.matches(RssUtil.LINK_PATTERN);
    }
}

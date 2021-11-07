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
 * @description /sub
 */
@Service
public class CommandSubImpl implements ICommandService {

    @Autowired
    ISourcesService sourcesService;
    @Autowired
    ISubscribesService subscribesService;


    @Override
    public SendMessage execute(TgUpdate update) {

        String text = update.getText();
        String personalChatId = update.getChatId();
        String link;
        String[] args = text.split(" ");
        if (args.length==1 && !args[0].matches(RssUtil.LINK_PATTERN)){
            return new SendMessage(update.getChatId(), "快告诉老子要订阅什么网址");
        }else if (args.length==2){
            link = args[1].matches(RssUtil.LINK_PATTERN)? args[1]: null;
        }else if (args.length ==3 && args[1].startsWith("@")){
            // 频道订阅
            update.setChatId(args[1]);
            link = args[2].matches(RssUtil.LINK_PATTERN)? args[2]: null;
        }else {
            link = text;
        }


        String resMsg;
        if (link.matches(RssUtil.LINK_PATTERN)){
            Sources source = sourcesService.getOne(Wrappers.<Sources>lambdaQuery().eq(Sources::getLink, link));
            if (source == null){
                source = RssUtil.getSourceByLink(link);
                sourcesService.save(source);
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
                sourcesService.addUserCountById(source.getId());
            }

            resMsg = "["+source.getTitle()+"]("+ source.getLink()+")"+" 订阅成功，有新内容老子会告诉你的";
        }else {
            resMsg = "这是什么网址，老子不认识";
        }
        SendMessage msg = new SendMessage(personalChatId, resMsg);
        msg.enableMarkdown(true);
        msg.disableWebPagePreview();
        return msg;
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

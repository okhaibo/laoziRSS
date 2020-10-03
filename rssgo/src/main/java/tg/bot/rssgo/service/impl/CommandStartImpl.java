package tg.bot.rssgo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.bot.rssgo.entity.TgUpdate;
import tg.bot.rssgo.entity.Users;
import tg.bot.rssgo.service.ICommandService;
import tg.bot.rssgo.service.IUsersService;

import java.time.LocalDateTime;

/**
 * @author HIBO
 * @date 2020-07-09 17:04
 * @description
 */
@Service
public class CommandStartImpl implements ICommandService {
    @Autowired
    IUsersService usersService;

    @Override
    public SendMessage execute(TgUpdate tgUpdate) {
        Users user = usersService.getOne(Wrappers.<Users>lambdaQuery().eq(Users::getChatId, tgUpdate.getChatId()));

        if (user == null){
            user = new Users();
            user.setChatId(tgUpdate.getChatId());
            user.setName(tgUpdate.getUserName());
            user.setState(1);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            usersService.save(user);
        }

        return new SendMessage(tgUpdate.getChatId(), user.getName() + "，你可算来了. \n老子在此等候多时");
    }

    @Override
    public boolean isNeeded(TgUpdate tgUpdate) {
        return tgUpdate.getText().equals("/start");
    }
}

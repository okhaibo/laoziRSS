package tg.bot.rssgo.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import tg.bot.rssgo.config.TimerConfig;


/**
 * @author HAIBO
 * @date 2020-09-29 15:16
 * @description 定时获取RSS更新和推送任务
 */
@Log4j2
@Component
public class ScheduledTaskImpl implements SchedulingConfigurer {
    @Autowired
    RssHandleServiceImpl rssHandleService;
    @Autowired
    BotServiceImpl botService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {
            // 定时任务要执行的内容
            log.info("【 === 开始获取RSS更新 === 】");
            rssHandleService.updateAllMessagesForRss();


            for (SendMessage msg : rssHandleService.getTextMessageList()) {
                botService.execute(msg);
            }
            for (SendPhoto msg : rssHandleService.getPhotoMessageList()) {
                botService.execute(msg);
            }
            for (SendMediaGroup msg : rssHandleService.getMediaGroupMessageList()) {
                botService.execute(msg);
            }
            // 所有消息执行完后，清空待发送消息列表
            rssHandleService.clearMessageList();

        }, triggerContext -> {
            // 定时任务触发，可修改定时任务的执行周期
            String cron = TimerConfig.getTimerCorn();
            // 将这个测试用的定时时间换掉
            //String cron = "0/25 * * * * ?";
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}

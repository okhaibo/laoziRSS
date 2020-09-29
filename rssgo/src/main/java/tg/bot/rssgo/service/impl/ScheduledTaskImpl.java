/*
package tg.bot.rssgo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

*/
/**
 * @author: HAIBO
 * @date: 2020-09-29 15:16
 * @description: 定时获取RSS更新和推送任务
 *//*

@Component
public class ScheduledTaskImpl implements SchedulingConfigurer {
    @Autowired
    RssHandleServiceImpl rssHandleService;
    @Autowired
    BotServiceImpl botService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // 定时任务要执行的内容
                System.out.println("【开始执行定时任务。。。】");
                List<SendMessage> msgs = rssHandleService.getAllMessagesForRss();
                for(Iterator<SendMessage> iter = msgs.iterator(); iter.hasNext();){
                    SendMessage sendMessage = iter.next();
                    sendMessage.enableMarkdown(true).disableWebPagePreview();
                    botService.excute(sendMessage);
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 定时任务触发，可修改定时任务的执行周期
                String cron = "0 0/5 * * * ?";
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }
}
*/

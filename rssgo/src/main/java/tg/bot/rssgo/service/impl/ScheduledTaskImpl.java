package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
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
            log.info("【开始定时任务】");
            rssHandleService.updateAllMessagesForRss();
        }, triggerContext -> {
            // 定时任务触发，可修改定时任务的执行周期
            String cron = TimerConfig.getTimerCorn();
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}

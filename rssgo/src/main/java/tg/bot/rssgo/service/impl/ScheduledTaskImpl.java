package tg.bot.rssgo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.rssgo.config.TimerConfig;

import java.util.Stack;


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
            log.info("【获取RSS更新】");
            rssHandleService.updateAllMessagesForRss();
            Stack<SendMessage> textMessageStack = rssHandleService.getTextMessageStack();
            Stack<SendPhoto> photoMessageStack = rssHandleService.getPhotoMessageStack();
            Stack<SendMediaGroup> mediaGroupMessageStack = rssHandleService.getMediaGroupMessageStack();

            //TODO 发送失败的消息，下次继续发送

            while (!textMessageStack.empty()) {
                SendMessage msg = textMessageStack.pop();
                log.info("发送文字消息 ==> ");
                try {
                    botService.execute(msg);
                    Thread.sleep(100);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    log.info(e.getMessage(),e);
                }
            }
            while (!photoMessageStack.empty()) {
                SendPhoto msg = photoMessageStack.pop();
                log.info("发送单图消息 ==> ");
                try {
                    botService.execute(msg);
                    Thread.sleep(100);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage(),e);
                } catch (InterruptedException e) {
                    log.info(e.getMessage(),e);
                }
            }
            while (!mediaGroupMessageStack.empty()) {
                SendMediaGroup msg = mediaGroupMessageStack.pop();
                log.info("发送多图消息 ==> ");
                try {
                    botService.execute(msg);
                    Thread.sleep(100);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage(),e);
                } catch (InterruptedException e) {
                    log.info(e.getMessage(),e);
                }
            }
            // 所有消息执行完后，清空待发送消息列表
            log.info("【本次任务结束，等待下次更新】");

        }, triggerContext -> {
            // 定时任务触发，可修改定时任务的执行周期
            String cron = TimerConfig.getTimerCorn();
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}

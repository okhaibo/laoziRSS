package tg.bot.rssgo.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

/**
 * @author: HAIBO
 * @date: 2020-09-29 16:53
 * @description: 定时更新订阅的设置
 */
@Log4j2
@Component
public class TimerConfig {
    public static int timerId;
    public static int DEFAULT_TIMER_ID=5;

    @Value("${bot.timer}")
    public void setProxyHost(int timer) {
        timerId = timer;
    }

    public static String getTimerCorn(){
        String str = "0 0/"+timerId+" * * * ?";
        if (CronSequenceGenerator.isValidExpression(str)) {
            return str;
        }else {
            log.warn("TimerConfig中cron表达式解析出现问题");
            return "0 0/"+DEFAULT_TIMER_ID+" * * * ?";
        }
    }
}

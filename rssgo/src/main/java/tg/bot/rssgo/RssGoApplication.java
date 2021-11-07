package tg.bot.rssgo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bot.rssgo.config.ProxyConfig;

@SpringBootApplication
@MapperScan("tg.bot.rssgo.mapper")
@EnableScheduling
public class RssGoApplication {

   public static void main(String[] args) {

       SpringApplication.run(RssGoApplication.class, args);
       ProxyConfig.setProxy();
   }
}

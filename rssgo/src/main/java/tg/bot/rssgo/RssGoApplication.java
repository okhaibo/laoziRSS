package tg.bot.rssgo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import tg.bot.rssgo.config.ProxyConfig;

@SpringBootApplication
@MapperScan("tg.bot.rssgo.mapper")
@EnableScheduling
public class RssGoApplication {

   public static void main(String[] args) {
       ApiContextInitializer.init();
       SpringApplication.run(RssGoApplication.class, args);
       ProxyConfig.setProxy();
   }
}

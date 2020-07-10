package tg.bot.rssgo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@MapperScan("tg.bot.rssgo.mapper")
public class RssGoApplication {

   public static void main(String[] args) {
       ApiContextInitializer.init();
       SpringApplication.run(RssGoApplication.class, args);
   }
}

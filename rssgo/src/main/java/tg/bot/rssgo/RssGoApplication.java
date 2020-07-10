package tg.bot.rssgo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@MapperScan("tg.bot.rssgo.mapper")
public class RssGoApplication {

   public static void main(String[] args) {
       System.setProperty("http.proxyHost", "127.0.0.1");
       System.setProperty("http.proxyPort", "7890");

       // 对https也开启代理
       System.setProperty("https.proxyHost", "127.0.0.1");
       System.setProperty("https.proxyPort", "7890");
       ApiContextInitializer.init();
       SpringApplication.run(RssGoApplication.class, args);
   }
}

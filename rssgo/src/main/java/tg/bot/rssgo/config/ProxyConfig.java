package tg.bot.rssgo.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * @author: HIBO
 * @date: 2020-07-09 10:41
 * @description:
 */
@Component
public class ProxyConfig {
    //变量为static类型，属于类变量，在赋值前就已加载，所以无法赋值。
    /*@Value("${proxy.host}")
    private static String proxyHost;*/

    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private String proxyPort;


    public void setProxy(){

        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);

        // 对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
    }

    public static DefaultBotOptions getProxyOptions(){
        //代理
        HttpHost proxy = new HttpHost("127.0.0.1", Integer.parseInt("7890"));
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        DefaultBotOptions options = new DefaultBotOptions();
        options.setRequestConfig(config);
        return options;
    }

}

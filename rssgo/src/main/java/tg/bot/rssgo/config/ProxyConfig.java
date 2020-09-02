package tg.bot.rssgo.config;

import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author: HIBO
 * @date: 2020-07-09 10:41
 * @description:
 */
@Log4j2
@Component
public class ProxyConfig {
    //变量为static类型，属于类变量，在赋值前就已加载，所以无法赋值。
    /*@Value("${proxy.host}")
    private static String proxyHost;*/


    public static String proxyHost;
    public static String proxyPort;

    @Value("${proxy.host}")
    public void setProxyHost(String host) {
        proxyHost = host;
    }

    @Value("${proxy.port}")
    public void setProxyPort(String port) {
        proxyPort = port;
    }


    public static void setProxy(){
        if (proxyHost == null || proxyPort == null){
            log.error(" proxy 配置为 null ");
        }
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);

        // 对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
    }

    public static DefaultBotOptions getProxyOptions(){
        //代理
        HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        DefaultBotOptions options = new DefaultBotOptions();
        options.setRequestConfig(config);
        return options;
    }
}

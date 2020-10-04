package tg.bot.rssgo.entity;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import tg.bot.rssgo.config.ProxyConfig;
import tg.bot.rssgo.service.impl.UpdateHandleServiceImpl;
import tg.bot.rssgo.util.html2md.HTML2Md;

/**
 * @author HIBO
 * @date 2020-07-09 14:38
 * @description telegram bot
 */

@EqualsAndHashCode(callSuper = true)
@Log4j2
@Data
@Component
@AllArgsConstructor
public class TgBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botUsername;

    @Autowired
    UpdateHandleServiceImpl updateHandleService;

    @Override
    public void onUpdateReceived(Update update) {

        TgUpdate tgUpdate = null;
        if (update.getMessage() == null && update.getCallbackQuery() == null){
            return;
        }
        if (update.hasMessage()){
            log.info("收到信息： " + update.getMessage().getText());
            tgUpdate = TgUpdate.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(update.getMessage().getText())
                    .userName(update.getMessage().getChat().getUserName())
                    .build();

            String s="* 本期推荐的作品：英剧《荣誉谋杀》、美剧《科米的规则》、美剧《冰血暴》第四季、韩剧《灵能教师安恩英》、国产剧《风犬少年的天空》、院线电影《姜子牙》、动画《来自深渊：深沉灵魂的黎明》、动画《龙与魔女 / BURN THE WITCH》\n* 献给交通工具的片单：《爱在黎明破晓前》、《所有明亮的地方》、《东方快车谋杀案》、《遗愿清单》、《落魄大厨》\n" +
                    "* 几则资讯：《风林火山》有望 2020 公映、《喜宝》定档、《魔方大厦》院线电影项目启动、石原里美与圈外男性结婚\n" +
                    "\n" +
                    "## \uD83D\uDC40 9 部作品推荐\n" +
                    "\n" +
                    "根据读者反馈，如果推荐语涉及剧透（对故事核心、暗线、伏笔、结局等关键要素的透露），我们会在对应作品的标题前增加\uD83D\uDD26 的 Emoji。\n" +
                    "\n" +
                    "如有其他建议也欢迎在评论区提出，感谢大家对「本周看什么」栏目的喜爱和支持。\n" +
                    "\n" +
                    "### \uD83D\uDD26 [本期主推 • 英剧] 荣誉谋杀\n" +
                    "\n" +
                    "关键词：剧情\n" +
                    "片长：45 分钟（单集）× 2 集 / [豆瓣链接](https://movie.douban.com/subject/34444326/?tag=%26%2328909%3B%26%2338376%3B&from=gaia)\n" +
                    "女性解放任重道远。 \n" +
                    "\n" +
                    " *【假期看什么 | 最近值得一看的 9 部作品，以及一张交通工具片单】* \n" +
                    "\n" +
                    " #少数派  [原文](https://sspai.com/post/63027)";
            SendPhoto msg = new SendPhoto().setChatId(tgUpdate.getChatId()).setPhoto("https://cdn.sspai.com//2020/10/03/e7ea0e99e053fbfe3933a5344d5f44fb.png").setCaption(s).setParseMode(ParseMode.MARKDOWN);
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
        if (update.hasCallbackQuery()){
            log.info("收到回调请求： " + update.getCallbackQuery().getMessage().getText());
            answerCallbackAsync(update.getCallbackQuery().getId());
            tgUpdate = TgUpdate.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .text(update.getCallbackQuery().getMessage().getText())
                    .userName(update.getCallbackQuery().getMessage().getChat().getUserName())
                    .data(update.getCallbackQuery().getData())
                    .isCallbackQuery(true)
                    .build();
        }

        updateHandleService.handle(tgUpdate);
    }

    private void answerCallbackAsync(String callbackQueryId){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setShowAlert(false);
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        sendApiMethodAsync(answerCallbackQuery, new SentCallback<Boolean>() {
            @Override
            public void onResult(BotApiMethod<Boolean> botApiMethod, Boolean aBoolean) {

            }

            @Override
            public void onError(BotApiMethod<Boolean> botApiMethod, TelegramApiRequestException e) {

            }

            @Override
            public void onException(BotApiMethod<Boolean> botApiMethod, Exception e) {

            }
        });
    }

    public TgBot()
    {
        super(ProxyConfig.getProxyOptions());
    }

}

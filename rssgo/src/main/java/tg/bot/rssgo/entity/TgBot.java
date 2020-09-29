package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import tg.bot.rssgo.config.ProxyConfig;
import tg.bot.rssgo.service.impl.UpdateHandleServiceImpl;

/**
 * @author: HIBO
 * @date: 2020-07-09 14:38
 * @description:
 */

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
        /*if (update.hasMessage()){
            log.info("收到信息： " + update.getMessage().getText());
            tgUpdate = TgUpdate.builder()
                                .chatId(update.getMessage().getChatId())
                                .text(update.getMessage().getText())
                                .userName(update.getMessage().getChat().getUserName())
                                .build();
            SendPhoto photo= new SendPhoto().setChatId(update.getMessage().getChatId())
                                            .setPhoto("https://gitee.com/okhaibo/picsRepo/raw/master/2.jpg")
                                            .setCaption("锄禾日当午 \n 汗滴禾下土 \n 谁知盘中餐\n 粒粒皆辛苦")
                                            .setParseMode(ParseMode.MARKDOWNV2);

            SendMediaGroup myMediaGroup = new SendMediaGroup();

            List<InputMedia> photos = new ArrayList<>();
            photos.add(new InputMediaPhoto("https://gitee.com/okhaibo/picsRepo/raw/master/2.png", ""));
            photos.add(new InputMediaPhoto("https://gitee.com/okhaibo/picsRepo/raw/master/9.jpg", ""));
            photos.add(new InputMediaPhoto("https://gitee.com/okhaibo/picsRepo/raw/master/15.jpg", " 锄禾日当午 \n 汗滴禾下土 \n 谁知盘中餐\n 粒粒皆辛苦").setParseMode(ParseMode.MARKDOWNV2));
            myMediaGroup.setChatId(update.getMessage().getChatId()).setMedia(photos);


            try {
                execute(myMediaGroup);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }*/

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

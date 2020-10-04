package tg.bot.rssgo.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author: HIBO
 * @date: 2020-07-09 17:10
 * @description: telegram update实体
 */

@Builder
@Data
public class TgUpdate {
    private String chatId;
    private String text;
    private String userName;
    private String data;
    private boolean isCallbackQuery;
}

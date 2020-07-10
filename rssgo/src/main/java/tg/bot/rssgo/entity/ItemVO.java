package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: HIBO
 * @date: 2020-07-10 13:51
 * @description:
 */

@Data
@AllArgsConstructor
public class ItemVO {
    private String link;
    private String title;
}

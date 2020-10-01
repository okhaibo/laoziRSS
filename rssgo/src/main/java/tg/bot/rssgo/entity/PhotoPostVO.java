package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HAIBO
 * @date 2020-07-10 13:51
 * @description SendPhoto 对应的实体类
 */

@Setter
@Getter
@AllArgsConstructor
public class PhotoPostVO {
    // 图片对应的链接
    private String link;
    // 图片对应的文字内容
    private String caption;
}

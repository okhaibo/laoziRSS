package tg.bot.rssgo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author HAIBO
 * @date 2020-09-30 18:57
 * @description SendMediaGroup 对应的实体类
 */

@Setter
@Getter
@AllArgsConstructor
public class MediaGroupPostVO {
    // 图片链接列表
    private List<String> links;
    // 文字内容
    private String caption;
}

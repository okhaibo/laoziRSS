package tg.bot.rssgo.service;

import tg.bot.rssgo.entity.Subscribes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
public interface ISubscribesService extends IService<Subscribes> {
    public List<Long> getChatIDsBySourceId(Integer sourceId);
}

package tg.bot.rssgo.service;

import tg.bot.rssgo.entity.Sources;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
public interface ISourcesService extends IService<Sources> {
    public void addUserCountById(Integer id);
    public void delUserCountById(Integer id);
    public void updateLastUpdateTimeById(Integer id, LocalDateTime time);
    public void updateErrorCountById(Integer id);
}

package tg.bot.rssgo.mapper;

import org.springframework.stereotype.Component;
import tg.bot.rssgo.entity.Subscribes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
@Component
public interface SubscribesMapper extends BaseMapper<Subscribes> {
    public List<Long> getChatIDsBySouceId(Integer souceId);
}

package tg.bot.rssgo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import tg.bot.rssgo.entity.Subscribes;
import tg.bot.rssgo.mapper.SubscribesMapper;
import tg.bot.rssgo.service.ISubscribesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
@Service
public class SubscribesServiceImpl extends ServiceImpl<SubscribesMapper, Subscribes> implements ISubscribesService {
    @Autowired
    SubscribesMapper subscribesMapper;

    @Override
    public List<String> getChatIDsBySourceId(Integer sourceId) {
        return subscribesMapper.getChatIDsBySourceId(sourceId);
    }
}

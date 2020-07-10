package tg.bot.rssgo.service.impl;

import tg.bot.rssgo.entity.Options;
import tg.bot.rssgo.mapper.OptionsMapper;
import tg.bot.rssgo.service.IOptionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
@Service
public class OptionsServiceImpl extends ServiceImpl<OptionsMapper, Options> implements IOptionsService {

}

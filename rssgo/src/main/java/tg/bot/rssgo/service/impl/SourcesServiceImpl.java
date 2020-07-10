package tg.bot.rssgo.service.impl;

import tg.bot.rssgo.entity.Sources;
import tg.bot.rssgo.mapper.SourcesMapper;
import tg.bot.rssgo.service.ISourcesService;
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
public class SourcesServiceImpl extends ServiceImpl<SourcesMapper, Sources> implements ISourcesService {

}
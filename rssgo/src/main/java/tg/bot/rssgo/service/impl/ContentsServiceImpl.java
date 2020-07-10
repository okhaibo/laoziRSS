package tg.bot.rssgo.service.impl;

import tg.bot.rssgo.entity.Contents;
import tg.bot.rssgo.mapper.ContentsMapper;
import tg.bot.rssgo.service.IContentsService;
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
public class ContentsServiceImpl extends ServiceImpl<ContentsMapper, Contents> implements IContentsService {

}

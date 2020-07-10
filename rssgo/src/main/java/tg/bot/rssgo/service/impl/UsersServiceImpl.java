package tg.bot.rssgo.service.impl;

import tg.bot.rssgo.entity.Users;
import tg.bot.rssgo.mapper.UsersMapper;
import tg.bot.rssgo.service.IUsersService;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}

package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.UserUnread;
import cn.chasers.wehappy.message.mapper.UserUnreadMapper;
import cn.chasers.wehappy.message.service.IUserUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户未读数表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class UserUnreadServiceImpl extends ServiceImpl<UserUnreadMapper, UserUnread> implements IUserUnreadService {

}

package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.UserGroupUnread;
import cn.chasers.wehappy.message.mapper.UserGroupUnreadMapper;
import cn.chasers.wehappy.message.service.IUserGroupUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户群聊未读数表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class UserGroupUnreadServiceImpl extends ServiceImpl<UserGroupUnreadMapper, UserGroupUnread> implements IUserGroupUnreadService {

}

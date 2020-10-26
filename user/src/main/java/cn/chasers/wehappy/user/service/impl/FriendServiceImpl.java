package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.user.entity.Friend;
import cn.chasers.wehappy.user.mapper.FriendMapper;
import cn.chasers.wehappy.user.service.IFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 好友信息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

}

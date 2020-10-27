package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.entity.Friend;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.mapper.FriendMapper;
import cn.chasers.wehappy.user.service.IFriendService;
import cn.chasers.wehappy.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

    private final FriendMapper friendMapper;
    private final IUserService userService;

    @Autowired
    public FriendServiceImpl(FriendMapper friendMapper, IUserService userService) {
        this.friendMapper = friendMapper;
        this.userService = userService;
    }

    @Override
    public boolean addFriend(Long fromId, Long toId) {
        User to = userService.getById(fromId);
        if (to == null) {
            Asserts.fail(MessageConstant.USER_NOT_EXIST);
        }

        Friend friend1 = new Friend();
        friend1.setUserId(toId);
        friend1.setFriendId(fromId);

        Friend friend2 = new Friend();
        friend2.setUserId(fromId);
        friend2.setFriendId(toId);

        return saveBatch(Arrays.asList(friend1, friend2));
    }

    @Override
    public boolean handleAddFriend(Long fromId, Long toId, Boolean agree) {
        // TODO
        return false;
    }

    @Override
    public boolean list(Long userId) {
        // TODO
        return false;
    }
}

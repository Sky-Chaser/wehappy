package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.entity.Friend;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.mapper.FriendMapper;
import cn.chasers.wehappy.user.mq.Producer;
import cn.chasers.wehappy.user.service.IFriendService;
import cn.chasers.wehappy.user.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private final Producer producer;
    private final SnowflakeConfig snowflakeConfig;

    @Autowired
    public FriendServiceImpl(FriendMapper friendMapper, IUserService userService, Producer producer, SnowflakeConfig snowflakeConfig) {
        this.friendMapper = friendMapper;
        this.userService = userService;
        this.producer = producer;
        this.snowflakeConfig = snowflakeConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFriend(Long fromId, Long toId) {
        User to = userService.getById(fromId);
        if (to == null) {
            Asserts.fail(MessageConstant.USER_NOT_EXIST);
        }

        Friend friend1 = new Friend();
        friend1.setFromId(toId);
        friend1.setToId(fromId);

        Friend friend2 = new Friend();
        friend2.setFromId(fromId);
        friend2.setToId(toId);

        if (!saveBatch(Arrays.asList(friend1, friend2))) {
            Asserts.fail(MessageConstant.ERROR_ADD_FRIEND);
        }

        Map<String, Object> map = Map.of("type", "addFriend", "userId", toId, "fromId", fromId, "dateTime", new Date(System.currentTimeMillis()));
        pushMessage(map, toId);

        return true;
    }

    @Override
    public boolean deleteFriend(Long fromId, Long toId) {
        return friendMapper.delete(
                new LambdaQueryWrapper<Friend>()
                        .allEq(Map.of(Friend::getFromId, fromId, Friend::getToId, toId))
                        .allEq(Map.of(Friend::getFromId, toId, Friend::getToId, fromId))) > 0;
    }

    @Override
    public boolean handleAddFriend(Long fromId, Long toId, Boolean agree) {
        if (agree) {
            if (!lambdaUpdate().
                    allEq(Map.of(Friend::getFromId, fromId, Friend::getToId, toId))
                    .or()
                    .allEq(Map.of(Friend::getFromId, toId, Friend::getToId, fromId))
                    .update()
            ) {
                return false;
            }

            Map<String, Object> map = Map.of("type", "handleAddFriend", "userId", fromId, "fromId", toId, "result", true, "dateTime", new Date(System.currentTimeMillis()));
            pushMessage(map, toId);

            return true;
        }

        if (friendMapper.delete(
                new LambdaQueryWrapper<Friend>()
                        .allEq(Map.of(Friend::getFromId, fromId, Friend::getToId, toId))
                        .allEq(Map.of(Friend::getFromId, toId, Friend::getToId, fromId))) == 0
        ) {
            return false;
        }

        Map<String, Object> map = Map.of("type", "handleAddFriend", "userId", fromId, "fromId", toId, "result", false, "dateTime", new Date(System.currentTimeMillis()));
        pushMessage(map, toId);
        return true;
    }

    @Override
    public List<Friend> list(Long userId) {
        return lambdaQuery().eq(Friend::getFromId, userId).list();
    }

    private void pushMessage(Map<String, Object> map, long toId) {
        ProtoMsg.PushMessage pushMessage =
                ProtoMsg.PushMessage.newBuilder()
                        .setContentType(ProtoMsg.ContentType.TEXT)
                        .setTime(System.currentTimeMillis())
                        .setContent(new JSONObject(map).toJSONString())
                        .build();

        ProtoMsg.Message message =
                ProtoMsg.Message.newBuilder()
                        .setMessageType(ProtoMsg.MessageType.PUSH_MESSAGE)
                        .setTo(toId)
                        .setPushMessage(pushMessage)
                        .setSequence(snowflakeConfig.snowflakeId())
                        .build();

        producer.sendMessage(message);
    }
}

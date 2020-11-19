package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.common.util.MessageUtil;
import cn.chasers.wehappy.message.entity.*;
import cn.chasers.wehappy.message.feign.IGroupService;
import cn.chasers.wehappy.message.mapper.MessageIndexMapper;
import cn.chasers.wehappy.message.mapper.MessageMapper;
import cn.chasers.wehappy.message.mq.Producer;
import cn.chasers.wehappy.message.service.*;
import cn.chasers.wehappy.message.util.MessageConvertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    private final IMessageIndexService messageIndexService;
    private final MessageIndexMapper messageIndexMapper;
    private final IRedisService redisService;
    private final IConversationService conversationService;
    private final IConversationUnreadService conversationUnreadService;
    private final Producer producer;
    private final IGroupService groupService;
    private final IMessageDeleteService messageDeleteService;

    @Value("${redis.onlineUsers.key}")
    private String onlineUsersKey;

    @Autowired
    public MessageServiceImpl(IMessageIndexService messageIndexService, MessageIndexMapper messageIndexMapper, IRedisService redisService, IConversationService conversationService, IConversationUnreadService conversationUnreadService, Producer producer, IGroupService groupService, IMessageDeleteService messageDeleteService) {
        this.messageIndexService = messageIndexService;
        this.messageIndexMapper = messageIndexMapper;
        this.redisService = redisService;
        this.conversationService = conversationService;
        this.conversationUnreadService = conversationUnreadService;
        this.producer = producer;
        this.groupService = groupService;
        this.messageDeleteService = messageDeleteService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProtoMsg.Message msg) {
        Message message = MessageConvertUtil.proto2db(msg);

        // 1. 保存消息信息
        save(message);

        Long from = msg.getMessageTypeValue() == ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE
                || msg.getMessageTypeValue() == ProtoMsg.MessageType.GROUP_MESSAGE_VALUE ? Long.parseLong(msg.getChatMessage().getFrom()) : null;

        Long to = Long.parseLong(msg.getTo());

        // 2. 保存消息索引
        MessageIndex index1 = messageIndexService.save(msg.getMessageTypeValue(), from, to, message.getId());
        MessageIndex index2 = messageIndexService.save(msg.getMessageTypeValue(), to, from, message.getId());
        // 3. 保存会话信息
        Conversation conversation1 = conversationService.saveOrUpdate(index1);
        Conversation conversation2 = conversationService.saveOrUpdate(index2);

        // 4. 更新消息未读数
        conversationUnreadService.increase(conversation1.getId(), 1);
        conversationUnreadService.increase(conversation2.getId(), 1);

        // 5. 推送消息
        // 私聊消息
        if (message.getType() == ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE) {
            if (redisService.sIsMember(onlineUsersKey, to)) {
                producer.sendPushMessage(msg);
            }

            return true;
        }

        List<Long> userIds = groupService.getUserIds(to).getData();
        userIds.forEach(userId -> {
            if (redisService.sIsMember(onlineUsersKey, userId)) {
                producer.sendPushMessage(MessageUtil.newMessage(msg.getId(), msg.getSequence(), msg.getTime(), userId.toString(), msg.getMessageType(), msg.getChatMessage()));
            }
        });

        return true;
    }

    @Override
    public List<Message> getUnreadMessagesByConversationId(Long conversationId) {
        Conversation conversation = conversationService.getById(conversationId);
        if (conversation == null) {
            return null;
        }

        ConversationUnread conversationUnread = conversationUnreadService.getByConversationId(conversationId);

        List<Long> deleteIndices = messageDeleteService.list(new LambdaQueryWrapper<MessageDelete>().allEq(Map.of(MessageDelete::getFrom, conversation.getFromId(), MessageDelete::getTo, conversation.getToId()))).stream().map(MessageDelete::getMessageIndexId).collect(Collectors.toList());

        List<MessageIndex> messageIndices = messageIndexMapper.selectList(new LambdaQueryWrapper<MessageIndex>()
                .allEq(Map.of(MessageIndex::getFrom, conversation.getFromId(), MessageIndex::getTo, conversation.getToId(), MessageIndex::getType, conversation.getType()))
                .gt(MessageIndex::getMessageId, conversationUnread.getLastReadMessageId())
                .notIn(MessageIndex::getId, deleteIndices)
        );

        return lambdaQuery().in(Message::getId, messageIndices.stream().map(MessageIndex::getMessageId).collect(Collectors.toList())).list();
    }

    @Override
    public IPage<Message> getMessagesByConversationId(Long conversationId, Long messageId, Long currentPage, Long size) {
        Conversation conversation = conversationService.getById(conversationId);
        if (conversation == null) {
            return null;
        }

        List<Long> deleteIndices = messageDeleteService.list(new LambdaQueryWrapper<MessageDelete>().allEq(Map.of(MessageDelete::getFrom, conversation.getFromId(), MessageDelete::getTo, conversation.getToId()))).stream().map(MessageDelete::getMessageIndexId).collect(Collectors.toList());

        LambdaQueryWrapper<MessageIndex> queryWrapper = new LambdaQueryWrapper<MessageIndex>()
                .allEq(Map.of(MessageIndex::getFrom, conversation.getFromId(), MessageIndex::getTo, conversation.getToId(), MessageIndex::getType, conversation.getType()))
                .lt(MessageIndex::getMessageId, messageId)
                .notIn(MessageIndex::getId, deleteIndices)
                .orderByDesc(MessageIndex::getMessageId);

        size = Math.min(10, Math.max(5, size));
        IPage<MessageIndex> messageIndices = messageIndexMapper.selectPage(
                new Page<>(currentPage, size),
                queryWrapper
        );

        List<Message> messages = lambdaQuery().in(Message::getId, messageIndices.getRecords().stream().map(MessageIndex::getMessageId).collect(Collectors.toList())).list();

        Page<Message> result = new Page<>(messageIndices.getCurrent(), messageIndices.getSize(), messageIndices.getTotal());
        result.setRecords(messages);
        return result;
    }

    @Override
    public IPage<Message> getMessagesByToId(Integer type, Long fromId, Long toId, Long messageId, Long currentPage, Long size) {
        List<Long> deleteIndices = messageDeleteService.list(new LambdaQueryWrapper<MessageDelete>().allEq(Map.of(MessageDelete::getFrom, fromId, MessageDelete::getTo, toId))).stream().map(MessageDelete::getMessageIndexId).collect(Collectors.toList());

        LambdaQueryWrapper<MessageIndex> queryWrapper = new LambdaQueryWrapper<MessageIndex>()
                .allEq(Map.of(MessageIndex::getFrom, fromId, MessageIndex::getTo, toId, MessageIndex::getType, type))
                .lt(MessageIndex::getMessageId, messageId)
                .notIn(MessageIndex::getId, deleteIndices)
                .orderByDesc(MessageIndex::getMessageId);


        size = Math.min(10, Math.max(5, size));
        IPage<MessageIndex> messageIndices = messageIndexMapper.selectPage(
                new Page<>(currentPage, size),
                queryWrapper
        );

        List<Message> messages = lambdaQuery().in(Message::getId, messageIndices.getRecords().stream().map(MessageIndex::getMessageId).collect(Collectors.toList())).list();

        Page<Message> result = new Page<>(messageIndices.getCurrent(), messageIndices.getSize(), messageIndices.getTotal());
        result.setRecords(messages);
        return result;
    }
}

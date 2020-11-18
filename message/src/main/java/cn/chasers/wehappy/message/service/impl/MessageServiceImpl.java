package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.MessageMapper;
import cn.chasers.wehappy.message.mq.Producer;
import cn.chasers.wehappy.message.service.IConversationService;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import cn.chasers.wehappy.message.service.IMessageService;
import cn.chasers.wehappy.message.util.MessageConvertUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final IRedisService redisService;
    private final IConversationService conversationService;
    private final IConversationUnreadService conversationUnreadService;
    private final Producer producer;

    @Value("${redis.onlineUsers.key}")
    private String onlineUsersKey;

    @Autowired
    public MessageServiceImpl(IMessageIndexService messageIndexService, IRedisService redisService, IConversationService conversationService, IConversationUnreadService conversationUnreadService, Producer producer) {
        this.messageIndexService = messageIndexService;
        this.redisService = redisService;
        this.conversationService = conversationService;
        this.conversationUnreadService = conversationUnreadService;
        this.producer = producer;
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
        MessageIndex index = messageIndexService.save(msg.getMessageTypeValue(), from, to, message.getId());
        // 3. 保存会话信息
        Conversation conversation = conversationService.saveOrUpdate(index);

        // 4. 更新消息未读数
        conversationUnreadService.increase(conversation.getId(), 1);

        // 5. 推送消息
        // 私聊消息
        if (message.getType() == ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE) {
            if (redisService.sIsMember(onlineUsersKey, to)) {
                producer.sendPushMessage(msg);
            }

            return true;
        }

        // TODO 获取群聊中所有用户的 Id，将消息推送给在线用户

        return true;
    }
}

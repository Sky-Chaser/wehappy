package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.MessageIndexMapper;
import cn.chasers.wehappy.message.service.IConversationService;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息索引表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class MessageIndexServiceImpl extends ServiceImpl<MessageIndexMapper, MessageIndex> implements IMessageIndexService {

    private final MessageIndexMapper messageIndexMapper;
    private final IConversationService conversationService;

    @Autowired
    public MessageIndexServiceImpl(MessageIndexMapper messageIndexMapper, IConversationService conversationService) {
        this.messageIndexMapper = messageIndexMapper;
        this.conversationService = conversationService;
    }

    @Override
    public MessageIndex save(Integer type, Long from, Long to, Long messageId) {
        MessageIndex messageIndex = new MessageIndex();
        messageIndex.setType(type);
        messageIndex.setFrom(from);
        messageIndex.setTo(to);
        messageIndex.setMessageId(messageId);
        save(messageIndex);
        return messageIndex;
    }
}

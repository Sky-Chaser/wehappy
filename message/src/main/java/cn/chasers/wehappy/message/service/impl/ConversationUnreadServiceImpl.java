package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.ConversationUnread;
import cn.chasers.wehappy.message.mapper.ConversationUnreadMapper;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会话未读数表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class ConversationUnreadServiceImpl extends ServiceImpl<ConversationUnreadMapper, ConversationUnread> implements IConversationUnreadService {

    private final ConversationUnreadMapper conversationUnreadMapper;

    @Autowired
    public ConversationUnreadServiceImpl(ConversationUnreadMapper conversationUnreadMapper) {
        this.conversationUnreadMapper = conversationUnreadMapper;
    }

    @Override
    public boolean increase(Long conversationId, int count) {
        ConversationUnread conversationUnread = new ConversationUnread();
        conversationUnread.setConversationId(conversationId);
        conversationUnread.setCount(count);
        return conversationUnreadMapper.increase(conversationUnread);
    }

    @Override
    public boolean update(Long conversationId, int count) {
        return false;
    }

    @Override
    public boolean updateByLastReadMessageId(Long conversationId, Long messageId) {
        return false;
    }

    @Override
    public ConversationUnread get(Long conversationId) {
        return getById(conversationId);
    }
}

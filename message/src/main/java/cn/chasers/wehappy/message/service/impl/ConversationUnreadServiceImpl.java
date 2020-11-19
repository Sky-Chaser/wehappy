package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.ConversationUnread;
import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.ConversationUnreadMapper;
import cn.chasers.wehappy.message.service.IConversationService;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import cn.chasers.wehappy.message.service.IUnreadService;
import cn.chasers.wehappy.common.util.RedLockUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    private final IConversationService conversationService;
    private final IMessageIndexService messageIndexService;
    private final IUnreadService unreadService;

    @Autowired
    public ConversationUnreadServiceImpl(ConversationUnreadMapper conversationUnreadMapper, IConversationService conversationService, IMessageIndexService messageIndexService, IUnreadService unreadService) {
        this.conversationUnreadMapper = conversationUnreadMapper;
        this.conversationService = conversationService;
        this.messageIndexService = messageIndexService;
        this.unreadService = unreadService;
    }

    @Override
    public boolean increase(Long conversationId, int count) {
        Conversation conversation = conversationService.getById(conversationId);
        if (conversation == null) {
            Asserts.fail("会话不存在!");
        }

        RedissonRedLock lock = null;
        try {
            lock = RedLockUtil.tryLock(500, 2000, "unreadCount:" + conversation.getFromId());
            if (lock == null) {
                return false;
            }

            ConversationUnread conversationUnread = new ConversationUnread();
            conversationUnread.setConversationId(conversationId);
            conversationUnread.setCount(count);
            conversationUnreadMapper.increase(conversationUnread);
            unreadService.increase(conversation.getFromId(), count);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

        return false;
    }

    @Override
    public boolean updateByLastReadMessageId(Long conversationId, Long messageId) {
        Conversation conversation = conversationService.getById(conversationId);
        if (conversation == null) {
            Asserts.fail("会话不存在!");
        }

        RedissonRedLock lock = null;
        try {
            lock = RedLockUtil.tryLock(500, 2000, "unreadCount:" + conversation.getFromId());
            if (lock == null) {
                return false;
            }

            int conversationUnreadCount = messageIndexService.lambdaQuery()
                    .allEq(Map.of(MessageIndex::getFrom, conversation.getFromId(), MessageIndex::getTo, conversation.getToId()))
                    .gt(MessageIndex::getMessageId, messageId)
                    .count();

            ConversationUnread conversationUnread = new ConversationUnread();
            conversationUnread.setCount(conversationUnreadCount);
            lambdaUpdate().eq(ConversationUnread::getConversationId, conversationId).update(conversationUnread);
            conversationUnread.setLastReadMessageId(messageId);
            lambdaUpdate().eq(ConversationUnread::getConversationId, conversationId).lt(ConversationUnread::getLastReadMessageId, messageId).update(conversationUnread);
            unreadService.increase(conversation.getFromId(), conversationUnreadCount);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

        return false;
    }

    @Override
    public ConversationUnread getByConversationId(Long conversationId) {
        return getById(conversationId);
    }
}

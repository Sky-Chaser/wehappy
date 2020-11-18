package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.ConversationMapper;
import cn.chasers.wehappy.message.service.IConversationService;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 最近会话表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements IConversationService {

    private final IConversationUnreadService conversationUnreadService;

    public ConversationServiceImpl(IConversationUnreadService conversationUnreadService) {
        this.conversationUnreadService = conversationUnreadService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Conversation saveOrUpdate(MessageIndex index) {
        Conversation conversation = lambdaQuery().allEq(Map.of(Conversation::getFromId, index.getFrom(), Conversation::getToId, index.getTo())).one();

        if (conversation == null) {
            conversation = new Conversation();
            conversation.setFromId(index.getFrom());
            conversation.setToId(index.getId());
        }

        conversation.setMessageId(index.getMessageId());

        return conversation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long id) {
        remove(id);
        return conversationUnreadService.update(id, 0);
    }
}

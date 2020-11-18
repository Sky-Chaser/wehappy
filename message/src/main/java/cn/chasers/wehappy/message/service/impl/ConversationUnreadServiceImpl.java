package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.ConversationUnread;
import cn.chasers.wehappy.message.mapper.ConversationUnreadMapper;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public boolean increase(Long conversationId, int count) {
        return false;
    }

    @Override
    public boolean decrease(Long conversationId, int count) {
        return false;
    }

    @Override
    public boolean update(Long conversationId, int count) {
        return false;
    }

    @Override
    public boolean get(Long conversationId) {
        return false;
    }
}

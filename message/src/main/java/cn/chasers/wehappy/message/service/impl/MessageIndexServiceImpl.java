package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.MessageIndexMapper;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<MessageIndex> queryByConversationId(Long id, Long messageId, Long currentPage, Long size) {
        return null;
    }
}

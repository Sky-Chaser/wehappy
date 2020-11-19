package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.MessageDelete;
import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.feign.IGroupService;
import cn.chasers.wehappy.message.mapper.MessageDeleteMapper;
import cn.chasers.wehappy.message.service.IMessageDeleteService;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bouncycastle.jcajce.provider.digest.MD2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 消息删除记录表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class MessageDeleteServiceImpl extends ServiceImpl<MessageDeleteMapper, MessageDelete> implements IMessageDeleteService {

    private final IMessageIndexService messageIndexService;
    private final IGroupService groupService;

    @Autowired
    public MessageDeleteServiceImpl(IMessageIndexService messageIndexService, IGroupService groupService) {
        this.messageIndexService = messageIndexService;
        this.groupService = groupService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(Long userId, Long id) {
        MessageIndex index = messageIndexService.getByUserIdAndMessageId(userId, id);
        if (index.getType() == 0) {
            // 不是自己发的消息
            if (!userId.equals(index.getFrom()) && !userId.equals(index.getTo())) {
                return true;
            }
        } else if (index.getType() == 1) {
            // 不是群成员
            if (groupService.getGroupUser(index.getTo(), userId) == null) {
                return true;
            }
        } else {
            // 不是推送给自己的消息
            if (!userId.equals(index.getTo())) {
                return true;
            }
        }

        if (lambdaQuery().allEq(Map.of(MessageDelete::getFrom, userId, MessageDelete::getMessageIndexId, index.getId())).count() > 0) {
            return true;
        }

        MessageDelete messageDelete = new MessageDelete();
        messageDelete.setType(index.getType());
        messageDelete.setFrom(userId);
        messageDelete.setTo(index.getTo());
        messageDelete.setMessageIndexId(index.getId());
        return save(messageDelete);
    }
}

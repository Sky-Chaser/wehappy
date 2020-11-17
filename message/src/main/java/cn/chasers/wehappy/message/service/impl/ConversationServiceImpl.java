package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.mapper.ConversationMapper;
import cn.chasers.wehappy.message.service.IConversationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.MessageDelete;
import cn.chasers.wehappy.message.mapper.MessageDeleteMapper;
import cn.chasers.wehappy.message.service.IMessageDeleteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}

package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.mapper.MessageMapper;
import cn.chasers.wehappy.message.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

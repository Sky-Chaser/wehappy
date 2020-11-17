package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.MessageIndex;
import cn.chasers.wehappy.message.mapper.MessageIndexMapper;
import cn.chasers.wehappy.message.service.IMessageIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

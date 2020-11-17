package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.UserMessageDelete;
import cn.chasers.wehappy.message.mapper.UserMessageDeleteMapper;
import cn.chasers.wehappy.message.service.IUserMessageDeleteService;
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
public class UserMessageDeleteServiceImpl extends ServiceImpl<UserMessageDeleteMapper, UserMessageDelete> implements IUserMessageDeleteService {

}

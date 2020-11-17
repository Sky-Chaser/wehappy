package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.GroupMessageIndex;
import cn.chasers.wehappy.message.mapper.GroupMessageIndexMapper;
import cn.chasers.wehappy.message.service.IGroupMessageIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊信息索引表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class GroupMessageIndexServiceImpl extends ServiceImpl<GroupMessageIndexMapper, GroupMessageIndex> implements IGroupMessageIndexService {

}

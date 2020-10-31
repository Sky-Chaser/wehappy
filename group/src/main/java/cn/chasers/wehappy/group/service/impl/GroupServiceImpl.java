package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊信息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

}

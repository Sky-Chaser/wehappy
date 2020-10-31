package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.mapper.GroupUserMapper;
import cn.chasers.wehappy.group.service.IGroupUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊用户表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements IGroupUserService {

}

package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.dto.GroupDto;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import cn.chasers.wehappy.group.util.UserUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
    private final HttpServletRequest request;
    private final IGroupUserService groupUserService;

    @Value("${default.avatar}")
    private String defaultAvatar;

    @Autowired
    public GroupServiceImpl(HttpServletRequest request, IGroupUserService groupUserService) {
        this.request = request;
        this.groupUserService = groupUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(String groupName) {
        Group group = new Group();
        group.setName(groupName);
        group.setAdminCount(1);
        group.setAvatar(defaultAvatar);
        group.setMemberCount(1);
        group.setOwnerId(UserUtil.getCurrentUserId(request));
        group.setStatus(0);

        if (!save(group)) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(group.getOwnerId());
        groupUser.setType(2);
        groupUser.setStatus(0);
        groupUser.setInvitedUserId(0L);

        if (!groupUserService.save(groupUser)) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        return true;
    }

    @Override
    public boolean remove(Long groupId) {
        return false;
    }

    @Override
    public boolean update(GroupDto groupDto) {
        return false;
    }

    @Override
    public boolean transfer(Long groupId, Long toId) {
        return false;
    }

    @Override
    public Group search(String groupName) {
        return null;
    }
}

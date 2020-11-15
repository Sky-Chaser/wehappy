package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.feign.IUserService;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.mapper.GroupUserMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import cn.chasers.wehappy.group.util.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群聊用户表 服务实现类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements IGroupUserService {
    private final GroupMapper groupMapper;
    private final IUserService userService;
    private final HttpServletRequest request;
    private final IGroupService groupService;

    @Autowired
    public GroupUserServiceImpl(GroupMapper groupMapper, IUserService userService, HttpServletRequest request, IGroupService groupService, GroupUserMapper groupUserMapper) {
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.request = request;
        this.groupService = groupService;
    }



    @Override
    public boolean invite(Long userId, Long groupId) {
        Long currentUserId = UserUtil.getCurrentUserId(request);
        // 校验当前用户是否为管理员/群主
        if (currentUserId != 2 && currentUserId != 3) {
            Asserts.fail(MessageConstant.IS_NOT_ADMIN);
        }

        Group group = groupMapper.selectById(groupId);

        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        CommonResult<UserDto> result = userService.query(userId);
        if (result.getData() == null) {
            Asserts.fail(MessageConstant.USER_NOT_EXIST);
        }

        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(groupId);
        groupUser.setUserId(userId);
        groupUser.setInvitedUserId(currentUserId);
        groupUser.setType(0);
        groupUser.setStatus(1);

        return save(groupUser);
    }

    @Override
    public boolean apply(Long groupId) {
        // 校验该群组是否已经存在
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, groupId));
        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        // 校验当前用户是否已经在群中
        Long currentUserId = UserUtil.getCurrentUserId(request);
        GroupUser groupUser = getOne(new LambdaQueryWrapper<GroupUser>()
                .allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, currentUserId)));
        if (groupUser != null) {
            Asserts.fail(MessageConstant.ALREADY_EXIT_IN_GROUP);
        }

        // 开始创建
        GroupUser groupUserInsert = new GroupUser();
        groupUserInsert.setInvitedUserId(0L);
        groupUserInsert.setStatus(2);
        groupUserInsert.setType(0);
        groupUserInsert.setUserId(currentUserId);
        groupUserInsert.setGroupId(groupId);

        return save(groupUserInsert);
    }

    @Override
    public boolean handleApply(Long id, Boolean agree) {
        return false;
    }

    @Override
    public boolean handleInvite(Long id, Boolean agree) {
        return false;
    }

    @Override
    public boolean addAdmin(Long userId, Long groupId) {
        return false;
    }

    @Override
    public boolean deleteAdmin(Long userId, Long groupId) {
        return false;
    }

    @Override
    public boolean deleteMember(Long userId, Long groupId) {
        return false;
    }

    @Override
    public boolean forbidden(Long userId, Long groupId) {
        return false;
    }

    @Override
    public List<Group> getList() {
        return null;
    }

    @Override
    public boolean exit(Long groupId) {
        return false;
    }
}

package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.feign.IUserService;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.mapper.GroupUserMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final IGroupService groupService;

    @Autowired
    public GroupUserServiceImpl(GroupMapper groupMapper, IUserService userService, IGroupService groupService, GroupUserMapper groupUserMapper) {
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public boolean invite(Long currentUserId, Long userId, Long groupId) {
        // 校验当前用户是否为管理员/群主
        if (!isAdmin(currentUserId, groupId)) {
            return false;
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
    public boolean apply(Long currentUserId, Long groupId) {
        // 校验该群组是否已经存在
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, groupId));
        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        // 校验当前用户是否已经在群中
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
    public boolean handleApply(Long currentUserId, Long groupId, Long groupUserId, Boolean agree) {
        // 验证是否存在对应id的group记录
        GroupUser one = getOne(new LambdaQueryWrapper<GroupUser>()
                .allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, groupUserId)));
        if (one == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        // 验证当前用户是否为群主/管理员
        if (!isAdmin(currentUserId, one.getId())) {
            return false;
        }

        // 根据agree进行申请处理
        if (agree) {
            return update(new LambdaUpdateWrapper<GroupUser>().eq(GroupUser::getUserId, groupUserId).set(GroupUser::getStatus, 0));
        } else {
            Asserts.fail(MessageConstant.ADMIN_REFUSE);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleInvite(Long currentUserId, Long groupId, Boolean agree) {
        GroupUser one = getOne(lambdaQuery().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, currentUserId, GroupUser::getStatus, 1)));
        if (one == null) {
            return false;
        }

        if (agree) {
            one.setStatus(0);
            return updateById(one);
        }

        Asserts.fail(MessageConstant.USER_REFUSE);
        return false;
    }

    @Override
    public boolean addAdmin(Long currentUserId, Long userId, Long groupId) {
        // 校验当前用户是否为群主
        Group one = groupService.getOne(new LambdaQueryWrapper<Group>().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)));
        if (one == null) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        return update(new LambdaUpdateWrapper<GroupUser>().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId)).set(GroupUser::getType, 1));
    }

    @Override
    public boolean deleteAdmin(Long currentUserId, Long userId, Long groupId) {
        // 校验当前用户是否为群主
        Group one = groupService.getOne(new LambdaQueryWrapper<Group>().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)));
        if (one == null) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        return update(new LambdaUpdateWrapper<GroupUser>().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId)).set(GroupUser::getType, 0));
    }

    @Override
    public boolean deleteMember(Long currentUserId, Long userId, Long groupId) {
        // 校验当前用户是否为群主、管理员
        if (!isAdmin(currentUserId, groupId)) {
            return false;
        }

        return remove(lambdaQuery().allEq(Map.of(GroupUser::getUserId, userId, GroupUser::getGroupId, groupId)));
    }

    @Override
    public List<Group> getList(Long currentUserId) {
        List<Long> groupIds = list(lambdaQuery().eq(GroupUser::getUserId, currentUserId)).stream().map(GroupUser::getId).collect(Collectors.toList());
        return groupService.listByIds(groupIds);
    }

    @Override
    public boolean exit(Long currentUserId, Long groupId) {
        // 校验当前用户是否在群中
        GroupUser one = getOne(lambdaQuery().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, currentUserId, GroupUser::getStatus, 0)));
        if (one == null) {
            return false;
        }
        return removeById(one.getId());
    }

    @Override
    public List<Long> getUserIds(Long id) {
        return lambdaQuery().eq(GroupUser::getGroupId, id).list().stream().map(GroupUser::getUserId).collect(Collectors.toList());
    }

    @Override
    public GroupUser getByGroupIdAndUserId(Long groupId, Long userId) {
        return lambdaQuery().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId)).one();
    }

    @Override
    public List<GroupUser> getAppliedUsers(Long groupId) {
        return list(new LambdaQueryWrapper<GroupUser>().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getStatus, 2)));
    }

    @Override
    public boolean isAdmin(Long userId, Long groupId) {
        return lambdaQuery()
                .allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId))
                .ge(GroupUser::getType, 2)
                .count() > 0;
    }
}

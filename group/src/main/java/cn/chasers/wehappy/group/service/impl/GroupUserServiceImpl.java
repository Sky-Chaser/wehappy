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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean invite(Long userId, Long groupId) {
        Long currentUserId = ThreadLocalUtils.get().getId();
        // 校验当前用户是否为管理员/群主
        if (currentUserId != MessageConstant.ADMIN_USER && currentUserId != MessageConstant.OWNER_USER) {
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
        Long currentUserId = ThreadLocalUtils.get().getId();
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
    public boolean handleApply(Long id, Long groupUserId, Boolean agree) {
        // 验证是否存在对应id的group记录
        Long currentUserId = ThreadLocalUtils.get().getId();
        Group one = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, id));
        if (one == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        // 验证当前用户是否为群主/管理员（先验证群主）
        if (!one.getOwnerId().equals(currentUserId)) {
            List<GroupUser> groupUsers = list(new LambdaQueryWrapper<GroupUser>()
                    .eq(GroupUser::getGroupId, id));
            for (GroupUser groupUser : groupUsers) {
                if (groupUser.getUserId().equals(currentUserId) && groupUser.getType() == 1) {
                    break;
                }
            }
            Asserts.fail(MessageConstant.IS_NOT_ADMIN);
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
    public boolean handleInvite(Long id, Boolean agree) {
        if (agree) {
            return update(new LambdaUpdateWrapper<GroupUser>().eq(GroupUser::getId, id).set(GroupUser::getStatus, 0));
        }
        Asserts.fail(MessageConstant.USER_REFUSE);
        return false;
    }

    @Override
    public boolean addAdmin(Long userId, Long groupId) {
        // 校验当前用户是否为群主
        Long currentUserId = ThreadLocalUtils.get().getId();
        Group one = groupService.getOne(new LambdaQueryWrapper<Group>().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)));
        if (one == null) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        return update(new LambdaUpdateWrapper<GroupUser>().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId)).set(GroupUser::getType, 1));
    }

    @Override
    public boolean deleteAdmin(Long userId, Long groupId) {
        // 校验当前用户是否为群主
        Long currentUserId = ThreadLocalUtils.get().getId();
        Group one = groupService.getOne(new LambdaQueryWrapper<Group>().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)));
        if (one == null) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        return update(new LambdaUpdateWrapper<GroupUser>().allEq(Map.of(GroupUser::getGroupId, groupId, GroupUser::getUserId, userId)).set(GroupUser::getType, 0));
    }

    @Override
    public boolean deleteMember(Long userId, Long groupId) {
        // 校验当前用户是否为群主、管理员
        Long currentUserId = ThreadLocalUtils.get().getId();
        // TODO  xxx

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
}

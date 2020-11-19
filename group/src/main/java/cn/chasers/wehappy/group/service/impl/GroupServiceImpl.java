package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.dto.UpdateGroupParams;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 群聊信息表 服务实现类
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-13
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {
    private final IGroupUserService groupUserService;
    private final GroupMapper groupMapper;

    @Value("${default.avatar}")
    private String defaultAvatar;

    @Autowired
    public GroupServiceImpl(IGroupUserService groupUserService, GroupMapper groupMapper) {
        this.groupUserService = groupUserService;
        this.groupMapper = groupMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(Long currentUserId, String name, String avatar) {
        Group group = new Group();
        group.setName(name);
        group.setAdminCount(1);
        group.setAvatar(avatar == null ? defaultAvatar : avatar);
        group.setMemberCount(1);
        group.setOwnerId(currentUserId);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long currentUserId, Long groupId) {
        Integer count = lambdaQuery().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)).count();
        if (count == 0) {
            return false;
        }

        Group group = getById(groupId);

        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }


        List<Long> ids = groupUserService.list(
                new LambdaQueryWrapper<GroupUser>()
                        .allEq(Map.of(GroupUser::getGroupId, groupId))).stream().map(GroupUser::getId).collect(Collectors.toList());
        removeByIds(ids);

        return removeById(groupId);
    }

    @Override
    public boolean update(Long currentUserId, UpdateGroupParams updateGroupParams) {
        boolean admin = groupUserService.isAdmin(currentUserId, updateGroupParams.getId());
        if (!admin) {
            return false;
        }

        Group group = getById(updateGroupParams.getId());

        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        return update(group, new LambdaQueryWrapper<Group>()
                .eq(Group::getAvatar, updateGroupParams.getAvatar())
                .eq(Group::getName, updateGroupParams.getName()));
    }

    @Override
    public boolean transfer(Long currentUserId, Long groupId, Long toId) {
        Integer count = lambdaQuery().allEq(Map.of(Group::getId, groupId, Group::getOwnerId, currentUserId)).count();
        if (count == 0) {
            return false;
        }

        // 校验群组是否存在
        Group group = getById(groupId);
        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        // 校验新群主现在是否为群成员
        GroupUser desGroupUser = groupUserService.getOne(new LambdaQueryWrapper<GroupUser>()
                .eq(GroupUser::getUserId, toId));
        if (desGroupUser == null) {
            Asserts.fail(MessageConstant.IS_NOT_MEMBER);
        }

        // 校验当前用户是否为群主
        if (!group.getOwnerId().equals(ThreadLocalUtils.get().getId())) {
            Asserts.fail(MessageConstant.IS_NOT_ADMIN);
        }

        // 更新群主
        return update(group, new LambdaQueryWrapper<Group>()
                .eq(Group::getOwnerId, toId));
    }

    @Override
    public IPage<Group> search(String groupName, long currentPage, long size) {
        size = Math.min(10, Math.max(5, size));
        return groupMapper.selectPage(new Page<>(currentPage, size), new LambdaQueryWrapper<Group>().like(Group::getName, groupName));
    }
}

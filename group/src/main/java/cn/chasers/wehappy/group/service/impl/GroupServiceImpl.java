package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.dto.UpdateGroupParams;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import cn.chasers.wehappy.common.util.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long groupId) {
        Group group = getById(groupId);

        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        removeById(groupId);

        List<GroupUser> groupUserList = groupUserService.list(
                new LambdaQueryWrapper<GroupUser>()
                        .allEq(Map.of(GroupUser::getGroupId, groupId)));

        if (groupUserList == null || groupUserList.size() == 0) {
            Asserts.fail(MessageConstant.ERROR_SYS);
        }

        for (GroupUser groupUser : groupUserList) {
            groupUserService.removeById(groupUser.getId());
        }

        return true;
    }

    @Override
    public boolean update(UpdateGroupParams updateGroupParams) {
        Group group = getById(updateGroupParams.getId());

        if (group == null) {
            Asserts.fail(MessageConstant.GROUP_NOT_EXIST);
        }

        return update(group, new LambdaQueryWrapper<Group>()
                .eq(Group::getAvatar, updateGroupParams.getAvatar())
                .eq(Group::getName, updateGroupParams.getName()));
    }

    @Override
    public boolean transfer(Long groupId, Long toId) {
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
        if (!group.getOwnerId().equals(UserUtil.getCurrentUserId(request))) {
            Asserts.fail(MessageConstant.IS_NOT_ADMIN);
        }

        // 更新群主
        return update(group, new LambdaQueryWrapper<Group>()
                .eq(Group::getOwnerId, toId));
    }

    @Override
    public Group search(String groupName) {
        Long currentUserId = UserUtil.getCurrentUserId(request);

        // 获取当前用户所有群组
        List<GroupUser> groupList = groupUserService.list(new LambdaQueryWrapper<GroupUser>()
                .allEq(Map.of(GroupUser::getUserId, currentUserId)));
        List<Long> groupIds = new ArrayList<>();
        for (GroupUser groupUser : groupList) {
            groupIds.add(groupUser.getGroupId());
        }
        List<Group> groups = listByIds(groupIds);

        // 查找该群组并返回
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
        }
        Asserts.fail(MessageConstant.ERROR_PARAMS);
        return null;
    }
}

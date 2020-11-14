package cn.chasers.wehappy.group.service.impl;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.group.constant.MessageConstant;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.feign.IUserService;
import cn.chasers.wehappy.group.mapper.GroupMapper;
import cn.chasers.wehappy.group.mapper.GroupUserMapper;
import cn.chasers.wehappy.group.service.IGroupUserService;
import cn.chasers.wehappy.group.util.UserUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private final GroupUserMapper groupUserMapper;

    @Autowired
    public GroupUserServiceImpl(GroupMapper groupMapper, IUserService userService, HttpServletRequest request, GroupUserMapper groupUserMapper) {
        this.groupMapper = groupMapper;
        this.userService = userService;
        this.request = request;
        this.groupUserMapper = groupUserMapper;
    }



    @Override
    public boolean invite(Long userId, Long groupId) {
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
        groupUser.setInvitedUserId(UserUtil.getCurrentUserId(request));
        groupUser.setType(0);
        groupUser.setStatus(1);

        return groupUserMapper.insert(groupUser) > 0;
    }

    @Override
    public boolean apply(Long groupId) {
        return false;
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

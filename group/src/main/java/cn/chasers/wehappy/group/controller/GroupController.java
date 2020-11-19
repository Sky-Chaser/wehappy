package cn.chasers.wehappy.group.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 群聊信息表 前端控制器
 * </p>
 *
 * @author liamcoder
 * @since 2020-11-12
 */
@RestController
@Api(value = "/", tags = "群组模块")
public class GroupController {

    private final IGroupService groupService;
    private final IGroupUserService groupUserService;

    @Autowired
    public GroupController(IGroupService groupService, IGroupUserService groupUserService) {
        this.groupService = groupService;
        this.groupUserService = groupUserService;
    }

    /**
     * 查询群组信息
     *
     * @param id 群组 Id
     * @return 返回群组信息
     */
    @GetMapping("/{id}")
    @ApiOperation("查询群组信息")
    public CommonResult<Group> get(@ApiParam(value = "群组Id") @PathVariable Long id) {
        return CommonResult.success(groupService.getById(id));
    }

    /**
     * 查询群聊中的全部用户 Id
     *
     * @param id 群聊 Id
     * @return 群聊中的全部用户 Id
     */
    @ApiOperation("查询群聊中的全部用户 Id")
    @GetMapping("/{id}/users")
    public CommonResult<List<Long>> getUserIds(@ApiParam(value = "群组Id") @PathVariable Long id) {
        return CommonResult.success(groupUserService.getUserIds(id));
    }

    /**
     * 查询群组用户信息
     *
     * @param id     群聊 Id
     * @param userId 用户 Id
     * @return GroupUser
     */
    @ApiOperation("查询群组用户信息")
    @GetMapping("/{id}/users/{userId}")
    public CommonResult<GroupUser> getGroupUser(@ApiParam(value = "群组Id") @PathVariable Long id, @ApiParam(value = "用户Id") @PathVariable Long userId) {
        return CommonResult.success(groupUserService.getByGroupIdAndUserId(id, userId));
    }

}


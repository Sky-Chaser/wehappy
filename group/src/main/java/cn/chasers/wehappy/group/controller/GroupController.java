package cn.chasers.wehappy.group.controller;


import cn.chasers.wehappy.common.api.CommonPage;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.group.dto.GroupUserParam;
import cn.chasers.wehappy.group.dto.HandlerParam;
import cn.chasers.wehappy.group.dto.SearchParams;
import cn.chasers.wehappy.group.dto.UpdateGroupParams;
import cn.chasers.wehappy.group.entity.Group;
import cn.chasers.wehappy.group.entity.GroupUser;
import cn.chasers.wehappy.group.service.IGroupService;
import cn.chasers.wehappy.group.service.IGroupUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("创建群")
    @PostMapping
    public CommonResult<Boolean> createGroup(@RequestBody UpdateGroupParams updateGroupParams) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupService.create(userId, updateGroupParams.getName(), updateGroupParams.getAvatar()));
    }

    @ApiOperation("更新群基本信息")
    @PutMapping
    public CommonResult<Boolean> updateGroup(@RequestBody UpdateGroupParams updateGroupParams) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupService.update(userId, updateGroupParams));
    }

    @ApiOperation("退群")
    @DeleteMapping("/exit/{groupId}")
    public CommonResult<Boolean> exitGroup(@ApiParam(value = "欲退群的群组id") @PathVariable Long groupId) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.exit(userId, groupId));
    }

    @ApiOperation("解散群")
    @DeleteMapping("/{groupId}")
    public CommonResult<Boolean> removeGroup(@ApiParam(value = "欲解散的群组id") @PathVariable Long groupId) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupService.remove(userId, groupId));
    }

    @ApiOperation("转让群")
    @PostMapping("/transfer")
    public CommonResult<Boolean> transferGroup(@RequestBody GroupUserParam groupUserParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupService.transfer(userId, groupUserParam.getGroupId(), groupUserParam.getUserId()));
    }

    @ApiOperation("增加管理员")
    @PostMapping("/admin")
    public CommonResult<Boolean> addAdmin(@RequestBody GroupUserParam groupUserParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.addAdmin(userId, groupUserParam.getUserId(), groupUserParam.getGroupId()));
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/admin")
    public CommonResult<Boolean> deleteAdmin(@RequestBody GroupUserParam groupUserParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.deleteAdmin(userId, groupUserParam.getUserId(), groupUserParam.getGroupId()));
    }

    @ApiOperation("获取申请加入该群组的用户")
    @GetMapping("applied/{groupId}")
    public CommonResult<List<GroupUser>> getApplied(@ApiParam("群组id") @PathVariable() Long groupId) {
        return CommonResult.success(groupUserService.getAppliedUsers(groupId));
    }

    @ApiOperation("返回当前用户所有的群组列表")
    @GetMapping("/groups")
    public CommonResult<List<Group>> getGroups() {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.getList(userId));
    }

    @ApiOperation("搜索群组")
    @GetMapping("/search")
    public CommonPage<Group> searchGroups(@RequestBody SearchParams searchParams) {
        return CommonPage.restPage(groupService.search(searchParams.getGroupName(), searchParams.getCurrentPage(), searchParams.getSize()));
    }

    @ApiOperation("删除群成员")
    @DeleteMapping("member")
    public CommonResult<Boolean> deleteMember(@RequestBody GroupUserParam groupUserParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.deleteMember(userId, groupUserParam.getUserId(), groupUserParam.getGroupId()));
    }

    @ApiOperation("申请加入群")
    @PostMapping("/apply")
    public CommonResult<Boolean> applyJoinGroup(@RequestBody Long groupId) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.apply(userId, groupId));
    }

    @ApiOperation("处理入群申请")
    @PostMapping("/handle-apply")
    public CommonResult<Boolean> handleApply(@RequestBody HandlerParam handlerParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.handleApply(userId, handlerParam.getGroupId(), handlerParam.getGroupUserId(), handlerParam.getAgree()));
    }

    @ApiOperation("邀请用户入群")
    @PostMapping("invite")
    public CommonResult<Boolean> invite(@RequestBody GroupUserParam groupUserParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.invite(userId, groupUserParam.getUserId(), groupUserParam.getGroupId()));
    }

    @ApiOperation("处理加群邀请")
    @PostMapping("handle-invite")
    public CommonResult<Boolean> handleInvite(@RequestBody HandlerParam handlerParam) {
        Long userId = ThreadLocalUtils.get().getId();
        return CommonResult.success(groupUserService.handleInvite(userId, handlerParam.getGroupId(), handlerParam.getAgree()));
    }

}


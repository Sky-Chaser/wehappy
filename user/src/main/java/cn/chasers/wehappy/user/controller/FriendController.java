package cn.chasers.wehappy.user.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.user.entity.Friend;
import cn.chasers.wehappy.user.service.IFriendService;
import cn.chasers.wehappy.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 好友信息表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final IFriendService friendService;
    private final IUserService userService;

    @Autowired
    public FriendController(IFriendService friendService, IUserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @ApiOperation("获取好友列表")
    @GetMapping
    public CommonResult<List<Friend>> list() {
        return CommonResult.success(friendService.list(userService.getCurrentUser().getId()));
    }

    @ApiOperation("添加好友")
    @PostMapping("/{toId}")
    public CommonResult<Boolean> add(@PathVariable @Validated Long toId) {
        return CommonResult.success(friendService.addFriend(userService.getCurrentUser().getId(), toId));
    }

    @ApiOperation("处理添加好友请求")
    @PutMapping("/{fromId}")
    public CommonResult<Boolean> update(@PathVariable @Validated Long fromId, @RequestParam @Validated boolean agree) {
        return CommonResult.success(friendService.handleAddFriend(userService.getCurrentUser().getId(), fromId, agree));
    }

    @ApiOperation("删除好友")
    @PutMapping("/{toId}")
    public CommonResult<Boolean> remove(@PathVariable @Validated Long toId) {
        return CommonResult.success(friendService.deleteFriend(userService.getCurrentUser().getId(), toId));
    }
}


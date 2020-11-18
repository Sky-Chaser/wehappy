package cn.chasers.wehappy.group.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 群聊信息表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-01
 */
@RestController
public class GroupController {

    /**
     * 查询群聊中的全部用户 Id
     *
     * @param id 群聊 Id
     * @return 群聊中的全部用户 Id
     */
    @GetMapping("/{id}/users")
    public CommonResult<List<Long>> getUserIds(@ApiParam(value = "群组Id") @PathVariable Long id) {
        return null;
    }

    /**
     * 判断用户是否为群聊成员
     *
     * @param id     群聊 Id
     * @param userId 用户 Id
     * @return 返回用户是否为群聊成员
     */
    @GetMapping("/{id}/users/{userId}")
    public CommonResult<Boolean> isGroupUser(@ApiParam(value = "群组Id") @PathVariable Long id, @ApiParam(value = "用户Id") @PathVariable Long userId) {
        return null;
    }
}


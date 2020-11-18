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

    @GetMapping("/{id}/users")
    public CommonResult<List<User>> getUsers(@ApiParam(value = "群组Id") @PathVariable Long id) {
        return null;
    }

    @GetMapping("/{id}/users/{userId}")
    public CommonResult<User> getUser(@ApiParam(value = "群组Id") @PathVariable Long id, @ApiParam(value = "用户Id") @PathVariable Long userId) {
        return null;
    }
}


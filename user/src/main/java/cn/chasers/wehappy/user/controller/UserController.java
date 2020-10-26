package cn.chasers.wehappy.user.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.user.dto.LoginParams;
import cn.chasers.wehappy.user.dto.RegisterParams;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "用户模块")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation("登录成功返回token")
    @PostMapping("/login")
    public CommonResult<User> register(@Validated @RequestBody RegisterParams registerParams) {
        return CommonResult.success(userService.register(registerParams.getEmail(), registerParams.getUsername(), registerParams.getPassword(), registerParams.getCode()));
    }


    @ApiOperation("登录成功返回token")
    @PostMapping("/login")
    public CommonResult<Map<String, Object>> login(@Validated @RequestBody LoginParams loginParams) {
        return CommonResult.success(userService.login(loginParams.getUsername(), loginParams.getPassword()));
    }
}


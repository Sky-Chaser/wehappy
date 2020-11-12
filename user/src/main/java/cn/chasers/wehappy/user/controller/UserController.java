package cn.chasers.wehappy.user.controller;


import cn.chasers.wehappy.common.api.CommonPage;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.user.constant.DtoConstant;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.dto.QueryParams;
import cn.chasers.wehappy.user.dto.RegisterParams;
import cn.chasers.wehappy.user.dto.SearchParams;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.service.IUserService;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@RestController
@Api(value = "/user", tags = "用户模块")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation("发送注册所需验证码")
    @PostMapping("/code")
    public CommonResult<?> sendCode(@Validated @RequestBody @Email @ApiParam(value = "邮箱地址", required = true) String email) {
        userService.sendRegisterEmailCode(email);
        return CommonResult.success(null);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult<User> register(@Validated @RequestBody RegisterParams registerParams) {
        return CommonResult.success(userService.register(registerParams.getEmail(), registerParams.getUsername(), registerParams.getPassword(), registerParams.getCode()));
    }

    @ApiOperation("检查用户名或邮箱地址是否已经被注册过")
    @GetMapping("/check-exist/{type}/{data}")
    public CommonResult<Boolean> checkExist(@Validated @PathVariable @ApiParam(value = "字段类型，username或email", required = true) String type, @ApiParam(value = "字段值", required = true)@Validated @PathVariable String data) {
        if (DtoConstant.USERNAME.equals(type)) {
            return CommonResult.success(userService.getByUsername(data) == null);
        }

        if (DtoConstant.EMAIL.equals(type)) {
            return CommonResult.success(userService.getByEmail(data) == null);
        }

        return CommonResult.failed(MessageConstant.ERROR_PARAMS);
    }

    @ApiOperation("根据用户名或邮箱地址模糊查询用户信息")
    @GetMapping("/search")
    public CommonPage<User> search(@Validated SearchParams searchParams) {
        if (StrUtil.isEmpty(searchParams.getEmail())) {
            return CommonPage.restPage(userService.getByUsernameLike(searchParams.getUsername(), searchParams.getCurrentPage(), searchParams.getSize()));
        }

        return CommonPage.restPage(userService.getByEmailLike(searchParams.getEmail(), searchParams.getCurrentPage(), searchParams.getSize()));
    }

    @ApiOperation("根据用户名或邮箱地址查询用户信息")
    @GetMapping("/query")
    public CommonResult<User> query(@Validated QueryParams queryParams) {
        if (StrUtil.isEmpty(queryParams.getEmail())) {
            return CommonResult.success(userService.getByUsername(queryParams.getUsername()));
        }

        return CommonResult.success(userService.getByEmail(queryParams.getEmail()));
    }

    @ApiOperation("根据用户ID查询用户信息")
    @GetMapping("/{id}")
    public CommonResult<UserDto> query(@Validated @ApiParam("用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return CommonResult.success(null);
        }
        return CommonResult.success(UserDto.builder().id(user.getId()).username(user.getUsername()).build());
    }
}

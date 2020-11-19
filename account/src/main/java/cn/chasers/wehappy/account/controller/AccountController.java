package cn.chasers.wehappy.account.controller;


import cn.chasers.wehappy.account.entity.Account;
import cn.chasers.wehappy.account.service.IAccountService;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * <p>
 * 账户表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@RestController
@Slf4j
@Api(value = "/", tags = "账户模块")
public class AccountController {
    private final IAccountService accountService;

    @Autowired
    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation("查看账户信息")
    @GetMapping
    public CommonResult<Account> get() {
        return CommonResult.success(accountService.get(ThreadLocalUtils.get().getId()));
    }

    @ApiOperation("充值")
    @PostMapping("/invest")
    public CommonResult<Boolean> invest(@Validated @ApiParam("充值金额") BigDecimal money) {
        return CommonResult.success(accountService.invest(ThreadLocalUtils.get().getId(), money));
    }

    @ApiOperation("支付")
    @PostMapping("/pay")
    public CommonResult<Boolean> pay(@Validated @ApiParam("支付金额") BigDecimal money) {
        return CommonResult.success(accountService.pay(ThreadLocalUtils.get().getId(), money));
    }
}


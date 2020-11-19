package cn.chasers.wehappy.account.controller;

import cn.chasers.wehappy.account.entity.BigRedEnvelope;
import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import cn.chasers.wehappy.account.service.IBigRedEnvelopeService;
import cn.chasers.wehappy.account.service.ISmallRedEnvelopeService;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 * 红包信息 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@RestController
@RequestMapping("/red-envelope")
@Api(value = "/red-envelope", tags = "账户模块")
public class RedEnvelopeController {
    private final IBigRedEnvelopeService bigRedEnvelopeService;
    private final ISmallRedEnvelopeService smallRedEnvelopeService;

    @Autowired
    public RedEnvelopeController(IBigRedEnvelopeService bigRedEnvelopeService, ISmallRedEnvelopeService smallRedEnvelopeService) {
        this.bigRedEnvelopeService = bigRedEnvelopeService;
        this.smallRedEnvelopeService = smallRedEnvelopeService;
    }

    @ApiOperation("发红包")
    @PostMapping("/send")
    public CommonResult<Boolean> send(@Validated @ApiParam("红包类型，7表示私聊红包，8表示群聊运气红包") Long type, @Validated @ApiParam("接收者Id，type为7时表示用户Id，type为8时，表示群聊Id") Long to, @Validated @ApiParam("红包金额") BigDecimal money) {
        return CommonResult.success(bigRedEnvelopeService.send(ThreadLocalUtils.get().getId(), type, to, money));
    }

    @ApiOperation("抢红包")
    @PostMapping("/snap")
    public CommonResult<Boolean> snap(@Validated @ApiParam("大红包Id") Long bigRedEnvelopeId) {
        return CommonResult.success(bigRedEnvelopeService.snap(ThreadLocalUtils.get().getId(), bigRedEnvelopeId));
    }

    @ApiOperation("查看大红包详细信息")
    @GetMapping("/bigRedEnvelope/{id}")
    public CommonResult<BigRedEnvelope> getBigRedEnvelopId(@Validated @ApiParam("大红包Id") @PathVariable Long id) {
        return CommonResult.success(bigRedEnvelopeService.get(ThreadLocalUtils.get().getId(), id));
    }

    @ApiOperation("查看小红包详细信息")
    @GetMapping("/smallRedEnvelope/{id}")
    public CommonResult<SmallRedEnvelope> getSmallRedEnvelopId(@Validated @ApiParam("大红包Id") @PathVariable Long id) {
        return CommonResult.success(smallRedEnvelopeService.get(ThreadLocalUtils.get().getId(), id));
    }
}


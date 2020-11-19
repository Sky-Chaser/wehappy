package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.service.IMessageDeleteService;
import cn.chasers.wehappy.message.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 消息索引表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@RestController
@Api(value = "/", tags = "消息模块")
public class MessageController {
    private final IMessageService messageService;
    private final IMessageDeleteService messageDeleteService;

    @Autowired
    public MessageController(IMessageService messageService, IMessageDeleteService messageDeleteService) {
        this.messageService = messageService;
        this.messageDeleteService = messageDeleteService;
    }

    /**
     * 查询单个消息
     *
     * @param id 消息 Id
     * @return Message
     */
    @ApiOperation("查询单个消息")
    @GetMapping("/{id}")
    public CommonResult<Message> get(@PathVariable @ApiParam(value = "消息Id", required = true) Long id) {
        return CommonResult.success(messageService.getById(id));
    }

    /**
     * 查询多个消息
     *
     * @param ids 消息 Id
     * @return List<Message>
     */
    @ApiOperation("查询多个消息")
    @GetMapping
    public CommonResult<List<Message>> query(@RequestBody @ApiParam(value = "消息Id列表", required = true) List<Long> ids) {
        return CommonResult.success(messageService.listByIds(ids));
    }

    /**
     * 用户删除消息操作
     *
     * @param id 消息 Id
     * @return 操作结果
     */
    @ApiOperation("用户删除消息操作")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> remove(@PathVariable @ApiParam(value = "消息Id", required = true) Long id) {
        return CommonResult.success(messageDeleteService.add(ThreadLocalUtils.get().getId(), id));
    }

}


package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.service.IMessageService;
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
@RequestMapping
public class MessageController {
    private final IMessageService messageService;

    @Autowired
    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 查询单个消息
     *
     * @param id 消息 Id
     * @return Message
     */
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
    @GetMapping
    public CommonResult<List<Message>> query(@RequestBody @ApiParam(value = "消息Id列表", required = true) List<Long> ids) {
        return CommonResult.success(messageService.listByIds(ids));
    }

}


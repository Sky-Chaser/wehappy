package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonPage;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.message.dto.MessageQueryParam;
import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.service.IConversationService;
import cn.chasers.wehappy.message.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 最近会话表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/conversation")
@Api(value = "/conversation", tags = "消息模块")
public class ConversationController {

    private final IMessageService messageService;
    private final IConversationService conversationService;

    @Autowired
    public ConversationController(IMessageService messageService, IConversationService conversationService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
    }

    /**
     * 获取会话的聊天记录
     *
     * @param id    会话Id
     * @param param 分页参数
     * @return 消息
     */
    @ApiOperation("获取会话的聊天记录")
    @GetMapping("/messages/{id}")
    public CommonPage<Message> getMessages(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long id, @RequestParam MessageQueryParam param) {
        return CommonPage.restPage(messageService.getMessagesByConversationId(id, param.getMessageId(), param.getCurrentPage(), param.getSize()));
    }

    /**
     * 获取会话的未读聊天记录
     *
     * @param id 会话Id
     * @return 消息
     */
    @ApiOperation("获取会话的未读聊天记录")
    @GetMapping("/messages/unread/{id}")
    public CommonResult<List<Message>> getUnreadMessages(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long id) {
        return CommonResult.success(messageService.getUnreadMessagesByConversationId(id));
    }

    /**
     * 删除会话
     *
     * @param id 会话Id
     * @return 删除操作结果
     */
    @ApiOperation("删除会话")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> remove(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long id) {
        return CommonResult.success(conversationService.remove(id));
    }

    /**
     * 根据好友 Id 或群聊 Id 获取聊天记录
     *
     * @param toId  会话Id
     * @param type  聊天类型
     * @param param 分页参数
     * @return 消息
     */
    @ApiOperation("根据好友 Id 或群聊 Id 获取聊天记录")
    @GetMapping("/messages/unread/{type}/{toId}")
    public CommonPage<Message> getMessages(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long toId, @Validated @PathVariable @ApiParam(value = "好友 Id 或群聊 Id", required = true) Integer type, @RequestParam MessageQueryParam param) {
        return CommonPage.restPage(messageService.getMessagesByToId(type, ThreadLocalUtils.get().getId(), toId, param.getMessageId(), param.getCurrentPage(), param.getSize()));
    }

    /**
     * 获取会话列表
     *
     * @return 会话列表
     */
    @ApiOperation("获取会话列表")
    @GetMapping
    public CommonResult<List<Conversation>> getAll() {
        return CommonResult.success(conversationService.listByUserId(ThreadLocalUtils.get().getId()));
    }

}


package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.message.dto.MessageQueryParam;
import cn.chasers.wehappy.message.entity.Message;
import io.swagger.annotations.ApiParam;
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
public class ConversationController {

    /**
     * 获取会话的聊天记录
     *
     * @param id    会话Id
     * @param param 分页参数
     * @return 消息
     */
    @GetMapping("/{id}")
    public CommonResult<List<Message>> getMessages(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long id, @RequestParam MessageQueryParam param) {
        return null;
    }

    /**
     * 获取会话的未读聊天记录
     *
     * @param id    会话Id
     * @param param 分页参数
     * @return 消息
     */
    @GetMapping("/unread/{id}")
    public CommonResult<List<Message>> getUnreadMessages(@Validated @PathVariable @ApiParam(value = "会话Id", required = true) Long id, @RequestParam MessageQueryParam param) {
        return null;
    }

    /**
     * 获取私聊的聊天记录
     *
     * @param id    会话Id
     * @param param 分页参数
     * @return 消息
     */
    @GetMapping("/single/{id}")
    public CommonResult<List<Message>> getFriendMessages(@Validated @PathVariable @ApiParam(value = "用户Id", required = true) Long id, @RequestParam MessageQueryParam param) {
        return null;
    }

    /**
     * 获取群聊的聊天记录
     *
     * @param id    会话Id
     * @param param 分页参数
     * @return 消息
     */
    @GetMapping("/group/{id}")
    public CommonResult<List<Message>> getGroupMessages(@Validated @PathVariable @ApiParam(value = "群聊Id", required = true) Long id, @RequestParam MessageQueryParam param) {
        return null;
    }

}


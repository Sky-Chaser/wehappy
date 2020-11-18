package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.message.service.IConversationUnreadService;
import cn.chasers.wehappy.message.service.IUnreadService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户未读数表 前端控制器
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@RestController
@RequestMapping("/unread")
public class UnreadController {

    private final IUnreadService unreadService;
    private final IConversationUnreadService conversationUnreadService;

    @Autowired
    public UnreadController(IUnreadService unreadService, IConversationUnreadService conversationUnreadService) {
        this.unreadService = unreadService;
        this.conversationUnreadService = conversationUnreadService;
    }

    /**
     * 查询用户的总消息未读数
     *
     * @return 消息未读数
     */
    @GetMapping
    public CommonResult<Integer> get() {
        return CommonResult.success(unreadService.getCount(ThreadLocalUtils.get().getId()));
    }

    /**
     * 查询会话未读数
     *
     * @param conversationId 会话 Id
     * @return 未读数
     */
    @GetMapping("/{conversationId}")
    public CommonResult<Integer> getByConversationId(@PathVariable @ApiParam(value = "会话Id", required = true) Long conversationId) {
        return CommonResult.success(conversationUnreadService.getByConversationId(conversationId).getCount());
    }

    /**
     * 根据会话最后一次查看的消息 Id 更新未读消息个数
     *
     * @param conversationId    会话 Id
     * @param lastReadMessageId 会话最后一次查看的消息 Id
     * @return 更新结果
     */
    @PostMapping("/{conversationId}")
    public CommonResult<Boolean> updateByConversationId(@PathVariable @ApiParam(value = "会话Id", required = true) Long conversationId, @RequestParam Long lastReadMessageId) {
        return CommonResult.success(conversationUnreadService.updateByLastReadMessageId(conversationId, lastReadMessageId));
    }

}


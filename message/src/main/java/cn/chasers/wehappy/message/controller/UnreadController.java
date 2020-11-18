package cn.chasers.wehappy.message.controller;


import cn.chasers.wehappy.common.api.CommonResult;
import io.swagger.annotations.ApiParam;
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

    /**
     * 查询用户的总消息未读数
     *
     * @return 消息未读数
     */
    public CommonResult<Integer> get() {
        return null;
    }

    /**
     * 查询会话未读数
     *
     * @param id 会话 Id
     * @return 未读数
     */
    @GetMapping("/{id}")
    public CommonResult<Integer> getByConversationId(@PathVariable @ApiParam(value = "会话Id", required = true) Long id) {
        return null;
    }

    /**
     * 根据会话最后一次查看的消息 Id 更新未读消息个数
     *
     * @param id                会话 Id
     * @param lastReadMessageId 会话最后一次查看的消息 Id
     * @return 更新结果
     */
    @PostMapping("/{id}")
    public CommonResult<Boolean> updateByConversationId(@PathVariable @ApiParam(value = "会话Id", required = true) Long id, @RequestParam Long lastReadMessageId) {
        return null;
    }

}


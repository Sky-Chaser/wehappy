package cn.chasers.wehappy.chat.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * 调用 `user` 服务方法的接口
 * @author lollipop
 */
@FeignClient("user")
public interface IFriendService {

    /**
     * 判断 from 和 to 是不是好友
     * @param fromId
     * @param toId
     * @return
     */
    @GetMapping("/friend/is-friend")
    Mono<CommonResult<Boolean>> isFriend(Long fromId, Long toId);
}

package cn.chasers.wehappy.account.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用 `user` 服务方法的接口
 * @author lollipop
 */
@FeignClient("user")
public interface IUserService {

    /**
     * 判断 from 和 to 是不是好友
     * @param fromId
     * @param toId
     * @return
     */
    @GetMapping("/friend/is-friend")
    CommonResult<Boolean> isFriend(@RequestParam Long fromId, @RequestParam Long toId);
}

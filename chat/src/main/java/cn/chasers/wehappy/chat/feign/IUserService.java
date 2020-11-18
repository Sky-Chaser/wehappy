package cn.chasers.wehappy.chat.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
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
     * 根据用户Id查看用户的详细信息
     *
     * @param id 用户Id
     * @return 返回 CommonResult<UserDto> 类型结果，包含用户的详细信息
     */
    @GetMapping
    CommonResult<UserDto> get(@RequestParam Long id);

    /**
     * 判断 from 和 to 是不是好友
     * @param fromId
     * @param toId
     * @return
     */
    @GetMapping("/friend/is-friend")
    CommonResult<Boolean> isFriend(@RequestParam Long fromId, @RequestParam Long toId);
}

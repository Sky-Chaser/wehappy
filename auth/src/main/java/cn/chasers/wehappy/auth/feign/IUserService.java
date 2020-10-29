package cn.chasers.wehappy.auth.feign;

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
     * 根据邮箱地址查看用户的详细信息
     *
     * @param email 用户注册时的邮箱
     * @return 返回 CommonResult<UserDto> 类型结果，包含用户的详细信息
     */
    @GetMapping("/user")
    CommonResult<UserDto> queryByEmail(@RequestParam String email);

    /**
     * 根据用户名查看用户的详细信息
     *
     * @param username 用户名
     * @return 返回 CommonResult<UserDto> 类型结果，包含用户的详细信息
     */
    @GetMapping("/user")
    CommonResult<UserDto> queryByUsername(@RequestParam String username);
}

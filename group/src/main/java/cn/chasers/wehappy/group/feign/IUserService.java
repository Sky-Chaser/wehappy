package cn.chasers.wehappy.group.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liamcoder
 * @date 2020/11/12 9:40 下午
 */
@FeignClient("user")
public interface IUserService {
    /**
     * 根据用户ID查询用户
     * @param id        用户ID
     * @return          返回用户信息
     */
    @GetMapping("/{id}")
    CommonResult<UserDto> query(@Validated @PathVariable Long id);
}

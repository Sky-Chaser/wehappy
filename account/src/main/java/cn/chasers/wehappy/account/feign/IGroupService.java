package cn.chasers.wehappy.account.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 调用 `group` 服务方法的接口
 *
 * @author lollipop
 */
@FeignClient("group")
public interface IGroupService {

    /**
     * 查询群组用户信息
     *
     * @param id     群聊 Id
     * @param userId 用户 Id
     * @return GroupUser
     */
    @GetMapping("/{id}/users/{userId}")
    CommonResult<Map<String, Object>> getGroupUser(@ApiParam(value = "群组Id") @PathVariable Long id, @ApiParam(value = "用户Id") @PathVariable Long userId);
}

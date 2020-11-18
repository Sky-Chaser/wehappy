package cn.chasers.wehappy.chat.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.GroupDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 调用 `group` 服务方法的接口
 *
 * @author lollipop
 */
@FeignClient("group")
public interface IGroupService {

    /**
     * 查询群组信息
     *
     * @param id 群组 Id
     * @return 返回群组信息
     */
    @GetMapping("/{id}")
    CommonResult<GroupDto> get(@PathVariable Long id);

    /**
     * 查询群聊中的全部用户 Id
     *
     * @param id 群聊 Id
     * @return 群聊中的全部用户 Id
     */
    @GetMapping("/{id}/users")
    CommonResult<List<Long>> getUserIds(@PathVariable Long id);

    /**
     * 判断用户是否为群聊成员
     *
     * @param id     群聊 Id
     * @param userId 用户 Id
     * @return 返回用户是否为群聊成员
     */
    @GetMapping("/{id}/users/{userId}")
    CommonResult<Boolean> isGroupUser(@PathVariable Long id, @PathVariable Long userId);

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

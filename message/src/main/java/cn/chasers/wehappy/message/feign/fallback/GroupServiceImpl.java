package cn.chasers.wehappy.message.feign.fallback;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.GroupDto;
import cn.chasers.wehappy.message.feign.IGroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 调用 `group` 服务方法的兜底类
 *
 * @author lollipop
 */
@Service
public class GroupServiceImpl implements IGroupService {
    @Override
    public CommonResult<GroupDto> get(Long id) {
        return CommonResult.failed("group service 暂不可用.....");
    }

    @Override
    public CommonResult<List<Long>> getUserIds(Long id) {
        return CommonResult.failed("group service 暂不可用.....");
    }

    @Override
    public CommonResult<Boolean> isGroupUser(Long id, Long userId) {
        return CommonResult.failed("group service 暂不可用.....");
    }

    @Override
    public CommonResult<Map<String, Object>> getGroupUser(Long id, Long userId) {
        return CommonResult.failed("group service 暂不可用.....");
    }
}

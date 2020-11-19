package cn.chasers.wehappy.account.feign.fallback;

import cn.chasers.wehappy.account.feign.IGroupService;
import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用 `group` 服务方法的兜底类
 *
 * @author lollipop
 */
@Service
public class GroupServiceImpl implements IGroupService {

    @Override
    public CommonResult<Map<String, Object>> getGroupUser(Long id, Long userId) {
        return CommonResult.failed("group service 暂不可用.....");
    }
}

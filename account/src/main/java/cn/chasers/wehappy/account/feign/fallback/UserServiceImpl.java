package cn.chasers.wehappy.account.feign.fallback;

import cn.chasers.wehappy.account.feign.IUserService;
import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.stereotype.Service;

/**
 * 调用 `user` 服务方法的兜底类
 * @author lollipop
 */
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public CommonResult<Boolean> isFriend(Long fromId, Long toId) {
        return CommonResult.failed("user service 暂不可用.....");
    }
}

package cn.chasers.wehappy.auth.feign.fallback;

import cn.chasers.wehappy.auth.feign.IUserService;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.domain.UserDto;
import org.springframework.stereotype.Service;

/**
 * 调用 `user-service` 服务方法的兜底类
 * @author lollipop
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public CommonResult<UserDto> queryByEmail(String email) {
        return CommonResult.failed("user-service 暂不可用.....");
    }
}

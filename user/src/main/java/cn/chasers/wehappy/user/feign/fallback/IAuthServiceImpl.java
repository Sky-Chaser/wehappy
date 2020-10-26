package cn.chasers.wehappy.user.feign.fallback;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.user.feign.IAuthService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用 `auth-service` 服务方法的兜底类
 * @author lollipop
 */
@Service
public class IAuthServiceImpl implements IAuthService {
    @Override
    public CommonResult<Map<String, Object>> authentication(Map<String, String> parameters) {
        return CommonResult.failed("远程服务不可用。。。");
    }
}

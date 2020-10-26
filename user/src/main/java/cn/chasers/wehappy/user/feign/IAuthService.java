package cn.chasers.wehappy.user.feign;

import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 认证服务远程调用
 * @author lollipop
 */
@FeignClient("auth-service")
public interface IAuthService {

    /**
     * 调用`认证服务`进行身份认证的操作
     * @param parameters
     * @return
     */
    @PostMapping(value = "/authentication")
    CommonResult<Map<String, Object>> authentication(@RequestParam Map<String, String> parameters);

}
package cn.chasers.wehappy.auth.exception;

import cn.chasers.wehappy.common.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局处理Oauth2抛出的异常
 * @author lollipop
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {

    @ExceptionHandler(value = OAuth2Exception.class)
    public CommonResult<?> handleOauth2(OAuth2Exception e) {
        return CommonResult.failed(e.getMessage());
    }

}

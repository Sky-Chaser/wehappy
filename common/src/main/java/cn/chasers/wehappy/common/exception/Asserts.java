package cn.chasers.wehappy.common.exception;


import cn.chasers.wehappy.common.api.ResultCode;

/**
 * 断言处理类，用于抛出各种API异常
 * @author lollipop
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(ResultCode resultCode) {
        throw new ApiException(resultCode);
    }
}

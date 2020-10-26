package cn.chasers.wehappy.common.exception;

import cn.chasers.wehappy.common.api.ResultCode;

/**
 * 自定义API异常
 * @author lollipop
 */
public class ApiException extends RuntimeException {
    private ResultCode resultCode;

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultCode getErrorCode() {
        return resultCode;
    }
}

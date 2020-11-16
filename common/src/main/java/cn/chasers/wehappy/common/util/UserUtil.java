package cn.chasers.wehappy.common.util;

import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 从请求头部获取用户基本信息
 *
 * @author liamcoder
 * @date 2020/11/12 11:22 下午
 */
@Component
public class UserUtil {
    public static UserDto getCurrentUser(HttpServletRequest request) {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }

        return JSONUtil.toBean(userStr, UserDto.class);
    }

    public static Long getCurrentUserId(HttpServletRequest request) {
        return getCurrentUser(request).getId();
    }
}

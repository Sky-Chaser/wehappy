package cn.chasers.wehappy.group.util;

import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liamcoder
 * @date 2020/11/12 11:22 下午
 */
public class UserUtil {
    public static UserDto getCurrentUser(HttpServletRequest request) {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }

        return JSONUtil.toBean(userStr, UserDto.class);
    }

    public static Long getCurrentUserId(HttpServletRequest request) {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }

        return JSONUtil.toBean(userStr, UserDto.class).getId();
    }
}

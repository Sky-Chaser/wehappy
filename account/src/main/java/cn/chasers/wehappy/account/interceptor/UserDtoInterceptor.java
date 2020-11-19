package cn.chasers.wehappy.account.interceptor;

import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.common.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 利用 SpringMvc 拦截器，在请求到达前后获取和清除用户信息
 *
 * @author zhangyuanhang
 */
@Slf4j
@Component
public class UserDtoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            ThreadLocalUtils.set(UserUtil.getCurrentUser(request));
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }
}
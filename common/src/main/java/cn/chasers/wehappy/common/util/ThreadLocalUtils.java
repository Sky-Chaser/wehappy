package cn.chasers.wehappy.common.util;

import cn.chasers.wehappy.common.domain.UserDto;

/**
 * 保存用户信息的工具类，线程安全
 *
 * @author zhangyuanhang
 */
public class ThreadLocalUtils {
    private static final ThreadLocal<UserDto> threadLocal = new ThreadLocal<>();

    private ThreadLocalUtils() {
    }

    public static void set(UserDto user) {
        threadLocal.set(user);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static UserDto get() {
        return threadLocal.get();
    }
}
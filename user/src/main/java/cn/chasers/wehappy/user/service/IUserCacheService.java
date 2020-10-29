package cn.chasers.wehappy.user.service;

import cn.chasers.wehappy.user.entity.User;


/**
 * <p>
 * 用户信息缓存操作服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-28
 */
public interface IUserCacheService {
    /**
     * 删除用户缓存
     *
     * @param userId 用户id
     */
    void removeUser(Long userId);

    /**
     * 获取缓存用户信息
     *
     * @param userId 用户id
     * @return 用户缓存信息
     */
    User getUser(Long userId);

    /**
     * 设置缓存用户信息
     *
     * @param user 用户信息
     */
    void setUser(User user);
}

package cn.chasers.wehappy.user.service;

import cn.chasers.wehappy.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
public interface IUserService extends IService<User> {
    /**
     * 注册
     *
     * @param email    邮箱地址
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @return 返回注册结果
     */
    User register(String email, String username, String password, String code);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 调用认证结果
     */
    Map<String, Object> login(String username, String password);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据邮箱地址查询用户信息
     *
     * @param email 邮箱地址
     * @return 用户信息
     */
    User getByEmail(String email);
}

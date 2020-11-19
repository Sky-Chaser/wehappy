package cn.chasers.wehappy.user.service;

import cn.chasers.wehappy.user.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


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

    /**
     * 向邮箱中发送一条验证码，用来进行用户注册
     *
     * @param email 邮箱地址
     */
    void sendRegisterEmailCode(String email);

    /**
     * 根据用户名进行模糊查询
     *
     * @param username    用户名
     * @param currentPage 当前页
     * @param size        每页显示条数
     * @return 返回所有查询到的用户信息
     */
    IPage<User> getByUsernameLike(String username, long currentPage, long size);

    /**
     * 根据邮箱地址进行模糊查询
     *
     * @param email       邮箱地址
     * @param currentPage 当前页
     * @param size        每页显示条数
     * @return 返回所有查询到的用户信息
     */
    IPage<User> getByEmailLike(String email, long currentPage, long size);

    /**
     * 获取当前请求用户信息
     *
     * @return 返回用户详细信息
     */
    User getCurrentUser();

    /**
     * 点赞
     *
     * @param id 用户 Id
     * @return 返回最新的赞个数
     */
    Long like(Long id);


    /**
     * 查看用户最新的赞个数
     *
     * @param id 用户 Id
     * @return 返回最新的赞个数
     */
    Long getNumberLike(Long id);
}

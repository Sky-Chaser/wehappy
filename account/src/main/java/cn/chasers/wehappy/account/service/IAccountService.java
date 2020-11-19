package cn.chasers.wehappy.account.service;

import cn.chasers.wehappy.account.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
public interface IAccountService extends IService<Account> {

    /**
     * 获取用户账户信息
     *
     * @param userId 用户 Id
     * @return 返回用户账户信息
     */
    Account get(Long userId);

    /**
     * 当用户第一次查询账户信息时调用，用于初始化账户信息
     *
     * @param userId 用户 Id
     * @return 返回创建是否成功
     */
    boolean create(Long userId);

    /**
     * 充值操作
     *
     * @param userId 用户 Id
     * @param money  充值金额
     * @return 返回充值是否成功
     */
    boolean invest(Long userId, BigDecimal money);

    /**
     * 支付操作
     *
     * @param userId 用户 Id
     * @param money  支付金额
     * @return 返回支付是否成功
     */
    boolean pay(Long userId, BigDecimal money);
}

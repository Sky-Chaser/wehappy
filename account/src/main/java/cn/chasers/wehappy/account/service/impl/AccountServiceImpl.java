package cn.chasers.wehappy.account.service.impl;

import cn.chasers.wehappy.account.entity.Account;
import cn.chasers.wehappy.account.mapper.AccountMapper;
import cn.chasers.wehappy.account.service.IAccountService;
import cn.chasers.wehappy.common.util.UserUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account get(Long userId) {
        Account account = lambdaQuery().eq(Account::getUserId, userId).one();
        if (account != null) {
            return account;
        }

        create(userId);
        return lambdaQuery().eq(Account::getUserId, userId).one();
    }

    @Override
    public boolean create(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setMoney(new BigDecimal(0));
        account.setStatus(0);
        return save(account);
    }

    @Override
    public boolean invest(Long userId, BigDecimal money) {
        return accountMapper.addMoney(userId, money);
    }

    @Override
    public boolean pay(Long userId, BigDecimal money) {
        return accountMapper.addMoney(userId, money.multiply(new BigDecimal(-1)));
    }
}

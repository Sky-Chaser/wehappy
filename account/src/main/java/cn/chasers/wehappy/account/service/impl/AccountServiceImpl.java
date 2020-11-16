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

    private final HttpServletRequest request;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(HttpServletRequest request, AccountMapper accountMapper) {
        this.request = request;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account get() {
        Account account = lambdaQuery().eq(Account::getUserId, UserUtil.getCurrentUserId(request)).one();
        if (account != null) {
            return account;
        }

        create();
        return lambdaQuery().eq(Account::getUserId, UserUtil.getCurrentUserId(request)).one();
    }

    @Override
    public boolean create() {
        Account account = new Account();
        account.setUserId(UserUtil.getCurrentUserId(request));
        account.setMoney(new BigDecimal(0));
        account.setStatus(0);
        return save(account);
    }

    @Override
    public boolean invest(@Param("money") BigDecimal money) {
        return accountMapper.addMoney(UserUtil.getCurrentUserId(request), money);
    }

    @Override
    public boolean pay(@Param("money") BigDecimal money) {
        return accountMapper.addMoney(UserUtil.getCurrentUserId(request), money.multiply(new BigDecimal(-1)));
    }
}

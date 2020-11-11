package cn.chasers.wehappy.account.service.impl;

import cn.chasers.wehappy.account.entity.Account;
import cn.chasers.wehappy.account.mapper.AccountMapper;
import cn.chasers.wehappy.account.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

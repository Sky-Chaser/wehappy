package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.user.entity.Account;
import cn.chasers.wehappy.user.mapper.AccountMapper;
import cn.chasers.wehappy.user.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}

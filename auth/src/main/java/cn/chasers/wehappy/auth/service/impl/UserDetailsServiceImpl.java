package cn.chasers.wehappy.auth.service.impl;

import cn.chasers.wehappy.auth.constant.MessageConstant;
import cn.chasers.wehappy.auth.feign.IUserService;
import cn.chasers.wehappy.auth.domain.SecurityUser;
import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理业务类
 *
 * @author lollipop
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;
    private final HttpServletRequest request;

    @Autowired
    public UserDetailsServiceImpl(IUserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CommonResult<UserDto> result = userService.queryByUsername(username);

        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new DisabledException(MessageConstant.USER_SERVICE_ERROR);
        }

        UserDto userDto = result.getData();
        if (userDto == null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }

        // 暂时写死只有一种客户端
        userDto.setClientId(request.getHeader("client_id"));
        SecurityUser securityUser = new SecurityUser(userDto);

        // 账户被禁用
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        }

        // 账户被锁定
        if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 账户已过期
        if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        }

        // 登录凭证已过期
        if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }

        return securityUser;
    }
}

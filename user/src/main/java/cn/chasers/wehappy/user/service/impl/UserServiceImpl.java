package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.api.CommonResult;
import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.feign.IAuthService;
import cn.chasers.wehappy.user.mapper.UserMapper;
import cn.chasers.wehappy.user.service.IUserService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final IAuthService authService;
    private final IRedisService redisService;

    @Value("${bcrypt.salt}")
    private String salt;

    @Value("${redis.key.registerCode}")
    private String registerCodeKey;

    @Autowired
    public UserServiceImpl(IAuthService authService, IRedisService redisService) {
        this.authService = authService;
        this.redisService = redisService;
    }

    @Override
    public User register(String email, String username, String password, String code) {
        if (StrUtil.isEmpty(email)) {
            Asserts.fail("邮箱不能为空！");
        }

        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            Asserts.fail("用户名或密码不能为空！");
        }

        if (StrUtil.isBlank(code)) {
            Asserts.fail("验证码不能为空！");
        }

        if (!redisService.hHasKey(registerCodeKey, email) || !code.equals(redisService.hGet(registerCodeKey, email))) {
            Asserts.fail("验证码错误！");
        }

        if (getByEmail(email) != null) {
            Asserts.fail("该邮箱已经被注册过了！");
        }

        if (getByUsername(username) != null) {
            Asserts.fail("该用户名已经被注册过了！");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(username, salt));
        user.setAvatar("default");

        if (!save(user)) {
            Asserts.fail("注册失败！");
        }

        return user;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            Asserts.fail("用户名或密码不能为空！");
        }

        Map<String, String> params = Maps.newHashMap();
        params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        CommonResult<Map<String, Object>> result = authService.authentication(params);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            Asserts.fail(result.getMessage());
        }

        return result.getData();
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }
}

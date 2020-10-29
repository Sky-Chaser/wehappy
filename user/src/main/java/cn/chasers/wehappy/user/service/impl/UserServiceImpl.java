package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.mapper.UserMapper;
import cn.chasers.wehappy.user.mq.Producer;
import cn.chasers.wehappy.user.service.IUserService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    private final IRedisService redisService;
    private final UserMapper userMapper;
    private final Producer producer;

    @Value("${bcrypt.salt}")
    private String salt;

    @Value("${redis.registerCode.key}")
    private String registerCodeKey;

    @Value("${redis.registerCode.expire}")
    private long registerCodeExpire;

    @Value("${default.avatar}")
    private String defaultAvatar;

    @Autowired
    public UserServiceImpl(IRedisService redisService, UserMapper userMapper, Producer producer) {
        this.redisService = redisService;
        this.userMapper = userMapper;
        this.producer = producer;
    }

    @Override
    public User register(String email, String username, String password, String code) {
        if (StrUtil.isEmpty(email)) {
            Asserts.fail(MessageConstant.EMPTY_EMAIL);
        }

        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            Asserts.fail(MessageConstant.EMPTY_USERNAME_OR_PASSWORD);
        }

        if (StrUtil.isBlank(code)) {
            Asserts.fail(MessageConstant.EMPTY_CODE);
        }

        if ((!redisService.hHasKey(registerCodeKey, email) || !code.equals(redisService.hGet(registerCodeKey, email)))) {
            Asserts.fail(MessageConstant.ERROR_CODE);
        }

        if (getByEmail(email) != null) {
            Asserts.fail(MessageConstant.EMAIL_EXIST);
        }

        if (getByUsername(username) != null) {
            Asserts.fail(MessageConstant.USERNAME_EXIST);
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(username, salt));
        user.setAvatar(defaultAvatar);

        if (!save(user)) {
            Asserts.fail(MessageConstant.ERROR_REGISTER);
        }

        return user;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public User getByEmail(String email) {
        return userMapper.selectOne(lambdaQuery().eq(User::getEmail, email));
    }

    @Override
    public void sendRegisterEmailCode(String email) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(6);
        Map<String, Object> map = Map.of("to", email, "code", code);
        producer.sendRegisterCodeEmail(map, registerCodeExpire);
        redisService.hSet(registerCodeKey, email, code, registerCodeExpire);
    }

    @Override
    public IPage<User> getByUsernameLike(String username, long currentPage, long size) {
        size = Math.min(10, Math.max(5, size));
        return userMapper.selectPage(new Page<>(currentPage, size), new LambdaQueryWrapper<User>().like(User::getUsername, username));
    }

    @Override
    public IPage<User> getByEmailLike(String email, long currentPage, long size) {
        size = Math.min(10, Math.max(5, size));
        return userMapper.selectPage(new Page<>(currentPage, size), new LambdaQueryWrapper<User>().like(User::getEmail, email));
    }
}

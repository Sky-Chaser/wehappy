package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.api.ResultCode;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.mapper.UserMapper;
import cn.chasers.wehappy.user.mq.Producer;
import cn.chasers.wehappy.user.service.IUserCacheService;
import cn.chasers.wehappy.user.service.IUserService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private final IUserCacheService userCacheService;
    private final UserMapper userMapper;
    private final Producer producer;
    private final HttpServletRequest request;

    @Value("${redis.separator}")
    private String redisKeySeparator;

    @Value("${redis.database}")
    private String redisDatabase;

    @Value("${redis.registerCode.key}")
    private String registerCodeKey;

    @Value("${redis.registerCode.expire}")
    private long registerCodeExpire;

    @Value("${default.avatar}")
    private String defaultAvatar;

    @Autowired
    public UserServiceImpl(IRedisService redisService, IUserCacheService userCacheService, UserMapper userMapper, Producer producer, HttpServletRequest request) {
        this.redisService = redisService;
        this.userCacheService = userCacheService;
        this.userMapper = userMapper;
        this.producer = producer;
        this.request = request;
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

        if ((!redisService.hHasKey(getRedisEmailCodeKey(), email) || !code.equals(redisService.hGet(getRedisEmailCodeKey(), email)))) {
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
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setAvatar(defaultAvatar);

        if (!save(user)) {
            Asserts.fail(MessageConstant.ERROR_REGISTER);
        }

        return user;
    }

    @Override
    public User getByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Override
    public User getByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).one();
    }

    @Override
    public void sendRegisterEmailCode(String email) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(6);
        Map<String, Object> map = Map.of("to", email, "code", code);
        producer.sendRegisterCodeEmail(map, registerCodeExpire);
        redisService.hSet(getRedisEmailCodeKey(), email, code, registerCodeExpire);
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

    @Override
    public User getCurrentUser() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }

        UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
        User user = userCacheService.getUser(userDto.getId());
        if (user != null) {
            return user;
        }

        user = getById(userDto.getId());
        userCacheService.setUser(user);
        return user;
    }

    private String getRedisEmailCodeKey() {
        return redisDatabase + redisKeySeparator + registerCodeKey;
    }
}

package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.exception.Asserts;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.common.util.ThreadLocalUtils;
import cn.chasers.wehappy.user.constant.MessageConstant;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.mapper.UserMapper;
import cn.chasers.wehappy.user.mq.Producer;
import cn.chasers.wehappy.user.service.IUserCacheService;
import cn.chasers.wehappy.user.service.IUserService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Value("${redis.separator}")
    private String redisKeySeparator;

    @Value("${redis.database}")
    private String redisDatabase;

    @Value("${redis.registerCode.key}")
    private String registerCodeKey;

    @Value("${redis.registerCode.expire}")
    private long registerCodeExpire;

    @Value("${redis.likeInfo.key}")
    private String likeInfoKey;

    @Value("${redis.likeInfo.expire}")
    private long likeInfoExpire;

    @Value("${default.avatar}")
    private String defaultAvatar;

    @Autowired
    public UserServiceImpl(IRedisService redisService, IUserCacheService userCacheService, UserMapper userMapper, Producer producer) {
        this.redisService = redisService;
        this.userCacheService = userCacheService;
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
        long userId = ThreadLocalUtils.get().getId();
        User user = userCacheService.getUser(userId);
        if (user != null) {
            return user;
        }

        user = getById(userId);
        userCacheService.setUser(user);
        return user;
    }

    @Override
    @Transactional
    public Long like(Long id) {
        long userId = ThreadLocalUtils.get().getId();
        String key = likeInfoKey + redisKeySeparator + id;

        if (redisService.sIsMember(key, userId)) {
            return redisService.sSize(key);
        }

        User user = getById(userId);
        if (user == null) {
            Asserts.fail("用户不存在");
        }

        user.setNumberLike(user.getNumberLike() + 1);
        save(user);

        return redisService.sAdd(likeInfoKey + redisKeySeparator + id, userId);
    }

    @Override
    public Long getNumberLike(Long id) {
        return redisService.sSize(likeInfoKey + redisKeySeparator + id);
    }

    private String getRedisEmailCodeKey() {
        return redisDatabase + redisKeySeparator + registerCodeKey;
    }
}

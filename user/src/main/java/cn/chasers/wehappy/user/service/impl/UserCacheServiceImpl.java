package cn.chasers.wehappy.user.service.impl;

import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.user.entity.User;
import cn.chasers.wehappy.user.service.IUserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户缓存数据操作实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-10-26
 */
@Service
public class UserCacheServiceImpl implements IUserCacheService {

    private final IRedisService redisService;

    @Value("${redis.database}")
    private String redisDatabase;

    @Value("${redis.separator}")
    private String redisKeySeparator;

    @Value("${redis.userInfo.key}")
    private String userInfoKey;

    @Value("${redis.userInfo.expire}")
    private long userInfoExpire;

    @Autowired
    public UserCacheServiceImpl(IRedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void removeUser(Long userId) {
        redisService.hDel(redisDatabase + redisKeySeparator + userInfoKey, userId);
    }

    @Override
    public User getUser(Long userId) {
        return (User) redisService.hGet(redisDatabase + redisKeySeparator + userInfoKey, userId.toString());
    }

    @Override
    public void setUser(User user) {
        redisService.hSet(redisDatabase + redisKeySeparator + userInfoKey, user.getId().toString(), user, userInfoExpire);
    }
}

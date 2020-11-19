package cn.chasers.wehappy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * 这里就先写死了
 *
 * @author lollipop
 * @date 2020/11/18 19:34:08
 */
@Slf4j
public class RedLockUtil {

    private static final Config CONFIG1 = new Config();
    private static final RedissonClient REDISSON_CLIENT;

    static {
        CONFIG1.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
        REDISSON_CLIENT = Redisson.create(CONFIG1);
    }

    /**
     * 通过 Redisson 获取 RedLock
     *
     * @param waitTime  最长等待时间
     * @param leaseTime 最长持有时间
     * @return 获取成功或失败
     */
    public static RedissonRedLock tryLock(long waitTime, long leaseTime, String resourceName) throws Exception {
        RLock lock1 = REDISSON_CLIENT.getLock(resourceName);

        RedissonRedLock redLock = new RedissonRedLock(lock1);
        return redLock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS) ? redLock : null;
    }
}

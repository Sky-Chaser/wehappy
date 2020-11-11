package cn.chasers.wehappy.common.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法生成分布式id的配置类
 *
 * @author lollipop
 */
@Component
@Slf4j
public class SnowflakeConfig {
    /**
     * 终端Id
     */
    private long workerId;

    /**
     * 数据中心Id
     */
    private long datacenterId;

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) & (1 << 5 - 1);
        datacenterId = 1 & (1 << 5 - 1);
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        log.info("Current machine workId:{}", workerId);
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }
}
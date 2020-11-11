package cn.chasers.wehappy.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Netty配置类
 *
 * @author lollipop
 * @date 2020/11/4
 */
@Data
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
    private int bossThreadNum;
    private int workerThreadNum;
    private int businessCorePoolSize;
    private int businessMaxPoolSize;
    private int businessMaxKeepAliveTime;
}

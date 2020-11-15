package cn.chasers.wehappy.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liamcoder
 * @date 2020/11/12 12:40 上午
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
@EnableDiscoveryClient
@EnableFeignClients
public class GroupApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupApplication.class, args);
    }
}

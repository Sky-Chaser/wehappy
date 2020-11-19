package cn.chasers.wehappy.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lollipop
 * @date 2020/11/12
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}

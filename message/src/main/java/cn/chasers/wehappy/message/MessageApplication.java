package cn.chasers.wehappy.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lollipop
 * @date 2020/11/17 10:04:27
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
@EnableDiscoveryClient
@FeignClient
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}

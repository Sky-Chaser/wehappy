package cn.chasers.wehappy.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author lollipop
 * @date 2020/11/4
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
@EnableDiscoveryClient
@EnableFeignClients
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}

package cn.chasers.wehappy.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 发邮件短信服务
 *
 * @author lollipop
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }
}

package cn.chasers.wehappy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lollipop
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}

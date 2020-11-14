package cn.chasers.wehappy.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liamcoder
 * @date 2020/11/12 12:40 上午
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
public class GroupApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupApplication.class, args);
    }
}

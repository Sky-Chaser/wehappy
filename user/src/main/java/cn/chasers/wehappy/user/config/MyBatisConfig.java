package cn.chasers.wehappy.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 * @author lollipop
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"cn.chasers.wehappy.user.mapper"})
public class MyBatisConfig {
}
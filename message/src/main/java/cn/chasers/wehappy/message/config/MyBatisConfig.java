package cn.chasers.wehappy.message.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 *
 * @author lollipop
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"cn.chasers.wehappy.message.mapper"})
public class MyBatisConfig {
}
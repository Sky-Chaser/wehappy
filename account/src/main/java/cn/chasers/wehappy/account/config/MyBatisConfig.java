package cn.chasers.wehappy.account.config;

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
@MapperScan({"cn.chasers.wehappy.account.mapper"})
public class MyBatisConfig {
}
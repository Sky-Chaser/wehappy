package cn.chasers.wehappy.group.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 * @author liamcoder
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"cn.chasers.wehappy.group.mapper"})
public class MyBatisConfig {
}
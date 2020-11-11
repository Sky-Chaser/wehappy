package cn.chasers.wehappy.media.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 * @author liamcoder
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"cn.chasers.wehappy.media.mapper"})
public class MyBatisConfig {
}
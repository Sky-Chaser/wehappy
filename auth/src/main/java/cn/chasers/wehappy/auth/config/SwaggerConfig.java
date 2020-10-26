package cn.chasers.wehappy.auth.config;

import cn.chasers.wehappy.common.config.BaseSwaggerConfig;
import cn.chasers.wehappy.common.domain.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * @author lollipop
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("cn.chasers.wehappy.auth.controller")
                .title("认证中心")
                .description("认证中心相关接口文档")
                .contactName("wehappy")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}

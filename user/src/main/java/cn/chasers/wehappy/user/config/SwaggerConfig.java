package cn.chasers.wehappy.user.config;

import cn.chasers.wehappy.common.config.BaseSwaggerConfig;
import cn.chasers.wehappy.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * @author lollipop
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("cn.chasers.wehappy.user.controller")
                .title("用户服务")
                .description("用户服务相关接口文档")
                .contactName("lollipop")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}

package cn.chasers.wehappy.media.config;

import cn.chasers.wehappy.common.config.BaseSwaggerConfig;
import cn.chasers.wehappy.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * @author liamcoder
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("cn.chasers.wehappy.media.controller")
                .title("媒体服务")
                .description("媒体服务相关接口文档")
                .contactName("liamcoder")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}

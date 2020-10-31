package cn.chasers.wehappy.auth.config;

import cn.chasers.wehappy.common.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 *
 * @author lollipop
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenEnhancer jwtTokenEnhancer;

    @Value("${jwt.keyPair.password}")
    private String password;

    @Value("${jwt.keyPair.alias}")
    private String alias;

    @Value("${jwt.keyPair.filename}")
    private String filename;

    @Value("${oauth2.secret}")
    private String secret;


    @Autowired
    public Oauth2ServerConfig(PasswordEncoder passwordEncoder, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenEnhancer jwtTokenEnhancer) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(AuthConstant.ADMIN_CLIENT_ID)
                .secret(passwordEncoder.encode(secret))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                // access_token 的有效期为24小时
                .accessTokenValiditySeconds(3600 * 24)
                // refresh_token 的有效期为7天
                .refreshTokenValiditySeconds(3600 * 24 * 7)
                .and()
                .withClient(AuthConstant.PORTAL_CLIENT_ID)
                .secret(passwordEncoder.encode(secret))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                // access_token 的有效期为24小时
                .accessTokenValiditySeconds(3600 * 24)
                // refresh_token 的有效期为7天
                .refreshTokenValiditySeconds(3600 * 24 * 7);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        //配置JWT的内容增强器
        enhancerChain.setTokenEnhancers(delegates);
        endpoints.authenticationManager(authenticationManager)
                //配置加载用户信息的服务
                .userDetailsService(userDetailsService)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下获取RSA秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(filename), password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias, password.toCharArray());
    }

}

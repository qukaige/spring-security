package com.imooc.security.core.social;

import com.imooc.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @Title: SocialConfig
 * @ProjectName spring-security-main
 * @date 2020/12/713:57
 */
@Configuration
@EnableSocial
@Slf4j
public class SocialConfig extends SocialConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;
    // 数据源
    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    /**
     * 默认使用InMemoryUsersConnectionRepository类 , Primary 候选bean中优先使用,Bean 添加这两个注解
     * @param connectionFactoryLocator 作用就是查找ConnectionFactory
     * @return
     */
    @Primary
    @Bean
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        // 配置数据库前缀
//        repository.setTablePrefix("imooc_");
        // 如果是首次登录自动注册
        if(connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 配置自定义路径  默认是auth
     * 需要把SpringSocialConfigurer 加入到过滤器链上
     * @return
     */
    @Bean
    public SpringSocialConfigurer imoocSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        log.info(filterProcessesUrl);
        ImoocSpringSocialConfigurer imoocSpringSocialConfigurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
        // 登录成功 回调的注册url
        imoocSpringSocialConfigurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return imoocSpringSocialConfigurer;
//        return new SpringSocialConfigurer();
    }

    /**
     * 在注册过程中拿到springSocial 的信息,注册完成把信息在传给springSocial
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }
}

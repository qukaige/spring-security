package com.imooc.security.core.social;

import com.imooc.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
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
    @Autowired
    private DataSource dataSource;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }

    /**
     * 配置自定义路径  默认是auth
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer imoocSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        log.info(filterProcessesUrl);
        ImoocSpringSocialConfigurer imoocSpringSocialConfigurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
        imoocSpringSocialConfigurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return imoocSpringSocialConfigurer;
//        return new SpringSocialConfigurer();
    }
}

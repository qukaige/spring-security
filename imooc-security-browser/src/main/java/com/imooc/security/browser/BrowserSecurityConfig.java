package com.imooc.security.browser;

import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.SmsCodeFilter;
import com.imooc.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler imoocAuthenctiationFailureHandler;


    @Autowired
    private SecurityProperties securityProperties;
//    PersistentTokenRepository

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService; // MyUserDetailsService

    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;
    /**
     * 记住我功能
     * 1. 创建PersistentTokenRepository
     * 2. 设置过期时间
     * 3. 获取UserDetailsService 用户登录信息
     * 4. 配置rememberMe 生效
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // 配置的dataSource
//        jdbcTokenRepository.setCreateTableOnStartup(true); // 自动创建存放记住我的表,如果存在会报错
        return jdbcTokenRepository;
    }

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 图片验证码过滤器
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenctiationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        // 验证码过滤器
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenctiationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();

        // 添加一个图片验证filter, 在UsernamePasswordAuthenticationFilter之前执行
        http
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(imoocSocialSecurityConfig) // 添加过滤器SocialAuthenticationFilter
                .and()
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // .httpBasic() // 默认方式
                .formLogin() // 设置认证的登录方式 表单方式
                .loginPage("/authentication/require") // 自定义登录页面
                .loginProcessingUrl("/authentication/form") // 自定义表单提交的url, 默认是login
                .successHandler(imoocAuthenticationSuccessHandler) // 不适用默认的认证成功处理器
                .failureHandler(imoocAuthenctiationFailureHandler) // 登录失败处理器
//                .failureForwardUrl("/authentication/require")
//                .failureUrl("/authentication/require")
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                // rememberME 有效期
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .authorizeRequests() // 需要授权
                // 当匹配到这个页面时,不需要授权
                .antMatchers(securityProperties.getBrowser().getSignUpUrl(),
                        "/authentication/require",
                        "/qqLogin/*",
                        "/auth/*",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*","/user/regist","/user/me").permitAll()
                .anyRequest() // 所有请求
                .authenticated()
                .and()  // 关闭csrf
                .csrf()
                .disable();

    }
}

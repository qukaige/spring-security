package com.imooc.security.core.social.qq.connet;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @Title: QQServiceProvider
 * @ProjectName spring-security-main
 * @date 2020/12/711:01
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;
    // 1.导向认证服务器rul
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    // 4.用户同意授权,返回授权码去申请令牌的url
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
    public QQServiceProvider(String appId, String appSecret) {
//        super(oauth2Operations);
        // OAuth2Template 用系统默认的
//        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        // 默认Template不能满足使用
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }

}

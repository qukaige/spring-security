package com.imooc.security.core.social.qq.connet;
import com.imooc.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * 根据　QQAdapter 和 QQServiceProvider 可以构建QQConnectionFactory
     * @param providerId 服务商Id
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}

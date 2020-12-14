package com.imooc.security.core.social.qq.connet;
import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
/**
 * 服务商和第三方应用之间做适配的,适配的类型就是QQ
 */
public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 测试方法
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }
    /**
     * 主要方法, qq api的信息设置到connectionValues
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();
        values.setDisplayName(userInfo.getNickname());
        // 头像
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        // 个人主页
        values.setProfileUrl(null);
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }
    @Override
    public void updateStatus(QQ api, String message) {

    }
}

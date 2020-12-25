package com.imooc.security.core.social.weixin.api;

/**
 * 微信API调用接口
 */
public interface Weixin {

	/**
	 * 获取用户信息
	 * @param openId
	 * @return
	 */
	WeixinUserInfo getUserInfo(String openId);
	
}

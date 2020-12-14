package com.imooc.security.core.validate.code.sms;
public interface SmsCodeSender {
	/**
	 * 手机 验证码
	 * @param mobile
	 * @param code
	 */
	void send(String mobile, String code);

}

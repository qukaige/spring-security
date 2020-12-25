package com.imooc.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.core.properties.LoginResponseType;
import com.imooc.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("imoocAuthenticationSuccessHandler")
//public class ImoocAuthenticationSuccessHandler implements AuthenticationSuccessHandler { // json方式处理
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler { // 自定义返回方式

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * @param request
	 * @param response
	 * @param authentication 存放认证信息
	 * @throws IOException
	 * @throws ServletException
	 */
	/*@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("登录成功");
		response.setContentType("application/json;charset=UTF-8");
		// 返回对象信息
		response.getWriter().write(objectMapper.writeValueAsString(authentication));
	}*/


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		logger.info("登录成功");

		// 返回json
		if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(authentication));
		} else {
			// 父类的方法就是跳转网页
			super.onAuthenticationSuccess(request, response, authentication);
		}

	}
}

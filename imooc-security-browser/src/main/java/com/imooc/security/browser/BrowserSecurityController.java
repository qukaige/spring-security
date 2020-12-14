package com.imooc.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.security.browser.support.SimpleResponse;
import com.imooc.security.core.properties.SecurityProperties;
@RestController
public class BrowserSecurityController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 请求的缓存类
	private RequestCache requestCache = new HttpSessionRequestCache();
	// spring 提供的跳转类
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private SecurityProperties securityProperties;


	/**
	 * 当需要身份认证时，跳转到这里
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 之前缓存的请求
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();  // 引发跳转的请求url
			logger.info("引发跳转的请求是:" + targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) { // 如果url包含html跳转
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
			}
		}

		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
	}
}

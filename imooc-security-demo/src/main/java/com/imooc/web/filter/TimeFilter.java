package com.imooc.web.filter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//@Component  // 增加一个注解即可使用,不需要其他配置,会拦截所有请求
public class TimeFilter implements Filter {
	@Override
	public void destroy() {
		System.out.println("time filter destroy");
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("time filter start");
		long start = new Date().getTime();
		chain.doFilter(request, response); // 继续执行程序,不调用doFilter 不会往下执行
		System.out.println("time filter 耗时:"+ (new Date().getTime() - start));
		System.out.println("time filter finish");
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 程序启动会加载
		System.out.println("time filter init");
	}
}

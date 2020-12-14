package com.imooc.web.aspect;
import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
//@Aspect
//@Component
public class TimeAspect {
	// 切入具体的方法,拦截UserController的所有方法
	@Around("execution(* com.imooc.web.controller.UserController.*(..))")
	public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("time aspect start");
		Object[] args = pjp.getArgs(); // 可以获取方法参数
		for (Object arg : args) {
			System.out.println("arg is "+arg);
		}
		long start = new Date().getTime();
		Object object = pjp.proceed(); // 继续往下执行Controller
		System.out.println("time aspect 耗时:"+ (new Date().getTime() - start));
		System.out.println("time aspect end");
		return object;
	}
}

package com.mccottage.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

/*
 * this programe writted by micro
 */
@Component
public class MusicAdvisor implements MethodBeforeAdvice, AfterReturningAdvice{

	// 对应函数执行之后的返回
	@Override
	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		System.out.println("music play over...");
	}

	// 对应函数执行之前
	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("music play start...");
	}

}

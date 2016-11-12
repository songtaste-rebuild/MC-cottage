package com.mccottage.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

/*
 * this programe writted by micro
 */

public class MusicAdvisor{

	// 对应函数执行之后的返回
	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		System.out.println("music play over...");
	}

	// 对应函数执行之前
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("music play start...");
	}

}

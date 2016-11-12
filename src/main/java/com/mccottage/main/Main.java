package com.mccottage.main;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mccottage.aop.Media;

/*
 * this programe writted by micro
 */
public class Main {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:*.xml");
		// aop方法1
		/*Media media = (Media)context.getBean("aopProxy");
		media.play();*/
		
		// aop方法2
		Media media = (Media)context.getBean("musicPlay");
		media.play();
		System.out.println("main end");
	}
}

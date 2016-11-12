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
		Media media = (Media)context.getBean("aopProxy");
		media.play();
	}
}

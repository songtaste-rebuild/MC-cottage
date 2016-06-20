package com.songtaste.springTest;

import org.springframework.context.support.ClassPathXmlApplicationContext;
/*
 * this programe writted by micro
 */


public abstract class ApplicationTest {
	
	public static ClassPathXmlApplicationContext context = null;
	
	public void loadContext() {
		
	}
	
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath*:ApplicationContext.xml");
	}
}

package com.mccottage.exrt;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;


public class ApplicationListener extends ContextLoaderListener{
	
	private static final Logger log = Logger.getLogger(ApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// test get Log dir
		String webAppRootKey = event.getServletContext().getRealPath("/");
		System.out.println("getWebAppRootKey=" + webAppRootKey);
		System.out.println(System.getProperty("webapp.root"));
		log.debug("getWebAppRootKey=" + webAppRootKey);
	}

}

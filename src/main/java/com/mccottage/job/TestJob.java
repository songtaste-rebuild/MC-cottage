package com.mccottage.job;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TestJob {
	@Scheduled(cron = "*/10 * * * * ?")
	public void execute() {
		System.out.println("start job...");
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 100, TimeUnit.MINUTES, new LinkedBlockingDeque());
		executor.execute(new Runnable() {
			
			public void run() {
				for (int i = 0;i < 100;i ++) {
					System.out.println("star to this thread : " + Thread.currentThread().getName() + " i = " + i);
				}
			}
		});
		System.out.println("main end job");
	}
}

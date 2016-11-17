package com.mccottage.aop;

import org.springframework.stereotype.Component;

/*
 * this programe writted by micro
 */
@Component
public class MusicPlay implements Media{

	public void play() {
		System.out.println("music ...");
	}

}

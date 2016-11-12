package com.mccottage.aop;

import org.springframework.stereotype.Component;

/*
 * this programe writted by micro
 */
@Component
public class MusicPlay implements Media{

	@Override
	public void play() {
		System.out.println("music ...");
	}

}

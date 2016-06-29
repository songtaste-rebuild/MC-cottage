package com.mccottage.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.mccottage.service.MusicService;

/**
 * 
 * @author mapc BaseController.java 2016 6 20
 * @Description 控制层基础支持类
 */
public abstract class BaseController {

	@Autowired
	protected MusicService musicService;

}

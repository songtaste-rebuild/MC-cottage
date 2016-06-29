package com.mccottage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mccottage.base.BaseController;
import com.mccottage.entity.Music;
import com.mccottage.entity.MusicExample;
import com.mccottage.utils.Result;

@Controller
public class MusicController extends BaseController {
	
	private static final Logger log = Logger.getLogger(MusicController.class);
	@RequestMapping("list.do")
	public String getMusicList(HttpServletRequest request) {
		Result<List<Music>> result = musicService.searchMusicBySelective(new MusicExample());
		return "album";
	}
}

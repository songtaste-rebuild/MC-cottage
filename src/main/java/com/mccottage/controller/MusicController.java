package com.mccottage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mccottage.base.BaseController;
import com.mccottage.entity.Music;
import com.mccottage.entity.MusicExample;
import com.mccottage.entity.MusicExample.Criteria;
import com.mccottage.entity.User;
import com.mccottage.entity.UserExample;
import com.mccottage.utils.Result;

@Controller
public class MusicController extends BaseController {
	
	private static final Logger log = Logger.getLogger(MusicController.class);
	@RequestMapping("list.do")
	public @ResponseBody String getMusicList(@RequestParam("musicName") String musicName, @RequestParam("creatorName") String creatorName) {
		log.debug("params musicName : " + musicName + ", creatorName : " + creatorName);
		MusicExample musicExample = new MusicExample();
		Criteria criteria = musicExample.createCriteria();
		if (StringUtils.isNotBlank(musicName)) {
			criteria.andMusicNameLike("%" + musicName + "%");
		}
		if (StringUtils.isNotBlank(creatorName)) {
			List<Long> creatorIdList = null;
			UserExample userExample = new UserExample();
			userExample.or().andUserNameLike("%" + creatorName + "%");
			List<User> userList = userService.searchUserByExample(userExample);
			if (userList != null && userList.size() > 0) {
				creatorIdList = new ArrayList<Long>();
				for (User user : userList) {
					creatorIdList.add(user.getId());
				}
			}
			if (creatorIdList != null) 
				criteria.andCreatorIn(creatorIdList);
		}
		Result<List<Music>> result = musicService.searchMusicBySelective(musicExample);
		return parseResultToJSON(result);
	}
	
	// 音乐详情
	@RequestMapping("/music/detail.do")
	public ModelAndView musicDetail(@RequestParam("musicId") Long musicId) {
		log.debug("musicDetail , musicId =  " + musicId);
		Result<Music> result = musicService.selectMusicById(musicId);
		ModelAndView mav = new ModelAndView();
		if (result.isSuccess) {
			mav.addObject("music", result.getContext());
			mav.setViewName("musicDetail");
		} else {
			log.error("error message : " + result.getErrorMsg());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/music/uploadMusic.do")
	public String uploadMusic(@RequestParam("musicFile") MultipartFile file ,@RequestParam("fileName") String fileName) {
		BufferedOutputStream stream = null;
		try {
			// 读出文件
			byte[] bytes = file.getBytes();
			stream = new BufferedOutputStream(new FileOutputStream(new File("C:/" + fileName + "-upload.txt")));
			stream.write(bytes);
			stream.close();
		} catch (Exception ex) {
			log.error("uploadMusic exception, error msg : " + ex.getMessage());
		}
		return "upload";
	}
	
}

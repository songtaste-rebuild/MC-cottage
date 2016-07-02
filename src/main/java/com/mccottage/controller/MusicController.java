package com.mccottage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}

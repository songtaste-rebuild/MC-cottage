package com.mccottage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mccottage.base.BaseController;
import com.mccottage.entity.User;
import com.mccottage.utils.MD5Utils;
import com.mccottage.utils.Result;
import com.mccottage.utils.ResultConstant;

public class UserController extends BaseController {
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
	
	@RequestMapping("login.do")
	@ResponseBody
	public Map<String, Object> login(@RequestBody User user, HttpSession session) {
		log.debug("login ,userInfo " + user);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			user.setPassword(MD5Utils.getMD5Format(user.getPassword()));
			Result<User> result = userService.login(user);
			if (result.getContext() != null) {
				resultMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.SUCCESS.isSuccess());
				session.setAttribute("user", user);
			}
			else {
				resultMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.ERROR.isSuccess());
			}
		} catch (Exception ex) {
			log.error("login error");
		}
		return resultMap;
	}
	
}

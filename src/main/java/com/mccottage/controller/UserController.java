package com.mccottage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mccottage.base.BaseController;
import com.mccottage.entity.User;
import com.mccottage.utils.MD5Utils;
import com.mccottage.utils.Result;
import com.mccottage.utils.ResultConstant;
@Controller
public class UserController extends BaseController {
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
	
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public String login(@RequestBody User user, HttpSession session) {
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
				resultMap.put(ResultConstant.ERROR_MSG, "µÇÂ½Ãû»òÔòÃÜÂë´íÎó");
			}
		} catch (Exception ex) {
			log.error("login error");
		}
		return JSONObject.fromObject(resultMap).toString();
	}
	
	// update password
	@RequestMapping(value = "updatePassword.do", method = RequestMethod.POST)
	public @ResponseBody String updatePassword(@RequestParam("password") String password, HttpSession session) {
		HashMap<String, Object> reslutMap = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			reslutMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.ERROR.isSuccess());
			reslutMap.put(ResultConstant.ERROR_MSG, "µ±Ç°Î´µÇÂ½");
			return JSONObject.fromObject(reslutMap).toString();
		} else {
			try {
				// MD5¼ÓÃÜ
				user.setPassword(MD5Utils.getMD5Format(password));
				userService.updateUser(user);
			} catch (Exception ex) {
				log.error("update password error : msg : " + ex.getMessage());
			}
		}
		reslutMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.SUCCESS.isSuccess());
		return JSONObject.fromObject(reslutMap).toString();
			
	}
	
	// logout
	@RequestMapping(value = "logout.do" , method = RequestMethod.POST)
	public @ResponseBody String logout(HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			session.invalidate();
			resultMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.SUCCESS.isSuccess());
		} catch (Exception ex) {
			log.error("logout error : msg :" + ex.getMessage());
			resultMap.put(ResultConstant.ERROR_MSG, "µÇ³öÊ§°Ü");
		}
		return JSONObject.fromObject(resultMap).toString();
	}
	
}

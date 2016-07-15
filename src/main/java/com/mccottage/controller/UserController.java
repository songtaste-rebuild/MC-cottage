package com.mccottage.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mccottage.base.BaseController;
import com.mccottage.entity.User;
import com.mccottage.entity.UserGroup;
import com.mccottage.entity.UserGroupExample;
import com.mccottage.utils.MD5Utils;
import com.mccottage.utils.Result;

@Controller
public class UserController extends BaseController {

	private static final Logger log = Logger.getLogger(UserController.class);

	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public String login(@RequestBody User user, HttpSession session) {
		log.debug("login ,userInfo " + user);
		Result<User> result = null;
		try {
			user.setPassword(MD5Utils.getMD5Format(user.getPassword()));
			result = userService.login(user);
			if (result.isSuccess) {
				session.setAttribute("user", user);
			}
		} catch (Exception ex) {
			log.error("login error");
			result = Result.getError("异常");
		}
		return parseResultToJSON(result);
	}

	// update password
	@RequestMapping(value = "updatePassword.do", method = RequestMethod.POST)
	public @ResponseBody String updatePassword(@RequestParam("password") String password, HttpSession session) {
		Result<Object> result = null;
		try {
			User user = (User) session.getAttribute("user");
			// MD5加密
			user.setPassword(MD5Utils.getMD5Format(password));
			result = userService.updateUser(user);
		} catch (Exception ex) {
			log.error("update password error : msg : " + ex.getMessage());
			result = Result.getError("修改异常");
		}
		return parseResultToJSON(result);

	}

	// logout
	@RequestMapping(value = "logout.do", method = RequestMethod.POST)
	public @ResponseBody String logout(HttpSession session) {
		Result<Object> result = new Result<Object>();
		try {
			session.invalidate();
			result.setSuccess(true);
		} catch (Exception ex) {
			log.error("logout error : msg :" + ex.getMessage());
			result.setSuccess(false);
		}
		return parseResultToJSON(result);
	}

	// 用户组详情
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping("user/{groupId}/groupDetail.do")
	public ModelAndView groupDetail(@PathVariable("groupId") Long groupId) {
		log.debug("groupDetail : groupId = " + groupId);
		try {
			UserGroup userGroup = userGroupService.selectUserGroupById(groupId);
			ModelAndView mav = new ModelAndView();
			mav.addObject("userGroup", userGroup);
			// �û�������ҳ��
			mav.setViewName("groupDetail");
			return mav;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
			return new ModelAndView("error");
		}
	}
	
	// 用户组列表
	/**
	 * 
	 * @param groupName
	 * @return
	 */
	@RequestMapping("user/{groupName}/grouList.do")
	public ModelAndView groupList(@PathVariable("groupName") String groupName) {
		log.debug("groupList : search limit groupName = " + groupName);
		ModelAndView mav = new ModelAndView();
		try {
			UserGroupExample userGroupExample = new UserGroupExample();
			userGroupExample.or().andGroupNameEqualTo("%" + groupName + "%");
			List<UserGroup> userGroupList = userGroupService.searchUserGroup(userGroupExample);
			// �û����б���ͼ
			mav.setViewName("groupList");
			mav.addObject("groupList", userGroupList);
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
}

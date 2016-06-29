package com.mccottage.serviceImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mccottage.base.BaseService;
import com.mccottage.entity.User;
import com.mccottage.entity.UserExample;
import com.mccottage.service.UserService;
import com.mccottage.utils.Result;

@Service
public class UserServiceImpl extends BaseService implements UserService{
	
	private static final Logger log = Logger.getLogger(UserService.class);

	@SuppressWarnings("rawtypes")
	public Result registerUser(User user) {
		log.debug("register user : " + user);
		try {
			userMapper.insert(user);
		} catch (Exception ex) {
			log.error("register user error , message " + ex.getMessage());
			return new Result().getError("register error");
		}
		return new Result().setSuccess(true);
	}

	public Result<User> login(User user) {
		log.debug("user : " + user.getUserName() + "login...");
		Result<User> result = new Result<User>();
		try {
			UserExample userExample = new UserExample();
			userExample.or().andUserNameEqualTo(user.getUserName());
			userExample.or().andPasswordEqualTo(user.getPassword());
			List<User> userList = userMapper.selectByExample(userExample);
			if (userList != null && userList.size() > 0) {
				user = userList.get(0);
				result.setContext(user);
			}
		} catch (Exception ex) {
			log.error("login error : login Info : " + user);
		}
		return result;
	}

}

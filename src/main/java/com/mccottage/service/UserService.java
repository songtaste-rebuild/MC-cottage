package com.mccottage.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.mccottage.entity.User;
import com.mccottage.entity.UserExample;
import com.mccottage.utils.Result;
/**
 * 
 * @author mapc
 * UserService.java
 * 2016年7月2日
 * @Description
 */
public interface UserService {
	
	static final Logger log = Logger.getLogger(UserService.class);
	
	// register user
	@SuppressWarnings("rawtypes")
	public Result registerUser(User user);
	
	// login user
	public Result<User> login(User user);
	
	// update password
	public Result<Object> updateUser(User user);
	
	// user jion to useGroup
	public boolean addUserGroup(Long userId, List<Long> userGroupIdList);
	
	// remove user from userGroup
	public boolean removeUserFromGroup(Long userId, List<Long> userGroupIdList);
	
	// search user
	public List<User> searchUserByExample(UserExample userExample);
	
}

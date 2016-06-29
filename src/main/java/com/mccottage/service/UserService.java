package com.mccottage.service;

import com.mccottage.entity.User;
import com.mccottage.utils.Result;

public interface UserService {
	
	// register user
	public Result registerUser(User user);
	
	// login user
	public Result<User> login(User user);
}

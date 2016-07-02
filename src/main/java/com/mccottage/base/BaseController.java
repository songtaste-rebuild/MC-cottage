package com.mccottage.base;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.mccottage.service.MusicService;
import com.mccottage.service.UserGroupService;
import com.mccottage.service.UserService;
import com.mccottage.utils.Result;
import com.mccottage.utils.ResultConstant;

/**
 * 
 * @author mapc BaseController.java 2016 6 20
 * @Description 控制层基础支持类
 */
public abstract class BaseController {

	@Autowired
	protected MusicService musicService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	protected <T> String parseResultToJSON(Result<T> result) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (result.isSuccess) {
			resultMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.SUCCESS.isSuccess());
			if (result.getContext() != null)
				resultMap.put("data", result.getContext());
		} else {
			resultMap.put(ResultConstant.IS_SUCCESS, ResultConstant.Result.ERROR.isSuccess());
			resultMap.put(ResultConstant.ERROR_MSG, result.getErrorMsg());
		}
		return JSONObject.fromObject(resultMap).toString();
	}

}

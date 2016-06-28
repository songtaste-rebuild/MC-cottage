package com.songtaste.utils;

/**
 * 
 * @author mapc
 * Result.java
 * 2016年6月23日
 * @Description 返回结果封装，若失败，需要errorMsg
 */
public class Result {

	public Object context;

	public boolean isSuccess;

	public String errorMsg;

	public static Result getError(String errorMsg) {
		return new Result().getError(errorMsg);
	}

	public Object getContext() {
		return context;
	}

	public Result setContext(Object context) {
		this.context = context;
		return this;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public Result setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
		return this;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public Result setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

}

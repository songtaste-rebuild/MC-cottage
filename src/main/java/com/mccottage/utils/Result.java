package com.mccottage.utils;

/**
 * 
 * @author mapc
 * Result.java
 * 2016年6月23日
 * @Description 返回结果封装，若失败，需要errorMsg
 */
public class Result<T> {

	public T context;

	public boolean isSuccess;

	public String errorMsg;

	public static <T> Result<T> getError(String errorMsg) {
		return new Result<T>().getError(errorMsg);
	}

	public T getContext() {
		return context;
	}

	public Result<T> setContext(T context) {
		this.context = context;
		return this;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public Result<T> setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
		return this;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public Result<T> setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

}

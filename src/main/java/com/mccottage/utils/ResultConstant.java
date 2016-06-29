package com.mccottage.utils;

public class ResultConstant {

	public static final String IS_SUCCESS = "isSuccess";
	
	public static final String ERROR_MSG = "errorMsg";

	public enum Result {
		SUCCESS(0, true), ERROR(1, false);
		int staus;
		boolean isSuccess;

		Result(int status, boolean isSuccess) {
			this.staus = status;
			this.isSuccess = isSuccess;
		}

		public int getStaus() {
			return staus;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

	}
}

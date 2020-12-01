package com.spinwash.admin.exception;

public class WashNowHttpException extends Exception{
	
	private static final long serialVersionUID = 1L;
	protected int httpStatusCode;
	protected String message;
	
	public WashNowHttpException(int httpStatusCode, String message) {
		super(message);
		this.message = message;
		this.httpStatusCode = httpStatusCode;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getMessage() {
		return message;
	}

}

package com.spinwash.admin.vo;

public class TResponse< T > {
	
	private T responseContent;
	private boolean hasError=false;
	private int errorCode;
	
	public T getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(T responseContent) {
		this.responseContent = responseContent;
	}
	public boolean isHasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}

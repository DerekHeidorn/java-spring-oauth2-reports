package com.example.demo.web.dtos;

public final class RestApiFieldError {


	private String field;
	private String errorMsg;
	
	public RestApiFieldError(String field, String errorMsg) {
		super();
		this.field = field;
		this.errorMsg = errorMsg;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "RestFieldError [field=" + field + ", errorMsg=" + errorMsg
				+ "]";
	}

	
	


}

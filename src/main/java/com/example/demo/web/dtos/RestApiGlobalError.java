package com.example.demo.web.dtos;

public final class RestApiGlobalError {

	private String errorMsg;
	
	public RestApiGlobalError(String errorMsg) {
		super();

		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	@Override
	public String toString() {
		return "RestApiGlobalError [errorMsg=" + errorMsg + "]";
	}



	
	


}

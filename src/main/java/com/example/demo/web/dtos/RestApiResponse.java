package com.example.demo.web.dtos;

import java.util.ArrayList;
import java.util.List;

public class RestApiResponse {

	private List<String> globalInfoMsgs = new ArrayList<String>();
	private List<String> globalWarnings = new ArrayList<String>();
	
	private List<RestApiGlobalError> globalErrors = new ArrayList<RestApiGlobalError>();	
	private List<RestApiFieldError> fieldErrors = new ArrayList<RestApiFieldError>();	
	
	private Object data;
	
	public RestApiResponse() {
		super();
	}	
	
	public RestApiResponse(RestApiGlobalError globalError) {
		super();
		this.getGlobalErrors().add(globalError);
	}	
	
	public boolean hasErrors() {
		return globalErrors.size() > 0 || fieldErrors.size() > 0;
	}


	public List<RestApiFieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<RestApiFieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public List<RestApiGlobalError> getGlobalErrors() {
		return globalErrors;
	}

	public void setGlobalErrors(List<RestApiGlobalError> globalErrors) {
		this.globalErrors = globalErrors;
	}
	
	public void addGlobalError(String errorMessage) {
		getGlobalErrors().add(new RestApiGlobalError(errorMessage));
	}

	

	public List<String> getGlobalWarnings() {
		return globalWarnings;
	}

	public void setGlobalWarnings(List<String> globalWarnings) {
		this.globalWarnings = globalWarnings;
	}
	
	public void addGlobalWarning(String warningMessage) {
		getGlobalWarnings().add(warningMessage);
	}	

	public List<String> getGlobalInfoMsgs() {
		return globalInfoMsgs;
	}

	public void setGlobalInfoMsgs(List<String> globalInfoMsgs) {
		this.globalInfoMsgs = globalInfoMsgs;
	}

	public void addGlobalInfoMsg(String infoMessage) {
		getGlobalInfoMsgs().add(infoMessage);
	}	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	
}

package com.example.demo.web.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestApiResponse {
	
	@JsonProperty("global_success_msgs")
	private List<String> globalSuccessMsgs = new ArrayList<String>();

	@JsonProperty("global_info_msgs")
	private List<String> globalInfoMsgs = new ArrayList<String>();
	
	@JsonProperty("global_warning_msgs")
	private List<String> globalWarnings = new ArrayList<String>();
	
	@JsonProperty("global_error_msgs")
	private List<String> globalErrors = new ArrayList<String>();
	
	@JsonProperty("field_error_msgs")
	private List<RestApiFieldError> fieldErrors = new ArrayList<RestApiFieldError>();	
	
	private Object data;
	
	public RestApiResponse() {
		super();
	}	
	
	public RestApiResponse(String globalErrorMsg) {
		super();
		this.getGlobalErrors().add(globalErrorMsg);
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

	public void addGlobalError(String errorMessage) {
		getGlobalErrors().add(errorMessage);
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

	public List<String> getGlobalErrors() {
		return globalErrors;
	}

	public void setGlobalErrors(List<String> globalErrors) {
		this.globalErrors = globalErrors;
	}

	public List<String> getGlobalSuccessMsgs() {
		return globalSuccessMsgs;
	}

	public void setGlobalSuccessMsgs(List<String> globalSuccessMsgs) {
		this.globalSuccessMsgs = globalSuccessMsgs;
	}

	public void addGlobalSuccess(String successMessage) {
		getGlobalSuccessMsgs().add(successMessage);
	}	
	
}

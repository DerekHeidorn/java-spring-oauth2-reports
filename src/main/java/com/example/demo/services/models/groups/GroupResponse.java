package com.example.demo.services.models.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupResponse<T> {
	
	public GroupResponse(T data) {
		this.data = data;
	}
	
	private T data;
	
	public GroupResponse() {
		super();
	}	

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GroupResponse [data=" + data + "]";
	}


	
}

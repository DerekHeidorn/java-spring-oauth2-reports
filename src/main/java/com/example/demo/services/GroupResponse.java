package com.example.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupResponse<T> {

//	private List<String> globalInfoMsgs = new ArrayList<String>();
//	private List<String> globalWarnings = new ArrayList<String>();
	
//    // T stands for "Type"
//    private T t;
//
//    public void set(T t) { this.t = t; }
//    public T get() { return t; }
	
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

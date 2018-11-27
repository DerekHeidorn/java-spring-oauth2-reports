package com.example.demo.services.models.persons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	
    @JsonProperty("user_uuid")
	private String userUuid = null;
    
    @JsonProperty("alias")
    private String alias = null;

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


}

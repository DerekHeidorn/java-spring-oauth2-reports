package com.example.demo.services.models.groups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupMember {
	
    @JsonProperty("alias")
	private String alias = null;
    
    @JsonProperty("user_uuid")
	private String userUuid = null;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}


}

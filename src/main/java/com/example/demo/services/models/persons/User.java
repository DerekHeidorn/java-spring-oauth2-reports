package com.example.demo.services.models.persons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	@JsonProperty("user_uuid")
	private String userUuid = null;

	@JsonProperty("alias")
	private String alias = null;

	@JsonProperty("username")
	private String username = null;
	
	@JsonProperty("first_name")
	private String firstName = null;
	
	@JsonProperty("last_name")
	private String lastName = null;
	
	@JsonProperty("formatted_name")
	private String formattedName = null;
	
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}

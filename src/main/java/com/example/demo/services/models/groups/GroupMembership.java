package com.example.demo.services.models.groups;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupMembership {
	
    @JsonProperty("group")
    private Group group = new Group();
    
    @JsonProperty("active_members")
	private List<GroupMember> activeMembers = new ArrayList<>();
    
    @JsonProperty("active_managers")
	private List<GroupMember> activeManagers = new ArrayList<>();

	public List<GroupMember> getActiveMembers() {
		return activeMembers;
	}

	public void setActiveMembers(List<GroupMember> activeMembers) {
		this.activeMembers = activeMembers;
	}

	public List<GroupMember> getActiveManagers() {
		return activeManagers;
	}

	public void setActiveManagers(List<GroupMember> activeManagers) {
		this.activeManagers = activeManagers;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}



}

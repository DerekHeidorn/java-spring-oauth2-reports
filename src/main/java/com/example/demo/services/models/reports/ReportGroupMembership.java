package com.example.demo.services.models.reports;

import com.example.demo.services.models.groups.GroupMember;
import com.example.demo.services.models.groups.GroupMembership;

public class ReportGroupMembership {

	private String groupUuid = null;

	private String groupName = null;

	private String groupDescription = null;

	private String memberUserUuid = null;
    
	private String memberAlias = null;

	public ReportGroupMembership(GroupMembership groupMembership, GroupMember groupMember) {
		super();
		this.groupUuid = groupMembership.getGroup().getGroupUuid();
		this.groupName = groupMembership.getGroup().getGroupName();
		this.groupDescription = groupMembership.getGroup().getGroupDescription();
		
		this.memberUserUuid = groupMember.getUserUuid();
		this.memberAlias = groupMember.getAlias();
		
		
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getMemberUserUuid() {
		return memberUserUuid;
	}

	public void setMemberUserUuid(String memberUserUuid) {
		this.memberUserUuid = memberUserUuid;
	}

	public String getMemberAlias() {
		return memberAlias;
	}

	public void setMemberAlias(String memberAlias) {
		this.memberAlias = memberAlias;
	}

	@Override
	public String toString() {
		return "ReportGroupMembership [groupUuid=" + groupUuid + ", groupName=" + groupName + ", memberUserUuid="
				+ memberUserUuid + ", memberAlias=" + memberAlias + "]";
	}


    
	

}

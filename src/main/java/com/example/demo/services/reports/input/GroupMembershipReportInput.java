package com.example.demo.services.reports.input;

import java.util.List;

import com.example.demo.services.models.reports.ReportGroupMembership;

public final class GroupMembershipReportInput extends ReportInput {

	public GroupMembershipReportInput(ReportCriteria criteria) {
		super(criteria);
	}
	
	public GroupMembershipReportInput(ReportCriteria criteria, List<ReportGroupMembership> data) {
		super(criteria, data);
	}

}

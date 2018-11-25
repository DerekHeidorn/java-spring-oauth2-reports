package com.example.demo.services.reports.input;

import java.util.List;

import com.example.demo.services.models.groups.Group;

public final class GroupReportInput extends ReportInput {

	public GroupReportInput(ReportCriteria criteria) {
		super(criteria);
	}
	
	public GroupReportInput(ReportCriteria criteria, List<Group> data) {
		super(criteria, data);
	}

}

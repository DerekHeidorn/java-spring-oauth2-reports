package com.example.demo.services.reports.input;

import com.example.demo.services.models.groups.Group;

public final class GroupReportInput extends ReportInput {

	public GroupReportInput(ReportCriteria criteria) {
		super(criteria);
	}
	
	public GroupReportInput(ReportCriteria criteria, Group[] data) {
		super(criteria, data);
	}

	public Group[] getData() {
		return (Group[]) super.getData();
	}


	public void setData(Group[] data) {
		super.setData(data);
	}

}

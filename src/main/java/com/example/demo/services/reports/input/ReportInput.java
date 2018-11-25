package com.example.demo.services.reports.input;

import java.util.Collection;

public class ReportInput  {

	protected ReportCriteria criteria;
	protected Collection<?> data;
	
	public ReportInput() {
	}
	
	public ReportInput(ReportCriteria criteria) {
		this.criteria = criteria;
	}
	
	public ReportInput(ReportCriteria criteria, Collection<?> data) {
		this.criteria = criteria;
		this.data = data;
	}
	
	public Collection<?> getData() {
		return data;
	}


	public ReportCriteria getCriteria() {
		return criteria;
	}

	public void setData(Collection<?> data) {
		this.data = data;
	}
	
}

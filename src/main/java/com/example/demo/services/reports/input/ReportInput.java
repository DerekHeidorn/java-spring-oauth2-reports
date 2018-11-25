package com.example.demo.services.reports.input;

public class ReportInput  {

	private ReportCriteria criteria;
	private Object data;
	
	public ReportInput() {
	}
	
	public ReportInput(ReportCriteria criteria) {
		this.criteria = criteria;
	}
	
	public ReportInput(ReportCriteria criteria, Object data) {
		this.criteria = criteria;
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}


	public ReportCriteria getCriteria() {
		return criteria;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}

package com.example.demo.web.dtos;


public class DtoReportCriteriaResponseWithKey {

	private DtoReportCriteriaRequest criteria = null;
	
	private String key;

	public DtoReportCriteriaRequest getCriteria() {
		return criteria;
	}

	public void setCriteria(DtoReportCriteriaRequest criteria) {
		this.criteria = criteria;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}

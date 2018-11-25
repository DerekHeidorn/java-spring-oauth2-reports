package com.example.demo.services.reports.input;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.services.ReportManager.ReportOutputType;
import com.example.demo.services.ReportManager.ReportType;


/*
 * All report criteria have a similar values it passed to Jasper Report.
 * 
 * Note: This is persisted in the database temporarily when doing batch reports.
 */
public class ReportCriteria implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	protected ReportType reportType;
	protected ReportOutputType reportOutputType;
	protected Date reportDate;
	protected String failString;
	
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("ddMMMyyyy");
	public final static SimpleDateFormat MONTH_YEAR_FORMATTER = new SimpleDateFormat("MMMyyyy");
	public final static SimpleDateFormat MONTH_DAY_FORMATTER = new SimpleDateFormat("ddMMMyyyy");
	public final static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
	public final static SimpleDateFormat REPORT_DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	
	
	public ReportCriteria () {
		super();
	}


	public ReportCriteria (ReportType reportType, ReportOutputType reportOutputType, Date reportDate) {
		this.reportType = reportType;
		this.reportOutputType = reportOutputType;
		this.reportDate = reportDate;
		this.failString = "FAILED - " + reportType.getTitle();
		
	}

	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("reportTitle", reportType.getTitle());
		map.put("reportDate", reportDate);
		return map;
	}
	
	public ReportType getReportType() {
		return reportType;
	}

	public ReportOutputType getReportOutputType() {
		return reportOutputType;
	}	

	public String getFailString() {
		return failString;
	}
	public void setFailString(String failString) {
		this.failString = failString;
	}

	public Date getReportDate() {
		return reportDate;
	}
	
	public String getReportTitle() {
		if(getReportType() != null) {
			return getReportType().getTitle();
		}
		return "";
	}	

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}


	public void setReportOutputType(ReportOutputType reportOutputType) {
		this.reportOutputType = reportOutputType;
	}


	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}






	
}

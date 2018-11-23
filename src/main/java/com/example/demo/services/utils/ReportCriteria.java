package com.example.demo.services.utils;

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
	
	// REPORT Parameter
	// Query Parameter
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// TODO It would be nice to change these constants into an annotation with a Getter/Setter property.
	public final static String P_OUTPUT_TYPE = "P_OUTPUT_TYPE";
	public final static String P_REPORT_DT = "P_REPORT_DT";
	public final static String P_START_DT = "P_START_DT";
	public final static String P_END_DT = "P_END_DT";
	public final static String P_DATE = "P_DATE";
	public final static String P_RUN_TYPE = "P_RUN_TYPE";
	public final static String P_CG_ID = "P_CG_ID";
	public final static String P_CG_NM = "P_CG_NM";
	public final static String P_CG_IDS = "P_CG_IDS";
	
	public final static String P_REGION_NM = "P_REGION_NM";
	public final static String P_PARK_NM = "P_PARK_NM";
	
	public final static String P_PARK_ID = "P_PARK_ID";
	public final static String P_REGION_ID = "P_REGION_ID";
	public final static String P_SCRGRP_ID = "P_SCRGRP_ID";
	public final static String P_SCRGRP_NM = "P_SCRGRP_NM";
	public final static String P_USER_STA = "P_USER_STA";
	public final static String P_USER_STACD = "P_USER_STACD";
	
	public final static String P_RUNTIME = "P_RUNTIME";
	public final static String P_RUNDATE = "P_RUNDATE";
	public final static String P_SEARCH_BY = "P_SEARCH_BY";
	public final static String P_TITLE_1 = "P_TITLE_1";
	public final static String P_TITLE_2 = "P_TITLE_2";
	public final static String P_FIRST_DT = "P_FIRST_DT";
	public final static String P_SECOND_DT = "P_SECOND_DT";
	public final static String P_THIRD_DT = "P_THIRD_DT";
	public final static String P_ON_DATE = "P_ON_DATE";
	public final static String P_EXPORT_MONTH = "P_EXPORT_MONTH";
	public final static String P_DST_ID = "P_DST_ID";
	public final static String P_DATEOBJ = "P_DATEOBJ";
	public final static String P_DISTDATE = "P_DISTDATE";
	public final static String P_DATA_REPORTED = "P_DATA_REPORTED";
	public final static String P_RSVN_CONF_ID = "P_RSVN_CONF_ID";
	public final static String P_KEY_WORDS = "P_KEY_WORDS";
	public final static String P_CAMPING_TYPE = "P_CAMPING_TYPE";
	public final static String P_OPERATOR_NAMES_STRING = "P_OPERATOR_NAMES_STRING";
	public final static String P_OPERATOR_NAMES = "P_OPERATOR_NAMES";
	public final static String P_OPERATOR_TYPE = "P_OPERATOR_TYPE";
	public final static String P_MERCHANT_PERCENTAGE = "P_MERCHANT_PERCENTAGE";
	public final static String P_SUBREPORT_DATASOURCE = "P_SUBREPORT_DATASOURCE";
	public final static String P_YTD_MERCHANT_REVENUE = "P_YTD_MERCHANT_REVENUE";
	public final static String P_NEW_MERCHANT_REVENUE = "P_NEW_MERCHANT_REVENUE";
	public final static String P_YTD_MERCHANT_REFUNDS = "P_YTD_MERCHANT_REFUNDS";
	public final static String P_NEW_MERCHANT_REFUNDS = "P_NEW_MERCHANT_REFUNDS";
	public final static String P_SUBREPORT_EXPRESSION = "P_SUBREPORT_EXPRESSION";
	
	public final static String DAY1 = "DAY1";
	public final static String DAY2 = "DAY2";
	public final static String DAY3 = "DAY3";
	public final static String DAY4 = "DAY4";
	public final static String DAY5 = "DAY5";
	public final static String DAY6 = "DAY6";
	public final static String DAY7 = "DAY7";
	public final static String DAY8 = "DAY8";
	public final static String DAY9 = "DAY9";
	public final static String DAY10 = "DAY10";
	public final static String DAY11 = "DAY11";
	public final static String DAY12 = "DAY12";
	public final static String DAY13 = "DAY13";
	public final static String DAY14 = "DAY14";	
	
	protected ReportType reportType;
	protected ReportOutputType reportOutputType;
	protected Date reportDate;
	protected String failString;
	
	private Map<String,Object> map = new HashMap<String,Object>();
	
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
		
		// default parameters for a report 
		this.put(ReportCriteria.P_OUTPUT_TYPE, this.reportOutputType.getMimeType().getExtension());
		this.put(ReportCriteria.P_DATE, ReportCriteria.DATE_FORMATTER.format(reportDate));
		this.put(ReportCriteria.P_RUNDATE, ReportCriteria.DATE_FORMATTER.format(new Date()));
		this.put(ReportCriteria.P_RUNTIME, ReportCriteria.TIME_FORMATTER.format(new Date()));		
	}
	

	
	public void put(String key, Object value) {
		map.put(key, value);
	}		
	public Object get(Object key) {
		return map.get(key);
	}
	public Map<String, Object> getMap() {
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
	
	@Override
	public String toString() {
		return "ReportCriteria [" +
				"reportType=" + reportType 
				+ ", reportOutputType=" + reportOutputType
				+ ", map=" + debugMap(map)
				+ "]";
	}


    public String debugMap(Map<String,Object> map) {

    	if(map != null) {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append("{");
	    	for (Object key : map.keySet()) {
	    		if(key != null) {
	    			Object value = map.get(key);
	    			sb.append("" + key + "=");
	    			if(value != null) {
	    				sb.append("" + value);
	    			}
	    			sb.append(",");
				}
							
			}
	    	sb.append("}");
	    	return sb.toString();
    	} else {
    		return "null";
    	}
    	
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


	public void setMap(Map<String, Object> map) {
		this.map = map;
	}





	
}

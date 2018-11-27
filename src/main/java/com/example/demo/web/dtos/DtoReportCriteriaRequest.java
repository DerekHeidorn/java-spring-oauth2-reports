package com.example.demo.web.dtos;

import java.util.Date;

import com.example.demo.services.ReportManager;

public class DtoReportCriteriaRequest {

	private String reportCd = null;
	
	private String reportProcessType = ReportManager.ReportProcessType.RPT_PROCESS_TYPE_HTTP.getCode();
	private String reportOutputType = ReportManager.ReportOutputType.RPT_OUTPUT_TYPE_PDF.getMimeType().getExtension();	
	
	// Parameters
	private Date startDt;
	private Date endDt;
	
	
	public String getReportCd() {
		return reportCd;
	}
	public void setReportCd(String reportCd) {
		this.reportCd = reportCd;
	}
	public String getReportProcessType() {
		return reportProcessType;
	}
	public void setReportProcessType(String reportProcessType) {
		this.reportProcessType = reportProcessType;
	}
	public String getReportOutputType() {
		return reportOutputType;
	}
	public void setReportOutputType(String reportOutputType) {
		this.reportOutputType = reportOutputType;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	@Override
	public String toString() {
		return "DtoReportCriteriaRequest [reportCd=" + reportCd + ", reportProcessType=" + reportProcessType
				+ ", reportOutputType=" + reportOutputType + ", startDt=" + startDt + ", endDt=" + endDt + "]";
	}

	

	
}

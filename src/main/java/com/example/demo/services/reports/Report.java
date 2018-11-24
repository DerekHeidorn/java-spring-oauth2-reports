package com.example.demo.services.reports;

import com.example.demo.services.ReportManager.ReportType;
import com.example.demo.services.utils.Mime;

public final class Report {

	
	private ReportType reportType = null;
	private Mime.TYPE mimeType = null;
	private byte[] data = null;
	private String reportTitle = null;
	private boolean hasError = false;
	private String errorMessage = null;
	
	public Report(ReportType reportType, Mime.TYPE mimeType, byte[] data, String reportTitle) {
		super();
		this.reportType = reportType;
		this.mimeType = mimeType;
		this.data = data;
		this.reportTitle = reportTitle;
	}



	public Mime.TYPE getMimeType() {
		return mimeType;
	}

	public void setMimeType(Mime.TYPE mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public String getReportTitle() {
		return reportTitle;
	}


	@Override
	public String toString() {
		return "Report [data=" + ((data != null)? data.length : 0) + ", mimeType="
				+ mimeType + ", reportTitle=" + reportTitle + ", reportType="
				+ reportType + "]";
	}



	public boolean getHasError() {
		return hasError;
	}



	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}



	public String getErrorMessage() {
		return errorMessage;
	}



	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}		
	





	
}

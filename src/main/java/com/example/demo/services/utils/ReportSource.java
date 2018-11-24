package com.example.demo.services.utils;

import com.example.demo.services.ReportManager.ReportType;

import net.sf.jasperreports.engine.JRDataSource;


public final class ReportSource {

	private ReportType reportType = null;
	private JRDataSource dataSource = null;

	public ReportSource(ReportType reportType, JRDataSource dataSource) {
		super();
		this.reportType = reportType;
		this.dataSource = dataSource;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public JRDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(JRDataSource dataSource) {
		this.dataSource = dataSource;
	}

}

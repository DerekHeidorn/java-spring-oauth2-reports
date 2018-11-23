package com.example.demo.web.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.services.ReportManager;

public class DtoReportCriteriaRequest {

	private String reportName = "";
	
	private String userStaCd;
	private Integer securityGroupId;
	
	private String reportProcessType = ReportManager.ReportProcessType.RPT_PROCESS_TYPE_HTTP.getCode();
	private String reportOutputType = ReportManager.ReportOutputType.RPT_OUTPUT_TYPE_PDF.getMimeType().getExtension();	
	
	// Parameters
	private Date startDate;
	private Date endDate;
	private Integer regionId;
	private Integer parkId;
	private Integer campgroundId;
	private List<Integer> campgroundIds = new ArrayList<Integer>();
	private String runTypeCd;
	private String searchByCd;
	private Date onDate;
	private String imagisExportMonth;
	private Integer distributionId;
	private String dataToBeReported;
	private Integer year;
	private Integer month;
	private String campingType;
	private String operatorType;
	private String revenueRunType;
	private String keywords;
	
	private String campgroundRegType;
	private List<String> operatorNames = new ArrayList<String>();
	
	
	public String getUserStaCd() {
		return userStaCd;
	}
	public void setUserStaCd(String userStaCd) {
		this.userStaCd = userStaCd;
	}
	public Integer getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(Integer securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getParkId() {
		return parkId;
	}
	public void setParkId(Integer parkId) {
		this.parkId = parkId;
	}
	public List<Integer> getCampgroundIds() {
		return campgroundIds;
	}
	public void setCampgroundIds(List<Integer> selectedCampgroundIds) {
		this.campgroundIds = selectedCampgroundIds;
	}
	public String getRunTypeCd() {
		return runTypeCd;
	}
	public void setRunTypeCd(String runTypeCd) {
		this.runTypeCd = runTypeCd;
	}
	public String getSearchByCd() {
		return searchByCd;
	}
	public void setSearchByCd(String searchByCd) {
		this.searchByCd = searchByCd;
	}
	public Date getOnDate() {
		return onDate;
	}
	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}
	public String getImagisExportMonth() {
		return imagisExportMonth;
	}
	public void setImagisExportMonth(String imagisExportMonth) {
		this.imagisExportMonth = imagisExportMonth;
	}
	public Integer getDistributionId() {
		return distributionId;
	}
	public void setDistributionId(Integer distributionId) {
		this.distributionId = distributionId;
	}
	public String getDataToBeReported() {
		return dataToBeReported;
	}
	public void setDataToBeReported(String dataToBeReported) {
		this.dataToBeReported = dataToBeReported;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public String getCampingType() {
		return campingType;
	}
	public void setCampingType(String campingType) {
		this.campingType = campingType;
	}
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	public String getRevenueRunType() {
		return revenueRunType;
	}
	public void setRevenueRunType(String revenueRunType) {
		this.revenueRunType = revenueRunType;
	}
	public String getCampgroundRegType() {
		return campgroundRegType;
	}
	public void setCampgroundRegType(String campgroundRegType) {
		this.campgroundRegType = campgroundRegType;
	}
	public List<String> getOperatorNames() {
		return operatorNames;
	}
	public void setOperatorNames(List<String> selectedOperatorNames) {
		this.operatorNames = selectedOperatorNames;
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
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public Integer getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Integer campgroundId) {
		this.campgroundId = campgroundId;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	@Override
	public String toString() {
		return "DtoReportCriteriaRequest [reportName=" + reportName
				+ ", userStaCd=" + userStaCd + ", securityGroupId="
				+ securityGroupId + ", reportProcessType=" + reportProcessType
				+ ", reportOutputType=" + reportOutputType + ", startDate="
				+ startDate + ", endDate=" + endDate + ", regionId=" + regionId
				+ ", parkId=" + parkId + ", campgroundId=" + campgroundId
				+ ", campgroundIds=" + campgroundIds + ", runTypeCd="
				+ runTypeCd + ", searchByCd=" + searchByCd + ", onDate="
				+ onDate + ", imagisExportMonth=" + imagisExportMonth
				+ ", distributionId=" + distributionId + ", dataToBeReported="
				+ dataToBeReported + ", year=" + year + ", month=" + month
				+ ", campingType=" + campingType + ", operatorType="
				+ operatorType + ", revenueRunType=" + revenueRunType
				+ ", keywords=" + keywords + ", campgroundRegType="
				+ campgroundRegType + ", operatorNames=" + operatorNames + "]";
	}	
	
}

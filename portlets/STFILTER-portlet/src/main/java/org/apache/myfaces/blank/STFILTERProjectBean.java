package org.apache.myfaces.blank;

import java.util.Calendar;
import java.util.Date;

public class STFILTERProjectBean {
	// Project Parameters
	String projectName;
	Calendar creationDate; 
	String hostName;

	// These are SOPAC properties
    String siteCode="sio5";
    String beginDate="2004-01-01";
    String endDate="2006-01-10";
    boolean bboxChecked=false;
    double minLatitude=32.0;
    double maxLatitude=33.4;
    double minLongitude=-120.0;
    double maxLongitude=-117.0;
    String resource="procCoords";
    String contextGroup="reasonComb";
    String contextId="4";

    String sopacDataFileName;
    String sopacDataFileContent;
    String sopacDataFileUrl;

    // These are analyze_tseri Parameters
	int resOption = 1;
	int termOption = 556;
	double cutoffCriterion = 1.0;
	double estJumpSpan = 1.0;
	WeakObsCriteria weakObsCriteria = new WeakObsCriteria(30.0, 30.0, 50.0);
	OutlierCriteria outlierCriteria = new OutlierCriteria(800.0, 800.0, 800.0);
	BadObsCriteria badObsCriteria = new BadObsCriteria(10000.0, 10000.0, 10000.0);
	TimeInterval timeInterval = new TimeInterval(1998.0, 2006.800);

	// 
	double[][] globalParam;
	double[][] siteParam;

	AnalyzeTseriResultsBean resultsBean;

	public BadObsCriteria getBadObsCriteria() {
		return badObsCriteria;
	}

	public void setBadObsCriteria(BadObsCriteria badObsCriteria) {
		this.badObsCriteria = badObsCriteria;
	}

	public boolean isBboxChecked() {
		return bboxChecked;
	}

	public void setBboxChecked(boolean bboxChecked) {
		this.bboxChecked = bboxChecked;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getContextGroup() {
		return contextGroup;
	}

	public void setContextGroup(String contextGroup) {
		this.contextGroup = contextGroup;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public double getCutoffCriterion() {
		return cutoffCriterion;
	}

	public void setCutoffCriterion(double cutoffCriterion) {
		this.cutoffCriterion = cutoffCriterion;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getEstJumpSpan() {
		return estJumpSpan;
	}

	public void setEstJumpSpan(double estJumpSpan) {
		this.estJumpSpan = estJumpSpan;
	}

	public double[][] getGlobalParam() {
		return globalParam;
	}

	public void setGlobalParam(double[][] globalParam) {
		this.globalParam = globalParam;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public OutlierCriteria getOutlierCriteria() {
		return outlierCriteria;
	}

	public void setOutlierCriteria(OutlierCriteria outlierCriteria) {
		this.outlierCriteria = outlierCriteria;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getResOption() {
		return resOption;
	}

	public void setResOption(int resOption) {
		this.resOption = resOption;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public AnalyzeTseriResultsBean getResultsBean() {
		return resultsBean;
	}

	public void setResultsBean(AnalyzeTseriResultsBean resultsBean) {
		this.resultsBean = resultsBean;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public double[][] getSiteParam() {
		return siteParam;
	}

	public void setSiteParam(double[][] siteParam) {
		this.siteParam = siteParam;
	}

	public String getSopacDataFileContent() {
		return sopacDataFileContent;
	}

	public void setSopacDataFileContent(String sopacDataFileContent) {
		this.sopacDataFileContent = sopacDataFileContent;
	}

	public String getSopacDataFileName() {
		return sopacDataFileName;
	}

	public void setSopacDataFileName(String sopacDataFileName) {
		this.sopacDataFileName = sopacDataFileName;
	}

	public String getSopacDataFileUrl() {
		return sopacDataFileUrl;
	}

	public void setSopacDataFileUrl(String sopacDataFileUrl) {
		this.sopacDataFileUrl = sopacDataFileUrl;
	}

	public int getTermOption() {
		return termOption;
	}

	public void setTermOption(int termOption) {
		this.termOption = termOption;
	}

	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	public WeakObsCriteria getWeakObsCriteria() {
		return weakObsCriteria;
	}

	public void setWeakObsCriteria(WeakObsCriteria weakObsCriteria) {
		this.weakObsCriteria = weakObsCriteria;
	}
}

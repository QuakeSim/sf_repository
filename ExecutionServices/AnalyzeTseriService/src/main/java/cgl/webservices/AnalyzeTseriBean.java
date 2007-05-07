package cgl.webservices;

public class AnalyzeTseriBean {
	String siteCode;
	int resOption;
	int termOption;
	double cutoffCriterion;
	double estJumpSpan;
	double weakObsCriteriaEast;
	double weakObsCriteriaNorth;
	double weakObsCriteriaUp;
	double outlierCriteriaEast;
	double outlierCriteriaNorth;
	double outlierCriteriaUp;
	double badObsCriteriaEast;
	double badObsCriteriaNorth;
	double badObsCriteriaUp;
	double timeIntervalBeginTime;
	double timeIntervalEndTime;
	String sopacDataFileName;
	String sopacDataFileContent;
	String sopacDataFileUrl;
	double[][] globalParam;
	double[][] siteParam;
	
	public double getBadObsCriteriaEast() {
		return badObsCriteriaEast;
	}
	public void setBadObsCriteriaEast(double badObsCriteriaEast) {
		this.badObsCriteriaEast = badObsCriteriaEast;
	}
	public double getBadObsCriteriaNorth() {
		return badObsCriteriaNorth;
	}
	public void setBadObsCriteriaNorth(double badObsCriteriaNorth) {
		this.badObsCriteriaNorth = badObsCriteriaNorth;
	}
	public double getBadObsCriteriaUp() {
		return badObsCriteriaUp;
	}
	public void setBadObsCriteriaUp(double badObsCriteriaUp) {
		this.badObsCriteriaUp = badObsCriteriaUp;
	}
	public double getCutoffCriterion() {
		return cutoffCriterion;
	}
	public void setCutoffCriterion(double cutoffCriterion) {
		this.cutoffCriterion = cutoffCriterion;
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
	public double getOutlierCriteriaEast() {
		return outlierCriteriaEast;
	}
	public void setOutlierCriteriaEast(double outlierCriteriaEast) {
		this.outlierCriteriaEast = outlierCriteriaEast;
	}
	public double getOutlierCriteriaNorth() {
		return outlierCriteriaNorth;
	}
	public void setOutlierCriteriaNorth(double outlierCriteriaNorth) {
		this.outlierCriteriaNorth = outlierCriteriaNorth;
	}
	public double getOutlierCriteriaUp() {
		return outlierCriteriaUp;
	}
	public void setOutlierCriteriaUp(double outlierCriteriaUp) {
		this.outlierCriteriaUp = outlierCriteriaUp;
	}
	public int getResOption() {
		return resOption;
	}
	public void setResOption(int resOption) {
		this.resOption = resOption;
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
	public double getTimeIntervalBeginTime() {
		return timeIntervalBeginTime;
	}
	public void setTimeIntervalBeginTime(double timeIntervalBeginTime) {
		this.timeIntervalBeginTime = timeIntervalBeginTime;
	}
	public double getTimeIntervalEndTime() {
		return timeIntervalEndTime;
	}
	public void setTimeIntervalEndTime(double timeIntervalEndTime) {
		this.timeIntervalEndTime = timeIntervalEndTime;
	}
	public double getWeakObsCriteriaEast() {
		return weakObsCriteriaEast;
	}
	public void setWeakObsCriteriaEast(double weakObsCriteriaEast) {
		this.weakObsCriteriaEast = weakObsCriteriaEast;
	}
	public double getWeakObsCriteriaNorth() {
		return weakObsCriteriaNorth;
	}
	public void setWeakObsCriteriaNorth(double weakObsCriteriaNorth) {
		this.weakObsCriteriaNorth = weakObsCriteriaNorth;
	}
	public double getWeakObsCriteriaUp() {
		return weakObsCriteriaUp;
	}
	public void setWeakObsCriteriaUp(double weakObsCriteriaUp) {
		this.weakObsCriteriaUp = weakObsCriteriaUp;
	}
	
}

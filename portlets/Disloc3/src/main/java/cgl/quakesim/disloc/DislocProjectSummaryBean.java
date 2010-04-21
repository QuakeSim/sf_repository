package cgl.quakesim.disloc;

import java.util.*;
import javax.faces.component.html.HtmlDataTable;

public class DislocProjectSummaryBean {
	 DislocResultsBean resultsBean;
	 DislocParamsBean paramsBean;
	 String userName;
	 String projectName;
	 String jobUIDStamp;
	 String creationDate;
	 String kmlurl;
	 String insarKmlUrl;
	 String azimuth;
	 String elevation;
	 String frequency;
	 ArrayList insarParamsList;
	 HtmlDataTable insarDataTable;

	 public DislocProjectSummaryBean() {
		  insarParamsList=new ArrayList();
	 }

	 public String getCreationDate() { return this.creationDate; }
	 public void setCreationDate(String creationDate) { this.creationDate=creationDate; }

     public void setKmlurl(String tmp_str) {
         this.kmlurl=tmp_str;
     }
     public String getKmlurl() {
         return this.kmlurl;
     }
	 public String getUserName(){
		  return userName;
	 }
	 public void setUserName(String userName){
		  this.userName=userName;
	 }
	 
	 public String getProjectName() {
		  return projectName;
	 }
	 
	 public void setProjectName(String projectName) {
		  this.projectName=projectName;
	 }
	 
	 public String getJobUIDStamp() {
		  return jobUIDStamp;
	 }
	 public void setJobUIDStamp(String jobUIDStamp) {
		  this.jobUIDStamp=jobUIDStamp;
	 }

	 public void setInsarKmlUrl(String insarKmlUrl){
		  this.insarKmlUrl=insarKmlUrl;
	 }

	 public DislocResultsBean getResultsBean() { return this.resultsBean; }
	 public void setResultsBean(DislocResultsBean resultsBean) { this.resultsBean=resultsBean; }
	 
	 public DislocParamsBean getParamsBean() { return this.paramsBean; }
	 public void setParamsBean(DislocParamsBean paramsBean) { this.paramsBean=paramsBean; }
	 
	 public String getInsarKmlUrl() { return this.insarKmlUrl; }

	 /** 
	  * These methods are used by the InSAR KML Generation service
	  */
	 public void setAzimuth(String azimuth) {
		  this.azimuth=azimuth;
	 }

	 public void setElevation(String elevation){
		  this.elevation=elevation;
	 }
	 
	 public void setFrequency(String frequency){
		  this.frequency=frequency;
	 }
	 
	 public String getAzimuth() { return azimuth; }
	 public String getElevation() { return elevation; }
	 public String getFrequency() { return frequency; }
	 
	 public ArrayList getInsarParamsList() {
		  return insarParamsList;
	 }

	 public void setInsarParamsList(ArrayList insarParamsList){
		  this.insarParamsList=insarParamsList;
	 }
	 
	 public HtmlDataTable getInsarDataTable() {
		  return insarDataTable;
	 }

	 public void setInsarDataTable(HtmlDataTable insarDataTable){
		  this.insarDataTable=insarDataTable;
	 }
}
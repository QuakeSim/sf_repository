package cgl.quakesim.disloc;


public class DislocProjectSummaryBean {
	 DislocResultsBean resultsBean;
	 DislocParamsBean paramsBean;
	 String userName;
	 String projectName;
	 String jobUIDStamp;
	 String creationDate;
	 String kmlurl;

	 public DislocProjectSummaryBean() {
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

	 public DislocResultsBean getResultsBean() { return this.resultsBean; }
	 public void setResultsBean(DislocResultsBean resultsBean) { this.resultsBean=resultsBean; }
	 
	 public DislocParamsBean getParamsBean() { return this.paramsBean; }
	 public void setParamsBean(DislocParamsBean paramsBean) { this.paramsBean=paramsBean; }
}
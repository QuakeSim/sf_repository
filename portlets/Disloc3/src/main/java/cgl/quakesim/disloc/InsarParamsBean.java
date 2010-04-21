package cgl.quakesim.disloc;

public class InsarParamsBean implements TimeOrderedInterface {
	 String insarKmlUrl;
	 String azimuth;
	 String elevation;
	 String frequency;
	 String projectName;
	 String jobUIDStamp;
	 String creationDate;
	 String userName;
	 String dislocOutputUrl;
	 
	 public InsarParamsBean() {
	 }
	 
	 public InsarParamsBean(String userName,
									String projectName,
									String jobUIDStamp,
									String creationDate,
									String elevation, 
									String azimuth, 
									String frequency, 
									String insarKmlUrl,
									String dislocOutputUrl){
		  this.userName=userName;
		  this.projectName=projectName;
		  this.jobUIDStamp=jobUIDStamp;
		  this.creationDate=creationDate;
		  this.elevation=elevation;
		  this.azimuth=azimuth;
		  this.frequency=frequency;
		  this.insarKmlUrl=insarKmlUrl;
		  this.dislocOutputUrl=dislocOutputUrl;
	 }

	 public void setDislocOutputUrl(String dislocOutputUrl){
		  this.dislocOutputUrl=dislocOutputUrl;
	 }
	 public String getDislocOutputUrl() { return dislocOutputUrl; }

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

	 public String getCreationDate() { return this.creationDate; }
	 public void setCreationDate(String creationDate) { this.creationDate=creationDate; }

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

	 public String getInsarKmlUrl(String insarKmlUrl){ return insarKmlUrl; }

}
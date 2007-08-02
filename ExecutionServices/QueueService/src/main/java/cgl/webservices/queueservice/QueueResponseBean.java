package cgl.webservices.queueservice;

public class QueueResponseBean {
	 String jobUIDStamp;
	 String projectName;
	 String inputFileUrl;
	 String outputFileUrl;
	 String stdoutUrl;

	 public String getJobUIDStamp() {
		  return jobUIDStamp;
	 }
	 public String getProjectName(){
		  return projectName;
	 }
	 
	 public void setJobUIDStamp(String jobUIDStamp) {
		  this.jobUIDStamp=jobUIDStamp;
	 }

	 public void setProjectName(String projectName){
		  this.projectName=projectName;
	 }
	 
}
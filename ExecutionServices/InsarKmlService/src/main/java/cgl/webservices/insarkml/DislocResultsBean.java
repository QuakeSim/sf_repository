package cgl.webservices.disloc;

public class DislocResultsBean {
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
	 public String getInputFileUrl() {
		  return inputFileUrl;
	 }
	 public String getOutputFileUrl() {
		  return outputFileUrl;
	 }
	 public String getStdoutUrl() {
		  return stdoutUrl;
	 }
	 
	 public void setJobUIDStamp(String jobUIDStamp) {
		  this.jobUIDStamp=jobUIDStamp;
	 }

	 public void setProjectName(String projectName){
		  this.projectName=projectName;
	 }
	 
	 public void setInputFileUrl(String inputFileUrl) {
		  this.inputFileUrl=inputFileUrl;
	 }

	 public void setOutputFileUrl(String outputFileUrl) {
		  this.outputFileUrl=outputFileUrl;
	 }

	 public void setStdoutUrl(String stdoutUrl) {
		  this.stdoutUrl=stdoutUrl;
	 }
}
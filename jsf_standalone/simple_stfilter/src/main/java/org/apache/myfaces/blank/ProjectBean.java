package org.apache.myfaces.blank;


public class ProjectBean {
    String projectName;
    String creationDate;
    String hostName;
    String baseWorkDir;
    String fileServiceUrl;
    
    public String getProjectName() {
	return projectName;
    }
    public void setProjectName(String projectName){
	this.projectName=projectName;
    }
    public String getCreationDate() {
	return creationDate;
    }
    public void setCreationDate(String creationDate){
	this.creationDate=creationDate;
    }
    public String getBaseWorkDir() {
	return baseWorkDir;
    }
    public void setBaseWorkDir(String baseWorkDir){
	this.baseWorkDir=baseWorkDir;
    }
    public void setHostName(String hostName){
	this.hostName=hostName;
    }
    public String getHostName() {
	return this.hostName;
    }
    public String getFileServiceUrl(){
	return fileServiceUrl;
    }
    public void setFileServiceUrl(String fileServiceUrl){
	this.fileServiceUrl=fileServiceUrl;
    }
    
}

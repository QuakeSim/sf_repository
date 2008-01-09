package cgl.webservices.gridinfo;

public class TeraGridObject {
	 String userHome;
	 String userName;
	 String hostName;
	 String jobManager;

	 public void setUserHome(String userHome) {
		  this.userHome=userHome;
	 }

	 public String getUserHome() {
		  return this.userHome;
	 }

	 public void setUserName(String userName){
		  this.userName=userName;
	 }

	 public String getUserName(){
		  return this.userName;
	 }

	 public void setHostName(String hostName){
		  this.hostName=hostName;
	 }
	 
	 public String getHostName() {
		  return this.hostName;
	 }		  
	 
	 public void setJobManager(String jobManager){
		  this.jobManager=jobManager;
	 }
	 
	 public String getJobManager() {
		  return this.jobManager;
	 }
}
package cgl.webservices.geofest;

/**
 * This class is used to store all the mesh parameters (input and output) associated
 * with a particular mesh run.
 */
public class MeshRunBean {

	 //Member property fields are below.
	 String jobUIDStamp;
	 String projectName;
	 String autoref;
	 String autorefError;
	 String nodeUrl;
	 String tetraUrl;
	 String bcUrl;
	 String indexUrl;
	 String leeRefinerLog;
	 String refinerLog;
	 String tagbigfltLog;
	 String tstout;
	 String junkBox;

	 public void setJobUIDStamp(String jobUIDStamp){
		  this.jobUIDStamp=jobUIDStamp;
	 }
	 public String getJobUIDStamp() {
		  return jobUIDStamp;
	 }

	 public void setProjectName(String projectName){
		  this.projectName=projectName;
	 }
	 public String getProjectName() {
		  return projectName;
	 }

	 public void setAutoref(String autoref){
		  this.autoref=autoref;
	 }
	 public String getAutoref() {
		  return autoref;
	 }

	 public void setAutorefError(String autorefError){
		  this.autorefError=autorefError;
	 }
	 public String getAutorefError() {
		  return autorefError;
	 }

	 public void setNodeUrl(String nodeUrl){
		  this.nodeUrl=nodeUrl;
	 }
	 public String getNodeUrl() {
		  return nodeUrl;
	 }

	 public void setTetraUrl(String tetraUrl){
		  this.tetraUrl=tetraUrl;
	 }
	 public String getTetraUrl() {
		  return tetraUrl;
	 }

	 public void setBcUrl(String bcUrl){
		  this.bcUrl=bcUrl;
	 }
	 public String getBcUrl() {
		  return bcUrl;
	 }

	 public void setIndexUrl(String indexUrl){
		  this.indexUrl=indexUrl;
	 }
	 public String getIndexUrl() {
		  return indexUrl;
	 }
	 
	 public void setLeeRefinerLog(String leeRefinerLog){
		  this.leeRefinerLog=leeRefinerLog;
	 }
	 public String getLeeRefinerLog() {
		  return leeRefinerLog;
	 }
	 
	 public void setRefinerLog(String refinerLog){
		  this.indexUrl=indexUrl;
	 }
	 public String getRefinerLog() {
		  return refinerLog;
	 }
	 
	 public void setTagbigfltLog(String tagbigfltLog){
		  this.tagbigfltLog=tagbigfltLog;
	 }
	 public String getTagbigfltLog() {
		  return tagbigfltLog;
	 }
	 
	 public void setTstout(String tstout){
		  this.tstout=tstout;
	 }
	 public String getTstout() {
		  return tstout;
	 }

	 public void setJunkBox(String junkBox) {
		  this.junkBox=junkBox;
	 }
	 public String getJunkBox() {
		  return junkBox;
	 }
	 
}
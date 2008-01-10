package cgl.webservices.geofest;

/**
 * This bean holds the metadata of a GeoFEST run, including urls to output.
 */

public class GFOutputBean {

	 //Member property fields
	 String projectName;
	 String jobUIDStamp;
	 String tarOfEverythingUrl;
	 String inputUrl;
	 String outputUrl;
	 String logUrl;
	 String indexUrl;
	 String cghistUrl;
	 String jobStatusUrl;
	 String toptrisUrl;
	 String tetvolsUrl;
	 String tetraUrl;
	 String nodeUrl;
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

	 public String getTarOfEverythingUrl(){
		  return tarOfEverythingUrl;
	 }
	 public void setTarOfEverythingUrl(String tarOfEverythingUrl) {
		  this.tarOfEverythingUrl=tarOfEverythingUrl;
	 }

	 public String getInputUrl() {
		  return inputUrl;
	 }
	 public void setInputUrl(String inputUrl){
		  this.inputUrl=inputUrl;
	 }

	 public void setOutputUrl(String outputUrl) {
		  this.outputUrl=outputUrl;
	 }
	 public String getOutputUrl() {
		  return outputUrl;
	 }

	 public void setLogUrl(String logUrl) {
		  this.logUrl=logUrl;
	 }
	 public String getLogUrl() {
		  return logUrl;
	 }

	 public void setIndexUrl(String indexUrl) {
		  this.indexUrl=indexUrl;
	 }
	 public String getIndexUrl() {
		  return indexUrl;
	 }

	 public void setCghistUrl(String cghistUrl) {
		  this.cghistUrl=cghistUrl;
	 }
	 public String getCghistUrl() {
		  return cghistUrl;
	 }

	 public void setJobStatusUrl(String jobStatusUrl) {
		  this.jobStatusUrl=jobStatusUrl;
	 }
	 public String getJobStatusUrl() {
		  return jobStatusUrl;
	 }

	 public void setToptrisUrl(String toptrisUrl) {
		  this.toptrisUrl=toptrisUrl;
	 }
	 public String getToptrisUrl() {
		  return toptrisUrl;
	 }

	 public void setTetvolsUrl(String tetvolsUrl) {
		  this.tetvolsUrl=tetvolsUrl;
	 }
	 public String getTetvolsUrl() {
		  return tetvolsUrl;
	 }


	 public void setJunkBox(String junkBox) {
		  this.junkBox=junkBox;
	 }
	 public String getJunkBox() {
		  return junkBox;
	 }
	 


}

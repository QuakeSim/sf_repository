package cgl.quakesim.simplex;

public class projectEntry {
	 String projectName;
	 String projectDirectory;
	 String startTemp ;
	 String maxIters;
	 String hostName;	 
	 String creationDate;

	 static double DEFAULT_LON=-9999.99;
	 static double DEFAULT_LAT=-9999.99;
	 double origin_lon=DEFAULT_LON;	 
	 double origin_lat=DEFAULT_LAT;	 
	 
	 public projectEntry() {
	 }
	 
	 public void reset() {
		  projectName="";
		  projectDirectory="";
		  startTemp = "";
		  maxIters = "";
		  hostName = "";
		  creationDate = "";
	 }

	 public void setCreationDate(String tmp_str) {
		  this.creationDate= tmp_str;
	 }
	 
	 public String getCreationDate() {
		  return this.creationDate;
	 }
	 
	 public void setHostName ( String tmp_str ) {
		  this.hostName= tmp_str;
	 }
	 
	 public String getHostName ( ) {
		  return this.hostName;
	 }
	 
	 public void setStartTemp(String tmp_str) {
		  this.startTemp = tmp_str;
	 }
	 
	 public String getStartTemp() {
		  return startTemp;
	 }	
	 
	 public void setMaxIters(String tmp_str) {
		  this.maxIters = tmp_str;
	 }
	 
	 public String getMaxIters() {
		  return maxIters;
	 }
	 
	 public void setOrigin_lon(double origin_lon) {
		  this.origin_lon=origin_lon;
	 }
	 
	 public double getOrigin_lon() {
		  return origin_lon;
	 }
	 
	 public void setOrigin_lat(double origin_lat) {
		  this.origin_lat = origin_lat;
	 }
	 
	 public double getOrigin_lat() {
		  return origin_lat;
	 }	 
	 
	 public String getProjectName(){
		  return this.projectName;
	 }
	 
	 public void setProjectName(String tmp_str){
		  this.projectName=tmp_str;
	 }
	 
	 public String getProjectDirectory(){
		  return this.projectDirectory;
	 }
	 
	 public void setProjectDirectory(String tmp_str){
		  this.projectDirectory=tmp_str;
	 }
}

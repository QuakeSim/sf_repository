package cgl.quakesim.geofest;

import java.util.Date;

/**
 * This is a mesh bean convenient class, holds the web service response's mesh bean and adds
 * some additional methods.
 */

public class MeshDataMegaBean {
	 
	 //These default to empty beans, but this will be overridden later,.
	 MeshRunBean meshRunBean=new MeshRunBean();
	 GeotransParamsBean geotransParamsBean=new GeotransParamsBean();
	 //	 GFOutputBean gFOutputBean=new GFOutputBean();
	 GFOutputBean geofestOutputBean=new GFOutputBean();
	 String jnlpLayers;
	 String jnlpFaults;       //Stringifying fault info suitable for the webstart app.
	 String plotMesh="true";        //This is a legacy field, will always be "true".
	 String geoFESTBaseUrlForJnlp;
	 String projectName;
	 String userName;
	 String jobUIDStamp;
	 String creationDate;
	 String meshStatus="Unknown";
	 
	 public MeshDataMegaBean() {
		  creationDate=(new Date()).toString();
	 }
	 

	 public void setCreationDate(String creationDate){
		  this.creationDate=creationDate;
	 }

	 public String getCreationDate() {
		  return creationDate;
	 }

	 public void setGeofestOutputBean(GFOutputBean geofestOutputBean){
		  //System.out.println("gfoutputbean set");
		  this.geofestOutputBean=geofestOutputBean;
		  System.out.println("GFOUTPUT CGHIST:"+this.geofestOutputBean.getCghistUrl());
	 }

	 public GFOutputBean getGeofestOutputBean() {
		  //System.out.println("returning gfoutputbean");
		  return geofestOutputBean;
	 }

	 public void setGeotransParamsBean(GeotransParamsBean geotransParamsBean) {
		  this.geotransParamsBean=geotransParamsBean;
	 }

	 public GeotransParamsBean getGeotransParamsBean() {
		  return this.geotransParamsBean;
	 }

	 public MeshRunBean getMeshRunBean(){
		  return meshRunBean;
	 }
	 public void setMeshRunBean(MeshRunBean meshRunBean) {
		  this.meshRunBean=meshRunBean;
	 }
	 
	 public void setJnlpLayers(String jnlpLayers) {
		  this.jnlpLayers=jnlpLayers;
	 }
	 
	 public String getJnlpLayers() {
		  return jnlpLayers;
	 }

	 public void setJnlpFaults(String jnlpFaults) {
		  this.jnlpFaults=jnlpFaults;
	 }
	 
	 public String getJnlpFaults() {
		  return jnlpFaults;
	 }

	 public void setProjectName(String projectName) {
		  //System.out.println(projectName);
		  this.projectName=projectName;
	 }
	 public String getProjectName() {
		  //System.out.println(projectName);
		  return this.projectName;
	 }

	 public String getPlotMesh() {return this.plotMesh;}
	 public void setPlotMesh(String plotMesh) {this.plotMesh=plotMesh;}

	 public String getUserName() {return this.userName;}
	 public void setUserName(String userName){this.userName=userName;}

	 public String getJobUIDStamp() {return this.jobUIDStamp;}
	 public void setJobUIDStamp(String jobUIDStamp) {this.jobUIDStamp=jobUIDStamp;}

	 public String getGeoFESTBaseUrlForJnlp() {
		  //System.out.println("geofestbaseurlforjnlp in mega: "+geoFESTBaseUrlForJnlp);
		  return geoFESTBaseUrlForJnlp;
	 }

	 public void setGeoFESTBaseUrlForJnlp(String geoFESTBaseUrlForJnlp) {
		  //System.out.println("geofestbaseurlforjnlp in mega: "+geoFESTBaseUrlForJnlp);
		  this.geoFESTBaseUrlForJnlp=geoFESTBaseUrlForJnlp;
	 }

	 public void setMeshStatus(String meshStatus) {
		  this.meshStatus=meshStatus;
	 }

	 public String getMeshStatus() {
		  return this.meshStatus;
	 }
}
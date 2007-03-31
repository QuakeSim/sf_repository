package cgl.quakesim.geofest;

/**
 * This is a mesh bean convenient class, holds the web service response's mesh bean and adds
 * some additional methods.
 */

public class MeshDataMegaBean {
	 
	 //These default to empty beans, but this will be overridden later,.
	 MeshRunBean meshRunBean=new MeshRunBean();
	 GeotransParamsBean geotransParamsBean=new GeotransParamsBean();
	 String jnlpLayers;
	 String jnlpFaults;       //Stringifying fault info suitable for the webstart app.
	 String plotMesh="true";        //This is a legacy field, will always be "true".
	 String geoFESTBaseUrlForJnlp;
	 String projectName;
	 String userName;
	 String jobUIDStamp;
	 
	 public MeshDataMegaBean() {
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
		  System.out.println(projectName);
		  this.projectName=projectName;
	 }
	 public String getProjectName() {
		  System.out.println(projectName);
		  return this.projectName;
	 }

	 public String getPlotMesh() {return this.plotMesh;}
	 public void setPlotMesh(String plotMesh) {this.plotMesh=plotMesh;}

	 public String getUserName() {return this.userName;}
	 public void setUserName(String userName){this.userName=userName;}

	 public String getJobUIDStamp() {return this.jobUIDStamp;}
	 public void setJobUIDStamp(String jobUIDStamp) {this.jobUIDStamp=jobUIDStamp;}

	 public String getGeoFESTBaseUrlForJnlp() {
		  System.out.println("geofestbaseurlforjnlp in mega: "+geoFESTBaseUrlForJnlp);
		  return geoFESTBaseUrlForJnlp;
	 }

	 public void setGeoFESTBaseUrlForJnlp(String geoFESTBaseUrlForJnlp) {
		  System.out.println("geofestbaseurlforjnlp in mega: "+geoFESTBaseUrlForJnlp);
		  this.geoFESTBaseUrlForJnlp=geoFESTBaseUrlForJnlp;
	 }
}
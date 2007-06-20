package cgl.quakesim.disloc;



/**
 * This bean contains project metadata.
 */

public class DislocProjectBean {
	 DislocResultsBean resultsBean;
	 DislocParamsBean paramsBean;
	 Fault[] faults;
	 String projectName;
	 String origin_lat;
	 String origin_lon;
	 
	 public void DislocProjectBean() {
	 }

	 public void setOrigin_lat(String tmp_str) {
		 this.origin_lat=tmp_str;
	 }
	 public String getOrigin_lat() {
		 return this.origin_lat;
	 }
	 public void setOrigin_lon(String tmp_str) {
		 this.origin_lon=tmp_str;
	 }
	 public String getOrigin_lon() {
		 return this.origin_lon;
	 }
	 public void setProjectName(String projectName) { this.projectName=projectName; }
	 public void setResultsBean(DislocResultsBean resultsBean) { this.resultsBean=resultsBean; }
	 public void setParamsBean(DislocParamsBean paramsBean) { this.paramsBean=paramsBean; } 
	 public void setFaults(Fault[] faults) {this.faults=faults; }

	 public String getProjectName() { return this.projectName; }
	 public DislocResultsBean getResultsBean() { return this.resultsBean; }
	 public Fault[] getFaults() { return this.faults; }

}
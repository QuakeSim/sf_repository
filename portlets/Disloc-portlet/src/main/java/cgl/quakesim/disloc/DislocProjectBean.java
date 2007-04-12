package cgl.quakesim.disloc;



/**
 * This bean contains project metadata.
 */

public class DislocProjectBean {
	 DislocResultsBean resultsBean;
	 DislocParamsBean paramsBean;
	 Fault[] faults;
	 String projectName;
	 
	 public void DislocProjectBean() {
	 }

	 public void setProjectName(String projectName) { this.projectName=projectName; }
	 public void setResultsBean(DislocResultsBean resultsBean) { this.resultsBean=resultsBean; }
	 public void setParamsBean(DislocParamsBean paramsBean) { this.paramsBean=paramsBean; } 
	 public void setFaults(Fault[] faults) {this.faults=faults; }

	 public String getProjectName() { return this.projectName; }
	 public DislocResultsBean getResultsBean() { return this.resultsBean; }
	 public Fault[] getFaults() { return this.faults; }

}
package cgl.quakesim.simplex;

public class observationEntryForProject {
	 String observationName;
	 boolean view;
	 boolean delete;
	 String refSite;
	 
	 public void setObservationName(String tmp_str) {
		  this.observationName = tmp_str;
	 }

	 public String getObservationName() {
		  return this.observationName;
	 }		
	
	public void setView(boolean tmp_str) {
		this.view = tmp_str;
	}

	public boolean getView() {
		return this.view;
	}		
	
	public void setDelete(boolean tmp_str) {
		this.delete = tmp_str;
	}

	public boolean getDelete() {
		return this.delete;
	}		
	 
	 public String getRefSite(){
		  return this.refSite;
	 }
	 
	 public void setRefSite(String refSite) {
		  this.refSite=refSite;
	 }
}

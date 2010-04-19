package cgl.quakesim.simplex;

public class observationEntryForProject {
	
	String observationName;
	boolean update;
	boolean delete;
	String refSite;
	 
	 public void setObservationName(String tmp_str) {
		  this.observationName = tmp_str;
	 }

	 public String getObservationName() {
		  return this.observationName;
	 }		
	
	public void setUpdate(boolean tmp_str) {
		this.update = tmp_str;
	}

	public boolean getUpdate() {
		return this.update;
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

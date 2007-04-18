package org.apache.myfaces.blank;

public class observationEntryForProject {
	String observationName;
	boolean view;
	boolean delete;
	
	
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

}

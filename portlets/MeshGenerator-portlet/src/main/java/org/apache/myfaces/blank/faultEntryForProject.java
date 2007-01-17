package org.apache.myfaces.blank;

import javax.faces.model.SelectItem;

public class faultEntryForProject {
	
	String faultName;
	boolean view;
	boolean delete;
	
	
	public void setFaultName(String tmp_str) {
		this.faultName = tmp_str;
	}

	public String getFaultName() {
		return this.faultName;
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

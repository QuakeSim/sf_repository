package org.apache.myfaces.blank;

public class loadMeshTableEntry {
	String projectName;
	boolean view;
	String meshHost;
	String creationDate;

	public void setMeshHost(String tmp_str) {
		this.meshHost = tmp_str;
	}

	public String getMeshHost() {
		return this.meshHost;
	}		

	public void setCreationDate(String tmp_str) {
		this.creationDate = tmp_str;
	}

	public String getCreationDate() {
		return this.creationDate;
	}		
	
	public void setProjectName(String tmp_str) {
		this.projectName = tmp_str;
	}

	public String getProjectName() {
		return this.projectName;
	}		
	
	public void setView(boolean tmp_str) {
		this.view = tmp_str;
	}

	public boolean getView() {
		return this.view;
	}		
	


}

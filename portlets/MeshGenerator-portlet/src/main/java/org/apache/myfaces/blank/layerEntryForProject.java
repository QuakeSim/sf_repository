package org.apache.myfaces.blank;

public class layerEntryForProject {
	String layerName;
	boolean view;
	boolean delete;
	
	
	public void setLayerName(String tmp_str) {
		this.layerName = tmp_str;
	}

	public String getLayerName() {
		return this.layerName;
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

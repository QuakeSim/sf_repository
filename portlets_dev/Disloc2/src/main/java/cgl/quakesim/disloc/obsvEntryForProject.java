package cgl.quakesim.disloc;


import javax.faces.model.SelectItem;

public class obsvEntryForProject {
	
	boolean view;
	boolean delete;
	
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

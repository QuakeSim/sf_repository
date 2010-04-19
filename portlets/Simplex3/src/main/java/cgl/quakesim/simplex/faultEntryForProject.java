package cgl.quakesim.simplex;

import javax.faces.model.SelectItem;

public class faultEntryForProject {
	
	String faultName;
	String oldfaultName;
	boolean update;
	boolean delete;
	
	
	public void setFaultName(String tmp_str) {
		this.faultName = tmp_str;
	}

	public String getFaultName() {
		return this.faultName;
	}
	
    public java.lang.String getoldFaultName() {
        return oldfaultName;
    }

    public void setoldFaultName(java.lang.String oldfaultName) {
        this.oldfaultName = oldfaultName;
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


	
}

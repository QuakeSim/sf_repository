package cgl.quakesim.disloc;

import javax.faces.model.SelectItem;

public class FaultDBEntry {
	
	SelectItem faultName;
	String faultSegmentName="";
	String faultAuthor="";
	String faultSegmentCoordinates="";
	
	
	public void setFaultName(SelectItem tmp_str) {
		this.faultName = tmp_str;
	}

	public SelectItem getFaultName() {
		return faultName;
	}		
	
	public void setFaultSegmentName(String tmp_str) {
		this.faultSegmentName = tmp_str;
	}

	public String getFaultSegmentName() {
		return faultSegmentName;
	}		
	
	public void setFaultAuthor(String tmp_str) {
		this.faultAuthor = tmp_str;
	}

	public String getFaultAuthor() {
		return faultAuthor;
	}		

	public void setFaultSegmentCoordinates(String tmp_str) {
		this.faultSegmentCoordinates = tmp_str;
	}

	public String getFaultSegmentCoordinates() {
		return faultSegmentCoordinates;
	}		
	
}

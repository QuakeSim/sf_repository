package org.apache.myfaces.blank;

public class Observation {

	String obsvName = "";
	
	String obsvType = "";

	String obsvValue = "";

	String obsvError = "";

	String obsvLocationEast = "";

	String obsvLocationNorth = "";

	String obsvRefSite = "";

	public void reset()
	{
		obsvName = "";
		
		obsvType = "";

		obsvValue = "";

		obsvError = "";

		obsvLocationEast = "";

		obsvLocationNorth = "";

		obsvRefSite = "";
	}
	
	public void setObsvName(String tmp_str) {
		this.obsvName = tmp_str;
	}

	public String getObsvName() {
		return obsvName;
	}	
	
	public void setObsvType(String tmp_str) {
		this.obsvType = tmp_str;
	}

	public String getObsvType() {
		return obsvType;
	}
	
	public void setObsvValue(String tmp_str) {
		this.obsvValue = tmp_str;
	}

	public String getObsvValue() {
		return obsvValue;
	}

	public void setObsvError(String tmp_str) {
		this.obsvError = tmp_str;
	}

	public String getObsvError() {
		return obsvError;
	}
	
	public void setObsvLocationEast(String tmp_str) {
		this.obsvLocationEast = tmp_str;
	}

	public String getObsvLocationEast() {
		return obsvLocationEast;
	}


	public void setObsvLocationNorth(String tmp_str) {
		this.obsvLocationNorth = tmp_str;
	}

	public String getObsvLocationNorth() {
		return obsvLocationNorth;
	}

	public void setObsvRefSite(String tmp_str) {
		this.obsvRefSite = tmp_str;
	}

	public String getObsvRefSite() {
		return obsvRefSite;
	}

}

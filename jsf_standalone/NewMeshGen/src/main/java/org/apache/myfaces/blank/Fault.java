package org.apache.myfaces.blank;

public class Fault {
	
	String faultName = "";
	
	String faultLocationX = "";

	String faultLocationY = "";

	String faultLength = "";

	String faultWidth = "";

	String faultDepth = "";

	String faultDipAngle = "";

	String faultStrikeAngle = "";

	String faultSlip = "";

	String faultRakeAngle = "";


	public void reset()
	{
		faultName = "";
		
		faultLocationX = "";

		faultLocationY = "";

		faultLength = "";

		faultWidth = "";

		faultDepth = "";

		faultDipAngle = "";

		faultStrikeAngle = "";

		faultSlip = "";

		faultRakeAngle = "";

	}
	
	public void setFaultName(String tmp_str) {
		this.faultName = tmp_str;
	}

	public String getFaultName() {
		return faultName;
	}	
	
	public void setFaultLocationX(String tmp_str) {
		this.faultLocationX = tmp_str;
	}

	public String getFaultLocationX() {
		return faultLocationX;
	}
	
	public void setFaultLocationY(String tmp_str) {
		this.faultLocationY = tmp_str;
	}

	public String getFaultLocationY() {
		return faultLocationY;
	}

	public void setFaultLength(String tmp_str) {
		this.faultLength = tmp_str;
	}

	public String getFaultLength() {
		return faultLength;
	}
	
	public void setFaultWidth(String tmp_str) {
		this.faultWidth = tmp_str;
	}

	public String getFaultWidth() {
		return faultWidth;
	}


	public void setFaultDepth(String tmp_str) {
		this.faultDepth = tmp_str;
	}

	public String getFaultDepth() {
		return faultDepth;
	}

	public void setFaultDipAngle(String tmp_str) {
		this.faultDipAngle = tmp_str;
	}

	public String getFaultDipAngle() {
		return faultDipAngle;
	}
	
	public void setFaultStrikeAngle(String tmp_str) {
		this.faultStrikeAngle = tmp_str;
	}

	public String getFaultStrikeAngle() {
		return faultStrikeAngle;
	}
	
	public void setFaultSlip(String tmp_str) {
		this.faultSlip = tmp_str;
	}

	public String getFaultSlip() {
		return faultSlip;
	}

	public void setFaultRakeAngle(String tmp_str) {
		this.faultRakeAngle = tmp_str;
	}

	public String getFaultRakeAngle() {
		return faultRakeAngle;
	}
	
}

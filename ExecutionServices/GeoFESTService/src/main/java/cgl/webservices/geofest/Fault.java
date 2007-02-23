package cgl.webservices.geofest;

/**
 * This is a bean for modeling Faults.
 */

public class Fault {
	
	 String faultName = "";
	 String faultLocationX = "";
	 String faultLocationY = "";
	 String faultLocationZ = "";
	 String faultLength = "";
	 String faultWidth = "";
	 String faultDepth = "";
	 String faultDipAngle = "";
	 String faultStrikeAngle = "";
	 String faultSlip = "";
	 String faultRakeAngle = "";
	 String faultLatStart="";
	 String faultLatEnd="";
	 String faultLonStart="";
	 String faultLonEnd="";

	 public String getFaultLatStart() {
		  return faultLatStart;
	 }
	 
	 public void setFaultLatStart(String faultLatStart) {
		  this.faultLatStart=faultLatStart;
	 }

	 public String getFaultLatEnd() {
		  return faultLatEnd;
	 }
	 
	 public void setFaultLatEnd(String faultLatEnd) {
		  this.faultLatEnd=faultLatEnd;
	 }

	 public String getFaultLonStart() {
		  return faultLonStart;
	 }
	 
	 public void setFaultLonStart(String faultLonStart) {
		  this.faultLonStart=faultLonStart;
	 }

	 public String getFaultLonEnd() {
		  return faultLonEnd;
	 }
	 
	 public void setFaultLonEnd(String faultLonEnd) {
		  this.faultLonEnd=faultLonEnd;
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

	public void setFaultLocationZ(String tmp_str) {
		this.faultLocationZ = tmp_str;
	}

	public String getFaultLocationZ() {
		return faultLocationZ;
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


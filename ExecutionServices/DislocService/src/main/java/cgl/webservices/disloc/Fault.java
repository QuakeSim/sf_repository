package cgl.webservices.disloc;

/**
 * This is a bean for modeling Faults.
 */

public class Fault {
	
	 //These are some default values for testing.
	 //Derived from Northridge, of course.
	 String faultName="testFault";
	 double faultLocationX=0.0;
	 double faultLocationY=0.0;
	 double faultLocationZ=0.0;
	 double faultLength=14.009;
	 double faultWidth=21.0;
	 double faultDepth=19.5;
	 double faultDipAngle=40.0;
	 double faultDipSlip=1.22;
	 double faultStrikeAngle=122.0;
	 double faultStrikeSlip=1.22;
	 double faultTensileAngle=0.0;
	 double faultTensileSlip=0.0;
	 double faultRakeAngle=101;
	 double faultLatStart=34.243;
	 double faultLatEnd=34.176;
	 double faultLonStart=-118.72;
	 double faultLonEnd=-118.591;
	 double faultLameMu=35.0;
	 double faultLameLambda=35.0;
	 

	 public double getFaultDipSlip(){ return faultDipSlip;}
	 public double getFaultStrikeSlip(){ return faultStrikeSlip;}
	 public double getFaultTensileSlip(){ return faultTensileSlip;}

	 public void setFaultDipSlip(double faultDipSlip) {  
		  this.faultDipSlip=faultDipSlip;
	 }
	 public void setFaultStrikeSlip(double faultStrikeSlip){ 
		  this.faultStrikeSlip=faultStrikeSlip;
	 }
	 public void setFaultTensileSlip(double faultTensileSlip){
		  this.faultTensileSlip=faultTensileSlip;
	 }


	 public double getFaultLameMu(){
		  return faultLameMu;
	 }

	 public void setFaultLameMu(double faultLameMu){
		  this.faultLameMu=faultLameMu;
	 }
	 
	 public double getFaultLameLambda(){
		  return this.faultLameLambda;
	 }

	 public void setFaultLameLambda(double faultLameLambda){
		  this.faultLameLambda=faultLameLambda;
	 }
	 
	 public double getFaultLatStart() {
		  return faultLatStart;
	 }
	 
	 public void setFaultLatStart(double faultLatStart) {
		  this.faultLatStart=faultLatStart;
	 }

	 public double getFaultLatEnd() {
		  return faultLatEnd;
	 }
	 
	 public void setFaultLatEnd(double faultLatEnd) {
		  this.faultLatEnd=faultLatEnd;
	 }

	 public double getFaultLonStart() {
		  return faultLonStart;
	 }
	 
	 public void setFaultLonStart(double faultLonStart) {
		  this.faultLonStart=faultLonStart;
	 }

	 public double getFaultLonEnd() {
		  return faultLonEnd;
	 }
	 
	 public void setFaultLonEnd(double faultLonEnd) {
		  this.faultLonEnd=faultLonEnd;
	 }


	public void setFaultName(String tmp_str) {
		this.faultName = tmp_str;
	}

	public String getFaultName() {
		return faultName;
	}	
	
	public void setFaultLocationX(double tmp_str) {
		this.faultLocationX = tmp_str;
	}

	public double getFaultLocationX() {
		return faultLocationX;
	}
	
	public void setFaultLocationY(double tmp_str) {
		this.faultLocationY = tmp_str;
	}

	public double getFaultLocationY() {
		return faultLocationY;
	}

	public void setFaultLocationZ(double tmp_str) {
		this.faultLocationZ = tmp_str;
	}

	public double getFaultLocationZ() {
		return faultLocationZ;
	}

	public void setFaultLength(double tmp_str) {
		this.faultLength = tmp_str;
	}

	public double getFaultLength() {
		return faultLength;
	}
	
	public void setFaultWidth(double tmp_str) {
		this.faultWidth = tmp_str;
	}

	public double getFaultWidth() {
		return faultWidth;
	}


	public void setFaultDepth(double tmp_str) {
		this.faultDepth = tmp_str;
	}

	public double getFaultDepth() {
		return faultDepth;
	}

	public void setFaultDipAngle(double tmp_str) {
		this.faultDipAngle = tmp_str;
	}

	public double getFaultDipAngle() {
		return faultDipAngle;
	}
	
	public void setFaultStrikeAngle(double tmp_str) {
		this.faultStrikeAngle = tmp_str;
	}

	public double getFaultStrikeAngle() {
		return faultStrikeAngle;
	}
	
// 	public void setFaultSlip(double tmp_str) {
// 		this.faultSlip = tmp_str;
// 	}

// 	public double getFaultSlip() {
// 		return faultSlip;
// 	}

	public void setFaultRakeAngle(double tmp_str) {
		this.faultRakeAngle = tmp_str;
	}

	public double getFaultRakeAngle() {
		return faultRakeAngle;
	}
	
}


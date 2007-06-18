package cgl.webservices.simplex;

/**
 * This is a bean for modeling Faults.
 */

public class Fault {

	String faultName;

	String faultLocationX;

	String faultLocationY;

	String faultLength;

	String faultWidth;

	String faultDepth;

	String faultDipAngle;

	String faultStrikeAngle;

	String faultSlip;

	String faultRakeAngle;

	String faultLonStarts;

	String faultLatStarts;

	String faultLonEnds;

	String faultLatEnds;

	boolean faultOriginXVary;

	boolean faultOriginYVary;

	boolean faultLengthVary;

	boolean faultWidthVary;

	boolean faultDepthVary;

	boolean faultDipAngleVary;

	boolean faultStrikeAngleVary;

	boolean faultDipSlipVary;

	boolean faultStrikeSlipVary;

	public Fault() {

	}

	public void reset() {
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

		faultLonStarts = "";
		faultLatStarts = "";
		faultLonEnds = "";
		faultLatEnds = "";

		faultOriginXVary = false;
		faultOriginYVary = false;
		faultLengthVary = false;
		faultWidthVary = false;
		faultDepthVary = false;
		faultDipAngleVary = false;
		faultStrikeAngleVary = false;
		faultDipSlipVary = false;
		faultStrikeSlipVary = false;

	}

	public void setFaultLatEnds(String tmp_str) {
		this.faultLatEnds = tmp_str;
	}

	public String getFaultLatEnds() {
		return this.faultLatEnds;
	}	
	
	public void setFaultLonEnds(String tmp_str) {
		this.faultLonEnds = tmp_str;
	}

	public String getFaultLonEnds() {
		return this.faultLonEnds;
	}		

	public void setFaultLatStarts(String tmp_str) {
		this.faultLatStarts = tmp_str;
	}

	public String getFaultLatStarts() {
		return this.faultLatStarts;
	}	
	
	
	public void setFaultLonStarts(String tmp_str) {
		this.faultLonStarts = tmp_str;
	}

	public String getFaultLonStarts() {
		return this.faultLonStarts;
	}

	public void setFaultOriginXVary(boolean tmp) {
		this.faultOriginXVary = tmp;
	}

	public boolean getFaultOriginXVary() {
		return this.faultOriginXVary;
	}

	public void setFaultOriginYVary(boolean tmp) {
		this.faultOriginYVary = tmp;
	}

	public boolean getFaultOriginYVary() {
		return this.faultOriginYVary;
	}

	public void setFaultLengthVary(boolean tmp) {
		this.faultLengthVary = tmp;
	}

	public boolean getFaultLengthVary() {
		return this.faultLengthVary;
	}

	public void setFaultWidthVary(boolean tmp) {
		this.faultWidthVary = tmp;
	}

	public boolean getFaultWidthVary() {
		return this.faultWidthVary;
	}

	public void setFaultDepthVary(boolean tmp) {
		this.faultDepthVary = tmp;
	}

	public boolean getFaultDepthVary() {
		return this.faultDepthVary;
	}

	public void setFaultDipAngleVary(boolean tmp) {
		this.faultDipAngleVary = tmp;
	}

	public boolean getFaultDipAngleVary() {
		return this.faultDipAngleVary;
	}

	public void setFaultStrikeAngleVary(boolean tmp) {
		this.faultStrikeAngleVary = tmp;
	}

	public boolean getFaultStrikeAngleVary() {
		return this.faultStrikeAngleVary;
	}

	public void setFaultDipSlipVary(boolean tmp) {
		this.faultDipSlipVary = tmp;
	}

	public boolean getFaultDipSlipVary() {
		return this.faultDipSlipVary;
	}

	public void setFaultStrikeSlipVary(boolean tmp) {
		this.faultStrikeSlipVary = tmp;
	}

	public boolean getFaultStrikeSlipVary() {
		return this.faultStrikeSlipVary;
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
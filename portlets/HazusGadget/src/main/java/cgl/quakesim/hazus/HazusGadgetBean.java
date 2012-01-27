package cgl.quakesim.hazus;

import java.util.Arrays;
import java.lang.Math;

import org.servogrid.genericproject.GenericProjectBean;

public class HazusGadgetBean extends GenericProjectBean{

	 private OpenShaBean osrs;

	 private static final String RUN_HAZUS_ACTION="run-hazus";

	 //These are the corners of the bounding box.
	 private double lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3;
	 private double gridSpacing=0.1;
	 private double faultRake;
	 private double faultOriginLat;
	 private double faultOriginLon;
	 private double faultDepth=7.6;
	 private double faultDip=90.0;
	 private double magnitude=7.0;
	 private String faultType;
	 private String resultsLink;

	 public static final String LEFT_LATERAL_FAULT="Left Lateral Fault";
	 public static final String THRUST_FAULT="Thrust Fault";
	 public static final String RIGHT_LATERAL_FAULT="Right Lateral Fault";
	 public static final String NORMAL_FAULT="Normal Fault";
	 private static final double d2r = Math.acos(-1.0) / 180.0;
	 private static final double flatten = 1.0/298.247;

	 
	 private LatLon faultOrigin;

	 public HazusGadgetBean() {
		  System.out.println("Constructing the HazusGadget Bean");
		  osrs=new OpenShaBean();
		  resultsLink="";
	 }
	
	 /**
	  * This method sets up and calls the Hazus service. Note that the 
	  * magnitude, fault type, and bounding box must be set outside this method from the
	  * JSF user interface.  
	  */
	 public String invokeHazusService() throws Exception {
		  resultsLink="";
		  LatLon[] faultStuff=getFaultOriginAndBBox(lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3);
		  faultOriginLat=faultStuff[0].getLat();
		  faultOriginLon=faultStuff[0].getLon();
		  
		  Fault fault=new Fault(); //createFaultFromType(faultType,magnitude,faultOriginLat,faultOriginLon);
		  fault.setFaultRakeAngle(faultRake);
		  fault.setFaultLatStart(faultOriginLat);
		  fault.setFaultLonStart(faultOriginLon);
		  fault.setFaultDepth(faultDepth);
		  fault.setFaultDipAngle(faultDip);

		  System.out.println("Bottom Left:"+faultStuff[1].getLat()+" "+faultStuff[1].getLon());
		  System.out.println("Origin:"+faultStuff[0].getLat()+" "+faultStuff[0].getLon());
		  System.out.println("Top Right:"+faultStuff[2].getLat()+" "+faultStuff[2].getLon());
		  
		  try {
				
				resultsLink=osrs.getOpenShaHazusOutput(faultStuff[1].getLat(),faultStuff[2].getLat(),faultStuff[1].getLon(),faultStuff[2].getLon(),gridSpacing,magnitude,fault.getFaultRakeAngle(),fault.getFaultLatStart(), fault.getFaultLonStart(),fault.getFaultDepth(),fault.getFaultDipAngle());
				

		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
		  }		  
		  return RUN_HAZUS_ACTION;
	 }

	 /**
	  * Get the fault origin. We assume this is in the middle of the selected region
	  */
	 protected LatLon[] getFaultOriginAndBBox(double lat0, double lon0, double lat1, double lon1, double lat2, double lon2, double lat3, double lon3) {
		  
		  //		  System.out.println(lat0+" "+ lon0+" "+ lat1+" "+ lon1+" "+ lat2+" "+ lon2+" "+ lat3+" "+ lon3);
		  //sort the lats
		  double[] allLats={lat0, lat1, lat2, lat3};
		  Arrays.sort(allLats);
		  
		  //sort the lons
		  double[] allLons={lon0, lon1, lon2, lon3};
		  Arrays.sort(allLons);
		  
		  // System.out.println(allLons[0]+" "+allLons[1]+" "+allLons[2]+" "+allLons[3]);
		  // System.out.println(allLats[0]+" "+allLats[1]+" "+allLats[2]+" "+allLats[3]);
		  double aveLat=allLats[0]+(allLats[allLats.length-1]-allLats[0])/2.;
		  double aveLon=allLons[0]+(allLons[allLons.length-1]-allLons[0])/2.;
		  
		  // System.out.println(allLats+" "+allLons);
		  System.out.println("origin: "+aveLat+" "+aveLon);
		  
		  LatLon faultOrigin=new LatLon(aveLat,aveLon);
		  LatLon bboxMin=new LatLon(allLats[0],allLons[0]);
		  LatLon bboxMax=new LatLon(allLats[allLats.length-1],allLons[allLons.length-1]);
		  LatLon[] allPoints={faultOrigin,bboxMin,bboxMax};
		  return allPoints;
	 }

	 /**
	  * For the provided fault type, create the fault. 
	  * TODO: This method needs to be removed.
	  */
	 protected Fault createFaultFromType(String faultType, double magnitude, double originLat, double originLon) throws Exception {
		  //Set the generic stuff
		  boolean thrust;
		  double mu = 0.2E11;
		  double length, width, depth;
		  Fault fault=new Fault();
		  fault.setFaultName("Hazus Sample Fault");
		  fault.setFaultLatStart(originLat);
		  fault.setFaultLonStart(originLon);
		  fault.setFaultLameLambda(1.0);
		  fault.setFaultLameMu(1.0);
		  fault.setFaultLocationX(0.0);
		  fault.setFaultLocationY(0.0);

		  //Now set fault type specific params
		  if(faultType.equals(LEFT_LATERAL_FAULT)) { 
				fault=createLeftLateralFault(fault, magnitude, originLat, originLon);
				thrust=false;
		  }
		  else if (faultType.equals(THRUST_FAULT)) {
				fault=createThrustFault(fault, magnitude, originLat, originLon);
				thrust=false;
		  }
		  else if (faultType.equals(RIGHT_LATERAL_FAULT)) {
				fault=createRightLateralFault(fault, magnitude, originLat, originLon);
				thrust=true;
		  }
		  else if(faultType.equals(NORMAL_FAULT)) {
				fault=createNormalFault(fault, magnitude, originLat, originLon);
				thrust=true;
		  }
		  else {
				System.err.println("Invalid fault type provided");
				throw new Exception("Invalid fault type provided");
		  }

		  //--------------------------------------------------
		  //Now set the rest of the params
		  //--------------------------------------------------
		  if (magnitude<7.0) {
				//If magnitude is less than 7, the fault is square.
				length = Math.sqrt(Math.sqrt(Math.pow(10, (3*(magnitude+10.7)/2))/(mu*0.6E-10)))/1E5;
				width = length;
		  }
		  //Faults with M>=7 are rectangles, with a fixed width.
		  else {				
				//Use a fixed width for M>7; the fault is rectangular instead of square
				//REVIEW: "20" is some magic number.
				width = 20;
				length = Math.sqrt(Math.pow(10,(3*(magnitude+10.7)/2))/(mu*0.6E-10))/(width*1E10);
		  }
		  fault.setFaultLength(length);
		  fault.setFaultWidth(width);
		  fault.setFaultDepth(width/2.0);
		  double slip = 0.6*length*width*10.0; //Factor of 10 is for unit conversion.  Mag includes cm.				
		  //thr is thrust, which is set in in the previous 4 scenarios.
		  if (thrust) {
		  		fault.setFaultDipSlip(slip);
		  }
		  else {
		  		fault.setFaultStrikeSlip(slip);
		  }

		  double theFactor = d2r * Math.cos(d2r*originLat) * 6378.139 * ( 1.0 - Math.sin(d2r * originLat) * Math.sin(d2r * originLat) * flatten);
		  
		  //Set the strike angle
		  double xval, yval;
		  double sa = fault.getFaultStrikeAngle();
		  if (sa == 0) {
				xval = 0;
				yval = length;
		  }
		  else if (sa == 90) {
				xval = length;
				yval = 0;
		  }
		  else if (sa == 180) {
				xval = 0;
				yval = (-1.0) * length;
		  }
		  else if (sa == 270) {
				xval = (-1.0) * length;
				yval = 0;
		  }				
		  else {
				double sval = 90 - sa;
				double thetan = Math.tan(sval * d2r);
				xval = length / Math.sqrt(1+thetan*thetan);
				yval = Math.sqrt(length * length - xval * xval);
				
				if (sa > 0 && sa < 90) {
					 xval = xval * 1.0;
					 yval = yval * 1.0;
				}
				else if (sa > 90 && sa < 180) {
					 xval = xval * 1.0;
					 yval = yval * (-1.0);
				}
				else if (sa > 180 && sa < 270) {
					 xval = xval * (-1.0);
					 yval = yval * (-1.0);
				}
				else if (sa > 270 && sa < 360) {
					 xval = xval * (-1.0);
					 yval = yval * 1.0;
				}
		  }

		  //Set the rake
		  if (thrust) {
				fault.setFaultDipSlip(slip);
		  }
		  else {
				fault.setFaultStrikeSlip(slip);
		  }

		  //Now set the fault ending location lat/lon.				
		  double lon_end = xval/theFactor + originLon;
		  double lat_end = (yval/111.32) + originLat;
		  
		  lon_end = Math.round(lon_end * 100)/100.0;
		  lat_end = Math.round(lat_end * 100)/100.0;
		  
		  fault.setFaultLonEnd(lon_end);
		  fault.setFaultLatEnd(lat_end);

		  return fault;
	 }

	 protected Fault createLeftLateralFault(Fault fault, double magnitude, double originLat, double originLon){
		  fault.setFaultDipAngle(90);
		  fault.setFaultStrikeAngle(0);
		  return fault;
	 }
	 protected Fault createThrustFault(Fault fault, double magnitude, double originLat, double originLon){
		  fault.setFaultDipAngle(90);
		  fault.setFaultStrikeAngle(45);
		  return fault;
	 }
	 protected Fault createRightLateralFault(Fault fault, double magnitude, double originLat, double originLon){
		  fault.setFaultDipAngle(45);
		  fault.setFaultStrikeAngle(0);
		  return fault;
	 }
	 protected Fault createNormalFault(Fault fault, double magnitude, double originLat, double originLon){
		  fault.setFaultDipAngle(45);
		  fault.setFaultStrikeAngle(90);
		  return fault;
	 }


	 //--------------------------------------------------
	 //Project accessor methods are below
	 //--------------------------------------------------

	 public double getLat0() { return this.lat0; }
	 public double getLon0() { return this.lon0; }
	 public double getLat1() { return this.lat1; }
	 public double getLon1() { return this.lon1; }
	 public double getLat2() { return this.lat2; }
	 public double getLon2() { return this.lon2; }
	 public double getLat3() { return this.lat3; }
	 public double getLon3() { return this.lon3; }
	 public double getMagnitude() { return this.magnitude; }
	 public double getGridSpacing() { return this.gridSpacing; }
	 public double getFaultRake() { return this.faultRake; }
	 public double getFaultOriginLat() { return this.faultOriginLat; }
	 public double getFaultOriginLon() { return this.faultOriginLon; }
	 public double getFaultDepth() { return this.faultDepth; }
	 public double getFaultDip() { return this.faultDip; }
	 public String getFaultType() { return this.faultType; }
	 public String getLeftLateralFault() { return LEFT_LATERAL_FAULT; }
	 public String getThrustFault() { return THRUST_FAULT; }
	 public String getRightLateralFault() { return RIGHT_LATERAL_FAULT; }
	 public String getNormalFault() { return NORMAL_FAULT; }
	 public String getResultsLink() { return resultsLink; }

	 public void setLat0(double lat0) { this.lat0=lat0; }
	 public void setLon0(double lon0) { this.lon0=lon0; }
	 public void setLat1(double lat1) { this.lat1=lat1; }
	 public void setLon1(double lon1) { this.lon1=lon1; }
	 public void setLat2(double lat2) { this.lat2=lat2; }
	 public void setLon2(double lon2) { this.lon2=lon2; }
	 public void setLat3(double lat3) { this.lat3=lat3; }
	 public void setLon3(double lon3) { this.lon3=lon3; }
	 public void setMagnitude(double magnitude) { this.magnitude=magnitude; }
	 public void setGridSpacing(double gridSpacing) { this.gridSpacing=gridSpacing; }
	 public void setFaultRake(double faultRake) { this.faultRake=faultRake; }
	 public void setFaultOriginLat(double faultOriginLat) { this.faultOriginLat=faultOriginLat; }
	 public void setFaultOriginLon(double faultOriginLon) { this.faultOriginLon=faultOriginLon; }
	 public void setFaultDepth(double faultDepth) { this.faultDepth=faultDepth; }
	 public void setFaultDip(double faultDip) { this.faultDip=faultDip; }
	 public void setFaultType(String faultType) { this.faultType=faultType; }
	 public void setResultsLink(String resultsLink) { this.resultsLink=resultsLink; }
	 
}

class LatLon {
	 double lat, lon;
	 public LatLon() {
	 }
	 
	 public LatLon(double lat, double lon) {
		  this.lat=lat;
		  this.lon=lon;
	 }
	 
	 public double getLat() { return lat; }
	 public double getLon() { return lon; }
	 public void setLat(double lat) { this.lat=lat; }
	 public void setLon(double lon) { this.lon=lon; }

}
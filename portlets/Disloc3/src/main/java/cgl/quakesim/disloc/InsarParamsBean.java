package cgl.quakesim.disloc;

public class InsarParamsBean {
	 String insarKmlUrl;
	 String azimuth;
	 String elevation;
	 String frequency;
	 
	 public InsarParamsBean() {
	 }
	 public void setAzimuth(String azimuth) {
		  this.azimuth=azimuth;
	 }

	 public void setElevation(String elevation){
		  this.elevation=elevation;
	 }
	 
	 public void setFrequency(String frequency){
		  this.frequency=frequency;
	 }
	 
	 public String getAzimuth() { return azimuth; }
	 public String getElevation() { return elevation; }
	 public String getFrequency() { return frequency; }


}
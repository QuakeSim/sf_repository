package cgl.webservices;

public class GPSStationBean {
    String parentNetwork;
    String stationName;
    double stationLat;
    double stationLon;

    public GPSStationBean(){
    }

    public void setParentNetwork(String parentNetwork){
	this.parentNetwork=parentNetwork;
    }
    
    public String getParentNetwork() {
	return parentNetwork;
    }
    
    public void setStationName(String stationName){
	this.stationName=stationName;
    }

    public String getStationName(){
	return stationName;
    }
    
    public void setStationLat(double stationLat) {
	this.stationLat=stationLat;
    }
    
    public double getStationLat() {
	return this.stationLat;
    }

    public void setStationLon(double stationLon){
	this.stationLon=stationLon;
    }
    
    public double getStationLon(){
	return this.stationLon;
    }
}

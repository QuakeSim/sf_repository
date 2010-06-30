package cgl.quakesim.simplex;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class CandidateObservation {
	
	
	String stationName = new String();
	
	String gpsStationLat = new String();
	String gpsStationLon = new String();
	

	List stationSources = new ArrayList();
	String selectedSource = new String();
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
	public String getGpsStationLat() {
		return gpsStationLat;
	}
	public void setGpsStationLat(String gpsStationLat) {
		this.gpsStationLat = gpsStationLat;
	}
	public String getGpsStationLon() {
		return gpsStationLon;
	}
	public void setGpsStationLon(String gpsStationLon) {
		this.gpsStationLon = gpsStationLon;
	}
	
	
	public List getStationSources() {
		return stationSources;
	}
	public void setStationSources(List stationSources) {
		this.stationSources = stationSources;
	}
	public String getSelectedSource() {
		return selectedSource;
	}
	public void setSelectedSource(String selectedSource) {
		this.selectedSource = selectedSource;
	}
}

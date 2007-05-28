package cgl.webservices.simplex;
/**
 * This is a bean for modeling google map Marker element.
 */

public class googleMapMarker {
	
	private double lat;
	private double lon;
	private int width;
	private int height;
	private String mouseonimage;
	private String mouseoutimage;
	private String textimage;
	private String label;
	private String windowhtml;
	private String tiphtml;
	
	
	public googleMapMarker(){
		lat=0;
		lon=0;
		width=20;
		height=34;
		mouseonimage="";
		mouseoutimage="";
		textimage="";
		label="";
		windowhtml="";
		tiphtml="";
		
	}
	
	public void setLat(double l){
		lat=l;
	}
	public double getLat() {
		return lat;
	}

	public void setLon(double l){
		lon=l;
	}
	public double getLon() {
		return lon;
	}
	public void setWidth(int i) {
		width=i;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int i) {
		height=i;
	}
	public int getHeight() {
		return height;
	}
	
	public void setMouseonimage(String tmp) {
		mouseonimage=tmp;
	}
	public String getMouseonimage() {
		return this.mouseonimage;
	}
	
	public void setMouseoutimage(String tmp) {
		mouseoutimage=tmp;
	}
	public String getMouseoutimage() {
		return this.mouseoutimage;
	}
	
	public void setTextimage(String tmp) {
		textimage=tmp;
	}
	public String getTextimage() {
		return this.textimage;
	}
	
	public void setLabel(String tmp) {
		label=tmp;
	}
	public String getLabel() {
		return this.label;
	}
	
	public void setWindowhtml(String tmp) {
		windowhtml=tmp;
	}
	public String getWindowhtml() {
		return this.windowhtml;
	}
	
	public void setTiphtml(String tmp) {
		tiphtml=tmp;
	}
	public String getTiphtml() {
		return this.tiphtml;
	}


	
}

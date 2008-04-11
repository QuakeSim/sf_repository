package cgl.webservices.disloc;

public class XYPoint {
	 double x;
	 double y;
	 private double lon;
	 
	 private double lat;


    public XYPoint(
						 double x,
						 double y,
						 double lat,
						 double lon) {
           this.x = x;
           this.y = y;
			  this.lat=lat;
			  this.lon=lon;
    }

	 public double getLon() {
		  return lon;
	 }
	 
	 public double getLat() {
		  return lat;
	 }

	 public void setLon(double lon) {
		  this.lon=lon;
	 }

	 public void setLat(double lat){
		  this.lat=lat;
	 }

	 public double getX() {
		  return x;
	 }

	 public void setX(double x) {
		  this.x=x;
	 }

	 public double getY() {
		  return y;
	 }
	 
	 public void setY(double y) {
		  this.y=y;
	 }
}
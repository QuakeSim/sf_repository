package cgl.webservices.disloc;

public class XYPoint {
	 double x;
	 double y;
	 private double lon;
	 
	 private double lat;


    public XYPoint(
						 double lat,
						 double lon,
						 double x,
						 double y) {
           this.x = x;
           this.y = y;
			  this.lat=lat;
			  this.lon=lon;
			  System.out.println("XYPoints:"+lat+" "+lon+" "+x+" "+y);
    }

	 public double getLon() {
		  System.out.println("Lon:"+lon);
		  return lon;
	 }
	 
	 public double getLat() {
		  System.out.println("Lat:"+lat);
		  return lat;
	 }

	 public void setLon(double lon) {
		  System.out.println("Lon:"+lon);
		  this.lon=lon;
	 }

	 public void setLat(double lat){
		  System.out.println("Lat:"+lat);
		  this.lat=lat;
	 }

	 public double getX() {
		  System.out.println("X:"+x);
		  return x;
	 }

	 public void setX(double x) {
		  System.out.println("X:"+x);
		  this.x=x;
	 }

	 public double getY() {
		  System.out.println("Y:"+y);
		  return y;
	 }
	 
	 public void setY(double y) {
		  System.out.println("Y:"+y);
		  this.y=y;
	 }
}
package cgl.webservices.disloc;

public class ObsvPoint {

    String ycartPoint;
    String xcartPoint;
    String lonPoint;
    String latPoint;

    public String getXcartPoint() {
	return xcartPoint;
    }
  
    public String getYcartPoint() {
	return ycartPoint;
    }

    public void setXcartPoint(String xcartPoint) {
	this.xcartPoint=xcartPoint;
    }

    public void setYcartPoint(String ycartPoint) {
	this.ycartPoint=ycartPoint;
    }

    public String getLatPoint() {
	return latPoint;
    }

    public void setLatPoint(String latPoint) {
	this.latPoint=latPoint;
    }

    public String getLonPoint() {
	return lonPoint;
    }

    public void setLonPoint(String lonPoint) {
	this.lonPoint=lonPoint;
    }

}
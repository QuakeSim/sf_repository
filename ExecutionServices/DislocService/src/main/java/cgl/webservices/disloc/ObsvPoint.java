package cgl.webservices.disloc;

public class ObsvPoint {
	 double xPoint;
	 double yPoint;
	 private double lonPoint;
	 private double latPoint;

    public ObsvPoint(
						 double latPoint,
						 double lonPoint,
						 double xPoint,
						 double yPoint) {
           this.xPoint = xPoint;
           this.yPoint = yPoint;
			  this.latPoint=latPoint;
			  this.lonPoint=lonPoint;
			  System.out.println("XYPoints:"+latPoint+" "+lonPoint+" "+xPoint+" "+yPoint);
    }

	 public double getLonPoint() {
		  System.out.println("LonPoint:"+lonPoint);
		  return lonPoint;
	 }
	 
	 public double getLatPoint() {
		  System.out.println("LatPoint:"+latPoint);
		  return latPoint;
	 }

	 public void setLonPoint(double lonPoint) {
		  System.out.println("LonPoint:"+lonPoint);
		  this.lonPoint=lonPoint;
	 }

	 public void setLatPoint(double latPoint){
		  System.out.println("LatPoint:"+latPoint);
		  this.latPoint=latPoint;
	 }

	 public double getXPoint() {
		  System.out.println("X:"+xPoint);
		  return xPoint;
	 }

	 public void setXPoint(double xPoint) {
		  System.out.println("XPOINT:"+xPoint);
		  this.xPoint=xPoint;
	 }

	 public double getYPoint() {
		  System.out.println("Y:"+yPoint);
		  return yPoint;
	 }
	 
	 public void setYPoint(double yPoint) {
		  System.out.println("YPOINT:"+yPoint);
		  this.yPoint=yPoint;
	 }
}
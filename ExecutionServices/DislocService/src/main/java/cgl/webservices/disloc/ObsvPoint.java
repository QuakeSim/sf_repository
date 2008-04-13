package cgl.webservices.disloc;

public class ObsvPoint {
	 double xcartPoint;
	 double ycartPoint;
	 private double lonPoint;
	 private double latPoint;

    public ObsvPoint(
						 double latPoint,
						 double lonPoint,
						 double xcartPoint,
						 double ycartPoint) {
           this.xcartPoint = xcartPoint;
           this.ycartPoint = ycartPoint;
			  this.latPoint=latPoint;
			  this.lonPoint=lonPoint;
			  System.out.println("XYPoints:"+latPoint+" "+lonPoint+" "+xcartPoint+" "+ycartPoint);
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

	 public double getXcartPoint() {
		  System.out.println("X:"+xcartPoint);
		  return xcartPoint;
	 }

	 public void setXcartPoint(double xcartPoint) {
		  System.out.println("XPOINT:"+xcartPoint);
		  this.xcartPoint=xcartPoint;
	 }

	 public double getYcartPoint() {
		  System.out.println("Y:"+ycartPoint);
		  return ycartPoint;
	 }
	 
	 public void setYcartPoint(double ycartPoint) {
		  System.out.println("YPOINT:"+ycartPoint);
		  this.ycartPoint=ycartPoint;
	 }
}
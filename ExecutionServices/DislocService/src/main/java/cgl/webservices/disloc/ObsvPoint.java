package cgl.webservices.disloc;

public class ObsvPoint {
	 String xcartPoint;
	 String ycartPoint;
	 String lonPoint;
	 String latPoint;

    public ObsvPoint(
						 String latPoint,
						 String lonPoint,
						 String xcartPoint,
						 String ycartPoint) {
           this.xcartPoint = xcartPoint;
           this.ycartPoint = ycartPoint;
			  this.latPoint=latPoint;
			  this.lonPoint=lonPoint;
			  System.out.println("XYPoints:"+latPoint+" "+lonPoint+" "+xcartPoint+" "+ycartPoint);
    }

	 public String getLonPoint() {
		  System.out.println("LonPoint:"+lonPoint);
		  return lonPoint;
	 }
	 
	 public String getLatPoint() {
		  System.out.println("LatPoint:"+latPoint);
		  return latPoint;
	 }

	 public void setLonPoint(String lonPoint) {
		  System.out.println("LonPoint:"+lonPoint);
		  this.lonPoint=lonPoint;
	 }

	 public void setLatPoint(String latPoint){
		  System.out.println("LatPoint:"+latPoint);
		  this.latPoint=latPoint;
	 }

	 public String getXcartPoint() {
		  System.out.println("X:"+xcartPoint);
		  return xcartPoint;
	 }

	 public void setXcartPoint(String xcartPoint) {
		  System.out.println("XPOINT:"+xcartPoint);
		  this.xcartPoint=xcartPoint;
	 }

	 public String getYcartPoint() {
		  System.out.println("Y:"+ycartPoint);
		  return ycartPoint;
	 }
	 
	 public void setYcartPoint(String ycartPoint) {
		  System.out.println("YPOINT:"+ycartPoint);
		  this.ycartPoint=ycartPoint;
	 }
}
package cgl.webservices.disloc;

public class DislocParamsBean {
	 
	 double originLat=DislocConstants.FIXED_ORIGIN_LAT;
	 double originLon=DislocConstants.FIXED_ORIGIN_LON;
	 double gridMinXValue;
	 double gridMinYValue;
	 double gridXSpacing;
	 double gridYSpacing;
	 int gridXIterations;
	 int gridYIterations;

	 int observationPointStyle=1;

	 XYPoint[] XYPoints;

	 public DislocParamsBean() {
	 }

	 public void setXYPoints(XYPoint[] XYPoints) {
		  System.arraycopy(XYPoints,0,this.XYPoints,0,XYPoints.length);
	 }

	 public XYPoint[] getXYPoints(){
		  return XYPoints;
	 }

	 //Setters
	 public void setOriginLat(double originLat){
		  this.originLat=originLat;
	 }
	 
	 public void setOriginLon(double originLon){
		  this.originLon=originLon;
	 }
	 
	 public void setGridMinXValue(double gridMinXValue){
		  this.gridMinXValue=gridMinXValue;
	 }

	 public void setGridMinYValue(double gridMinYValue) {
		  this.gridMinYValue=gridMinYValue;
	 }
	 
	 public void setGridXSpacing(double gridXSpacing){
		  this.gridXSpacing=gridXSpacing;
	 }

	 public void setGridYSpacing(double gridYSpacing) {
		  this.gridYSpacing=gridYSpacing;
	 }

	 public void setGridXIterations(int gridXIterations) {
		  this.gridXIterations=gridXIterations;
	 }

	 public void setGridYIterations(int gridYIterations) {
		  this.gridYIterations=gridYIterations;
	 }

	 public void setObservationPointStyle(int observationPointStyle){
		  this.observationPointStyle=observationPointStyle;
	 }
	 

	 //Getters
	 public double getOriginLon() {
		  return this.originLon;
	 }
	 
	 public double getOriginLat(){
		  return this.originLat;
	 }

	 public double getGridMinXValue() {
		  return gridMinXValue;
	 }

	 public double getGridMinYValue() {
		  return gridMinYValue;
	 }

	 public double getGridXSpacing(){
		  return this.gridXSpacing;
	 }

	 public double getGridYSpacing() {
		  return this.gridYSpacing;
	 }

	 public int getGridXIterations() {
		  return this.gridXIterations;
	 }

	 public int getGridYIterations() {
		  return this.gridYIterations;
	 }

	 public int getObservationPointStyle(){
		  return this.observationPointStyle;
	 }
	 
}
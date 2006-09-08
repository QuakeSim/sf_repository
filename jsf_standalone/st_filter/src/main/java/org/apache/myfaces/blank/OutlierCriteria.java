package org.apache.myfaces.blank;

public class OutlierCriteria {
    
    double north;    
    double east;
    double up;
    
    public OutlierCriteria() {
    }

    public OutlierCriteria(double north, double east, double up){
	this.north=north;
	this.east=east;
	this.up=up;
    }

    public double getNorth(){
	return north;
    }
    public void setNorth(double north){
	this.north=north;
    }

    public double getEast(){
	return east;
    }
    public void setEast(double east){
	this.east=east;
    }

    public double getUp(){
	return up;
    }
    public void setUp(double up){
	this.up=up;
    }
}

package cgl.webservices;

public class BadObsCriteria {
    
    double north;    
    double east;
    double up;
    
    public BadObsCriteria() {
    }

    public BadObsCriteria(double north, double east, double up){
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

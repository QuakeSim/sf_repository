package cgl.webservices;

public class TimeInterval {
    
    double beginTime;    
    double endTime;
    
    public TimeInterval() {
    }

    public TimeInterval(double beginTime, double endTime){
	this.beginTime=beginTime;
	this.endTime=endTime;
    }

    public double getBeginTime(){
	return beginTime;
    }
    public void setBeginTime(double beginTime){
	this.beginTime=beginTime;
    }

    public double getEndTime(){
	return endTime;
    }
    public void setEndTime(double endTime){
	this.endTime=endTime;
    }
}

package org.apache.myfaces.blank;

public class EpisodicBias extends EstimateParameter {
    
    double startDate;
    double endDate;
    String space="  ";

    public double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(double startDate){
	this.startDate=startDate;
    }

    public double getEndDate() {
	return endDate;
    }
    
    public void setEndDate(double endDate){
	this.endDate=endDate;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue+space+
	    aprioriConstraint+space+startDate+space+endDate;
	return retString;
    }    

}

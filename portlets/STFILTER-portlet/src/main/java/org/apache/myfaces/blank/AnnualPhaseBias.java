package org.apache.myfaces.blank;

public class AnnualPhaseBias extends EstimateParameter {
    
    double startDate;
    double periodLength;
    String space="  ";

    public double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(double startDate){
	this.startDate=startDate;
    }

    public double getPeriodLength() {
	return periodLength;
    }
    
    public void setPeriodLength(double periodLength){
	this.periodLength=periodLength;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue+space+
	    aprioriConstraint+space+periodLength+space+startDate;
	return retString;
    }    

}

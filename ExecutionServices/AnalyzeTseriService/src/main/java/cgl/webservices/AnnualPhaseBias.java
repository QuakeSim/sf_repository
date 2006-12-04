package cgl.webservices;

public class AnnualPhaseBias extends EstimateParameter {
    
    Double startDate;
    Double periodLength;
    String space="  ";

    public Double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(Double startDate){
	this.startDate=startDate;
    }

    public Double getPeriodLength() {
	return periodLength;
    }
    
    public void setPeriodLength(Double periodLength){
	this.periodLength=periodLength;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue.toString()+space+
	    aprioriConstraint.toString()+space+periodLength.toString()+space+startDate.toString();
	return retString;
    }    

}

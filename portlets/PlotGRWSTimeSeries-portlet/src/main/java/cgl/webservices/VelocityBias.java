package cgl.webservices;

public class VelocityBias extends EstimateParameter {
    
    Double startDate;
    Double endDate;
    String space="  ";

    public Double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(Double startDate){
	this.startDate=startDate;
    }

    public Double getEndDate() {
	return endDate;
    }
    
    public void setEndDate(Double endDate){
	this.endDate=endDate;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue.toString()+space+
	    aprioriConstraint.toString()+space+startDate.toString()+space+endDate.toString();
	return retString;
    }    

}

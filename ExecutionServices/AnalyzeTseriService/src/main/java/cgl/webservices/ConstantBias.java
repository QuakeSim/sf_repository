/**
 * There are 
 */

package cgl.webservices;

public abstract class ConstantBias extends EstimateParameter {
    
    //These are additional fields.  See also the parent.
    Double startDate;
    String space="  ";
    
    public Double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(Double startDate){
	this.startDate=startDate;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue.toString()+space+
	    aprioriConstraint.toString()+space+startDate.toString();
	return retString;
    }    

}

/**
 * There are 
 */

package org.apache.myfaces.blank;

public abstract class ConstantBias extends EstimateParameter {
    
    //These are additional fields.  See also the parent.
    double startDate;
    String space="  ";
    
    public double getStartDate() {
	return startDate;
    }
    
    public void setStartDate(double startDate){
	this.startDate=startDate;
    }

    public String printStringLine() {
	String retString=parameterType+space+aprioriValue+space+
	    aprioriConstraint+space+startDate;
	return retString;
    }    

}

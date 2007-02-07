/**
 * This class is the parent for all the ST Filter parameter
 * types.  See http://gipsy.jpl.nasa.gov/qoca/advclass/tsa_plist.html.
 * This classs just defines the minimum required get and set methods
 * and their associated fields.  
 */
package cgl.webservices;

public abstract class EstimateParameter {
    
    //The following are always supported by all children.
    int parameterType;
    String parameterFullName;
    Double aprioriValue;
    Double aprioriConstraint;
    
    //The following are optional properties that are set to 
    //some N/A value.  These are overridden as necessary by
    //the children.
    Double startDate;
    Double endDate;
    Double periodLength;
    
    public EstimateParameter() {}

    //These are the standard required methods.
    public int getParameterType() {
	return parameterType;
    }

    public void setParameterType(int parameterType) {
	this.parameterType=parameterType;
    }

    public String getParameterFullName() {
	return parameterFullName;
    }
    
    public void setParameterFullName(String parameterFullName) {
	this.parameterFullName=parameterFullName;
    }
    
    public Double getAprioriValue() {
	return aprioriValue;
    }
    
    public void setAprioriValue(Double aprioriValue) {
	this.aprioriValue=aprioriValue;
    }

    public Double getAprioriConstraint() {
	return aprioriConstraint;
    }
    
    public void setAprioriConstraint(Double aprioriConstraint) {
	this.aprioriConstraint=aprioriConstraint;
    }
    
    // This method is overridden to simplify line printing in
    // the parameter file.
    public abstract String printStringLine();
    
    public Double getStartDate() {
	return null;
    }

    public Double getEndDate() {
	return null;
    }

    public Double getPeriodLength() {
	return null;
    }

}

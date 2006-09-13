/**
 * This class is the parent for all the ST Filter parameter
 * types.  See http://gipsy.jpl.nasa.gov/qoca/advclass/tsa_plist.html.
 * This classs just defines the minimum required get and set methods
 * and their associated fields.  
 */
package org.apache.myfaces.blank;

public abstract class EstimateParameter {
    
    int parameterType;
    String parameterFullName;
    double aprioriValue;
    double aprioriConstraint;

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
    
    public double getAprioriValue() {
	return aprioriValue;
    }
    
    public void setAprioriValue(double aprioriValue) {
	this.aprioriValue=aprioriValue;
    }

    public double getAprioriConstraint() {
	return aprioriConstraint;
    }
    
    public void setAprioriConstraint(double aprioriConstraint) {
	this.aprioriConstraint=aprioriConstraint;
    }
    
    // This method is overridden to simplify line printing in
    // the parameter file.
    public abstract String printStringLine();
    

}

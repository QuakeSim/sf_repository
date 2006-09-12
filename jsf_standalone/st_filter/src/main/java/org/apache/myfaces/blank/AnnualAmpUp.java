package org.apache.myfaces.blank;

public class AnnualAmpUp extends AnnualAmpBias {
    
    public AnnualAmpUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=18;
	parameterFullName="Constant bias east (mm)";
	aprioriValue=0.0;
	aprioriConstraint=50.0;
	startDate=2002.0;
	periodLength=365.25;
    }
}

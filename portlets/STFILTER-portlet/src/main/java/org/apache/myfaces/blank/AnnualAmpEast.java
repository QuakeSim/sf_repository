package org.apache.myfaces.blank;

public class AnnualAmpEast extends AnnualAmpBias {
    
    public AnnualAmpEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=16;
	parameterFullName="Constant bias east (mm)";
	aprioriValue=0.0;
	aprioriConstraint=3.0;
	startDate=2002.0;
	periodLength=365.25;
    }
}

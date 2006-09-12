package org.apache.myfaces.blank;

public class SemiannualAmpUp extends SemiannualAmpBias {
    
    public SemiannualAmpUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=24;
	parameterFullName="Constant bias north (mm)";
	aprioriValue=0.0;
	aprioriConstraint=3.0;
	startDate=2002.0;
	periodLength=182.625;
    }
}

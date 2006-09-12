package org.apache.myfaces.blank;

public class AnnualPhaseUp extends AnnualPhaseBias {
    
    public AnnualPhaseUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=19;
	parameterFullName="Constant bias up (mm)";
	aprioriValue=0.0;
	aprioriConstraint=3.0;
	startDate=2002.0;
	periodLength=365.25;
    }
}

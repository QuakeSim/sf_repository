package org.apache.myfaces.blank;

public class EpisodicUp extends EpisodicBias {
    
    public EpisodicUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=9;
	parameterFullName="Constant bias up (mm)";
	aprioriValue=0.0;
	aprioriConstraint=50.0;
	startDate=2002.0;
	endDate=2004.0;
    }
}

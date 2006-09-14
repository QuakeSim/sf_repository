package org.apache.myfaces.blank;

public class EpisodicUp extends EpisodicBias {
    
    public EpisodicUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=9;
	parameterFullName="Constant bias up (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2002.0);
	endDate=new Double(2004.0);
    }
}

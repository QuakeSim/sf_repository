package org.apache.myfaces.blank;

public class EpisodicEast extends EpisodicBias {
    
    public EpisodicEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=7;
	parameterFullName="Constant bias east (mm)";
	aprioriValue=0.0;
	aprioriConstraint=50.0;
	startDate=2002.0;
	endDate=2004.0;
    }
}

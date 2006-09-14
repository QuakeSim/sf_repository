package org.apache.myfaces.blank;

public class SemiannualAmpNorth extends SemiannualAmpBias {
    
    public SemiannualAmpNorth(){
	//Set the default values of these fields inherited from the parent
	parameterType=23;
	parameterFullName="Constant bias north (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(3.0);
	startDate=new Double(2002.0);
	periodLength=new Double(182.625);
    }
}

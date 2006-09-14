package org.apache.myfaces.blank;

public class VelocityNorth extends VelocityBias {
    
    public VelocityNorth(){
	//Set the default values of these fields inherited from the parent
	parameterType=5;
	parameterFullName="Velocity north (mm/yr)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2002.0);
	endDate=new Double(2004.0);
    }
}

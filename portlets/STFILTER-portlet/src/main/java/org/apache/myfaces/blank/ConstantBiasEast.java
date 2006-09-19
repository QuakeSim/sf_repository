package org.apache.myfaces.blank;

public class ConstantBiasEast extends ConstantBias {
    
    public ConstantBiasEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=1;
	parameterFullName="Constant bias east (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2004.0);
    }   
}

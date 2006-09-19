package org.apache.myfaces.blank;

public class ConstantBiasUp extends ConstantBias {
    public ConstantBiasUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=3;
	parameterFullName="Constant bias up (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2004.0);
    }
}

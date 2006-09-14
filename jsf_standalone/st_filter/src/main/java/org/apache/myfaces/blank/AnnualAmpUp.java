package org.apache.myfaces.blank;

public class AnnualAmpUp extends AnnualAmpBias {
    
    public AnnualAmpUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=18;
	parameterFullName="Constant bias east (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(3.0);
	startDate=new Double(2002.0);
	periodLength=new Double(365.25);
    }
}

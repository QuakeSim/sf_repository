package cgl.webservices;

public class AnnualAmpEast extends AnnualAmpBias {
    
    public AnnualAmpEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=16;
	parameterFullName="Annual amp east (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(3.0);
	startDate=new Double(2002.0);
	periodLength=new Double(365.25);
    }
}

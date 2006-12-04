package cgl.webservices;

public class AnnualAmpNorth extends AnnualAmpBias {
    
    public AnnualAmpNorth(){
	//Set the default values of these fields inherited from the parent
	parameterType=17;
	parameterFullName="Annual amp north (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(3.0);
	startDate=new Double(2002.0);
	periodLength=new Double(365.25);
    }
}

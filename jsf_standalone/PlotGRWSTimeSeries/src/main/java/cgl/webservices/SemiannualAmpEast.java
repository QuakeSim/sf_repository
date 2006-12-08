package cgl.webservices;

public class SemiannualAmpEast extends SemiannualAmpBias {
    
    public SemiannualAmpEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=22;
	parameterFullName="Semiannual amp east (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(3.0);
	startDate=new Double(2002.0);
	periodLength=new Double(182.625);
    }
}

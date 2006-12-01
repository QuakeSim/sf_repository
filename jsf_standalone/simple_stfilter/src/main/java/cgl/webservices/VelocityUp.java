package cgl.webservices;

public class VelocityUp extends VelocityBias {
    
    public VelocityUp(){
	//Set the default values of these fields inherited from the parent
	parameterType=6;
	parameterFullName="Velocity up (mm/yr)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2002.0);
	endDate=new Double(2004.0);

    }
}

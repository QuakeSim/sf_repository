package cgl.webservices;

public class ConstantBiasNorth extends ConstantBias {
    
    public ConstantBiasNorth(){
	//Set the default values of these fields inherited from the parent
	parameterType=2;
	parameterFullName="Constant bias north (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2004.0);

    }
}

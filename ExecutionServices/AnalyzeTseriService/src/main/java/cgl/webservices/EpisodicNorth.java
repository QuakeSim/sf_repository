package cgl.webservices;

public class EpisodicNorth extends EpisodicBias {
    
    public EpisodicNorth(){
	//Set the default values of these fields inherited from the parent
	parameterType=8;
	parameterFullName="Episodic north (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2002.0);
	endDate=new Double(2004.0);
    }
}

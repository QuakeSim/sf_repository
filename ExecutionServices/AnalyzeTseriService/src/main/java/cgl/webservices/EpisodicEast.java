package cgl.webservices;

public class EpisodicEast extends EpisodicBias {
    
    public EpisodicEast(){
	//Set the default values of these fields inherited from the parent
	parameterType=7;
	parameterFullName="Episodic east (mm)";
	aprioriValue=new Double(0.0);
	aprioriConstraint=new Double(50.0);
	startDate=new Double(2002.0);
	endDate=new Double(2004.0);
    }

}

/**
* This class is used to create and manage the station lists..
*/
package org.apache.myfaces.blank;

import java.util.Vector;

public class StationParamList {

    EstimateParameter constantBiasEast, constantBiasNorth, constantBiasUp;
    EstimateParameter velocityEast, velocityNorth, velocityUp;
    EstimateParameter episodicEast,episodicNorth, episodicUp;
    EstimateParameter annualAmpEast, annualAmpNorth, annualAmpUp;
    EstimateParameter annualPhaseEast, annualPhaseNorth, annualPhaseUp;
    EstimateParameter semiannualAmpEast, semiannualAmpNorth, semiannualAmpUp;
    
    Vector stationParamList=null;
    
    public StationParamList() {
	stationParamList=new Vector();
	initializeEstimateParams();
    }
    
    public Vector getStationParamList(){
	return stationParamList;
    }
    
    public void setStationParamList(Vector stationParamList) {
	this.stationParamList=stationParamList;
    }

    public void initializeEstimateParams() {
	constantBiasEast=new ConstantBiasEast();
	constantBiasNorth=new ConstantBiasNorth();
	constantBiasUp=new ConstantBiasUp();
	velocityEast=new VelocityEast();
	velocityNorth=new VelocityNorth();
	velocityUp=new VelocityUp();
	episodicEast=new EpisodicEast();
	episodicNorth=new EpisodicNorth();
	episodicUp=new EpisodicUp();
	annualAmpEast=new AnnualAmpEast();
	annualAmpNorth=new AnnualAmpNorth();
	annualAmpUp=new AnnualAmpUp();
	annualPhaseEast=new AnnualPhaseEast();
	annualPhaseNorth=new AnnualPhaseNorth();
	annualPhaseUp=new AnnualPhaseUp();
	semiannualAmpEast=new SemiannualAmpEast();
	semiannualAmpNorth=new SemiannualAmpNorth();
	semiannualAmpUp=new SemiannualAmpUp();
    }
}

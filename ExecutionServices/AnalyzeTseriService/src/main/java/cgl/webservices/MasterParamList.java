/**
 * This is basically a specialized StationParamList that contains a 
 * list of all the available objects.  These do not include the default
 * values, which are set in the AllSitesParamList class.
 */
package cgl.webservices;

import java.util.Vector;

public class MasterParamList extends StationParamList {
    
    public MasterParamList() {
	super();
	initMasterParamList();
    }

    /**
     * The stationParamList vector contains one of each
     * parameter type. 
     */
    public void initMasterParamList() {
	System.out.println("Initializing master list");
	stationParamList.add(episodicEast);
	stationParamList.add(episodicNorth);
	stationParamList.add(episodicUp);
//	stationParamList.add(annualAmpEast);
//	stationParamList.add(annualAmpNorth);
//	stationParamList.add(annualAmpUp);
//	stationParamList.add(annualPhaseEast);
//	stationParamList.add(annualPhaseNorth);
//	stationParamList.add(annualPhaseUp);
//	stationParamList.add(semiannualAmpEast);
//	stationParamList.add(semiannualAmpNorth);
//	stationParamList.add(semiannualAmpUp);
    }
}

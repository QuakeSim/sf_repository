/**
 * These are the "all_sites" parameters, which are the default values.
 */
package org.apache.myfaces.blank;

import java.util.Vector;

public class AllSitesParamList extends StationParamList {
    
    public AllSitesParamList() {
	super();
	initAllSitesParamList();
    }

    /**
     * The stationParamList vector contains one of each
     * parameter type. 
     */
    public void initAllSitesParamList() {
	System.out.println("Initializing allstation list");
	stationParamList.add(constantBiasEast);
	stationParamList.add(constantBiasNorth);
	stationParamList.add(constantBiasUp);
	stationParamList.add(velocityEast);
	stationParamList.add(velocityNorth);
	stationParamList.add(velocityUp);
    }
}

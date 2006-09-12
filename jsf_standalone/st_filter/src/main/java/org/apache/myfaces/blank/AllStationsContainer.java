/**
 * This container holds the default "all_site" estimated parameters.
 */

package org.apache.myfaces.blank;

public class AllStationsContainer extends StationContainer {
    public AllStationsContainer() {
	setSiteName("all_site");
	//	addDefaultEstParams();
    }

    public void addDefaultEstParams() {
	addEstParameter(new ConstantBiasEast());
	addEstParameter(new ConstantBiasNorth());
	addEstParameter(new ConstantBiasUp());
	addEstParameter(new VelocityEast());
	addEstParameter(new VelocityNorth());
	addEstParameter(new VelocityUp());
    }
}

/**
 * This container holds the estimate parameters associated with the 
 * particular station that is being analyzed.  These params are in
 * addition to the default "all_station" params.
 */

package cgl.webservices;

public class MyStationContainer extends StationContainer {

    public MyStationContainer() {
	super();
    }

    public MyStationContainer(String stationName){
	setSiteName(stationName);
    }
}

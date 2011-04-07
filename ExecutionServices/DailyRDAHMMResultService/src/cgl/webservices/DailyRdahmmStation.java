package cgl.webservices;

import java.util.HashMap;

public class DailyRdahmmStation {
	
	String stationID;
	float latitude;
	float longitude;
	int[] stateChanges;
	int[] noDataSections;
	HashMap<Long, Byte> stateSequence;
	
	public DailyRdahmmStation() {
		stationID = null;
		latitude = 0;
		longitude = 0;
		stateChanges = null;
		noDataSections = null;
		stateSequence = null;
	}
}

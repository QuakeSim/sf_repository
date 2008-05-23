package cgl.webservices;

public class StateChange {
	int oldState;
	int newState;
	int changeDateNum;	//number of days passed since 1970-1-1 to the date the change happens

	public StateChange() {
		oldState = newState = changeDateNum = -1;
	}
}

package cgl.webservices;

public class NoDataSection {
	
	int beginDateNum;	//number of days passed since 1970-1-1 to the beginning date of the section
	int endDateNum;		//number of days passed since 1970-1-1 to the end date of the section
	
	public NoDataSection() {
		beginDateNum = endDateNum = -1;
	}
}

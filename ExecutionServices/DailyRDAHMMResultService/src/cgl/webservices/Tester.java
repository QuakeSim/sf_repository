package cgl.webservices;

import java.util.Calendar;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sDate = "1995-10-13";
		String resUrl = "http://gf13.ucs.indiana.edu:8080//rdahmmexec/daily/SOPAC_FILL/station-status-change-SOPAC_FILL.xml";
		
		DailyRdahmmResultService s = new DailyRdahmmResultService();
		System.out.println(s.getDataLatestDate(resUrl));
		String str1 = s.calcStationColors(sDate, resUrl);
		System.out.println(str1);
	}

}

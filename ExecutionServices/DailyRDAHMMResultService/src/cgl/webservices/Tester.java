package cgl.webservices;

import java.util.Calendar;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sDate = "1995-10-13";
		
		DailyRdahmmResultService s = new DailyRdahmmResultService();
		System.out.println(s.getDataLatestDate());
		String str1 = s.calcStationColors(sDate);
		System.out.println(str1);
	}

}

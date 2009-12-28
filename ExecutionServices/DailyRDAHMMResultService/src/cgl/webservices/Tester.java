package cgl.webservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sDate06 = "2006-10-01";
		String sDate07 = "2007-10-01";
		String resUrlSopac = "http://gf13.ucs.indiana.edu:8080//rdahmmexec/daily/SOPAC_FILL/station-status-change-SOPAC_FILL.xml";
		String resUrlJpl = "http://gf13.ucs.indiana.edu:8080//rdahmmexec/daily/JPL_FILL/station-status-change-JPL_FILL.xml";
		
		String pathSopac06 = "sopac06.txt";
		String pathSopac07 = "sopac07.txt";
		String pathJpl06 = "jpl06.txt";
		String pathJpl07 = "jpl07.txt";
		
		/*
		Calendar date06 = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		UtilSet.setDateByString(date06, sDate06);
		int t = (int) (date06.getTimeInMillis() / DailyRdahmmResultAnalyzer.DAY_MILLI);
		date06.setTimeInMillis((t) * DailyRdahmmResultAnalyzer.DAY_MILLI);
		System.out.println(UtilSet.getDateString(date06) + " " 
				+ date06.get(Calendar.HOUR_OF_DAY) + ":" + date06.get(Calendar.MINUTE)
				+ ":" + date06.get(Calendar.SECOND) + "." + date06.get(Calendar.MILLISECOND));
		*/
		
		DailyRdahmmResultService s = new DailyRdahmmResultService();
		checkInputDate(sDate06, resUrlSopac, pathSopac06, s);
		checkInputDate(sDate07, resUrlSopac, pathSopac07, s);
		checkInputDate(sDate06, resUrlJpl, pathJpl06, s);
		checkInputDate(sDate07, resUrlJpl, pathJpl07, s);
	}

	public static void checkInputDate(String date, String url, String path, DailyRdahmmResultService s) {
		String sopacInputPathPattern = "/globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/daily/" +
									"SOPAC_FILL/daily_project_<stationId>_2009-04-15/daily_project_<stationId>_2009-04-15.all.raw"; 
		String jplInputPathPattern = "/globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/daily/" +
									"JPL_FILL/daily_project_<stationId>_2009-04-15/daily_project_<stationId>_2009-04-15.all.raw"; 

		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(path));
			String colors = s.calcStationColors(date, url);
			DailyRdahmmResultAnalyzer analyzer = s.analyzerTable.get(url);
			for (int i=0; i<analyzer.stationArray.length; i++) {
				char color = colors.charAt(i * 2);
				if (color == '1') {
					String stationId = analyzer.stationArray[i].stationID;
					String line = "";
					String inputPath = "";
					if (path.startsWith("sopac")) {
						inputPath = sopacInputPathPattern.replaceAll("<stationId>", stationId);
					} else {
						inputPath = jplInputPathPattern.replaceAll("<stationId>", stationId);
					}
					
					line = UtilSet.findLineInFile(new File(inputPath), stationId);
					int pos = line.indexOf(' ');
					pos = line.indexOf(' ', pos+1);
					String newLine = line.substring(0, pos);
					pw.println(newLine);
				}
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

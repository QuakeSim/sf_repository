package cgl.webservices;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyRdahmmResultsBean db = new DailyRdahmmResultsBean();
		db.setXmlResPath("/home/gaoxm/projects/test/QuakeSim2/portal_deploy/" +
				"apache-tomcat-5.5.20/webapps/DailyRDAHMM-portlet/station-status-change-rss.xml");
		db.setXmlResURL("http://gf13.ucs.indiana.edu:8080//DailyRDAHMM-portlet/station-status-change-rss.xml");
		System.out.println("path and url set....");
		db.setDate("2008-3-8");
		db.setRegionLimit("36.2636,-123.4094;40.4637,-115.1697");
		long time1 = System.currentTimeMillis();
		db.calcStationColors();
		long time2 = System.currentTimeMillis();		
		String res = db.getColorResult();
		System.out.println(res.length() + " time used:" + (time2 - time1));
		System.out.println("latest date with data: " + db.getDataLatestDate());
	}

}

package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class DailyRdahmmResultService {
	
	static final String PROP_FILE_NAME = "dailyRdahmmResultService.properties";	
	static String defaultJplResultUrl;
	static String defaultSopacResultUrl;
	static String destPlotDir;
	static String plotUrlPattern;
	static String plotBinPath;
	protected HashMap<String, DailyRdahmmResultAnalyzer> analyzerTable;
	
	
	public DailyRdahmmResultService() {
		analyzerTable = new HashMap<String, DailyRdahmmResultAnalyzer>();
		try {
			URI propFileUri = getClass().getClassLoader().getResource(PROP_FILE_NAME).toURI();
			Properties propConfig = new Properties();
			propConfig.load(new FileInputStream(new File(propFileUri)));
			
			defaultJplResultUrl = propConfig.getProperty("defaultJplResultUrl");
			defaultSopacResultUrl = propConfig.getProperty("defaultSopacResultUrl");
			destPlotDir = propConfig.getProperty("destPlotDir");
			plotUrlPattern = propConfig.getProperty("plotUrlPattern");
			plotBinPath = propConfig.getProperty("plotBinPath");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String calcStationColors(String date, String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.calcStationColors(date);
	}
	
	public String getLatLongForStation(String stationId, String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.getLatLongForStation(stationId);
	}
	
	public String getDataLatestDate(String resUrl) {
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		return analyzer.getDataLatestDate();
	}
	
	public String getStateChangeNumberPlot(String dataSource, float minLat, float maxLat, float minLong, float maxLong) {
		System.out.println("minLat:" + minLat + " maxLat:" + maxLat + " minLong:" + minLong + " maxLong:" + maxLong);
		String resUrl = "";
		if (dataSource.equals("JPL"))
			resUrl = defaultJplResultUrl;
		else if (dataSource.equals("SOPAC"))
			resUrl = defaultSopacResultUrl;
		else
			return "";
		
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		// count state changes for all stations within the bounding box
		HashMap<String, Integer> stateChangeNums = new HashMap<String, Integer>();
		Calendar tmpDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		for (int i=0; i<analyzer.stationArray.length; i++) {
			if (analyzer.stationArray[i].latitude < minLat || analyzer.stationArray[i].latitude > maxLat
				|| analyzer.stationArray[i].longitude < minLong || analyzer.stationArray[i].longitude > maxLong ){
				continue;
			}
			
			int[] stateChanges = analyzer.stationArray[i].stateChanges;
			if (stateChanges == null) {
				continue;
			}
			for (int j=0; j<stateChanges.length; j+=3) {
				tmpDate.setTimeInMillis(stateChanges[j] * DailyRdahmmResultAnalyzer.DAY_MILLI);
				String dateStr = UtilSet.getDateString(tmpDate);
				Integer oldCount = stateChangeNums.get(dateStr);
				if (oldCount == null) {
					oldCount = 0;
				}
				stateChangeNums.put(dateStr, oldCount+1);
			}
		}
		
		// save the counts into file
		File destDirFile = new File(destPlotDir);
		if (!destDirFile.exists()) {
			if (!destDirFile.mkdirs())
				return "";
		}
		long timeStamp = System.currentTimeMillis();
		String fileName = "scn_" + timeStamp + ".txt";
		String txtPath = destPlotDir + File.separator + fileName;
		Calendar calTmp = UtilSet.getDateFromString("1994-01-01");
		Calendar calToday = Calendar.getInstance();
		calToday.set(Calendar.HOUR_OF_DAY, 12);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);
		
		try {
			FileWriter fw = new FileWriter(txtPath, false);
			while (calTmp.compareTo(calToday) <= 0) {
				String strDate = UtilSet.getDateString(calTmp);
				fw.write(strDate + " ");
				if (stateChangeNums.containsKey(strDate)) {
					fw.write(stateChangeNums.get(strDate).intValue() + "\n");
				} else {
					fw.write("0\n");
				}				
				calTmp.set(Calendar.DATE, calTmp.get(Calendar.DATE) + 1);
			}
			
			fw.flush();
			fw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// do plot
		int idx = plotBinPath.lastIndexOf(File.separatorChar);
		String workDir = plotBinPath.substring(0, idx);
		String res = UtilSet.exec(plotBinPath + " " + txtPath, new File(workDir));
		System.out.println("output from plotting script: " + res);
		return plotUrlPattern.replaceAll("<fileName>", fileName + ".png");
	}
	
	/** 
	 * call a http service and return the content. used by javascripts for accessing services
	 * in different domains 
	 * @param serviceUrl
	 * @return
	 */
	public String proxyCallHttpService(String serviceUrl) {
		System.out.println("url: " + serviceUrl);
		return UtilSet.callHttpService(serviceUrl);
	}
	
	// add an analyzer to the table of analyzers
	protected DailyRdahmmResultAnalyzer addAnalyzer(String xmlResUrl) {
		DailyRdahmmResultAnalyzer analyzer = new DailyRdahmmResultAnalyzer(xmlResUrl);
		analyzerTable.put(xmlResUrl, analyzer);
		return analyzer;
	}
	
}

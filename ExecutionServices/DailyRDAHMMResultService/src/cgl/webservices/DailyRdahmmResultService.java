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
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DailyRdahmmResultService {
	
	static final String PROP_FILE_NAME = "dailyRdahmmResultService.properties";	
	static String defaultJplResultUrl;
	static String defaultSopacResultUrl;
	static String destPlotDir;
	static String plotUrlPattern;
	static String plotBinPath;
	static String destKmlDir;
	static String kmlUrlPattern;
	static String popUpWinHtml;
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
			destKmlDir = propConfig.getProperty("destKmlDir");
			kmlUrlPattern = propConfig.getProperty("kmlUrlPattern");
			popUpWinHtml = propConfig.getProperty("popUpWinHtml");
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
	 * make a kml file for a given date, and return the url of the kml
	 * @param dateStr
	 * @param resUrl
	 * @return
	 */
	public String getKmlForDate(String dateStr, String resUrl) {
		String contextGroup = "SOPAC";
		if (resUrl.toUpperCase().indexOf("JPL") >= 0) {
			contextGroup = "JPL";
		}
		String preTreat = "FILL";
		if (resUrl.toUpperCase().indexOf("NOFILL") >= 0) {
			preTreat = "NOFILL";
		}
		Calendar today = Calendar.getInstance();
		String todayStr = UtilSet.getDateString(today);
		// dateStr is the date this kml file describes, and todaystr is the date when the kml is created 
		String kmlFileName = contextGroup + "_" + preTreat + "_" + dateStr + "." + todayStr + ".kml";
		String kmlPath = destKmlDir + File.separator + kmlFileName;
		File kmlFile = new File(kmlPath);
		if (kmlFile.exists() && kmlFile.isFile()) {
			return kmlUrlPattern.replace("<fileName>", kmlFileName);
		}
		
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		String colors = analyzer.calcStationColors(dateStr);
		DailyRdahmmStation[] stations = analyzer.stationArray;
		
		Document kmlDoc = DocumentHelper.createDocument();
		Element eleKml = kmlDoc.addElement("kml");
		eleKml.addAttribute("xmlns", "http://www.opengis.net/kml/2.2");
		Element eleDoc = eleKml.addElement("Document");
		String[] styleIds = {"greenIconStyle", "redIconStyle", "yellowIconStyle", "blueIconStyle", "grayIconStyle"};
		String[] colorStrs = {"ff00ff00", "ff0000ff", "ff00ffff", "ffff0000", "ff7f7f7f"};
		String[] urls = {"http://labs.google.com/ridefinder/images/mm_20_green.png",
						"http://labs.google.com/ridefinder/images/mm_20_red.png",
						"http://labs.google.com/ridefinder/images/mm_20_yellow.png",
						"http://labs.google.com/ridefinder/images/mm_20_blue.png",
						"http://labs.google.com/ridefinder/images/mm_20_gray.png"};
		for (int i=0; i<5; i++) {
			Element eleStyle = eleDoc.addElement("Style");
			eleStyle.addAttribute("id", styleIds[i]);
			Element eleIconStyle = eleStyle.addElement("IconStyle");
			Element eleScale = eleIconStyle.addElement("scale");
			eleScale.setText("0.7");
			Element eleColor = eleIconStyle.addElement("color");
			eleColor.setText(colorStrs[i]);
			Element eleIcon = eleIconStyle.addElement("Icon");
			Element eleHref = eleIcon.addElement("href");
			eleHref.setText(urls[i]);
		}
		
		for (int i=0; i<stations.length; i++) {
			Element eleMark = eleDoc.addElement("Placemark");
			Element eleName = eleMark.addElement("name");
			eleName.setText(stations[i].stationID);
			Element elePoint = eleMark.addElement("Point");
			Element eleCoord = elePoint.addElement("coordinates");
			eleCoord.setText(stations[i].longitude + "," + stations[i].latitude);
			Element eleStyleUrl = eleMark.addElement("styleUrl");
			int colorNum = Integer.valueOf(colors.substring(2*i, 2*i+1));
			eleStyleUrl.setText("#" + styleIds[colorNum]);
			String fileUrlPrefix = analyzer.urlPattern.replace("{!station-id!}", stations[i].stationID)
							+ "/" + analyzer.dirPattern.replace("{!station-id!}", stations[i].stationID) + "/";
			String rawUrl = fileUrlPrefix + analyzer.rawInputPattern.replace("{!station-id!}", stations[i].stationID);
			String qUrl = fileUrlPrefix + analyzer.qPattern.replace("{!station-id!}", stations[i].stationID);
			String popUpHtml = popUpWinHtml;
			popUpHtml = popUpHtml.replace("{!rawFileURL!}", rawUrl);
			popUpHtml = popUpHtml.replace("{!qFileURL!}", qUrl);
			Element eleDesc = eleMark.addElement("description");
			//eleDesc.setText("<![CDATA[" + popUpHtml + "]]");
			eleDesc.setText(popUpHtml);
		}
			
		try {
			FileWriter fw = new FileWriter(kmlPath);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(kmlDoc);
			writer.close();
			fw.close();

			return kmlUrlPattern.replace("<fileName>", kmlFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
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

package cgl.webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
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
	static String kmlObsoleteNamePattern;
	static String plotObsoleteNamePattern;
	static long kmlObsoleteThreshold;
	static long plotObsoleteThreshold;
	static long kmlCleanUpPeriod;
	static long plotCleanUpPeriod;
	protected HashMap<String, DailyRdahmmResultAnalyzer> analyzerTable;
	protected long lastKmlDeletionTime;
	protected long lastPlotDeletionTime;
	
	public DailyRdahmmResultService() {
		analyzerTable = new HashMap<String, DailyRdahmmResultAnalyzer>();
		lastKmlDeletionTime = 0;
		lastPlotDeletionTime = 0;
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
			kmlObsoleteNamePattern = propConfig.getProperty("kmlObsoleteNameRegPattern");
			plotObsoleteNamePattern = propConfig.getProperty("plotObsoleteNameRegPattern");
			kmlObsoleteThreshold = Long.parseLong(propConfig.getProperty("kmlObsoleteThresholdDays")) 
									* ObsoleteFileDeleter.DAY_MILISEC_COUNT;
			plotObsoleteThreshold = Long.parseLong(propConfig.getProperty("plotObsoleteThresholdDays")) 
									* ObsoleteFileDeleter.DAY_MILISEC_COUNT;
			kmlCleanUpPeriod = Long.parseLong(propConfig.getProperty("kmlCleanUpPeriodDays")) 
								* ObsoleteFileDeleter.DAY_MILISEC_COUNT;
			plotCleanUpPeriod = Long.parseLong(propConfig.getProperty("plotCleanUpPeriodDays")) 
								* ObsoleteFileDeleter.DAY_MILISEC_COUNT;
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
		deleteObsoletePlot();
		
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
		deleteObsoleteKml();
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
		dateStr = UtilSet.getDateString(UtilSet.getDateFromString(dateStr));
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
			eleStyle.addElement("LabelStyle").addElement("scale").setText("0");
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
		
		// add the "TimeSpan" element
		Element eleTP = eleDoc.addElement("TimeSpan");
		eleTP.addElement("begin").setText(dateStr + "T00:00:00Z");
		eleTP.addElement("end").setText(dateStr + "T23:59:59Z");
			
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
	 * make a kml file for time between fromDateStr and toDateStr, and return the url of the kml
	 * @param fromDateStr
	 * @param toDateStr
	 * @param resUrl
	 * @return
	 */
	public String getKmlForDateRange(String fromDateStr, String toDateStr, String resUrl) {
		deleteObsoleteKml();
		String contextGroup = "SOPAC";
		if (resUrl.toUpperCase().indexOf("JPL") >= 0) {
			contextGroup = "JPL";
		}
		String preTreat = "FILL";
		if (resUrl.toUpperCase().indexOf("NOFILL") >= 0) {
			preTreat = "NOFILL";
		}
		
		Calendar calFrom = UtilSet.getDateFromString(fromDateStr);
		Calendar calTo = UtilSet.getDateFromString(toDateStr);
		if (calFrom.compareTo(calTo) > 0) {
			Calendar calTmp = calFrom;
			calFrom = calTo;
			calTo = calTmp;
		}
		Calendar today = Calendar.getInstance();
		String todayStr = UtilSet.getDateString(today);
		fromDateStr = UtilSet.getDateString(calFrom);
		toDateStr = UtilSet.getDateString(calTo);
		 
		String kmlFileName = contextGroup + "_" + preTreat + "_" + fromDateStr + "to" + toDateStr + "." + todayStr + ".kml";
		String kmlPath = destKmlDir + File.separator + kmlFileName;
		String kmzFileName = kmlFileName + ".zip";
		String kmzPath = destKmlDir + File.separator + kmzFileName;
		File kmzFile = new File(kmzPath);
		if (kmzFile.exists() && kmzFile.isFile()) {
			return kmlUrlPattern.replace("<fileName>", kmzFileName);
		}
		File kmlFile = new File(kmlPath);
		if (kmlFile.exists() && kmlFile.isFile()) {
			return kmlUrlPattern.replace("<fileName>", kmlFileName);
		}
		
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		DailyRdahmmStation[] stations = analyzer.stationArray;
		
		Document kmlDoc = DocumentHelper.createDocument();
		Element eleKml = kmlDoc.addElement("kml");
		eleKml.addAttribute("xmlns", "http://www.opengis.net/kml/2.2");
		Element eleFolder = eleKml.addElement("Folder");
		eleFolder.addElement("open").setText("1");
		Element eleDoc = eleFolder.addElement("Document");
		eleDoc.addElement("name").setText("Style Definitions");
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
			eleStyle.addElement("LabelStyle").addElement("scale").setText("0");
		}
		
		while (calFrom.compareTo(calTo) <= 0) {
			eleDoc = eleFolder.addElement("Document");
			String dateStr = UtilSet.getDateString(calFrom);
			eleDoc.addElement("name").setText(contextGroup + "_" + preTreat + "_" + dateStr);
			// add the "TimeSpan" element
			Element eleTP = eleDoc.addElement("TimeSpan");
			eleTP.addElement("begin").setText(dateStr + "T00:00:00Z");
			eleTP.addElement("end").setText(dateStr + "T23:59:59Z");
			String colors = analyzer.calcStationColors(dateStr);
			for (int i = 0; i < stations.length; i++) {
				Element eleMark = eleDoc.addElement("Placemark");
				Element eleName = eleMark.addElement("name");
				eleName.setText(stations[i].stationID);
				Element elePoint = eleMark.addElement("Point");
				Element eleCoord = elePoint.addElement("coordinates");
				eleCoord.setText(stations[i].longitude + "," + stations[i].latitude);
				Element eleStyleUrl = eleMark.addElement("styleUrl");
				int colorNum = Integer.valueOf(colors.substring(2*i, 2*i+1));
				eleStyleUrl.setText("#" + styleIds[colorNum]);
				if (calFrom.equals(calTo)) {
					String fileUrlPrefix = analyzer.urlPattern.replace("{!station-id!}", stations[i].stationID) + "/" 
											+ analyzer.dirPattern.replace("{!station-id!}", stations[i].stationID) + "/";
					String rawUrl = fileUrlPrefix + analyzer.rawInputPattern.replace("{!station-id!}", stations[i].stationID);
					String qUrl = fileUrlPrefix	+ analyzer.qPattern.replace("{!station-id!}", stations[i].stationID);
					String popUpHtml = popUpWinHtml;
					popUpHtml = popUpHtml.replace("{!rawFileURL!}", rawUrl);
					popUpHtml = popUpHtml.replace("{!qFileURL!}", qUrl);
					Element eleDesc = eleMark.addElement("description");
					// eleDesc.setText("<![CDATA[" + popUpHtml + "]]");
					eleDesc.setText(popUpHtml);
				}
			}
			
			calFrom.set(Calendar.DATE, calFrom.get(Calendar.DATE) + 1);
		}
		
		try {
			FileWriter fw = new FileWriter(kmlPath);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(kmlDoc);
			writer.close();
			fw.close();
			
			String res = UtilSet.exec("zip -D " + kmzPath + " " + kmlFileName, new File(destKmlDir));
			res = res.toLowerCase();
			if (res.indexOf("error") >= 0 || res.indexOf("not found") >= 0) {
				return kmlUrlPattern.replace("<fileName>", kmlFileName);
			} else {
				File fileKml = new File(kmlPath);
				fileKml.delete();
				return kmlUrlPattern.replace("<fileName>", kmzFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * make a kmz file for time between fromDateStr and toDateStr, and return the url of the kml
	 * @param fromDateStr
	 * @param toDateStr
	 * @param resUrl
	 * @return
	 */
	public String getKmlForDateRange2(String fromDateStr, String toDateStr, String resUrl) {
		deleteObsoleteKml();
		String contextGroup = "SOPAC";
		if (resUrl.toUpperCase().indexOf("JPL") >= 0) {
			contextGroup = "JPL";
		}
		String preTreat = "FILL";
		if (resUrl.toUpperCase().indexOf("NOFILL") >= 0) {
			preTreat = "NOFILL";
		}
		
		Calendar calFrom = UtilSet.getDateFromString(fromDateStr);
		Calendar calTo = UtilSet.getDateFromString(toDateStr);
		if (calFrom.compareTo(calTo) > 0) {
			Calendar calTmp = calFrom;
			calFrom = calTo;
			calTo = calTmp;
		}
		Calendar today = Calendar.getInstance();
		String todayStr = UtilSet.getDateString(today);
		fromDateStr = UtilSet.getDateString(calFrom);
		toDateStr = UtilSet.getDateString(calTo);
		
		// create the directory for the kmz
		String kmzDirName = contextGroup + "_" + preTreat + "_" + fromDateStr + "to" + toDateStr + "on" + todayStr;
		String kmzDirPath = destKmlDir + File.separator + kmzDirName;
		String kmzFileName = kmzDirName + ".kmz";
		String kmzFilePath = destKmlDir + File.separator + kmzFileName;
		File kmzFile = new File(kmzFilePath);
		if (kmzFile.exists() && kmzFile.isFile()) {
			return kmlUrlPattern.replace("<fileName>", kmzFileName);
		}
		
		File kmzDirFile = new File(kmzDirPath);
		if (kmzDirFile.exists()) {
			if (kmzDirFile.isFile()) {
				kmzDirFile.delete();
			} else {
				UtilSet.deleteDirectory(kmzDirFile);
			}
		}
		kmzDirFile.mkdirs();
		
		// create the directory for sub kmls
		String subDirName = "subdocs";
		String subDirPath = kmzDirPath + File.separator + subDirName;
		File subDirFile = new File(subDirPath);
		subDirFile.mkdir();
		
		// create the top level kml
		String kmlFileName = contextGroup + "_" + preTreat + "_" + fromDateStr + "to" + toDateStr + "on" + todayStr + ".kml";
		String kmlPath = kmzDirPath + File.separator + kmlFileName;		
		Document kmlDoc = DocumentHelper.createDocument();
		Element eleKml = kmlDoc.addElement("kml");
		eleKml.addAttribute("xmlns", "http://www.opengis.net/kml/2.2");
		Element eleFolder = eleKml.addElement("Folder");
		eleFolder.addElement("open").setText("1");		
		
		Calendar calTmp = Calendar.getInstance();
		calTmp.setTimeInMillis(calFrom.getTimeInMillis());
		Calendar calTmp2 = Calendar.getInstance();
		calTmp2.set(Calendar.MONTH, Calendar.DECEMBER);
		calTmp2.set(Calendar.DAY_OF_MONTH, 31);
		String tmpFromDateStr;
		String tmpToDateStr;
		while (calTmp.get(Calendar.YEAR) <= calTo.get(Calendar.YEAR)) {
			if (calTmp.get(Calendar.YEAR) < calTo.get(Calendar.YEAR)) {
				calTmp2.set(Calendar.YEAR, calTmp.get(Calendar.YEAR));
				tmpFromDateStr = UtilSet.getDateString(calTmp);
				tmpToDateStr = UtilSet.getDateString(calTmp2);
			} else {
				tmpFromDateStr = UtilSet.getDateString(calTmp);
				tmpToDateStr = UtilSet.getDateString(calTo);
			}
			String relSubDocPath = subDirName + File.separator + tmpFromDateStr + "to" + tmpToDateStr + ".kml";
			eleFolder.addElement("Link").addElement("href").setText(relSubDocPath);
			calTmp.set(Calendar.YEAR, calTmp.get(Calendar.YEAR) + 1);
			calTmp.set(Calendar.MONTH, Calendar.JANUARY);
			calTmp.set(Calendar.DAY_OF_MONTH, 1);
		}		
		try {
			FileWriter fw = new FileWriter(kmlPath);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(kmlDoc);
			writer.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		// create sub kml documents
		
		
		DailyRdahmmResultAnalyzer analyzer = analyzerTable.get(resUrl);
		if (analyzer == null) {
			analyzer = addAnalyzer(resUrl);
		}
		
		DailyRdahmmStation[] stations = analyzer.stationArray;
		
		Element eleDoc = eleFolder.addElement("Document");
		eleDoc.addElement("name").setText("Style Definitions");
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
			eleStyle.addElement("LabelStyle").addElement("scale").setText("0");
		}
		
		while (calFrom.compareTo(calTo) <= 0) {
			eleDoc = eleFolder.addElement("Document");
			String dateStr = UtilSet.getDateString(calFrom);
			eleDoc.addElement("name").setText(contextGroup + "_" + preTreat + "_" + dateStr);
			// add the "TimeSpan" element
			Element eleTP = eleDoc.addElement("TimeSpan");
			eleTP.addElement("begin").setText(dateStr + "T00:00:00Z");
			eleTP.addElement("end").setText(dateStr + "T23:59:59Z");
			String colors = analyzer.calcStationColors(dateStr);
			for (int i = 0; i < stations.length; i++) {
				Element eleMark = eleDoc.addElement("Placemark");
				Element eleName = eleMark.addElement("name");
				eleName.setText(stations[i].stationID);
				Element elePoint = eleMark.addElement("Point");
				Element eleCoord = elePoint.addElement("coordinates");
				eleCoord.setText(stations[i].longitude + "," + stations[i].latitude);
				Element eleStyleUrl = eleMark.addElement("styleUrl");
				int colorNum = Integer.valueOf(colors.substring(2*i, 2*i+1));
				eleStyleUrl.setText("#" + styleIds[colorNum]);
				if (calFrom.equals(calTo)) {
					String fileUrlPrefix = analyzer.urlPattern.replace("{!station-id!}", stations[i].stationID) + "/" 
											+ analyzer.dirPattern.replace("{!station-id!}", stations[i].stationID) + "/";
					String rawUrl = fileUrlPrefix + analyzer.rawInputPattern.replace("{!station-id!}", stations[i].stationID);
					String qUrl = fileUrlPrefix	+ analyzer.qPattern.replace("{!station-id!}", stations[i].stationID);
					String popUpHtml = popUpWinHtml;
					popUpHtml = popUpHtml.replace("{!rawFileURL!}", rawUrl);
					popUpHtml = popUpHtml.replace("{!qFileURL!}", qUrl);
					Element eleDesc = eleMark.addElement("description");
					// eleDesc.setText("<![CDATA[" + popUpHtml + "]]");
					eleDesc.setText(popUpHtml);
				}
			}
			
			calFrom.set(Calendar.DATE, calFrom.get(Calendar.DATE) + 1);
		}
		
		try {
			FileWriter fw = new FileWriter(kmlPath);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(kmlDoc);
			writer.close();
			fw.close();
			
			String res = UtilSet.exec("zip -D " + kmzFilePath + " " + kmlFileName, new File(destKmlDir));
			res = res.toLowerCase();
			if (res.indexOf("error") >= 0 || res.indexOf("not found") >= 0) {
				return kmlUrlPattern.replace("<fileName>", kmlFileName);
			} else {
				File fileKml = new File(kmlPath);
				fileKml.delete();
				return kmlUrlPattern.replace("<fileName>", kmzFileName);
			}
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
	
	/** add an analyzer to the table of analyzers
	 * @param xmlResUrl
	 * @return
	 */
	protected DailyRdahmmResultAnalyzer addAnalyzer(String xmlResUrl) {
		DailyRdahmmResultAnalyzer analyzer = new DailyRdahmmResultAnalyzer(xmlResUrl);
		analyzerTable.put(xmlResUrl, analyzer);
		return analyzer;
	}
	
	protected void deleteObsoleteKml() {
		long timeNow = System.currentTimeMillis();
		if (timeNow - lastKmlDeletionTime > kmlCleanUpPeriod) {
			try {
				ObsoleteFileDeleter oldKmlDeleter = new ObsoleteFileDeleter(kmlObsoleteNamePattern, -1, kmlObsoleteThreshold);
				oldKmlDeleter.addDirPath(destKmlDir);
				oldKmlDeleter.start();
				lastKmlDeletionTime = timeNow;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void deleteObsoletePlot() {
		long timeNow = System.currentTimeMillis();
		if (timeNow - lastPlotDeletionTime > plotCleanUpPeriod) {
			try {
				ObsoleteFileDeleter oldPlotDeleter = new ObsoleteFileDeleter(plotObsoleteNamePattern, -1, plotObsoleteThreshold);
				oldPlotDeleter.addDirPath(destPlotDir);
				oldPlotDeleter.start();
				lastPlotDeletionTime = timeNow;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

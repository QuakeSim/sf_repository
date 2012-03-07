package cgl.webservices;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class StationListMaker {
	
	/** make xml list from txt list of station name, latitude, and longitude
	 * @param args
	 */
	public static void main2(String[] args) {
		// TODO Auto-generated method stub
		StationListMaker maker = new StationListMaker();
		String fileName;
		String dir = "E:\\Projects\\eclipse-workspace\\QuakeSim2_DailyRdahmmService\\bin\\";	
		
		Vector fileLines = new Vector();
		if (args.length > 0)
			fileName = args[0];
		else
			fileName = "cal_sites.txt";
		maker.readFileToVector (dir + fileName, fileLines);
		
		// read the station info
		Document resDoc = null;
		Element eleXml = null;
		resDoc = DocumentHelper.createDocument();
		eleXml = resDoc.addElement("xml");
		String line = null, station = null, lat = null, longt = null, netUnknown = "unknown";
		for (int i=0; i<fileLines.size(); i++) {
			line = (String)fileLines.get(i);
			Element eleStation = eleXml.addElement("station");
			Element eleNetwork = eleStation.addElement("network");
			eleNetwork.addElement("name").setText(netUnknown);
			eleNetwork.addElement("ip").setText("");
			eleNetwork.addElement("port").setText("");
			
			// token are devided up by two spaces
			StringTokenizer st = new StringTokenizer(line, "  ");
			String token = "";
			int j = 0;
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (j == 0) {
					j++;
					continue;
				}
				switch (j++) {
				case 1:
					eleStation.addElement("id").addText(token);
					break;
				case 2:
					eleStation.addElement("latitude").addText(token);
					break;
				case 3:
					eleStation.addElement("longitude").addText(token);
					break;
				}				
			}
			eleStation.addElement("status").setText("up");
		}
		
		// write the station info
		// write the document back to file
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			FileWriter fw = new FileWriter(dir + "stations-rss-new.xml");
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(resDoc);
			writer.close();
			fw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		try {
			if (args.length < 1) {
				printUsage();
				return;
			}
		
			if (args[0].equals("makeXmlDeleteNonExisting")) {
				if (args.length < 4) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.makeXmlDeleteNonExisting(args[1], args[2], args[3]);
			} else if (args[0].equals("makeXmlFromUnavcoXml")){
				if (args.length < 3) {
					printUsage();
					return;
				}				
				StationListMaker maker = new StationListMaker();
				maker.makeXmlFromUnavcoXml(args[1], args[2]);
			} else if (args[0].equals("makeDataWithFakeTime")) {
				if (args.length < 6) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.makeDataWithFakeTime(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5]);
			} else if (args[0].equals("makeDataFromBigFile")) {
				if (args.length < 4) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.makeDataFromBigFile(args[1], args[2], args[3]);
			} else if (args[0].equals("makeAriaXmlResult")) {
				if (args.length < 4) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.makeAriaXmlResult(args[1], args[2], args[3]);
			} else if (args[0].equals("makeDateTimeList")) {
				if (args.length < 6) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.makeDateTimeList(args[1], args[2], Long.parseLong(args[3]), args[4], args[5]);
			} else if (args[0].equals("get30MinAriaDataPoint")) {
				if (args.length < 3) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.get30MinAriaDataPoint(args[1], args[2]);
			} else if (args[0].equals("updateUnavcoRawFile")) {
				if (args.length < 3) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.updateUnavcoRawFile(args[1], args[2]);
			} else if (args[0].equals("repairBadRawFile")) {
				if (args.length < 2) {
					printUsage();
					return;
				}
				StationListMaker maker = new StationListMaker();
				maker.repairBadRawFile(args[1]);
			} else {
				printUsage();
				return;
			}
		} catch (Exception e ){
			e.printStackTrace();
		}		 
	}
	
	static void printUsage() {
		System.out.println("Usage: java cgl.webservices.StationListMaker <option> <parameter list>, where '<option> <parameter list>' could be one of the following:");
		System.out.println("makeXmlDeleteNonExisting <non existing station list txt path> <xml station list path> <result xml station list path>");
		System.out.println("makeXmlFromUnavcoXml <UNAVCO xml station list path> <result xml station list path>");
		System.out.println("makeDataWithFakeTime <old data file path> <starting date time> <interval in minutes> ");
		System.out.println("makeDataFromBigFile <big data file path> <state sequence file path> <destination directory>");
		System.out.println("makeAriaXmlResult <station list file path> <path of the directory for all stations' data> <result XML path>");
		System.out.println("makeDateTimeList <startTime> <endTime> <intervalInSec> <delimiter> <outputPath>");
		System.out.println("updateUnavcoRawFile <parent directory> <unavco script path>");
		System.out.println("repairBadRawFile <parent directory>");
		System.out.println("get30MinAriaDataPoint <aria data input path> <output path>");
	}
	
	/**
	 * read the list of non-existing stations and an xml station list, delete the non-existing ones
	 * from the xml list, and write the result to resXmlPath
	 * @param nonExistingStationListPath
	 * @param xmlPath
	 * @param resXmlPath
	 */
	void makeXmlDeleteNonExisting(String nonExistingStationListPath, String xmlPath, String resXmlPath) {
		// read the non-existing stations to a hashmap
		HashMap<String, Integer> stationsToDelete = new HashMap<String, Integer>();
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(nonExistingStationListPath));
			line = br.readLine();
			while (line != null) {
				if (line.length() > 0)
					stationsToDelete.put(line, 1);
				line = br.readLine();
			}
			br.close();

			// read the xml sation list
			br = new BufferedReader(new FileReader(xmlPath));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			br.close();
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(sb.toString()));

			List l = document.selectNodes("//station");
			Vector<Element> eleStationVec = new Vector<Element>();
			for (int i = 0; i < l.size(); i++) {
				Element e = (Element) l.get(i);
				if (e != null) {
					// get station id
					String id = e.selectSingleNode("id").getText().toLowerCase();
					e.selectSingleNode("modelStartDate").setText("");
					e.selectSingleNode("modelEndDate").setText("");
					if (stationsToDelete.containsKey(id)) {
						eleStationVec.add(e);
					}
				}
			}
			System.out.println(eleStationVec.size()	+ " stations to delete from the xml list.");

			for (int i = 0; i < eleStationVec.size(); i++) {
				l.remove(eleStationVec.get(i));
			}
			System.out.println(l.size() + " stations left in the result xml station list");

			// write the station info
			Document resDoc = DocumentHelper.createDocument();
			Element eleXml = resDoc.addElement("xml");
			Iterator iter = l.iterator();
			while (iter.hasNext()) {
				Element eleStation = (Element) iter.next();
				eleStation.setParent(null);
				eleXml.add(eleStation);
			}

			// write the document back to file
			OutputFormat format = OutputFormat.createPrettyPrint();
			FileWriter fw = new FileWriter(resXmlPath);
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(resDoc);
			writer.close();
			fw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * make an xml list of the format used by daily RDAHMM service from an xml of the format used by
	 * UNAVCO station lists
	 * @param unavcoXmlPath
	 * @param resXmlPath
	 */
	void makeXmlFromUnavcoXml(String unavcoXmlPath, String resXmlPath) {
		try {
			// write the station info
			Document resDoc = DocumentHelper.createDocument();
			Element eleXml = resDoc.addElement("xml");
			
			// read the UNVACO xml station list
			BufferedReader br = new BufferedReader(new FileReader(unavcoXmlPath));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			br.close();
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(sb.toString()));

			/* A station element in the UNAVCO xml looks like:
			 * <Monument>
			 *  <MonumentID>11756</MonumentID>
			 *  <StationID>108</StationID>
			 *  <MonumentName>SHIN_BRGN_CN1996</MonumentName>
			 *  <MonumentType>deep-drilled braced</MonumentType>
			 *  <FourCharCode>SHIN</FourCharCode>
			 *  <Grouping>PBO Extension, Nucleus-BARGEN, Nucleus, Basin and Range BARGEN</Grouping>
			 *  <Latitude>40.5917</Latitude>
			 *  <Longitude>-120.225</Longitude>
			 *  <StartTime>1996-07-24T00:00:00.000Z</StartTime>
			 *  <EndTime>2011-02-15T23:59:00.000Z</EndTime>
			 *  <Operational>Active</Operational>
			 *  <ArchiveStart>1996-09-12T00:00:00.000Z</ArchiveStart>
			 *  <DataPublic>Anonymous</DataPublic>
			 *  <HasMetPack>false</HasMetPack>
			 * </Monument>
			 */
			
			List l = document.selectNodes("//Monument");
			for (int i = 0; i < l.size(); i++) {
				Element eleMonument = (Element) l.get(i);
				if (eleMonument != null) {
					// get station id
					String id = eleMonument.selectSingleNode("FourCharCode").getText().toLowerCase();
					String latitude = eleMonument.selectSingleNode("Latitude").getText();
					String longitude = eleMonument.selectSingleNode("Longitude").getText();
					
					Element eleStation = eleXml.addElement("station");
					Element eleNetwork = eleStation.addElement("network");
					eleNetwork.addElement("name").setText("unknown");
					eleNetwork.addElement("ip").setText("");
					eleNetwork.addElement("port").setText("");
					eleStation.addElement("id").setText(id);
					eleStation.addElement("latitude").setText(latitude);
					eleStation.addElement("longitude").setText(longitude);
					eleStation.addElement("status").setText("up");
					eleStation.addElement("modelStartDate").setText("");
					eleStation.addElement("modelEndDate").setText("");
				}
			}
			
			// write the document to file
			OutputFormat format = OutputFormat.createPrettyPrint();
			FileWriter fw = new FileWriter(resXmlPath);
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(resDoc);
			writer.close();
			fw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * make a new data file with fake date and time from oldDataFilePath, with date time starting from startDateTime,
	 * and increasing by intervalInMin for lineCount lines.
	 * @param oldDataFilePath
	 * @param startDateTime
	 * @param intervalInMin
	 * @param lineCount
	 * @param newFilePath
	 */
	void makeDataWithFakeTime(String oldDataFilePath, String startDateTime, int intervalInMin, int lineCount, String newFilePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(oldDataFilePath));
			PrintWriter pw = new PrintWriter(new FileWriter(newFilePath));
			Calendar calTmp = UtilSet.getDateTimeFromString(startDateTime);
			int count = 1;
			String oldLine = br.readLine();
			while (oldLine != null && count <= lineCount) {
				// oldLine is like "2 2000-12-08 3.8452255539596081e-03 6.0139568522572517e-03 -6.3946684822440147e-03"
				int idx1 = oldLine.indexOf(' ');
				int idx2 = oldLine.indexOf(' ', idx1+1);
				String part1 = oldLine.substring(0, idx1+1);
				String part2 = oldLine.substring(idx2);
				String newLine = part1 + UtilSet.getDateTimeString(calTmp) + part2;
				pw.println(newLine);

				calTmp.set(Calendar.MINUTE, calTmp.get(Calendar.MINUTE) + intervalInMin);
				oldLine = br.readLine();
				count++;
			}
			
			br.close();
			pw.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * make .input, .Q, .raw, and .plotswf.input files for all stations from a big input data file and 
	 * a big state sequence file containing data for all stations
	 * @param bigDataFilePath
	 * @param stateSeqFilePath
	 * @param destDir
	 */
	void makeDataFromBigFile(String bigDataFilePath, String stateSeqFilePath, String destDir) {
		try {
			// read the station ids and their data for each date
			// we know there are 1248 stations
			Vector<String> stationIds = new Vector<String>(1300);
			Vector<Vector<StationDataItem>> stationDataVecList = new Vector<Vector<StationDataItem>>(1300);
			
			BufferedReader brInput = new BufferedReader(new FileReader(bigDataFilePath));
			String lineInput = brInput.readLine();
			// the first line is like "date time 0001-lat 0001-lon 0001-height 0002-lat 0002-lon 0002-height ..."
			StringTokenizer stInput = new StringTokenizer(lineInput, " \t");
			String date = stInput.nextToken();
			String time = stInput.nextToken();
			while (stInput.hasMoreTokens()) {
				String idLat = stInput.nextToken();
				String idLon = stInput.nextToken();
				String idHgt = stInput.nextToken();
				int idx = idLat.indexOf('-');
				String id = idLat.substring(0, idx);
				stationIds.add(id);
				stationDataVecList.add(new Vector<StationDataItem>());
			}			
			
			BufferedReader brStateSeq = new BufferedReader(new FileReader(stateSeqFilePath));
			lineInput = brInput.readLine();
			String lineStateSeq = brStateSeq.readLine();
			String startDateTime = "";
			String endDateTime = "";
			int lineCount = 1;
			int intervalInMin = 30;
			while (lineInput != null && lineInput.length() > 0) {
				// lineInput is like "2011-03-10 00:00:00 45.4029924295 141.750435337 74.6858 44.4336857788 143.224179069 54.5787 ..."
				stInput = new StringTokenizer(lineInput, " ");
				date = stInput.nextToken();
				time = stInput.nextToken();
				if (lineCount == 1) {
					startDateTime = date + "T" + time;
				} else if (lineCount == 2) {
					String secondDateTime = date + "T" + time;
					Calendar calStart = UtilSet.getDateTimeFromString(startDateTime);
					Calendar calSecond = UtilSet.getDateTimeFromString(secondDateTime);
					intervalInMin = (int)(calSecond.getTimeInMillis() - calStart.getTimeInMillis()) / (60000);
				}
				lineCount++;
				StringTokenizer stStateSeq = new StringTokenizer(lineStateSeq, " \t");
				
				System.out.print("Processing " + date + " " + time + ":");
				
				int stationListIdx = 0;
				int count = 0;
				while (stInput.hasMoreTokens()) {
					String lat = stInput.nextToken();
					String lon = stInput.nextToken();
					String hgt = stInput.nextToken();
					String state = stStateSeq.nextToken();
					
					if (lat.contains("NaN") || lon.contains("NaN") || hgt.contains("NaN")) {
						// leave as empty
						stationListIdx++;
						continue;
					} else {
						// add the station data to the corresponding vector
						StationDataItem dataItem = new StationDataItem();
						dataItem.dateTime = UtilSet.getDateTimeFromString(date + "T" + time);
						dataItem.latitude = Double.parseDouble(lat);
						dataItem.longitude = Double.parseDouble(lon);
						dataItem.height = Double.parseDouble(hgt);
						int idx = state.indexOf('.');
						dataItem.state = Integer.parseInt(state.substring(0, idx));
						stationDataVecList.get(stationListIdx).add(dataItem);
						
						count++;
						stationListIdx++;
					}
				}
				
				System.out.println(stationListIdx + " stations in total, data added for " + count);
				
				lineInput = brInput.readLine();
				lineStateSeq = brStateSeq.readLine();
			}
			endDateTime = date + "T" + time;
			
			brInput.close();
			brStateSeq.close();
			
			// write the .input, .Q, .raw and .plotswf.input files for each station.
			// write the station list to a single file in destDir
			PrintWriter pwStationList = new PrintWriter(new FileWriter(destDir + File.separator + "stationList.txt"));
			pwStationList.println(intervalInMin + "," + startDateTime + "to" + endDateTime);
			System.out.print("Processing station ");
			for (int i=0; i<stationIds.size(); i++) {
				String stationId = stationIds.get(i);
				Vector<StationDataItem> dataVec = stationDataVecList.get(i);
				
				System.out.print(stationId + "... ");
				
				// write the station id, latitude, and longitude to the station list
				if (dataVec.size() > 0) {
					pwStationList.println(stationId + " " + dataVec.get(0).longitude + " " + dataVec.get(0).latitude);
				} else {
					pwStationList.println(stationId);
				}
				
				String stationDir = destDir + File.separator + stationId;
				File stationDirFile = new File(stationDir);
				stationDirFile.mkdirs();
				
				// write the other station files
				String inputPath = stationDir + File.separator + stationId + ".input";
				String qPath = stationDir + File.separator + stationId + ".Q";
				String rawPath = stationDir + File.separator + stationId + ".raw";
				String plotInputPath = stationDir + File.separator + stationId + ".plotswf.input";
				PrintWriter pwInput = new PrintWriter(new FileWriter(inputPath));
				PrintWriter pwQ = new PrintWriter(new FileWriter(qPath));
				PrintWriter pwRaw = new PrintWriter(new FileWriter(rawPath));
				PrintWriter pwPlotInput = new PrintWriter(new FileWriter(plotInputPath));
				for (int j=0; j<dataVec.size(); j++) {
					StationDataItem item = dataVec.get(j);
					// a line in .input file is like "<longitude> <latitude> <height>"
					String inputLine = item.longitude + " " + item.latitude + " " + item.height;
					pwInput.println(inputLine);
					pwQ.println(item.state);
					
					// a line in .raw file is like "<id> <date time> <longitude> <latitude> <height> ..."
					String dateTimeStr = UtilSet.getDateTimeString(item.dateTime);
					String rawLine = stationId + " " + dateTimeStr + " " + inputLine;
					pwRaw.println(rawLine);
					
					// a line in .plotswf.input file is like "<state> <date time> <longitude> <latitude> <height>"
					String plotInputLine = item.state + " " + dateTimeStr + " " + inputLine;
					pwPlotInput.println(plotInputLine);
				}
				pwInput.close();
				pwQ.close();
				pwRaw.close();
				pwPlotInput.close();
			}
			
			pwStationList.close();
			System.out.println(" Done with all stations!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * make the xml result file for the ARIA data and analysis results
	 * @param stationListPath
	 * @param dataDir
	 * @param resultPath
	 */
	void makeAriaXmlResult(String stationListPath, String dataDir, String resultPath) {
		try {
			Document resDoc = DocumentHelper.createDocument();
			Element eleXml = resDoc.addElement("xml");
		
			// read the output patterns and data point intervalInMin
			/* The station list file is like:
			 * 30,2011-03-01T00:00:00to2011-03-13T23:30:00
			 * http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.input
			 * http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.Q
			 * http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.raw
			 * http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.plotswf.input
			 * 0001 141.750435337 45.4029924295
			 * 0002 143.224179069 44.4336857788
			 * ...
			 */
			BufferedReader brList = new BufferedReader(new FileReader(stationListPath));
			String stationListLine = brList.readLine();
			int idxComma = stationListLine.indexOf(',');
			int idxTo = stationListLine.indexOf("to", idxComma);
			int intervalInMin = Integer.parseInt(stationListLine.substring(0, idxComma));
			String startDateTime = stationListLine.substring(idxComma+1, idxTo);
			String endDateTime = stationListLine.substring(idxTo+2);
			eleXml.addElement("dataIntervalInMin").setText(Integer.toString(intervalInMin));
			eleXml.addElement("startDateTime").setText(startDateTime);
			eleXml.addElement("endDateTime").setText(endDateTime);
			Element eleOutput = eleXml.addElement("output-pattern");
			stationListLine = brList.readLine();
			eleOutput.addElement("inputUrlPattern").setText(stationListLine);
			stationListLine = brList.readLine();
			eleOutput.addElement("qUrlPattern").setText(stationListLine);
			stationListLine = brList.readLine();
			eleOutput.addElement("rawUrlPattern").setText(stationListLine);
			stationListLine = brList.readLine();
			eleOutput.addElement("plotInputUrlPattern").setText(stationListLine);
			
			Element eleStationCount = eleXml.addElement("station-count");
			eleStationCount.setText("1248");
			
			// now generate the element for each station
			long stationCount = 0;
			stationListLine = brList.readLine();
			while (stationListLine != null && stationListLine.trim().length() > 0) {
				stationCount++;
				String[] items = stationListLine.split(" ");
				String id = items[0];
				
				Element eleStation = eleXml.addElement("station");
				eleStation.addElement("id").setText(items[0]);
				eleStation.addElement("long").setText(items[1]);
				eleStation.addElement("lat").setText(items[2]);
				
				// the raw and q file paths are like <dataDir>/<id>/<id>.raw and <dataDir>/<id>/<id>.Q 
				String rawPath = dataDir + File.separator + id + File.separator + id + ".raw";
				String qPath = dataDir + File.separator + id + File.separator + id + ".Q";
				System.out.print("processing station " + items[0] + "... ");
				fillStationElement(eleStation, rawPath, qPath, startDateTime, endDateTime, intervalInMin);
				stationListLine = brList.readLine();
			}
			brList.close();
			
			// write the document back to file
			OutputFormat format = OutputFormat.createPrettyPrint();
			eleStationCount.setText(Long.toString(stationCount));
			FileWriter fw = new FileWriter(resultPath);
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(resDoc);
			writer.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * fill the xml element for a station by analyzing its .raw file and .Q file
	 * @param eleStation
	 * @param rawPath
	 * @param qPath
	 */
	void fillStationElement(Element eleStation, String rawPath, String qPath, String startDateTime, String endDateTime, int intervalInMin) {
		// now the status changes
		long intervalInMilli  = intervalInMin * 60 * 1000;
		long status1 = -1, status2 = -1;
		String str1 = "", str2 = "", dateTime1 = "", dateTime2 = "";
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar calEnd = UtilSet.getDateTimeFromString(endDateTime);
		Calendar calStart = UtilSet.getDateTimeFromString(startDateTime);
		Calendar calMissingEnd = Calendar.getInstance();
		Calendar calMissingBegin = Calendar.getInstance();
		Vector<String> evalQLines = new Vector<String>();
		Vector<String> evalRawLines = new Vector<String>();
		UtilSet.readFileToVector(qPath, evalQLines);
		UtilSet.readFileToVector(rawPath, evalRawLines);
		
		// every status change is formated like 2007-10-09T01:30:00#4to3;, and there are 20 change in one change group
		StringBuffer sbgroup = new StringBuffer(26*20);
		// every missing data section is formated like 2006-10-09T01:00:00to2007-10-13T00:00:00;
		// and we estimate that there are less than 20 sections for every station
		StringBuffer sbMissData = new StringBuffer(41*20);
		int count20 = 0;
		int changeCount = 0;
		int missCount = 0;
		if (evalQLines.size() > 0) {
			// data missing between last "data-available" time and endDateTime?
			dateTime2 = UtilSet.getDateTimeFromRawLine((String)evalRawLines.get(evalQLines.size()-1));
			UtilSet.setDateTimeByString(cal2, dateTime2);
			cal2.setTimeInMillis(cal2.getTimeInMillis() + intervalInMilli);
			if (cal2.compareTo(calEnd) < 0) {
				sbMissData.append(endDateTime).append("to");
				sbMissData.append(UtilSet.getDateTimeString(cal2)).append(';');
				missCount++;
			}
			
			// get data-missing time sections and state changes during the evaluation time
			for (int i = evalQLines.size() - 1; i > 0; i--) {
				// record section with missing data
				dateTime1 = UtilSet.getDateTimeFromRawLine((String)evalRawLines.get(i-1));
				dateTime2 = UtilSet.getDateTimeFromRawLine((String)evalRawLines.get(i));
				UtilSet.setDateTimeByString(cal1, dateTime1);
				UtilSet.setDateTimeByString(cal2, dateTime2);
	
				if (cal1.getTimeInMillis() + intervalInMilli < cal2.getTimeInMillis()) {
					calMissingBegin.setTimeInMillis(cal1.getTimeInMillis() + intervalInMilli);
					calMissingEnd.setTimeInMillis(cal2.getTimeInMillis() - intervalInMilli);
					sbMissData.append(UtilSet.getDateTimeString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateTimeString(calMissingBegin)).append(';');
					missCount++;
				}

				// record status changes
				str1 = evalQLines.get(i - 1);
				status1 = Long.parseLong(str1);
				if (status2 < 0) {
					str2 = evalQLines.get(i);
					status2 = Long.parseLong(str2);
				}
				if (status1 != status2) {
					changeCount++;
					sbgroup.append(dateTime2);
					sbgroup.append('#').append(str1).append("to").append(str2);
					count20++;
					if (count20 >= 20) {
						// add a status change element
						eleStation.addElement("status-changes").setText(sbgroup.toString());
						sbgroup.setLength(0);
						count20 = 0;						
					} else {
						sbgroup.append(';');
					}					
				}
				status2 = status1;
				str2 = str1;				
			}
			
			// data missing between startDateTime and the first "data-available" time?
			dateTime1 = UtilSet.getDateTimeFromRawLine((String)evalRawLines.get(0));
			UtilSet.setDateTimeByString(cal1, dateTime1);
			cal1.setTimeInMillis(cal1.getTimeInMillis() - intervalInMilli);
			if (calStart.compareTo(cal1) < 0) {
				sbMissData.append(UtilSet.getDateTimeString(cal1)).append("to");
				sbMissData.append(startDateTime).append(';');
				missCount++;
			}
		} else {
			// if there is even no data for training model, there must be no data at all
			sbMissData.append(endDateTime).append("to");
			sbMissData.append(startDateTime).append(';');
			missCount++;
		}
		
		// if there are still some status change information not written in to xml
		if (sbgroup.length() > 0) {
			eleStation.addElement("status-changes").setText(sbgroup.toString());
		}
		
		eleStation.addElement("change-count").setText(String.valueOf(changeCount));

		if (sbMissData.length() > 0) {
			eleStation.addElement("time-nodata").setText(sbMissData.toString());
		}
		
		eleStation.addElement("nodata-count").setText(String.valueOf(missCount));
		
		System.out.println(changeCount + " state changes, " + missCount + " time sections of missing data");
	}
	
	/** read the content of a file to a vector */
	void readFileToVector(String path, Vector<String> vec) {
		if (path == null || path.length() == 0 || vec == null)
			return;
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(path));
			line = br.readLine();
			while (line != null) {
				if (line.length() > 0)
					vec.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * make a list of date and time, from startTime to endTime, and save it in outputPath
	 * @param startTime in the form of 2011-04-01T00:00:00
	 * @param endTime in the form of 2011-04-02T00:00:00
	 * @param intervalInSec
	 * @param delimiter
	 * @param outputPath
	 */
	void makeDateTimeList(String startTime, String endTime, long intervalInSec, String delimiter, String outputPath) {
		try {
			Calendar calStart = UtilSet.getDateTimeFromString(startTime);
			Calendar calEnd = UtilSet.getDateTimeFromString(endTime);
			long inc = intervalInSec * 1000;
			if (delimiter.length() > 1 && delimiter.equalsIgnoreCase("space")) {
				delimiter = " ";
			}
			PrintWriter pwOut = new PrintWriter(new FileWriter(outputPath));
			while (calStart.compareTo(calEnd) <= 0) {
				String line = UtilSet.getDateTimeString(calStart);
				if (!delimiter.equals("T")) {
					line = line.replace("T", delimiter);
				}
				if (calStart.compareTo(calEnd) == 0) {
					pwOut.print(line + " ");
				} else {
					pwOut.println(line + " ");
				}
				calStart.setTimeInMillis(calStart.getTimeInMillis() + inc);
			}
			pwOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * pick the 30-minute data points (00:00:00, 00:30:00, 01:00:00, etc.) from ariaInputPath and save them in outputPath
	 * @param ariaInputPath
	 * @param outputPath
	 */
	void get30MinAriaDataPoint(String ariaInputPath, String outputPath) {
		try {
			BufferedReader brInput = new BufferedReader(new FileReader(ariaInputPath));
			PrintWriter pwOut = new PrintWriter(new FileWriter(outputPath));
			// the first line contains the title
			String line = brInput.readLine();
			pwOut.println(line);
			Calendar calTmp = Calendar.getInstance();
			line = brInput.readLine();
			while (line != null) {
				int idx1 = line.indexOf(' ');
				int idx2 = line.indexOf(' ', idx1+1);
				String dtStr = line.substring(0, idx1) + "T" + line.substring(idx1+1, idx2);
				UtilSet.setDateTimeByString(calTmp, dtStr);
				int sec = calTmp.get(Calendar.SECOND);
				int min = calTmp.get(Calendar.MINUTE);
				if (sec == 0 && (min == 0|| min == 30)) {
					pwOut.println(line);
				}
				line = brInput.readLine();
			}
			brInput.close();
			pwOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * update the raw files of all UNAVCO stations under parentDir to the ones generated by unavcoExePath 
	 * @param parentDir
	 * @param unavcoExePath
	 */
	void updateUnavcoRawFile(String parentDir, String unavcoExePath) {
		try {
			File fileUnavcoDir = new File(UtilSet.getDirPart(unavcoExePath));
			File fileParentDir = new File(parentDir);
			File[] stationDirs = fileParentDir.listFiles();
			Vector<String> rawLines = new Vector<String>();
			for (int i=0; i<stationDirs.length; i++) {
				if (stationDirs[i].isFile()) {
					continue;
				}
				String dirName = stationDirs[i].getName();
				String dirPath = stationDirs[i].getPath();
				int idx = dirName.lastIndexOf('_');
				String stationId = dirName.substring(idx+1);
				rawLines.clear();
				String unavcoRawPath = dirPath + File.separator + dirName + ".unavco.raw";
				if (new File(unavcoRawPath).exists()) {
					UtilSet.readFileToVector(unavcoRawPath, rawLines);
					String beginDate = UtilSet.getDateFromRawLine(rawLines.firstElement());
					String endDate = UtilSet.getDateFromRawLine(rawLines.lastElement());
					String output = UtilSet.exec(unavcoExePath + " " + stationId + " " + dirPath + " " + beginDate + " " + endDate, fileUnavcoDir);
					System.out.println("Output of unavcotrans.py for " + stationId + ": " + output);
					String filledRawPath = dirPath + File.separator + dirName + ".raw";
					boolean fillSucc = fillMissingDataWithDup(unavcoRawPath, filledRawPath);
					System.out.println("Fill missing data succeed? - " + fillSucc);
				} else {
					System.out.println(unavcoRawPath + " does not exit, go to next station...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static protected boolean fillMissingDataWithDup(String srcPath, String destPath) {
		String tmpDestPath = destPath;
		if (srcPath.equals(destPath)) {
			tmpDestPath = destPath + ".tmp";
		}
		
		// fill missing data. a raw line is like 
		//"dond 2007-02-22T12:00:00 -2517566.0543 -4415531.3935 3841177.1618 0.0035 0.0055 0.0047"
		try {
			BufferedReader brSrc = new BufferedReader(new FileReader(srcPath));
			PrintWriter prTmpDest = new PrintWriter(new FileWriter(tmpDestPath));
		
			String lineSrc = brSrc.readLine();
			if (lineSrc != null) {
				String stationId = "";
				int idx = lineSrc.indexOf(' ');
				if (idx >= 0) {
					stationId = lineSrc.substring(0, idx);
				}
				String lineSrcPre = lineSrc;
				String restLineSrcPre = lineSrcPre.substring(lineSrcPre.indexOf(' ', idx+1));
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar calTmp = Calendar.getInstance();

				lineSrc = brSrc.readLine();
				while (lineSrc != null) {
					prTmpDest.println(lineSrcPre);
					UtilSet.setDateByString(cal1, DailyRDAHMMStation.getDateFromRawLine(lineSrcPre));
					UtilSet.setDateByString(cal2, DailyRDAHMMStation.getDateFromRawLine(lineSrc));

					// if there is a gap between cal1+1day and cal2, fill it with the last line
					UtilSet.ndaysBeforeToday(cal1, -1, calTmp);
					while (calTmp.compareTo(cal2) < 0) {
						// we use the time 22:22:22 to denote a duplicated line that used to be missing data
						prTmpDest.println(stationId + ' ' + UtilSet.getDateString(calTmp) + "T22:22:22" + restLineSrcPre);
						UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
					}

					lineSrcPre = lineSrc;
					restLineSrcPre = lineSrcPre.substring(lineSrcPre.indexOf(' ', 5));
					lineSrc = brSrc.readLine();
				}
				prTmpDest.println(lineSrcPre);
			}
			brSrc.close();
			prTmpDest.flush();
			prTmpDest.close();			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to fill missing data to " + srcPath + " and save filled data to "
								+ destPath + ": I/O error!");
			return false;
		}
		
		// replace the file of srcPath with tmpDestPath if srcPath and destPath are the same
		if (srcPath.equals(destPath)) {
			File srcFile = new File(srcPath);
			if (!srcFile.delete()) {
				System.out.println("Failed to fill missing data to " + srcPath + " and save filled data to "
									+ destPath + ": can't delete original file!");
				return false;
			}
			File tmpFile = new File(tmpDestPath);
			if (!tmpFile.renameTo(srcFile)) {
				System.out.println("Failed to fill missing data to " + srcPath + " and save filled data to "
									+ destPath + ": can't rename tmp file to original file name!");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * repair bad raw files with "null" in them. Repair is done by refilling the .unavco.raw files
	 * @param parentDir
	 */
	void repairBadRawFile(String parentDir) {
		try {
			File fileParentDir = new File(parentDir);
			File[] stationDirs = fileParentDir.listFiles();
			for (int i=0; i<stationDirs.length; i++) {
				if (stationDirs[i].isFile()) {
					continue;
				}
				String dirName = stationDirs[i].getName();
				String dirPath = stationDirs[i].getPath();
				String unavcoRawPath = dirPath + File.separator + dirName + ".unavco.raw";
				String filledRawPath = dirPath + File.separator + dirName + ".raw";
				System.out.print("Processing " + unavcoRawPath + "...");
				boolean fillSucc = fillMissingDataWithDup(unavcoRawPath, filledRawPath);
				System.out.println(fillSucc);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * a station data item containing lat, long, height, and q for some date
	 * @author gaoxm
	 *
	 */
	public class StationDataItem {
		protected Calendar dateTime;
		protected double latitude;
		protected double longitude;
		protected double height;
		protected int state;
	}

}

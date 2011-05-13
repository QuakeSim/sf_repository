package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DailyRdahmmResultAnalyzer {
	
	protected class StateSeqLoader extends Thread {
		public void run() {
			long startTime = System.currentTimeMillis();
			// load all stations' state sequences
			String stationSwfInputPattern = DailyRdahmmResultService.resultLocalDir + File.separator + dataSourceSubDir
											+ File.separator + dirPattern + File.separator + swfInputPattern;
			try {
				if (stationArray != null) {
					Calendar calTmp = Calendar.getInstance();
					for (int i=0; i<stationArray.length; i++) {
						String swfInputPath = stationSwfInputPattern.replace("{!station-id!}", stationArray[i].stationID);					
						File swfInputFile = new File(swfInputPath);
						if (!swfInputFile.exists()) {
							continue;
						}
						
						Vector<String> vecSwfInput = new Vector<String>(3000);
						UtilSet.readFileToVector(swfInputPath, vecSwfInput);
						HashMap<Long, Byte> stateSeqMap = new HashMap<Long, Byte>(vecSwfInput.size());
						for (int j=0; j<vecSwfInput.size(); j++) {
							String line = vecSwfInput.get(j);
							// line is like "1 2001-04-20 -2407750.8815 -4706536.7110 3557571.3914"
							int idx1 = line.indexOf(' ');
							int idx2 = line.indexOf(' ', idx1 + 1);
							UtilSet.setDateByString(calTmp, line.substring(idx1+1, idx2));
							stateSeqMap.put(calTmp.getTimeInMillis(), Byte.parseByte(line.substring(0, idx1)));
						}
						stationArray[i].stateSequence = stateSeqMap; 
						if (stationArray[i].stateSequence == null) {
							System.out.println("failed to create stateSequence hashmap for station " + i);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			synchronized (dataSourceSubDir) {
				stateSeqLoaded = true;
				dataSourceSubDir.notifyAll();
			}
			long endTime = System.currentTimeMillis();
			System.out.println("All stations' state sequences loaded! Time used: " + (endTime - startTime)/1000 + " seconds.");
		}
	}
	
	public static long DAY_MILLI = 86400000;
	
	private String xmlResUrl = "http://gf13.ucs.indiana.edu:8080//rdahmmexec/station-status-change-rss.xml";			//the url of the result xml file of daily rdahmm service, used when local copy is out of date
	
	protected DailyRdahmmStation[] stationArray = null;	//array of all stations recorded in the xml result file
	
	private Calendar calLastUpdate = null;
	private Calendar calResXml = null;
	private Calendar calBeginDate = null;
	private Calendar calEndDate = null;
	private String dataLatestDate = null;
	
	String urlPattern;
	String scnPattern;
	String videoUrl;
	String allInputPattern;
	String dirPattern;
	String aPattern;
	String bPattern;
	String inputPattern;
	String rawInputPattern;
	String lPattern;
	String xPattern;
	String yPattern;
	String zPattern;
	String piPattern;
	String qPattern;
	String maxPattern;
	String minPattern;
	String rangePattern;
	String modelPattern;
	String swfInputPattern;
	String dataSourceSubDir;
	String dataSource;
	String mapCenterLon;
	String mapCenterLat;
	boolean stateSeqLoaded;
	
	public DailyRdahmmResultAnalyzer (String xmlResUrl) {
		try {
			this.xmlResUrl = xmlResUrl;
			calResXml = UtilSet.getDateFromString("1994-01-01");
			calLastUpdate = Calendar.getInstance();
			getAndAnalyzeXmlRes();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void getAndAnalyzeXmlRes() throws Exception {
		Document statusDoc = getXmlResDoc();
		
		Element eleXml = statusDoc.getRootElement();
		Calendar calXml = UtilSet.getDateTimeFromString(eleXml.element("update-time").getText());
		if (calResXml.compareTo(calXml) < 0) {
			calResXml.setTimeInMillis(calXml.getTimeInMillis());
		} else if (stationArray != null) {
			return;
		}
		
		Element eleOutput = eleXml.element("output-pattern");
		urlPattern = eleOutput.element("server-url").getText();
		// urlPattern is like: http://gf5.ucs.indiana.edu:8080//daily_rdahmmexec/daily/SOPAC_FILL
		dataSourceSubDir = urlPattern.substring(urlPattern.lastIndexOf('/') + 1);
		scnPattern = eleOutput.element("stateChangeNumTxtFile").getText();
		videoUrl = eleOutput.element("video-url").getText();
		allInputPattern = eleOutput.element("allStationInputName").getText();
		dirPattern = eleOutput.element("pro-dir").getText();
		aPattern = eleOutput.element("AFile").getText();
		bPattern = eleOutput.element("BFile").getText();
		inputPattern = eleOutput.element("InputFile").getText();
		rawInputPattern = eleOutput.element("RawInputFile").getText();
		lPattern = eleOutput.element("LFile").getText();
		xPattern = eleOutput.element("XPngFile").getText();
		yPattern = eleOutput.element("YPngFile").getText();
		zPattern = eleOutput.element("ZPngFile").getText();
		piPattern = eleOutput.element("PiFile").getText();
		qPattern = eleOutput.element("QFile").getText();
		maxPattern = eleOutput.element("MaxValFile").getText();
		minPattern = eleOutput.element("MinValFile").getText();
		rangePattern = eleOutput.element("RangeFile").getText();
		modelPattern = eleOutput.element("ModelFiles").getText();
		swfInputPattern = eleOutput.element("SwfInputFile").getText();
		dataSource = eleXml.elementText("data-source");
		if (calBeginDate == null){
			calBeginDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		}
		UtilSet.setDateByString(calBeginDate, eleXml.elementText("begin-date"));
		if (calEndDate == null) {
			calEndDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		}
		UtilSet.setDateByString(calEndDate, eleXml.elementText("end-date"));
		mapCenterLon = eleXml.elementText("center-longitude");
		mapCenterLat = eleXml.elementText("center-latitude");
		
		List lStations = eleXml.elements("station");
		stationArray = new DailyRdahmmStation[lStations.size()];
		// every station in the station array has 5 attributes: id, lat, long, no data sections, state change details
		// "state change details" is an array of 3*[state change count] elements; so there are 3 elements in the array for every state change: 
		// the old state, the new state, and number of days passed since 1970-1-1 till the date the change happens,
		//  "no data sections" is an array of 2*[no data section count] elements; so there are 2 elements in the array for no data section: 
		// the number of days passed since 1970-1-1 till the beginning and end date the section
		int changeCount = 0;
		int nodataCount = 0;
		Calendar tmpCaldr = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		tmpCaldr.set(Calendar.HOUR_OF_DAY, 12);
		tmpCaldr.set(Calendar.MINUTE, 0);
		tmpCaldr.set(Calendar.SECOND, 0);
		tmpCaldr.set(Calendar.MILLISECOND, 0);
		
		Calendar dataLatest = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		dataLatest.set(Calendar.HOUR_OF_DAY, 12);
		dataLatest.set(Calendar.MINUTE, 0);
		dataLatest.set(Calendar.SECOND, 0);
		dataLatest.set(Calendar.MILLISECOND, 0);
		UtilSet.setDateByString(dataLatest, "1994-1-1");
		
		// now we load status change information from the xml file, and create markers for all stations
		for (int i=0; i<lStations.size(); i++) {
			Element eleStation = (Element)lStations.get(i);
			float x = Float.parseFloat(eleStation.element("lat").getText());
			float y = Float.parseFloat(eleStation.element("long").getText());
			changeCount = Integer.parseInt(eleStation.element("change-count").getText());					
			nodataCount = Integer.parseInt(eleStation.element("nodata-count").getText());

			stationArray[i] = new DailyRdahmmStation();	
			stationArray[i].stationID = eleStation.element("id").getText();
			stationArray[i].latitude = x;	
			stationArray[i].longitude = y;
	
			if (changeCount != 0) {
				stationArray[i].stateChanges = new int[changeCount * 3];	
				int changeIdx = 0;
				List lChanges = eleStation.elements("status-changes");
				for (int j=0; j<lChanges.size(); j++) {
					String changeStr = ((Element)lChanges.get(j)).getText();
					int idx1 = 0;
					int idx2 = changeStr.indexOf(';');
					if (idx2 < 0 && changeStr.length() > 0)
						idx2 = changeStr.length();
					while (idx2 >= 0) {
						String oneChange = changeStr.substring(idx1, idx2).trim();
						int idxCollon = oneChange.indexOf(':');
						int idxTo = oneChange.indexOf("to");
						String changeDate = oneChange.substring(0, idxCollon);
						String oldStatus = oneChange.substring(idxCollon + 1, idxTo);
						String newStatus = oneChange.substring(idxTo + 2);
						UtilSet.setDateByString(tmpCaldr, changeDate);
		
						stationArray[i].stateChanges[changeIdx++] = (int)(tmpCaldr.getTimeInMillis() / DAY_MILLI); 
						stationArray[i].stateChanges[changeIdx++] = Integer.parseInt(oldStatus); 
						stationArray[i].stateChanges[changeIdx++] = Integer.parseInt(newStatus);
	
						idx1 = idx2 + 1;
						if (idx1 >= changeStr.length())
							break;
						else {
							idx2 = changeStr.indexOf(';', idx1);
							// if we set idx2 to changeStr.length(), idx1 will be larger than changeStr.length(), so we can break anyway, and this is just 
							// for dealing with the case where there is not a ';' at the end of the changeStr
							if (idx2 < 0)
								idx2 = changeStr.length();
						}
					}		
				}
			}
			
			if (nodataCount != 0) {
				stationArray[i].noDataSections = new int[nodataCount * 2];	
				int nodataIdx = 0;
				String nodataStr = eleStation.element("time-nodata").getText();
				int idx1 = 0;
				int idx2 = nodataStr.indexOf(';');
				try {
				while (idx2 >= 0) {
					String oneMissSec = nodataStr.substring(idx1, idx2).trim();
					int idxTo = oneMissSec.indexOf("to");
					String lateDate = oneMissSec.substring(0, idxTo);
					String earlyDate = oneMissSec.substring(idxTo + 2);
				
					UtilSet.setDateByString(tmpCaldr, lateDate);							
					stationArray[i].noDataSections[nodataIdx++] = (int)(tmpCaldr.getTimeInMillis() / DAY_MILLI);
					UtilSet.setDateByString(tmpCaldr, earlyDate);
					stationArray[i].noDataSections[nodataIdx++] = (int)(tmpCaldr.getTimeInMillis() / DAY_MILLI);
					
					
					// get the latest date that any station has data
					if (nodataIdx == 2) {
						UtilSet.nDaysBefore(tmpCaldr, 1, tmpCaldr);
						if (tmpCaldr.compareTo(dataLatest) > 0)
							dataLatest.setTimeInMillis(tmpCaldr.getTimeInMillis());
					}
							
					idx1 = idx2 + 1;
					if (idx1 >= nodataStr.length())
						break;
					else {
						idx2 = nodataStr.indexOf(';', idx1);
					}
				}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("nodataCount*2: " + (nodataCount *2) + " nodataIdx:" + nodataIdx + " i:" + i 
										+ " stationID:" + stationArray[i].stationID);
					System.exit(-1);
				}
			}					
		}
		dataLatestDate = UtilSet.getDateString(dataLatest);
		stateSeqLoaded = false;
		
		StateSeqLoader stateLoader = new StateSeqLoader();
		stateLoader.start();
	}
	
	protected Document getXmlResDoc() {
		Document statusDoc = null;
		try {
			System.out.println("xmlResUrl in getXmlResDoc(): " + xmlResUrl);
			InputStream inUrl = new URL(xmlResUrl).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inUrl));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			br.close();
			inUrl.close();
			SAXReader reader = new SAXReader();
			statusDoc = reader.read( new StringReader(sb.toString()) );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return statusDoc;
	}
	
	public String calcStationColors(String date) {
		try {
			if (isUpdateNeeded()) {
				getAndAnalyzeXmlRes();
				calLastUpdate.setTimeInMillis(System.currentTimeMillis());
			}

			StringBuffer res;
			res = new StringBuffer();

			Calendar theDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			UtilSet.setDateByString(theDate, date);
			for (int i = 0; i < stationArray.length; i++) {
				res.append(getColorForStation(theDate, stationArray[i])).append(',');
			}

			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Get the color for a station on a specific date. Return values:
	 *  0:green, 1:red, 2:yellow, 3:grey, 4:blue	
	 * */
	protected char getColorForStation(Calendar theDate, DailyRdahmmStation station) {
		try {
			if (isUpdateNeeded()) {
				getAndAnalyzeXmlRes();
				calLastUpdate.setTimeInMillis(System.currentTimeMillis());
			}
			if (theDate.before(calBeginDate)) {
				return '3';
			}
			
			int NDAYS = 30;
			Calendar firstDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			UtilSet.nDaysBefore(firstDate, NDAYS, theDate);

			// since javascript time is larger than java time by timeDiff, and the time stored in the station Array is java time, we should
			// reduce javascript time by timeDiff to make it comparable with java time
			int startTime = (int) (firstDate.getTimeInMillis() / DAY_MILLI);
			int endTime = (int) (theDate.getTimeInMillis() / DAY_MILLI);
			int nodataIdx = getNoDataIdx(theDate, station);
			// if no data for a month before the date, then there is no need to check state change
			if (nodataIdx >= 0 && station.noDataSections[nodataIdx + 1] < startTime)
				return '3';

			if (station.stateChanges == null) {
				if (nodataIdx >= 0)
					return '3';
				else
					return '0';
			}

			int idx1 = 0;
			int idx2 = station.stateChanges.length / 3 - 1;
			char color = '*';

			int midTime;
			int yellowMidTime = -1;
			if (idx1 == idx2) {
				midTime = station.stateChanges[idx1 * 3];
				if (midTime == endTime) {
					color = '1';
				} else if (midTime >= startTime && midTime < endTime) {
					yellowMidTime = midTime;
					color = '2';
				} else {
					color = '0';
				}
			}

			if (color == '*') {
				// since the changes are desendantly ordered by date, we use binary search to find the date
				color = '0';
				while (idx1 < idx2) {
					int mid = (idx1 + idx2) / 2;
					midTime = station.stateChanges[mid * 3];
					if (midTime == endTime) {
						color = '1';
						break;
					} else {
						if (midTime >= startTime && midTime < endTime) {
							yellowMidTime = midTime;
							color = '2';
						}

						if (mid == idx1) {
							// this implies that idx1 == idx2-1; so we just check idx2 and get out
							midTime = station.stateChanges[idx2 * 3];
							if (midTime == endTime) {
								color = '1';
							} else if (midTime >= startTime	&& midTime < endTime) {
								yellowMidTime = midTime;
								color = '2';
							}
							break;
						}

						if (midTime > endTime) {
							idx1 = mid;
						} else {
							idx2 = mid;
						}
					}
				}
			}

			// if there is no data on that date, modify the color as necessary
			if (nodataIdx >= 0) {
				if (color == '2')
					color = '4';
				else if (color == '0')
					color = '3';
			}

			/*
			 * if (color == '2') { System.out.println("color for " +
			 * station.stationID + ":" + color + " startTime:" + startTime +
			 * " endTime:" + endTime + " midTime:" + yellowMidTime); Calendar
			 * calStart = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			 * calStart.setTimeInMillis(startTime * DAY_MILLI); Calendar calEnd
			 * = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			 * calEnd.setTimeInMillis(endTime * DAY_MILLI); Calendar calMiddle =
			 * new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			 * calMiddle.setTimeInMillis(yellowMidTime * DAY_MILLI);
			 * System.out.println("startTime:" + UtilSet.getDateString(calStart)
			 * + " endTime:" + UtilSet.getDateString(calEnd) + " midTime:" +
			 * UtilSet.getDateString(calMiddle)); }
			 */
			return color;
		} catch (Exception e) {
			e.printStackTrace();
			return '*';
		}
	}
	
	/**
	 * get the states of all stations for date
	 * @param date
	 * @return
	 */
	public String calcStationStates(String date) {
		try {
			if (isUpdateNeeded()) {
				getAndAnalyzeXmlRes();
				calLastUpdate.setTimeInMillis(System.currentTimeMillis());
			}			
			synchronized (dataSourceSubDir) {
				if (!stateSeqLoaded) {
					dataSourceSubDir.wait();
				}
			}
			Calendar cal = UtilSet.getDateFromString(date);
			long time = cal.getTimeInMillis();
			StringBuffer res;
			res = new StringBuffer();
			for (int i=0; i<stationArray.length; i++) {
				Byte state = null; 
				if (stationArray[i].stateSequence != null) {
					state = stationArray[i].stateSequence.get(time);
				}
				if (state == null) {
					res.append('0');
				} else {
					res.append(state.toString());
				}
				res.append(',');
			}
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * get the "lat long" string for a station
	 * @param stationId
	 * @return
	 */
	public String getLatLongForStation(String stationId) {
		try {
			if (isUpdateNeeded()) {
				getAndAnalyzeXmlRes();
				calLastUpdate.setTimeInMillis(System.currentTimeMillis());
			}

			for (int i = 0; i < stationArray.length; i++) {
				if (stationArray[i].stationID.equals(stationId)) {
					return stationArray[i].latitude + " " + stationArray[i].longitude;
				}
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * If there is no data for 'station' on 'theDate', get the index in the no data sections array
	 * */
	protected int getNoDataIdx(Calendar theDate, DailyRdahmmStation station) {
		if (theDate.before(calBeginDate)) {
			return 0;
		}
		if (theDate.after(calEndDate)) {
			return station.noDataSections.length - 2;
		}
		
		int idx1 = 0;
		int idx2 = station.noDataSections.length / 2 - 1;
		// since javascript time is larger than java time by timeDiff, and the time stored in the station Array is java time, we should reduce javascript time by timeDiff to make it comparable with java time
		int theTime = (int)(theDate.getTime().getTime() / DAY_MILLI);
		if (theTime > station.noDataSections[0])
			return 0;
		if (theTime < station.noDataSections[station.noDataSections.length - 1])
			return station.noDataSections.length - 2;
		
		int lateTime;
		int earlyTime;
        	
		if (idx1 == idx2) {
			lateTime = station.noDataSections[idx1*2];
			earlyTime = station.noDataSections[idx1*2 + 1];
			if (theTime >= earlyTime && theTime <= lateTime) {
				return idx1*2;
			}
		}

		// since the missing data time sections are descendantly ordered by date, we use binary search to find the date
		while (idx1 < idx2) { 
			int mid = (idx1 + idx2) / 2;
			lateTime = station.noDataSections[mid*2];
			earlyTime = station.noDataSections[mid*2+1];
        		
			if (theTime >= earlyTime && theTime <= lateTime) {
				return mid*2;
			} else {
				if (mid == idx1) {
					lateTime = station.noDataSections[idx2*2];
					earlyTime = station.noDataSections[idx2*2+1];
					if (theTime >= earlyTime && theTime <= lateTime)
						return idx2*2;
					break;
				}       			
				if (theTime < earlyTime) {
					idx1 = mid;
				} else {
					idx2 = mid;
				}
			}
		}
		
		return -1;
	}
	
	protected boolean isUpdateNeeded() {
		if (stationArray == null)
			return true;
		Calendar now = Calendar.getInstance();
		if (calLastUpdate.get(Calendar.YEAR) == now.get(Calendar.YEAR) 
			&& calLastUpdate.get(Calendar.MONTH) == now.get(Calendar.MONTH) 
			&& calLastUpdate.get(Calendar.DATE) == now.get(Calendar.DATE)) {
			// calLastUpdate and now are in the same day, check if calLastUpdate was before 5 and now is after 5
			if (calLastUpdate.get(Calendar.HOUR_OF_DAY) < 5 && now.get(Calendar.HOUR_OF_DAY) >= 5) {
				return true;
			} else {
				return false;
			}			
		} else {
			return true;
		}		
	}

	public String getDataLatestDate() {
		try {
			if (isUpdateNeeded()) {
				getAndAnalyzeXmlRes();
				calLastUpdate.setTimeInMillis(System.currentTimeMillis());
			}
			return dataLatestDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}

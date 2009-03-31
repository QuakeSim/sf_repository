package cgl.webservices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DailyRdahmmResultAnalyzer {
	public static long DAY_MILLI = 86400000;
	
	private String xmlResUrl = "http://gf13.ucs.indiana.edu:8080//rdahmmexec/station-status-change-rss.xml";			//the url of the result xml file of daily rdahmm service, used when local copy is out of date
	
	private DailyRdahmmStation[] stationArray = null;	//array of all stations recorded in the xml result file
	
	private Calendar calLastUpdate = null;
	
	private String dataLatestDate = null;
	
	public DailyRdahmmResultAnalyzer (String xmlResUrl) {
		this.xmlResUrl = xmlResUrl;
		getAndAnalyzeXmlRes();
		calLastUpdate = Calendar.getInstance();
	}
	
	protected void getAndAnalyzeXmlRes() {
		Document statusDoc = getXmlResDoc();
		
		Element eleXml = statusDoc.getRootElement();
		
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
	}
	
	protected Document getXmlResDoc() {
		Document statusDoc = null;
		try {	
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
		if (isUpdateNeeded()) {
			getAndAnalyzeXmlRes();
			calLastUpdate.setTimeInMillis(System.currentTimeMillis());
		}
		
		StringBuffer res;
		res = new StringBuffer();
		
		Calendar theDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		theDate.set(Calendar.HOUR_OF_DAY, 12);
		theDate.set(Calendar.MINUTE, 0);
		theDate.set(Calendar.SECOND, 0);
		theDate.set(Calendar.MILLISECOND, 0);
		UtilSet.setDateByString(theDate, date);
		for (int i=0; i<stationArray.length; i++) {
			res.append(getColorForStation(theDate, stationArray[i])).append(',');
		}		
		
		System.out.println("about to return from calcStationColors");
		return res.toString();
	}
	
	/**
	 * Get the color for a station on a specific date. Return values:
	 *  0:green, 1:red, 2:yellow, 3:grey, 4:blue	
	 * */
	protected char getColorForStation(Calendar theDate, DailyRdahmmStation station) {
		if (isUpdateNeeded()) {
			getAndAnalyzeXmlRes();
			calLastUpdate.setTimeInMillis(System.currentTimeMillis());
		}
		
		int NDAYS = 30;
		Calendar firstDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		UtilSet.nDaysBefore(firstDate, NDAYS, theDate);
		
		// since javascript time is larger than java time by timeDiff, and the time stored in the station Array is java time, we should reduce javascript time by timeDiff to make it comparable with java time
		int startTime = (int)(firstDate.getTimeInMillis() / DAY_MILLI);
		int endTime = (int)(theDate.getTimeInMillis() / DAY_MILLI);
		int nodataIdx = getNoDataIdx(theDate, station);
		// if no data for a month before the date, then there is no need to check state change
		if (nodataIdx >= 0 && station.noDataSections[nodataIdx+1] < startTime)
			return '3';
		
		/*
		if (station.stationID.equals("gnps"))
			System.out.println("calc color for gnps, endtime:" + endTime + "; first change time:" 
															+ station.stateChanges[0] + " len:" + station.stateChanges.length);
		*/                                                   
		
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
		if (idx1 == idx2) {
			midTime = station.stateChanges[idx1*3];
			if (midTime == endTime) {
				color = '1';
			} else if (midTime >= startTime && midTime < endTime) {
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
						color = '2';
					}

					if (mid == idx1) {
						// this implies that idx1 == idx2-1; so we just check
						// idx2 and get out
						midTime = station.stateChanges[idx2 * 3];
						if (midTime == endTime)
							color = '1';
						else if (midTime >= startTime && midTime < endTime)
							color = '2';
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
		
		//System.out.println("color for " + station.stationID + ":" + color);
		return color;
	}
	
	/**
	 * get the "lat long" string for a station
	 * @param stationId
	 * @return
	 */
	public String getLatLongForStation(String stationId) {
		if (isUpdateNeeded()) {
			getAndAnalyzeXmlRes();
			calLastUpdate.setTimeInMillis(System.currentTimeMillis());
		}
		
		for (int i=0; i<stationArray.length; i++) {
			if (stationArray[i].stationID.equals(stationId)) {
				return stationArray[i].latitude + " " + stationArray[i].longitude;
			}
		}
		return null;
	}
	
	/**
	 * If there is no data for 'station' on 'theDate', get the index in the no data sections array
	 * */
	protected int getNoDataIdx(Calendar theDate, DailyRdahmmStation station) {
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
		return now.get(Calendar.DATE) != calLastUpdate.get(Calendar.DATE)
				|| (now.get(Calendar.HOUR_OF_DAY) >= 5
					&& calLastUpdate.get(Calendar.HOUR_OF_DAY) < 5);	
	}

	public String getDataLatestDate() {
		if (isUpdateNeeded()) {
			getAndAnalyzeXmlRes();
			calLastUpdate.setTimeInMillis(System.currentTimeMillis());
		}
		return dataLatestDate;
	}
}

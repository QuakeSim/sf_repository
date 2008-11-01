package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.dom4j.*;
import org.dom4j.io.*;

/**
 * Managed bean for calculating the color of every station.
 * */

public class DailyRdahmmResultsBean {
	
	String regionLimit;		//the latitude and longitude of the region of google-map, formatted like lat1,long1;lat2,long2;
	
	String date;			//the selected date
	
	String dataLatestDate;		//the latest date when any station has some input data
	
	String colorResult;		//the string representation containing the color of each station within the region 
	
	String xmlResPath;		//the file path of the result xml file of daily rdahmm service
	
	String xmlResURL;			//the url of the result xml file of daily rdahmm service, used when local copy is out of date
	
	DailyRdahmmStation[] stationArray;	//array of all stations recorded in the xml result file
	
	public static long DAY_MILLI = 86400000;
	
	public DailyRdahmmResultsBean() {
		regionLimit = null;
		date = null;
		colorResult = null;
		xmlResPath = null;
		xmlResURL = null;
		stationArray = null;
	}

	public String getRegionLimit() {
		return regionLimit;
	}

	public void setRegionLimit(String regionLimit) {
		this.regionLimit = regionLimit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getColorResult() {
		return colorResult;
	}

	public void setColorResult(String colorResult) {
		this.colorResult = colorResult;
	}

	public String getXmlResPath() {
		return xmlResPath;
	}

	public void setXmlResPath(String xmlResPath) {
		this.xmlResPath = xmlResPath;
		//since xmlResURL and xmlResPath are set only once, getAndAnalyzeXmlRes() is called only once
		if (xmlResURL != null)
			getAndAnalyzeXmlRes();
	}

	public String getXmlResURL() {
		return xmlResURL;
	}

	public void setXmlResURL(String xmlResURL) {
		this.xmlResURL = xmlResURL;
		//since xmlResURL and xmlResPath are set only once, getAndAnalyzeXmlRes() is called only once
		if (xmlResPath != null)
			getAndAnalyzeXmlRes();
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
		setDateByString(dataLatest, "1994-1-1");
		
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
						setDateByString(tmpCaldr, changeDate);
		
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
				while (idx2 >= 0) {
					String oneMissSec = nodataStr.substring(idx1, idx2).trim();
					int idxTo = oneMissSec.indexOf("to");
					String lateDate = oneMissSec.substring(0, idxTo);
					String earlyDate = oneMissSec.substring(idxTo + 2);
									
					setDateByString(tmpCaldr, lateDate);							
					stationArray[i].noDataSections[nodataIdx++] = (int)(tmpCaldr.getTimeInMillis() / DAY_MILLI);
					setDateByString(tmpCaldr, earlyDate);
					stationArray[i].noDataSections[nodataIdx++] = (int)(tmpCaldr.getTimeInMillis() / DAY_MILLI);
					
					// get the latest date that any station has data
					if (nodataIdx == 2) {
						nDaysBefore(tmpCaldr, 1, tmpCaldr);
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
			}					
		}
		setDataLatestDate(getDateString(dataLatest));
	}
	
	protected Document getXmlResDoc() {
		Document statusDoc = null;
		//String xmlUrl = "http://gf13.ucs.indiana.edu:8080//DailyRDAHMM-portlet/station-status-change-rss.xml";
		try {
			// if the file is old or does not exist, copy it from xmlResUrl
			boolean shouldCopy = false;		
			File localFile = new File(xmlResPath);
			if (localFile.exists()) {		
				Calendar calFile1 = Calendar.getInstance();
				Calendar calFile2 = Calendar.getInstance();
				calFile2.setTimeInMillis(localFile.lastModified());
				shouldCopy = !( calFile1.get(Calendar.YEAR) == calFile2.get(Calendar.YEAR) 
								&& calFile1.get(Calendar.MONTH) == calFile2.get(Calendar.MONTH) 
								&& calFile1.get(Calendar.DATE) == calFile2.get(Calendar.DATE) );
				// since the service runs at 4:00am every day, if the file was copied before 4:00am, and now is
				// after 4:00am, then the file should be copied again.
				if (!shouldCopy && calFile2.get(Calendar.HOUR_OF_DAY) <= 4 && calFile1.get(Calendar.HOUR_OF_DAY) >= 4) {
					shouldCopy = true;
				}
				if (shouldCopy) {
					localFile.delete();
				}
			} else {
				shouldCopy = true;		
			}

			if (shouldCopy) {
				InputStream inUrl = new URL(xmlResURL).openStream();
				OutputStream outLocalFile = new FileOutputStream(localFile);
				byte[] buf = new byte[1024];
				int length;
				while((length = inUrl.read(buf))>0) {
					outLocalFile.write(buf,0,length);
				}
				inUrl.close();
				outLocalFile.close();
			}		

			BufferedReader br = new BufferedReader(new FileReader(localFile));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			SAXReader reader = new SAXReader();
			statusDoc = reader.read( new StringReader(sb.toString()) );          
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return statusDoc;
	}
	
	public static void setDateByString(Calendar date, String str) {
		String year, month, day;
		int i1, i2;
		i1 = str.indexOf("-");
		i2 = str.indexOf("-", i1+1);
		year = str.substring(0, i1);
		month = str.substring(i1+1, i2);
		day = str.substring(i2+1);
		date.set(Calendar.YEAR, Integer.parseInt(year, 10));
		date.set(Calendar.MONTH, Integer.parseInt(month, 10)-1);
		date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));
	}
	
	public static String getDateString(Calendar date) {
		String str = date.get(Calendar.YEAR) + "-";
		if (date.get(Calendar.MONTH) < 9)
			str += '0';
		str += date.get(Calendar.MONTH) + 1 + "-";
		if (date.get(Calendar.DAY_OF_MONTH) < 10)
			str += '0';
		str += date.get(Calendar.DAY_OF_MONTH);
		return str;
	}
	
	public void calcStationColors() {
		StringBuffer res;
		res = new StringBuffer();
		
		float leftLat = 0, downLong = 0, rightLat = 0, upLong = 0;
		// regionLimmit is formatted like "lat1,long1;lat2,long2"
		int scIdx, commerIdx;
		scIdx = regionLimit.indexOf(';'	);
		if (scIdx >= 0) {
			commerIdx = regionLimit.indexOf(',');
			if (commerIdx >= 0 && commerIdx < scIdx) {
				leftLat = Float.parseFloat(regionLimit.substring(0, commerIdx));
				downLong = Float.parseFloat(regionLimit.substring(commerIdx + 1, scIdx));
				
				commerIdx = regionLimit.indexOf(',', scIdx + 1);
				if (commerIdx >= 0) {
					rightLat = Float.parseFloat(regionLimit.substring(scIdx + 1, commerIdx));
					upLong = Float.parseFloat(regionLimit.substring(commerIdx + 1));
					
					Calendar theDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					theDate.set(Calendar.HOUR_OF_DAY, 12);
					theDate.set(Calendar.MINUTE, 0);
					theDate.set(Calendar.SECOND, 0);
					theDate.set(Calendar.MILLISECOND, 0);
					setDateByString(theDate, date);
					for (int i=0; i<stationArray.length; i++) {
						/*
						if (stationArray[i].latitude > leftLat && stationArray[i].longitude > downLong
								 && stationArray[i].latitude < rightLat && stationArray[i].longitude < upLong) {
							res.append(getColorForStation(theDate, stationArray[i])).append(',');
						} */
						res.append(getColorForStation(theDate, stationArray[i])).append(',');
					}
				}
			}
		}
		
		System.out.println("about to return from calcStationColors");
		setColorResult(res.toString());
	}
	
	/**
	 * Get the color for a station on a specific date. Return values:
	 *  0:green, 1:red, 2:yellow, 3:grey, 4:blue	
	  * */
	public char getColorForStation(Calendar theDate, DailyRdahmmStation station) {
		int NDAYS = 30;
		Calendar firstDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		nDaysBefore(firstDate, NDAYS, theDate);
		
		// since javascript time is larger than java time by timeDiff, and the time stored in the station Array is java time, we should reduce javascript time by timeDiff to make it comparable with java time
		int startTime = (int)(firstDate.getTimeInMillis() / DAY_MILLI);
		int endTime = (int)(theDate.getTimeInMillis() / DAY_MILLI);
		int nodataIdx = getNoDataIdx(theDate, station);
		// if no data for a month before the date, then there is no need to check state change
		if (nodataIdx >= 0 && station.noDataSections[nodataIdx+1] < startTime)
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
	 * If there is no data for 'station' on 'theDate', get the index in the no data sections array
	 * */
	public int getNoDataIdx(Calendar theDate, DailyRdahmmStation station) {
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
	
	/**
	 * Set the argument 'before' as the date n days before 'theDate'  
	 * */
	public void nDaysBefore(Calendar before, int n, Calendar theDate) {
		before.setTimeInMillis(theDate.getTimeInMillis() - n * DAY_MILLI);
	}

	public String getDataLatestDate() {
		return dataLatestDate;
	}

	public void setDataLatestDate(String dataLatestDate) {
		this.dataLatestDate = dataLatestDate;
	}
	
}

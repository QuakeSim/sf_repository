package cgl.webservices;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DailyRDAHMMThread implements Runnable {

	protected DailyRDAHMMRunner runner = null;
	static final long dayMilli = 86400*1000;
	static final Object fileMutex = new Object();
	static int nStations = -1;
	static int nStationsDone = 0;
	static final Object lock_nStationsDone = new Object();
	static final Object lock_runRdahmm = new Object();
	static TreeSet<String> proNamesForBigInput = new TreeSet<String>();
	static int objCount = 0;
	
	protected Vector<String> evalRawLines = new Vector<String>();		//for saving lines from the GRWS queried input file in evaluating phase
	protected Vector<String> evalQLines = new Vector<String>();		//for saving lines from the .Q result file in evaluating phase
	protected int threadNum;
	
	public DailyRDAHMMThread (DailyRDAHMMRunner runner) {
		this.runner = runner;
		threadNum = objCount++;
	}
	
	/**
	 * keep getting the next station to run, run RDAHMM on it, and add the running results to the result xml file
	 */
	public void run() {
		if (runner == null) {
			UtilSet.log(threadNum, "runner is null, the DailyRDAHMMThread is going to exit...");
			return;
		}
		
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		Calendar today = Calendar.getInstance();
		Calendar nextRunDateTime = Calendar.getInstance();
		today.setTimeInMillis(time);
		DailyRDAHMMStation station = null;
		Vector<String> proNames = new Vector<String>();
		Vector<String> yestoday_proNames = new Vector<String>();
		String resPath = null;
		while (true) {
			try {
				//get the next station to run
				station = getStationToRun(today);
				
				//if all stations are done today, go to sleep
				if (station == null) {					
					//rename the temporary file to the destined xml file, only done when the results for 
					//all stations have been added to the temporary file;
					boolean doRename = false;
					synchronized (lock_nStationsDone) {
						UtilSet.log(threadNum, "nStations = " + nStations + "; nStationsDone = " + nStationsDone);
						if (nStationsDone >= nStations) {
							doRename = true;
							nStationsDone = 0;
						}
					}
					if (runner.isStationXmlChanged()) {
						runner.saveStationXml();
					}
					if (doRename) {
						// rename the result file
						String xmlPath = DailyRDAHMMStation.stateChangeXMLPath;
						renameFile(resPath, xmlPath);
						renameFile(resPath.substring(0, resPath.length()-4) + "2.xml", 
									xmlPath.substring(0, xmlPath.length()-4) + "2.xml");
						
						// draw the plot for number of stations with state changes vs time
						String scnPath = DailyRDAHMMStation.stateChangeNumTracePath;
						saveStateChangeNums(scnPath, DailyRDAHMMStation.stateChangeNumJsiPath);
						plotStateChangeNums(scnPath, DailyRDAHMMStation.binDir);
						runner.stateChangeNums.clear();
						runner.saveStationXml();
						
						// make a video of the whole time period
						StringBuffer urlSb = new StringBuffer(DailyRDAHMMStation.videoServiceUrlPrefix);
						urlSb.append("dataSource=").append(DailyRDAHMMStation.dataSource).append("&preProcTreat=");
						urlSb.append(DailyRDAHMMStation.noDataTreatment).append("&resultUrl=");
						urlSb.append(DailyRDAHMMStation.resAccessUrl).append('/').append(UtilSet.getFileNamePart(xmlPath)).append("&startDate=");
						urlSb.append(DailyRDAHMMStation.defaultModelStartDate).append("&endDate=").append(UtilSet.getDateString(today));
						String videoUrl = callHttpService(urlSb.toString());
						UtilSet.log(threadNum, "video URL:" + videoUrl);
						// sleep for 1 minute to avoid concurrent access to the xml file with the video service
						Thread.sleep(1*60*1000);
						writeVideoUrlToXml(xmlPath, videoUrl);
					}
					
					//make a big flat input file containing 
					makeAllStationInput(DailyRDAHMMStation.baseDestDir, proNamesForBigInput, 
							UtilSet.getDateFromString(DailyRDAHMMStation.defaultModelStartDate), today);
					//delete yestoday's project directories
					deleteDirectories(yestoday_proNames, DailyRDAHMMStation.baseWorkDir, 
							DailyRDAHMMStation.baseDestDir, DailyRDAHMMStation.binDir);					
					
					nextRunDateTime.setTimeInMillis(System.currentTimeMillis());
					UtilSet.ndaysBeforeToday(nextRunDateTime, -1*DailyRDAHMMStation.repeatPeriodInDays, nextRunDateTime);
					nextRunDateTime.set(Calendar.HOUR_OF_DAY, 4);
					nextRunDateTime.set(Calendar.MINUTE, 0);
					nextRunDateTime.set(Calendar.SECOND, 0);
					
					System.gc();
					Thread.sleep(nextRunDateTime.getTimeInMillis() - System.currentTimeMillis());
					
					//get the wake up time, which is also the starting time of "today"
					time = System.currentTimeMillis();
					today.setTimeInMillis(time);
					
					// move projects names from proNames to yestoday_proNames
					yestoday_proNames.removeAllElements();
					for (Iterator<String> itr = proNames.iterator(); itr.hasNext();)
						yestoday_proNames.add(itr.next());
					proNames.removeAllElements();
					proNamesForBigInput.clear();
					
					continue;
				}
				
				UtilSet.log(threadNum, "about to process station " + station.getStationId() + "...");
				// build model
				if (!station.buildModel()) {
					continue;
				}
				if (!station.zipModelFiles()) {
					System.out.println("Failed to zip model files for station " + station.getStationId());
				}
				
				// do evaluation
				if (!station.evalAndPlot()) {
					continue;
				}

				// record the projects' names so that we can delete the directories "tomorrow"
				String projectName = station.getProjectName();
				proNames.add(projectName);
				
				// make the flat input file
				if (station.makeFlatInputFile()) {
					synchronized (proNamesForBigInput) {
						proNamesForBigInput.add(projectName);
					}
				} else {
					UtilSet.log(threadNum, "Failed to make flat input file for station " + station.getStationId());
				}
			
				// write results to xml file, change the file name of the form A.xml to the form of A_tmp.xml
				resPath = DailyRDAHMMStation.stateChangeXMLPath;
				resPath = resPath.substring(0, resPath.length() - 4) + "_tmp.xml";
				writeResToXML(today, resPath, station);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// ignore the error on one station, just treat it as if it's completed
				synchronized (lock_nStationsDone) {
					if (station != null)
						nStationsDone++;
				}
			}
		}
	}
	
	/** read the content of a file to a vector */
	void readFileToVector(String path, Vector<String> vec) {
		if (path == null || path.length() == 0 || vec == null)
			return;
		try {
			vec.removeAllElements();
			UtilSet.log(threadNum, "readFileToVector : " + path);
			String line;
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(path));
			line = br.readLine();
			while (line != null) {
				if (line.length() > 0) {
					vec.add(line);
					count++;
				}
				line = br.readLine();
			}
			UtilSet.log(threadNum, count + "lines read from " + path);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * read the "lineNum"th line of a file; lineNum ==  -1: get the last line
	 * */
	String readOneLineFromFile(String path, int lineNum) {
		if (path == null || path.length() == 0 || lineNum < -1)
			return null;
		try {
			UtilSet.log(threadNum, "readOneLineFromFile : " + path + " " + lineNum);
			String line = null;
			int count = -1;
			BufferedReader br = new BufferedReader(new FileReader(path));
			line = br.readLine();
			String lastLine = line;
			while (line != null) {
				if (line.length() > 0) {
					count++;
					if (count == lineNum)
						break;					
				}
				lastLine = line;
				line = br.readLine();
			}
			UtilSet.log(threadNum, count + "lines read from " + path);
			br.close();
			if (count == lineNum)
				return line;
			else if (lineNum == -1)
				return lastLine;
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

	/** 
	 * get the status change status from lastLines, add them to the result xml file 
	 * 
	 * @param today
	 * @param resPath
	 * @param rds
	 * @param lastLines
	 * @throws InterruptedException
	 */
	private void writeResToXML(Calendar today, String resPath, DailyRDAHMMStation station)
			throws InterruptedException {
		try {
			String projectName = station.getProjectName();
			readFileToVector(DailyRDAHMMStation.baseDestDir + File.separator + projectName + File.separator
								 + projectName + ".all.raw", evalRawLines);
			readFileToVector(DailyRDAHMMStation.baseDestDir + File.separator + projectName + File.separator
								 + projectName + ".all.Q", evalQLines);
			UtilSet.log(threadNum, "station " + station.getStationId() + ": evalRawLines " + evalRawLines.size() + "; evalQLines " + evalQLines.size());
			
			synchronized (fileMutex) {
				boolean doNextStep = true, newFile = false;
				resPath = resPath.replace('/', File.separatorChar);
				UtilSet.log(threadNum, "resPath: " + resPath);
				File outputFile = new File(resPath);
				File outputPretty = new File(resPath.substring(0, resPath.length()-4) + "2.xml");
				// if result file out of date, create a new one
				if (outputFile.exists()) {
					Calendar modifyDate = Calendar.getInstance();
					modifyDate.setTimeInMillis(outputFile.lastModified());
					if (modifyDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)
						|| modifyDate.get(Calendar.MONTH) < today.get(Calendar.MONTH)
						|| modifyDate.get(Calendar.DAY_OF_MONTH) < today.get(Calendar.DAY_OF_MONTH)) {
						if (outputFile.delete()) {
							outputPretty.delete();
							doNextStep = outputFile.createNewFile() && outputPretty.createNewFile();
							newFile = true;
						} else {
							UtilSet.log(threadNum, "Failed to delete the old RDAHMM result file!");
							doNextStep = false;
						}
					}					
				} else {
					doNextStep = outputFile.createNewFile() && outputPretty.createNewFile();
					newFile = true;
				}

				// create the xml document to add stuff to
				Document resDoc = null, resDocPretty = null;
				Element eleXml = null, eleXmlPretty = null;
				if (doNextStep) {
					// create the xml document to append
					if (newFile) {
						resDoc = DocumentHelper.createDocument();
						eleXml = resDoc.addElement("xml");
						resDocPretty = DocumentHelper.createDocument();
						eleXmlPretty = resDocPretty.addElement("xml");
						doNextStep = true;
					} else {
						SAXReader xmlReader = new SAXReader();
						try {
							FileReader fr = new FileReader(outputFile);
							resDoc = xmlReader.read(fr);
							eleXml = resDoc.getRootElement();
							fr.close();							
							fr = new FileReader(outputPretty);
							resDocPretty = xmlReader.read(fr);
							eleXmlPretty = resDocPretty.getRootElement();
							fr.close();
							doNextStep = true;
						} catch (DocumentException ex) {
							ex.printStackTrace();
							doNextStep = false;
						}
					}
				}

				// add the results to xml document, and write to the file
				if (doNextStep) {
					addResToXMLDoc(today, station, eleXml, eleXmlPretty);

					// write the document back to file					
					OutputFormat format = OutputFormat.createPrettyPrint();
					FileWriter fw2 = new FileWriter(outputPretty);
					XMLWriter writer2 = new XMLWriter(fw2, format);
					writer2.write(resDocPretty);
					writer2.close();
					fw2.close();
					
					FileWriter fw = new FileWriter(outputFile);
					XMLWriter writer = new XMLWriter(fw);
					writer.write(resDoc);
					writer.close();
					fw.close();				
				}
			} 
		} catch (Exception e) {
			UtilSet.log(threadNum, "exception when trying to write RDAHMM results of " + station.getStationId() + " to file!");
			e.printStackTrace();
		} 
		UtilSet.log(threadNum, "Daily RDAHMMThread -- Done with running RDAHMM on " + station.getStationId() + "...");	
	}

	/**
	 * get the status change status from lastLines, add them to xml document
	 * 
	 * @param today
	 * @param station
	 * @param rds
	 * @param lastLines
	 * @param eleRoot
	 */
	private void addResToXMLDoc(Calendar today, DailyRDAHMMStation station, Element eleRoot, Element eleRootPretty) {
		// if the rdahmm result output-pattern element is not there, create a new one
		addElePattern(station, eleRoot);
		addElePattern(station, eleRootPretty);
		
		if (eleRoot.element("station-count") == null) {
			eleRoot.addElement("station-count").setText( String.valueOf(runner.statoinList.size()) );
			eleRootPretty.addElement("station-count").setText( String.valueOf(runner.statoinList.size()) );
		}		
		
		// add the element to the xml document
		Element eleStation = eleRoot.addElement("station");
		Element eleStation2 = eleRootPretty.addElement("station");
		// id
		eleStation.addElement("id").setText(station.getStationId());
		eleStation2.addElement("id").setText(station.getStationId());
		
		// latitude and longitude
		String latText = String.valueOf(station.getLatitude());
		String longText = String.valueOf(station.getLongitude());
		eleStation.addElement("lat").setText(latText);
		eleStation2.addElement("lat").setText(latText);
		eleStation.addElement("long").setText(longText);
		eleStation2.addElement("long").setText(longText);
		
		// now the status changes
		long status1 = -1, status2 = -1;
		String str1 = "", str2 = "", date1 = "", date2 = "";
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar calToday2 = UtilSet.getDateFromString(UtilSet.getDateString(today));
		Calendar calMissingEnd = Calendar.getInstance();
		Calendar calMissingBegin = Calendar.getInstance();
		// every status change is formated like 2007-10-09:4to3;, and there are 20 change in one change group
		StringBuffer sbgroup = new StringBuffer(16*20);
		// every missing data section is formated like 2006-10-09to2007-10-13;
		// and we estimate that there are less than 20 sections for every station
		StringBuffer sbMissData = new StringBuffer(23*20);
		int count20 = 0;
		int changeCount = 0;
		int missCount = 0;
		if (evalQLines.size() > 0) {
			// data missing between last "data-available" date and "today"?
			date2 = getDateFromRawLine((String)evalRawLines.get(evalQLines.size()-1));
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal2, -1, cal2);
			if (cal2.compareTo(calToday2) < 0) {
				sbMissData.append(UtilSet.getDateString(calToday2)).append("to");
				sbMissData.append(UtilSet.getDateString(cal2)).append(';');
				missCount++;
			}
			
			// get data-missing time sections and state changes during the evaluation time
			for (int i = evalQLines.size() - 1; i > 0; i--) {
				// record section with missing data
				String dateTime1 = getDateTimeFromRawLine((String)evalRawLines.get(i-1));
				String dateTime2 = getDateTimeFromRawLine((String)evalRawLines.get(i));
				UtilSet.setDateTimeByString(cal1, dateTime1);
				UtilSet.setDateTimeByString(cal2, dateTime2);
				date1 = UtilSet.getDatePart(dateTime1);
				date2 = UtilSet.getDatePart(dateTime2);
				
				// if the last line is like "2008-10-03T22:22:22", then it's an end of a missing-data time section
				if (i == evalQLines.size()-1 
					&& dateTime2.indexOf(DailyRDAHMMStation.dupDataTime) >= 0) {
					calMissingEnd.setTimeInMillis(cal2.getTimeInMillis());
				}
				
				// situation like "2008-10-03T22:22:22 ..."
				//				  "2008-10-04T12:00:00 ...", where 2008-10-03 is an end of missing-data time section
				if (dateTime1.indexOf(DailyRDAHMMStation.dupDataTime) >=0 
					&& dateTime2.indexOf(DailyRDAHMMStation.dupDataTime) < 0) {
					calMissingEnd.setTimeInMillis(cal1.getTimeInMillis());
				}
				
				// if the first line is like "1994-01-01T22:22:22", then it's a start of a missing-data time section
				if (i == 1 && dateTime1.indexOf(DailyRDAHMMStation.dupDataTime) >= 0) {
					calMissingBegin.setTimeInMillis(cal1.getTimeInMillis());
					sbMissData.append(UtilSet.getDateString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateString(calMissingBegin)).append(';');
					missCount++;
				}
				
				// situation like "2008-09-03T12:00:00 ..."
				//				  "2008-09-04T22:22:22 ...", where 2008-09-04 is a start of missing-data time section
				if (dateTime1.indexOf(DailyRDAHMMStation.dupDataTime) < 0
					&& dateTime2.indexOf(DailyRDAHMMStation.dupDataTime) >= 0) {
					calMissingBegin.setTimeInMillis(cal2.getTimeInMillis());
					sbMissData.append(UtilSet.getDateString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateString(calMissingBegin)).append(';');
					missCount++;
				}

				// record status changes
				str1 = (String)evalQLines.get(i - 1);
				status1 = Long.valueOf(str1).longValue();
				if (status2 < 0) {
					str2 = (String)evalQLines.get(i);
					status2 = Long.valueOf(str2).longValue();
				}				
				if (status1 != status2) {
					changeCount++;
					sbgroup.append(date2);
					sbgroup.append(':').append(str1).append("to").append(str2);
					count20++;
					addStateChangeNum(date2, 1);
					if (count20 >= 20) {
						// add a status change element
						eleStation.addElement("status-changes").setText(sbgroup.toString());
						eleStation2.addElement("status-changes").setText(sbgroup.toString());
						sbgroup.setLength(0);
						count20 = 0;						
					} else {
						sbgroup.append(';');
					}					
				}
				status2 = status1;
				str2 = str1;				
			}
			
			// data missing between 1994-01-01 and the first "data-available" date?
			date1 = DailyRDAHMMStation.defaultModelStartDate;
			date2 = getDateFromRawLine((String)evalRawLines.get(0));
			UtilSet.setDateByString(cal1, date1);
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal2, 1, cal2);
			if (cal1.compareTo(cal2) < 0) {
				sbMissData.append(UtilSet.getDateString(cal2)).append("to");
				sbMissData.append(date1).append(';');
				missCount++;
			}
		} else {
			// if there is even no data for training model, there must be no data at all
			sbMissData.append(UtilSet.getDateString(calToday2)).append("to");
			sbMissData.append(DailyRDAHMMStation.defaultModelStartDate).append(';');
			missCount++;
		}
		
		// if there are still some status change information not written in to xml
		if (sbgroup.length() > 0) {
			eleStation.addElement("status-changes").setText(sbgroup.toString());
			eleStation2.addElement("status-changes").setText(sbgroup.toString());
		}
		
		eleStation.addElement("change-count").setText(String.valueOf(changeCount));
		eleStation2.addElement("change-count").setText(String.valueOf(changeCount));

		if (sbMissData.length() > 0) {
			eleStation.addElement("time-nodata").setText(sbMissData.toString());
			eleStation2.addElement("time-nodata").setText(sbMissData.toString());
		}
		
		eleStation.addElement("nodata-count").setText(String.valueOf(missCount));
		eleStation2.addElement("nodata-count").setText(String.valueOf(missCount));
	}

	protected void addElePattern(DailyRDAHMMStation station, Element eleRoot) {
		Element elePattern = eleRoot.element("output-pattern");
		if (elePattern == null) {
			elePattern = eleRoot.addElement("output-pattern");						
			Element tmpNode = elePattern.addElement("server-url");
			tmpNode.setText(DailyRDAHMMStation.resAccessUrl);
			
			tmpNode = elePattern.addElement("stateChangeNumTxtFile");
			String scnPath = DailyRDAHMMStation.stateChangeNumTracePath;
			tmpNode.setText(scnPath.substring(scnPath.lastIndexOf(File.separator)+1));
			
			tmpNode = elePattern.addElement("stateChangeNumJsInput");
			String scnJsiPath = DailyRDAHMMStation.stateChangeNumJsiPath;
			tmpNode.setText(scnJsiPath.substring(scnJsiPath.lastIndexOf(File.separator)+1));
			
			tmpNode = elePattern.addElement("allStationInputName");
			tmpNode.setText(DailyRDAHMMStation.allStationInputName);
			
			String filters = "Fill-Missing";
			if (DailyRDAHMMStation.detrendEnabled && DailyRDAHMMStation.denoiseEnabled) {
				filters += ",De-trend,De-noise";
			}
			tmpNode = elePattern.addElement("Filters");
			tmpNode.setText(filters);
			
			String projectName = station.getProjectName();
			String proNamePat = projectName.replaceAll(station.getStationId(), "{!station-id!}");
			String modelBaseName = station.getModelBaseName();
			String modelBasePat = modelBaseName.replaceAll(station.getStationId(), "{!station-id!}");
			
			tmpNode = elePattern.addElement("pro-dir");
			tmpNode.setText(proNamePat);			
			tmpNode = elePattern.addElement("AFile");
			tmpNode.setText(modelBasePat + ".A");		
			tmpNode = elePattern.addElement("BFile");
			tmpNode.setText(modelBasePat + ".B");			
			tmpNode = elePattern.addElement("InputFile");
			tmpNode.setText(proNamePat + ".all.input");
			tmpNode = elePattern.addElement("RawInputFile");
			tmpNode.setText(proNamePat + ".all.raw");
			tmpNode = elePattern.addElement("SwfInputFile");
			tmpNode.setText(proNamePat + ".plotswf.input");
			tmpNode = elePattern.addElement("LFile");
			tmpNode.setText(modelBasePat + ".L");			
			tmpNode = elePattern.addElement("XPngFile");
			tmpNode.setText(proNamePat + ".all.input.X.png");			
			tmpNode = elePattern.addElement("YPngFile");
			tmpNode.setText(proNamePat + ".all.input.Y.png");			
			tmpNode = elePattern.addElement("ZPngFile");
			tmpNode.setText(proNamePat + ".all.input.Z.png");			
			tmpNode = elePattern.addElement("PiFile");
			tmpNode.setText(modelBasePat + ".pi");	
			tmpNode = elePattern.addElement("QFile");
			tmpNode.setText(proNamePat + ".all.Q");			
			tmpNode = elePattern.addElement("MaxValFile");
			tmpNode.setText(modelBasePat + ".maxval");		
			tmpNode = elePattern.addElement("MinValFile");
			tmpNode.setText(modelBasePat + ".minval");		
			tmpNode = elePattern.addElement("RangeFile");
			tmpNode.setText(modelBasePat + ".range");
			tmpNode = elePattern.addElement("ModelFiles");
			tmpNode.setText(modelBasePat + ".zip");
		}
	}
	
	/**  get the date string from the a line of the raw data file received from GRWS query */
	static String getDateFromRawLine(String rawLine) {
		// a raw line is like "dond 2007-02-22T12:00:00 -2517566.0543 -4415531.3935 3841177.1618 0.0035 0.0055 0.0047"
		int idx = rawLine.indexOf(' ');
		if (idx < 0)
			return null;
		
		int idx2 = rawLine.indexOf('T', idx);
		if (idx2 < 0)
			return null;
		
		return rawLine.substring(idx+1, idx2);
	}
	
	/**  get the date-time string from the a line of the raw data file received from GRWS query */
	static String getDateTimeFromRawLine(String rawLine) {
		// a raw line is like "dond 2007-02-22T12:00:00 -2517566.0543 -4415531.3935 3841177.1618 0.0035 0.0055 0.0047"
		int idx = rawLine.indexOf(' ');
		if (idx < 0)
			return null;
		
		int idx2 = rawLine.indexOf(' ', idx+1);
		if (idx2 < 0)
			return null;
		
		return rawLine.substring(idx+1, idx2);
	}

	/**
	 * get the next station to run
	 * 
	 * @param today
	 * @return
	 * @throws InterruptedException
	 */
	private DailyRDAHMMStation getStationToRun(Calendar today) throws InterruptedException {
		int year, month, day;
		DailyRDAHMMStation ret = null;
		
		synchronized (runner.statoinList) {
			if (nStations < 0)
				nStations = runner.statoinList.size();
			
			boolean found = false;
			Iterator<DailyRDAHMMStation> iter = runner.statoinList.listIterator(0);
			while (iter.hasNext()) {
				DailyRDAHMMStation station = iter.next();
				// the station's latest evaluation time is recorded in the "thisEvalDateTime" field
				Calendar evalCal = station.getThisEvalDateTime();
				if (evalCal == null) {
					found = true;
				} else {
					year = evalCal.get(Calendar.YEAR);
					month = evalCal.get(Calendar.MONTH);
					day = evalCal.get(Calendar.DAY_OF_MONTH);

					if (year < today.get(Calendar.YEAR) || month < today.get(Calendar.MONTH)
							|| day < today.get(Calendar.DAY_OF_MONTH)) {
						found = true;
					}
				}
				if (found) {
					station.setThisEvalDateTime(today);
					ret = station;
					break;
				}
			}
		}
		return ret;
	}
	
	/**
	 * delete the projects' directories in baseWorkDir, baseDestDir, and binDir  
	 * @param proNames	Vector of the names of projects
	 * @param baseWorkDir
	 * @param baseDestDir
	 * @param binDir
	 */
	private void deleteDirectories(Vector<String> proNames, String baseWorkDir, String baseDestDir, String binDir) {
		String proName, workPath, destPath;
		boolean res;
		
		for (int i=0; i<proNames.size(); i++) {
			proName = (String)proNames.get(i);
			workPath = baseWorkDir + File.separator + proName;
			res = UtilSet.deleteDirectory(new File(workPath));
		
			if (!res) {
				UtilSet.log(threadNum, "failed to delete dir " + workPath);
			}
			
			destPath = baseDestDir + File.separator + proName;
			res = UtilSet.deleteDirectory(new File(destPath));
			
			if (!res) {
				UtilSet.log(threadNum, "failed to delete dir " + destPath);
			}
		}
	}
	
	/**
	 * rename the file of srcPath to newPath
	 * @param srcPath
	 * @param newPath
	 */
	private void renameFile(String srcPath, String newPath) {
		UtilSet.log(threadNum, "renameFile -- srcPath : " + srcPath);
		UtilSet.log(threadNum, "renameFile -- newPath : " + newPath);
		
		if (srcPath == null || newPath == null)
			return;
		
		synchronized (fileMutex) {
			File srcFile = new File(srcPath);
			if (!srcFile.exists())
				return;

			File newPathFile = new File(newPath);
			// if file of newPath exists, delete it
			if (newPathFile.exists()) {
				if (!newPathFile.delete()) {
					UtilSet.log(threadNum, "Failed to delete the old RDAHMM result file!");
					return;
				}
			}

			// rename the srcFile
			if (!srcFile.renameTo(newPathFile)) {
				UtilSet.log(threadNum, "Failed to rename the temporary file to the destined xml file!");
			}
		}
	}
	
	/**
	 * make a large input file containing all stations' input data since 1994-01-01
	 * @param baseDestDir
	 * @param proNames
	 * @param startDate
	 * @param endDate
	 */
	protected void makeAllStationInput(String baseDestDir, TreeSet<String> proNames, Calendar startDate, Calendar endDate){
		try {
			int i = 0;
			// create the ".tmp0" file, which contains the floating date strings, like "2008.0327" 
			String tmpPathPre = baseDestDir + File.separator + "all_stations.tmp";
			String tmpPath = tmpPathPre + i;
			PrintWriter prDates = new PrintWriter(new FileWriter(tmpPath, false));
			Calendar calTmp = Calendar.getInstance();
			calTmp.setTimeInMillis(startDate.getTimeInMillis());
			calTmp.set(Calendar.HOUR_OF_DAY, 12);
		  	calTmp.set(Calendar.MINUTE, 0);
		  	calTmp.set(Calendar.SECOND, 0);
		  	calTmp.set(Calendar.MILLISECOND, 0);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(endDate.getTimeInMillis());
			calEnd.set(Calendar.HOUR_OF_DAY, 12);
		  	calEnd.set(Calendar.MINUTE, 0);
		  	calEnd.set(Calendar.SECOND, 0);
		  	calEnd.set(Calendar.MILLISECOND, 0);
			while (calTmp.compareTo(calEnd) <= 0) {
				prDates.println(UtilSet.getDateString(calTmp));
				calTmp.set(Calendar.DATE, calTmp.get(Calendar.DATE) + 1);
			}
			prDates.close();
			
			// path of the title file containing one line: "time station1.x station1.y station1.z ..."
			String titlePath = baseDestDir + File.separator + "all_stations.title";
			StringBuffer sbTitle = new StringBuffer();
			for (Iterator<String> itr = proNames.iterator(); itr.hasNext();) {
				String proName = itr.next();
				String proInputPath = baseDestDir + File.separator + proName + File.separator + proName + ".all.input.flat";
				File fInput = new File(proInputPath);
				if (!fInput.exists()) {
					continue;
				}
				
				if (i == 0) {
					sbTitle.append("time ");
				}
				// a project name is like "daily_project_azu1_2008-02-22", where azu1 is the station id
				int idx = DailyRDAHMMStation.proNamePrefix.length() + 1;
				String stationID = proName.substring(idx, idx+4);
				sbTitle.append(stationID).append("-x ").append(stationID).append("-y ").append(stationID).append("-z ");
				
				// paste the temp file containing the content of previous ".all.input" files with the new ".all.input" file
				UtilSet.pasteFileAsColumn(tmpPathPre + i, proInputPath, tmpPathPre + (i+1), ' ');
				File fTmp = new File(tmpPathPre+i);
				fTmp.delete();
				
				i++;
			}
			PrintWriter prTitle = new PrintWriter(new FileWriter(titlePath, false));
			prTitle.println(sbTitle.toString());
			prTitle.close();
			
			String finalPath = baseDestDir + File.separator + DailyRDAHMMStation.allStationInputName;
			UtilSet.catTwoFiles(titlePath, tmpPathPre + i, finalPath);
			
			File fTmpLast = new File(tmpPathPre + i);
			fTmpLast.delete();
			File fTitle = new File(titlePath);
			fTitle.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	// add the state change number on date by addition
	protected void addStateChangeNum(String strDate, int addition) {
		synchronized (runner.stateChangeNums) {
			Integer oldValue = runner.stateChangeNums.get(strDate);
			if (oldValue == null) {
				runner.stateChangeNums.put(strDate, addition);
			} else {
				runner.stateChangeNums.put(strDate, oldValue + addition);
			}
		}
	}
	
	// save the state change numbers to a file
	protected void saveStateChangeNums(String scnPath, String jsiPath) {
		Calendar calTmp = UtilSet.getDateFromString(DailyRDAHMMStation.defaultModelStartDate);
		Calendar calToday = Calendar.getInstance();
		calToday.set(Calendar.HOUR_OF_DAY, 12);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);
		
		try {
			FileWriter fw = new FileWriter(scnPath, false);
			FileWriter fwJsi = new FileWriter(jsiPath, false);
			fwJsi.write("Date,Count\n");
			synchronized (runner.stateChangeNums) {
				while (calTmp.compareTo(calToday) <= 0) {
					String strDate = UtilSet.getDateString(calTmp);
					fw.write(strDate + " ");
					fwJsi.write(strDate + ","); 
					if (runner.stateChangeNums.containsKey(strDate)) {
						String count = runner.stateChangeNums.get(strDate).toString();
						fw.write(count + "\n");
						fwJsi.write(count + "\n");
					} else {
						fw.write("0\n");
						fwJsi.write("0\n");
					}				
					calTmp.set(Calendar.DATE, calTmp.get(Calendar.DATE) + 1);
				}
			}
			fw.close();
			fwJsi.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/** plot the state change number by time
	 * @param scnFilePath
	 * @param shDir
	 */
	protected void plotStateChangeNums(String scnFilePath, String shDir) {
		String plotScnCmd = DailyRDAHMMStation.plotScnCmdPattern;
		plotScnCmd = plotScnCmd.replaceFirst("<plotDir>", shDir);
		plotScnCmd = plotScnCmd.replaceAll("<scnTxtPath>", scnFilePath);
		String progPath = UtilSet.getProgFromCmdLine(plotScnCmd);
		String[] args = UtilSet.getArgsFromCmdLine(plotScnCmd);
		String destDir = UtilSet.getDirPart(scnFilePath);
		String outputPath = scnFilePath + ".plot_out";
		String errPath = scnFilePath + ".plot_err";
		UtilSet.antExecute(progPath, args, shDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Plotting failed when executing command:");
			System.out.println(plotScnCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
		} else {
			System.out.println("Successfully executed plot command " + plotScnCmd);
		}
	}

	/**
	 * call a web service through an http port
	 * @param url
	 * @return
	 */
	protected String callHttpService(String url) {
		try {
			URL serviceURL = new URL(url);
			URLConnection conn = serviceURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String htmlLine = in.readLine();
			StringBuffer sb = new StringBuffer();
			while (htmlLine != null) {
				sb.append(htmlLine);
				htmlLine = in.readLine();
			}
			in.close();
			
            int idx = sb.indexOf("return>");
            if (idx < 0)
                    return "";
            int idx2 = sb.indexOf("</", idx);
            return sb.substring(idx + "return>".length(), idx2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * write the url to daily rdahmm video to the xml result file
	 * @param resPath
	 */
	protected void writeVideoUrlToXml(String resPath, String videoUrl) {
		try {
			resPath = resPath.replace('/', File.separatorChar);
			File outputFile = new File(resPath);
			File outputPretty = new File(resPath.substring(0, resPath.length()-4) + "2.xml");
			
			// create the xml document to add stuff to
			Document resDoc = null, resDocPretty = null;
			Element eleXml = null, eleXmlPretty = null;
			SAXReader xmlReader = new SAXReader();

			FileReader fr = new FileReader(outputFile);
			resDoc = xmlReader.read(fr);
			eleXml = resDoc.getRootElement();
			fr.close();							
			fr = new FileReader(outputPretty);
			resDocPretty = xmlReader.read(fr);
			eleXmlPretty = resDocPretty.getRootElement();
			fr.close();
			
			Element elePattern = eleXml.element("output-pattern");
			Element eleVideoUrl = elePattern.addElement("video-url");
			eleVideoUrl.setText(videoUrl);
			
			Element elePatternPretty = eleXmlPretty.element("output-pattern");
			Element eleVideoUrlPretty = elePatternPretty.addElement("video-url");
			eleVideoUrlPretty.setText(videoUrl);
			
			// write the document back to file					
			OutputFormat format = OutputFormat.createPrettyPrint();
			FileWriter fw2 = new FileWriter(outputPretty);
			XMLWriter writer2 = new XMLWriter(fw2, format);
			writer2.write(resDocPretty);
			writer2.close();
			fw2.close();					
					
			FileWriter fw = new FileWriter(outputFile);
			XMLWriter writer = new XMLWriter(fw);
			writer.write(resDoc);
			writer.close();
			fw.close();				
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

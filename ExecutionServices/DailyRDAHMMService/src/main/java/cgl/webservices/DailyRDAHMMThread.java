package cgl.webservices;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class DailyRDAHMMThread implements Runnable {

	protected DailyRDAHMMRunner runner = null;
	static String evalStartDate = "2006-10-01";
	static String shortModelEndDate = "2007-09-30";
	static String shortEvalStartDate = "2007-10-01";
	static String modelStartDate = "1994-01-01";
	static String modelEndDate = "2006-09-30";	
	static final long dayMilli = 86400*1000;
	static final Object fileMutex = new Object();
	static String xmlPath = null;
	static String scnPath = null;
	static int nStations = -1;
	static int nStationsDone = 0;
	static final Object lock_nStationsDone = new Object();
	static final Object lock_runRdahmm = new Object();
	static final Object lock_shortEvalList = new Object();
	static Vector<String> modeledStations = new Vector<String>();
	static Vector<String> shortEvalList = new Vector<String>();	//some stations don't have historical data, so we just build the model with 
												//data from 2006-10-01 to 2007-09-30, and do evaluation from 2007-10-01 to today
	static TreeSet<String> allProNames = new TreeSet<String>();
	static int objCount = 0;
	
	protected Vector<String> modelRawLines = new Vector<String>(); 	//for saving lines from the GRWS queried input file in modeling phase
	protected Vector<String> modelQLines = new Vector<String>();		//for saving lines from the .Q result file in modeling phase
	protected Vector<String> evalRawLines = new Vector<String>();		//for saving lines from the GRWS queried input file in evaluating phase
	protected Vector<String> evalQLines = new Vector<String>();		//for saving lines from the .Q result file in evaluating phase
	protected String latStr = "";						//temporarily saved latitude for the station being modeled and evaluated
	protected String longStr = "";					//temporarily saved longitude for the station being modeled and evaluated
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
		Calendar tomorrow4 = Calendar.getInstance();
		Calendar modelEndCal = Calendar.getInstance();
		Calendar evalStartCal = UtilSet.getDateFromString(evalStartDate);
		Calendar shortModelEndCal = UtilSet.getDateFromString(shortModelEndDate);
		today.setTimeInMillis(time);
		String station = null;
		Vector<String> proNames = new Vector<String>();
		Vector<String> yestoday_proNames = new Vector<String>();
		String baseWorkDir = null;
		String baseDestDir = null;
		String binDir = null;
		String resPath = null;
		String yesterdayVideoPath = null;
		String videoConfigPath = null;
		boolean modelSuc = false;
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
					if (doRename) {
						// rename the result file
						renameFile(resPath, xmlPath);
						renameFile(resPath.substring(0, resPath.length()-4) + "2.xml", 
									xmlPath.substring(0, xmlPath.length()-4) + "2.xml");
						
						// draw the plot for number of stations with state changes vs time
						saveStateChangeNums(scnPath);
						plotStateChangeNums(scnPath, binDir);
						runner.stateChangeNums.clear();
						
						// make a video of the whole time period
						DailyRDAHMMVideoThread vt = new DailyRDAHMMVideoThread(videoConfigPath, null, today);
						vt.setVideoPathToDelete(yesterdayVideoPath);
						vt.setFinalVideoDir(resPath.substring(0, resPath.lastIndexOf(File.separator)));
						vt.start();
						yesterdayVideoPath = vt.getFinalVideoPath();
					}
					
					//make a big flat input file containing 
					makeAllStationInput(baseDestDir, allProNames, UtilSet.getDateFromString(modelStartDate), today);
					//delete yestoday's project directories
					deleteDirectories(yestoday_proNames, baseWorkDir, baseDestDir, binDir);					
					
					tomorrow4.setTimeInMillis(System.currentTimeMillis());
					UtilSet.ndaysBeforeToday(tomorrow4, -1, tomorrow4);
					tomorrow4.set(Calendar.HOUR_OF_DAY, 4);
					tomorrow4.set(Calendar.MINUTE, 0);
					tomorrow4.set(Calendar.SECOND, 0);
					
					System.gc();
					Thread.sleep(tomorrow4.getTimeInMillis() - System.currentTimeMillis());
					
					//get the wake up time, which is also the starting time of "today"
					time = System.currentTimeMillis();
					today.setTimeInMillis(time);
					
					// move projects names from proNames to yestoday_proNames
					yestoday_proNames.removeAllElements();
					for (Iterator<String> itr = proNames.iterator(); itr.hasNext();)
						yestoday_proNames.add(itr.next());
					proNames.removeAllElements();
					allProNames.clear();
					
					continue;
				}
				
				// run RDAHMM on this station
				DailyRDAHMMService rds = new DailyRDAHMMService(true, station);
				rds.runningLock = lock_runRdahmm;
				RDAHMMResultsBean rrb = null;
				
				UtilSet.log(threadNum, "Daily RDAHMMThread -- About to run RDAHMM on " + station + " from " 
										+ evalStartDate + " to " + UtilSet.getDateString(today) + " ...");
				if (!modeledStations.contains(station)) {
					modelSuc = trainModelOnStation(station, rds, today);
					if (modelSuc) {
						modeledStations.add(station);
					} else {
						if (!trainModelAndPlot(station, evalStartDate, UtilSet.getDateString(today), 
												UtilSet.getDateString(today), rds)) {
							continue;
						} else {
							fillNaNToFile(UtilSet.getDateFromString(modelStartDate), 
										UtilSet.getDateFromString(modelEndDate), 
										rds.modelWorkDir + File.separator + rds.modelBaseName + ".input.flat", 
										true);
						}
					}
				} else {
					modelSuc = true;
				}
				
				createModelZips(station, rds);
				
				readFileToVector(rds.modelWorkDir + File.separator + rds.modelBaseName + ".raw", modelRawLines);
				readFileToVector(rds.modelWorkDir + File.separator + rds.modelBaseName + ".Q", modelQLines);
				UtilSet.log(threadNum, "station:" + station + "; modelRawLines " + modelRawLines.size() + "; modelQLines " + modelQLines.size());
				if (modelRawLines.size() > 0) {
					UtilSet.setDateByString(modelEndCal, getDateFromRawLine((String)modelRawLines.get(modelRawLines.size()-1)) );
					// if the end date of model phase is later than normal evaluation starting date, short evaluation
					// should be used
					if (modelEndCal.compareTo(evalStartCal) >= 0 && modelEndCal.compareTo(shortModelEndCal) <= 0) {
						synchronized (lock_shortEvalList) {
							if (!shortEvalList.contains(station))
								shortEvalList.add(station);
						}
					}
				}
				
				if (modelSuc) {
					if (shortEvalList.contains(station)) {
						rrb = rds.runBlockingRDAHMM2(station, shortEvalStartDate, UtilSet.getDateString(today), runner.numOfStates);
					} else {
						rrb = rds.runBlockingRDAHMM2(station, evalStartDate, UtilSet.getDateString(today), runner.numOfStates);
					}
				}
				
				// concatenate the flat input files
				UtilSet.catTwoFiles(rds.modelWorkDir + File.separator + rds.modelBaseName + ".input.flat", 
						rds.outputDestDir + File.separator + rds.projectName + ".input.flat", 
						rds.outputDestDir + File.separator + rds.projectName + ".all.input.flat");
				
				// record the projects' names so that we can delete the directories "tomorrow"
				proNames.add(rds.projectName);
				synchronized (allProNames) {
					allProNames.add(rds.projectName);
				}
				if (baseWorkDir == null) {
					baseWorkDir = rds.baseWorkDir;
					baseDestDir = rds.baseDestDir;
					binDir = rds.binPath;
				}
			
				// generate the results
				readFileToVector(rds.baseWorkDir + File.separator + rds.projectName + File.separator
									 + rds.projectName+".raw", evalRawLines);
				readFileToVector(rds.baseWorkDir + File.separator + rds.projectName + File.separator
									 + rds.projectName+".Q", evalQLines);
				UtilSet.log(threadNum, "station:" + station + "; evalRawLines " + evalRawLines.size() + "; evalQLines " + evalQLines.size());
				
				// write results to file
				resPath = rds.properties.getProperty("dailyRdahmm.output.path");
				if (xmlPath == null)
					xmlPath = resPath;
				if (scnPath == null)
					scnPath = rds.properties.getProperty("dailyRdahmm.stateChangeNumTrace.path");
				if (videoConfigPath == null)
					videoConfigPath = rds.properties.getProperty("dailyRdahmm.video.config.path");
				// change the file of the form A.xml to the form of A_tmp.xml
				resPath = resPath.substring(0, resPath.length() - 4) + "_tmp.xml";
				writeResToXML(today, station, resPath, rds);				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// ignore the error on one station, just see it as completed
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
	 * @param station
	 * @param resPath
	 * @param rds
	 * @param lastLines
	 * @throws InterruptedException
	 */
	private void writeResToXML(Calendar today, String station, String resPath,
			DailyRDAHMMService rds)
			throws InterruptedException {
		try {
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
					addResToXMLDoc(today, station, rds, eleXml, eleXmlPretty);

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
			UtilSet.log(threadNum, "exception when trying to write RDAHMM results of " + station + " to file!");
			e.printStackTrace();
		} 
		UtilSet.log(threadNum, "Daily RDAHMMThread -- Done with running RDAHMM on " + station + "...");	
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
	private void addResToXMLDoc(Calendar today, String station,
			DailyRDAHMMService rds, Element eleRoot, Element eleRootPretty) {
		// if the rdahmm result output-pattern element is not there, create a new one
		addElePattern(station, rds, eleRoot);
		addElePattern(station, rds, eleRootPretty);
		
		if (eleRoot.element("station-count") == null) {
			eleRoot.addElement("station-count").setText( String.valueOf(runner.statoinList.size()) );
			eleRootPretty.addElement("station-count").setText( String.valueOf(runner.statoinList.size()) );
		}		
		
		// add the element to the xml document
		Element eleStation = eleRoot.addElement("station");
		Element eleStation2 = eleRootPretty.addElement("station");
		// id
		eleStation.addElement("id").setText(station);
		eleStation2.addElement("id").setText(station);
		
		String latText = latStr;
		String longText = longStr;
		if (latText.length() == 0 || longText.length() == 0) {
			UtilSet.log(threadNum, "latStr or longStr is empty!");
			for (int p=0; p<runner.statoinList.size(); p++) {
				String[] ele = (String [])runner.statoinList.get(p);
				if (ele[0].equals(station)) {
					latText = ele[2];
					longText = ele[3];
					break;
				}
			}
		}
		
		// latitude
		eleStation.addElement("lat").setText(latText);
		eleStation2.addElement("lat").setText(latText);
		// longitude
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
		// every missing data section is formated like 2006-10-09to2007-10-13;, and we estimate that there are less than 20 sections for every 
		StringBuffer sbMissData = new StringBuffer(23*20);
		int count20 = 0;
		int changeCount = 0;
		int missCount = 0;
		UtilSet.log(threadNum, "station " + station + ": evalQLines " + evalQLines.size() + "; evalRawLines " 
							+ evalRawLines.size() + "; modelQLines " + modelQLines.size() + "; modelRawLines " 
							+ modelRawLines.size());
		if (evalQLines.size() > 0) {
			date2 = getDateFromRawLine((String)evalRawLines.get(evalQLines.size()-1));
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal2, -1, cal2);
			if (cal2.compareTo(calToday2) < 0) {
				sbMissData.append(UtilSet.getDateString(calToday2)).append("to");
				sbMissData.append(UtilSet.getDateString(cal2)).append(';');
				missCount++;
			}
			UtilSet.ndaysBeforeToday(cal2, 1, cal2);
		} else if (modelQLines.size() > 0) {
			date2 = getDateFromRawLine((String)modelRawLines.get(modelQLines.size()-1));
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal2, -1, cal2);
			if (cal2.compareTo(calToday2) < 0) {
				sbMissData.append(UtilSet.getDateString(calToday2)).append("to");
				sbMissData.append(UtilSet.getDateString(cal2)).append(';');
				missCount++;
			}
			UtilSet.ndaysBeforeToday(cal2, 1, cal2);
		} else {
			// if there is even no data for training model, there must be no data at all
			sbMissData.append(UtilSet.getDateString(calToday2)).append("to").append(modelStartDate).append(';');
			missCount++;
		}
		
		if (evalQLines.size() > 1) {			
			for (int i = evalQLines.size() - 1; i > 0; i--) {
				// record section with missing data
				date1 = getDateTimeFromRawLine((String)evalRawLines.get(i-1));
				date2 = getDateTimeFromRawLine((String)evalRawLines.get(i));
				UtilSet.setDateTimeByString(cal1, date1);
				UtilSet.setDateTimeByString(cal2, date2);
				date1 = UtilSet.getDatePart(date1);
				date2 = UtilSet.getDatePart(date2);
				
				// if the last line is like "2008-10-03T22:22:22", then it's an end of a missing-data time section
				if (i == evalQLines.size()-1 
					&& cal2.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingEnd.setTimeInMillis(cal2.getTimeInMillis());
				}
				
				// situation like "2008-10-03T22:22:22 ..."
				//				  "2008-10-04T12:00:00 ...", where 2008-10-03 is an end of missing-data time section
				if (cal1.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME
					&& cal2.get(Calendar.HOUR_OF_DAY) != DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingEnd.setTimeInMillis(cal1.getTimeInMillis());
				}
				
				// if the first line is like "1994-01-01T22:22:22", then it's a start of a missing-data time section
				if (i == 1 && cal1.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingBegin.setTimeInMillis(cal1.getTimeInMillis());
					sbMissData.append(UtilSet.getDateString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateString(calMissingBegin)).append(';');
					missCount++;
				}
				
				// situation like "2008-09-03T12:00:00 ..."
				//				  "2008-09-04T22:22:22 ...", where 2008-09-04 is a start of missing-data time section
				if (cal1.get(Calendar.HOUR_OF_DAY) != DailyRDAHMMService.DUP_LINE_TIME
					&& cal2.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
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
		}
		
		// the state sequence before the evaluation starting date is contained in the .Q file of the modeling phase
		// this is to compare the first line of evalQLines and the last line of modelQLines
		if (evalQLines.size() > 0 && modelQLines.size() > 0) {
			// since we make sure that evalQLines.size() > 1 && modelQLines.size() > 1
			// by filling missing data, there is no need to check missing data here
			date1 = getDateFromRawLine(modelRawLines.get(modelRawLines.size() - 1));
			date2 = getDateFromRawLine(evalRawLines.get(0));
			UtilSet.setDateByString(cal1, date1);
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal1, -1, cal1);
			UtilSet.ndaysBeforeToday(cal2, 1, cal2);
			if (cal1.compareTo(cal2) < 0) {
				sbMissData.append(UtilSet.getDateString(cal2)).append("to");
				sbMissData.append(UtilSet.getDateString(cal1)).append(';');
				missCount++;
			}
			UtilSet.ndaysBeforeToday(cal1, 1, cal1);
			UtilSet.ndaysBeforeToday(cal2, -1, cal2);
			
			// record status changes
			str1 = (String)modelQLines.get(modelQLines.size() - 1);
			str2 = (String)(evalQLines.get(0));
			status1 = Long.valueOf(str1).longValue();
			status2 = Long.valueOf(str2).longValue();
			if (status1 != status2) {
				changeCount++;
				sbgroup.append( date2 );
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
		}
		
		// this is for the modelQLines
		if (modelQLines.size() > 1) {
			status1 = -1;
			status2 = -1;
			for (int j = modelQLines.size() - 1; j > 0; j--) {				
				// record section with missing data
				date1 = getDateTimeFromRawLine((String)modelRawLines.get(j-1));
				date2 = getDateTimeFromRawLine((String)modelRawLines.get(j));
				UtilSet.setDateTimeByString(cal1, date1);
				UtilSet.setDateTimeByString(cal2, date2);
				date1 = UtilSet.getDatePart(date1);
				date2 = UtilSet.getDatePart(date2);
				
				// if the last line is like "2008-10-03T22:22:22", then it's an end of a missing-data time section
				if (j == modelQLines.size()-1 
					&& cal2.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingEnd.setTimeInMillis(cal2.getTimeInMillis());
				}
				
				// situation like "2008-10-03T22:22:22 ..."
				//				  "2008-10-04T12:00:00 ...", where 2008-10-03 is an end of missing-data time section
				if (cal1.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME
					&& cal2.get(Calendar.HOUR_OF_DAY) != DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingEnd.setTimeInMillis(cal1.getTimeInMillis());
				}
				
				// if the first line is like "1994-01-01T22:22:22", then it's a start of a missing-data time section
				if (j == 1 && cal1.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingBegin.setTimeInMillis(cal1.getTimeInMillis());
					sbMissData.append(UtilSet.getDateString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateString(calMissingBegin)).append(';');
					missCount++;
				}
				
				// situation like "2008-09-03T12:00:00 ..."
				//				  "2008-09-04T22:22:22 ...", where 2008-09-04 is a start of missing-data time section
				if (cal1.get(Calendar.HOUR_OF_DAY) != DailyRDAHMMService.DUP_LINE_TIME
					&& cal2.get(Calendar.HOUR_OF_DAY) == DailyRDAHMMService.DUP_LINE_TIME) {
					calMissingBegin.setTimeInMillis(cal2.getTimeInMillis());
					sbMissData.append(UtilSet.getDateString(calMissingEnd)).append("to");
					sbMissData.append(UtilSet.getDateString(calMissingBegin)).append(';');
					missCount++;
				}
				
				// record status changes
				str1 = (String)modelQLines.get(j - 1);
				status1 = Long.valueOf(str1).longValue();
				if (status2 < 0) {
					str2 = (String)modelQLines.get(j);
					status2 = Long.valueOf(str2).longValue();
				}
				if (status1 != status2) {
					changeCount++;
					sbgroup.append( getDateFromRawLine((String)modelRawLines.get(j)) );
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
		}
		
		if (modelQLines.size() > 0) {
			date1 = modelStartDate;
			date2 = getDateFromRawLine((String)modelRawLines.get(0));
			UtilSet.setDateByString(cal1, date1);
			UtilSet.setDateByString(cal2, date2);
			UtilSet.ndaysBeforeToday(cal2, 1, cal2);
			if (cal1.compareTo(cal2) < 0) {
				sbMissData.append(UtilSet.getDateString(cal2)).append("to");
				sbMissData.append(date1).append(';');
				missCount++;
			}
			UtilSet.ndaysBeforeToday(cal2, -1, cal2);
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

	protected void addElePattern(String station, DailyRDAHMMService rds,
			Element eleRoot) {
		Element elePattern = eleRoot.element("output-pattern");
		if (elePattern == null) {
			elePattern = eleRoot.addElement("output-pattern");						
			Element tmpNode = elePattern.addElement("server-url");
			tmpNode.setText(rds.serverUrl);
			
			tmpNode = elePattern.addElement("stateChangeNumTxtFile");
			if (scnPath == null)
				scnPath = rds.properties.getProperty("dailyRdahmm.stateChangeNumTrace.path");
			tmpNode.setText(scnPath.substring(scnPath.lastIndexOf(File.separator)+1));
			
			tmpNode = elePattern.addElement("allStationInputName");
			tmpNode.setText(DailyRDAHMMService.allStationInputName);
			
			tmpNode = elePattern.addElement("videoFile");
			tmpNode.setText(modelStartDate + "to" + UtilSet.getDateString(Calendar.getInstance()) + ".mpeg");
			
			String proNamePat = rds.projectName.replaceAll(station, "{!station-id!}");			
			String modelBasePat = rds.modelBaseName.replaceAll(station, "{!station-id!}");
			
			tmpNode = elePattern.addElement("pro-dir");
			tmpNode.setText(proNamePat);			
			tmpNode = elePattern.addElement("AFile");
			tmpNode.setText(modelBasePat + ".A");		
			tmpNode = elePattern.addElement("BFile");
			tmpNode.setText(modelBasePat + ".B");			
			tmpNode = elePattern.addElement("InputFile");
			tmpNode.setText(proNamePat + ".input");		
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
			tmpNode.setText(proNamePat + ".Q");			
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
	 * get the id of the next station to run
	 * 
	 * @param today
	 * @return
	 * @throws InterruptedException
	 */
	private String getStationToRun(Calendar today) throws InterruptedException {
		String ret = null;
		int year, month, day, pos1, pos2;
		latStr = "";
		longStr = "";
		
		synchronized (runner.statoinList) {
			if (nStations < 0)
				nStations = runner.statoinList.size();
			
			for (int i = 0; i < runner.statoinList.size(); i++) {
				// the element in stationList : [stationId, dateOfRun]
				String[] s = (String[]) runner.statoinList.elementAt(i);
				String dateStr = s[1];

				// dateStr is formatted like 2007-10-29
				pos1 = dateStr.indexOf('-');
				pos2 = -1;
				if (pos1 >= 0)
					pos2 = dateStr.indexOf('-', pos1 + 1);
				if (pos1 >= 0 && pos2 >= 0) {
					year = Integer.valueOf(dateStr.substring(0, pos1)).intValue();
					month = Integer.valueOf(dateStr.substring(pos1 + 1, pos2)).intValue();
					day = Integer.valueOf(dateStr.substring(pos2 + 1)).intValue();
				} else
					year = month = day = 0;

				if (year < today.get(Calendar.YEAR) || month < today.get(Calendar.MONTH)+1
						|| day < today.get(Calendar.DAY_OF_MONTH)) {
					ret = s[0];
					s[1] = UtilSet.getDateString(today);
					latStr = s[2];
					longStr = s[3];
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
		String proName, baseWorkPath, baseDestPath, binPath, res;
		
		for (int i=0; i<proNames.size(); i++) {
			proName = (String)proNames.get(i);
			baseWorkPath = baseWorkDir + proName;
			res = UtilSet.exec("rm -f -r " + baseWorkPath.replace('/', File.separatorChar));
			
			baseDestPath = baseDestDir + '/' + proName;
			res = UtilSet.exec("rm -f -r " + baseDestPath.replace('/', File.separatorChar));
						
			binPath = binDir + '/' + proName + ".*";
			res = UtilSet.exec("rm -f -r " + binPath.replace('/', File.separatorChar));
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
	 * Fill "NaN NaN NaN" lines to the file specified with path, for time between calStart and calEnd
	 * @param calStart
	 * @param calEnd
	 * @param path
	 */
	protected void fillNaNToFile(Calendar calStart, Calendar calEnd, String path, boolean addToStart) {
		try {
			String tmpPath = path + ".tmp";
			PrintWriter pwTmp = new PrintWriter(new FileWriter(tmpPath, true));
			Calendar calTmp = Calendar.getInstance();
			calTmp.setTimeInMillis(calStart.getTimeInMillis());
			while (calTmp.compareTo(calEnd) <= 0) {
				pwTmp.println("NaN NaN NaN");
				UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
			}
			pwTmp.flush();
			pwTmp.close();
			
			String tmpPath2 = path + ".tmp2";
			if (addToStart)
				UtilSet.catTwoFiles(tmpPath, path, tmpPath2);
			else
				UtilSet.catTwoFiles(path, tmpPath, tmpPath2);
			
			File fTmp = new File(tmpPath);
			fTmp.delete();
			fTmp = new File(path);
			fTmp.delete();
			
			UtilSet.renameFile(tmpPath2, path);
		} catch (Exception e) {
			e.printStackTrace();
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
				prDates.println(UtilSet.getFloatDateString(calTmp));
				calTmp.set(Calendar.DATE, calTmp.get(Calendar.DATE) + 1);
			}
			prDates.close();
			
			// path of the title file containing one line: "time station1.x station1.y station1.z ..."
			String titlePath = baseDestDir + File.separator + "all_stations.title";
			StringBuffer sbTitle = new StringBuffer();
			String res = null;
			for (Iterator<String> itr = proNames.iterator(); itr.hasNext();) {
				String proName = itr.next();
				String proInputPath = baseDestDir + File.separator + proName + File.separator + proName + ".all.input.flat";
				File fInput = new File(proInputPath);
				if (!fInput.exists()) {
					continue;
				}
				
				if (i == 0) {
					sbTitle.append("time ");
				} else {
					// a project name is like "daily_project_azu1_2008-02-22", where azu1 is the station id
					int idx = DailyRDAHMMService.proNamePrefix.length();
					String stationID = proName.substring(idx, idx+4);
					sbTitle.append(stationID).append("-x ").append(stationID).append("-y ").append(stationID).append("-z ");
				}
				
				// paste the temp file containing the content of previous ".all.input" files with the new ".all.input" file
				res = UtilSet.exec("paste -d\" \" " + tmpPathPre + i + " " + proInputPath + " > " + tmpPathPre + (i+1));
				if (res != null && res.length() > 0)
					UtilSet.log(threadNum, "paste result: " + res);
				File fTmp = new File(tmpPathPre+i);
				fTmp.delete();
				
				i++;
			}
			PrintWriter prTitle = new PrintWriter(new FileWriter(titlePath, false));
			prTitle.println(sbTitle.toString());
			prTitle.close();
			
			String finalPath = baseDestDir + File.separator + DailyRDAHMMService.allStationInputName;
			res = UtilSet.exec("cat " + titlePath + " " + tmpPathPre + i + " > " + finalPath);
			UtilSet.log(threadNum, "cat result: " + res);
			
			File fTmpLast = new File(tmpPathPre + i);
			fTmpLast.delete();
			File fTitle = new File(titlePath);
			fTitle.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * train the RDAHMM model for a station, set the modelSuc parameter to whether the model has been
	 * built successfully, and return whether evaluation should be done on this station
	 * @param station
	 * @param rds
	 */
	protected boolean trainModelOnStation(String station, DailyRDAHMMService rds, Calendar today) {
		// check if the model has already been built
		File modelDirFile = new File(rds.modelWorkDir);
		if ((modelDirFile.exists())) {
			if (modelDirFile.isDirectory()) {
				File modelQFile = new File(rds.modelWorkDir + File.separator + rds.modelBaseName + ".Q");
				if (modelQFile.exists() && modelQFile.isFile() && modelQFile.length() > 0) {
					String rawPath = rds.modelWorkDir + File.separator + rds.modelBaseName + ".raw";
					Calendar cal1 = UtilSet.getDateFromString(getDateFromRawLine(readOneLineFromFile(rawPath, -1)));
					Calendar modelEnd = UtilSet.getDateFromString(shortModelEndDate);
					if (cal1.getTimeInMillis() <= modelEnd.getTimeInMillis()) {
						UtilSet.log(threadNum, "Model Files for " + station + " Exists, No Need to train Model!!!!!!!!!!!!! " 
									+ UtilSet.getDateString(cal1) + "<=" + UtilSet.getDateString(modelEnd));
						return true;
					}
				}
				UtilSet.exec("rm -f -r " + rds.modelWorkDir + File.separator + "*");
			} else {
				UtilSet.exec("rm -f -r " + rds.modelWorkDir);
			}
		}	
		
		// make the modelWorkDir for saving model results
		String err;
		if (!modelDirFile.exists()) {
			if(!modelDirFile.mkdirs())
				return false;
		}
		
		// download the input file
		String inputFile = null;
		boolean isShortModel = false;
		try {
			String url = rds.querySOPACGetURL(station, modelStartDate, modelEndDate);
			if (url.indexOf("ERROR") >= 0) {
				url = rds.querySOPACGetURL(station, evalStartDate, shortModelEndDate);
				if (url.indexOf("ERROR") >= 0) {
					return false;
				} else
					isShortModel = true;
			}
			inputFile = rds.makeRdahmmInputFile(url, rds.modelBaseName, rds.modelWorkDir);
			if (isShortModel) {
				Calendar calStart = UtilSet.getDateFromString(modelStartDate);
				Calendar calEnd = UtilSet.getDateFromString(modelEndDate);
				String flatPath = rds.modelWorkDir + File.separator + rds.modelBaseName + ".input.flat";
				fillNaNToFile(calStart, calEnd, flatPath, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// run rdahmm in training mode on the specific input file
		String exeRdahmm = rds.binPath + "/rdahmm" + " -data " + inputFile + " -T " + rds.getLineCount(inputFile) 
							+ " -D " + rds.getFileDimension(inputFile) + " -N " + runner.numOfStates 
							+ " -output_type gauss -anneal -annealfactor 1.1 -betamin 0.1" 
							+ " -regularize -omega 0 0 1 1.0e-6 -ntries 10 -seed 1234";
		err = UtilSet.exec(exeRdahmm);
		
		if (err.length() > 0)
			UtilSet.log(threadNum, "trainModelOnStation training output: " + err);
		
		return err.length() == 0;
	}
	
	protected boolean trainModelAndPlot(String station, String modelStartDate, String modelEndDate, 
										String evalEndDate, DailyRDAHMMService rds) {
		UtilSet.log(threadNum, "about to do trainModelAndPlot on station " + station);
		
		// first train the model		
		// make the modelWorkDir for saving model results
		File modelDirFile = new File(rds.modelWorkDir);		
		String err;
		if (!modelDirFile.exists()) {
			err = UtilSet.exec("mkdir " + rds.modelWorkDir);
			if (err.length() > 0)
				return false;	
		}
		UtilSet.exec("rm -f -r " + rds.modelWorkDir + File.separator + "*");
		
		// download the input file
		String inputFile = null;
		try {
			String url = rds.querySOPACGetURL(station, modelStartDate, modelEndDate);
			if (url.indexOf("ERROR") >= 0) {
				return false;
			}
			inputFile = rds.makeRdahmmInputFile(url, rds.modelBaseName, rds.modelWorkDir);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// run rdahmm in training mode on the specific input file
		String exeRdahmm = rds.binPath + "/rdahmm" + " -data " + inputFile + " -T " + rds.getLineCount(inputFile) 
							+ " -D " + rds.getFileDimension(inputFile) + " -N " + runner.numOfStates 
							+ " -output_type gauss -anneal -annealfactor 1.1 -betamin 0.1" 
							+ " -regularize -omega 0 0 1 1.0e-6 -ntries 10 -seed 1234";
		err = UtilSet.exec(exeRdahmm);
		
		if (err.length() > 0) { 
			UtilSet.log(threadNum, "trainModelAndPlot Fails: " + err);
			return false;
		}
		
		// then do fake evaluation on the model, which is just plotting and file copying stuff
		err = UtilSet.exec("mkdir " + rds.baseDestDir);
		// Set up the work directory
		String workDir = rds.baseWorkDir + File.separator + rds.projectName;
		err = UtilSet.exec("mkdir " + workDir);
		Calendar calEvalStart = UtilSet.getDateFromString(modelEndDate);
		UtilSet.ndaysBeforeToday(calEvalStart, -1, calEvalStart);
		rds.fakeEvaluation(calEvalStart, UtilSet.getDateFromString(evalEndDate));
		return true;
	}
	
	// create the zipped model files
	protected void createModelZips(String station, DailyRDAHMMService rds){
		String zipPath = rds.baseDestDir + File.separator + rds.modelBaseName + ".zip";
		File fzip = new File(zipPath);
		if (fzip.exists())
			return;
		String exeZip = "zip -r " + zipPath + " " + rds.modelBaseName;
		String err = UtilSet.exec(exeZip, new File(rds.baseWorkDir));
		UtilSet.log(threadNum, "zipping model " + exeZip + "output: " + err);
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
	protected void saveStateChangeNums(String filePath) {
		Calendar calTmp = UtilSet.getDateFromString(modelStartDate);
		Calendar calToday = Calendar.getInstance();
		calToday.set(Calendar.HOUR_OF_DAY, 12);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);
		
		try {
			FileWriter fw = new FileWriter(filePath, false);
			synchronized (runner.stateChangeNums) {
				while (calTmp.compareTo(calToday) <= 0) {
					String strDate = UtilSet.getDateString(calTmp);
					fw.write(strDate + " ");
					if (runner.stateChangeNums.containsKey(strDate)) {
						fw.write(runner.stateChangeNums.get(strDate).intValue() + "\n");
					} else {
						fw.write("0\n");
					}				
					calTmp.set(Calendar.DATE, calTmp.get(Calendar.DATE) + 1);
				}
			}
			fw.flush();
			fw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/** plot the state change number by time
	 * @param scnFilePath
	 * @param shDir
	 */
	protected void plotStateChangeNums(String scnFilePath, String shDir) {
		String plotSh = shDir + File.separator + "plot_stateNum.sh";
		System.out.println("about to executing " + plotSh + " " + scnFilePath);
		String res = UtilSet.exec(plotSh + " " + scnFilePath, new File(shDir));
		System.out.println("result for plotting state change numbers:" + res);
	}
}

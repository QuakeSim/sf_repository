package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

public class DailyRDAHMMStation {
	/** we have one process running for each data source, so keep this with a static variable*/ 
	static String dataSource;
	/** pre-processing treatment to missing data times */
	static String noDataTreatment;
	/** time in days to re-run the service */
	static int repeatPeriodInDays;
	/** URL for accessing the daily RDAHMM results, e.g., state change xml, plots, files, etc.*/
	static String resAccessUrl;
	/** base directory to save the daily RDAHMM results. a sub dir is created for each station under this dir*/
	static String baseDestDir;
	/** base directory to save the RDAHMM model for each station. each station has a sub dir for its model files*/
	static String baseWorkDir;
	/** project name prefix. a "project" refers a run of rdahmm. there are 2 types of projects for each station,
	 * 1 for modeling and 1 for evaluation. a dir named "proNamePrefix_{stationId}" is used to save its model 
	 * files, and a dir named "proNamePrefix_{stationId}_{date}" is used to save evaluation results.*/
	static String proNamePrefix;
	/** directory containing executable binaries and shell scripts, e.g., radahmm, plot_go.sh, etc.*/
	static String binDir;
	/** default start date for model input */
	static String defaultModelStartDate;
	/** default end date for model input*/
	static String defaultModelEndDate;
	/** path to the file containing each station's start and end date for model input*/
	static String modelDatesFilePath;
	/** path to the file containing all stations' state change and missing data information */
	static String stateChangeXMLPath;
	/** path to the file containing the trace of number of state changes of the whole network */
	static String stateChangeNumTracePath;
	/** name of the file containing all station's input. this file is saved under baseDestDir */
	static String allStationInputName;
	/** GRWS context group name for the input GPS data corresponding to dataSource */
	static String inputContextGroup;
	/** GRWS context group id for the input GPS data corresponding to dataSource */
	static String inputContextId;
	/** prefix for the video service URL*/
	static String videoServiceUrlPrefix;
	/** command line pattern for rdahmm modeling process */
	static String modelCmdPattern;
	/** command line pattern for rdahmm evaluation process */
	static String evalCmdPattern;
	/** command line pattern for plotting GPS data*/
	static String plotCmdPattern;
	/** command line pattern for making the state change number vs. time plot*/
	static String plotScnCmdPattern;
	/** command line pattern for zipping model files */
	static String zipCmdPattern;
	/** path to the XML file containing all stations */
	static String stationXmlPath;
	/** time used for duplicated data **/
	static String dupDataTime;
	/** minimum number of input data for modeling **/
	static int minModelInputNum;
	/** command line pattern for de-trending the input data */ 
	static String detrendCmdPattern;
	/** library directory path for the de-trending application */
	static String detrendLibDir;
	/** command line pattern for de-noising the input data */ 
	static String denoiseCmdPattern;
	/** library directory path for the de-noising application */
	static String denoiseLibDir;
	
	String stationId;
	float latitude;
	float longitude;
	String network;
	Calendar modelStartDate;
	Calendar modelEndDate;
	Calendar lastEvalDateTime;
	Calendar thisEvalDateTime;
	String modelBaseName;
	DailyRDAHMMRunner runner;
	boolean isModelBuilt;
	
	public DailyRDAHMMStation (String stationId, float latitude, float longitude, DailyRDAHMMRunner runner) {
		this.stationId = stationId;
		this.latitude = latitude;
		this.longitude = longitude;
		modelStartDate = null;
		modelEndDate = null;
		lastEvalDateTime = null;
		thisEvalDateTime = null;
		modelBaseName = proNamePrefix + "_" + stationId;
		this.runner = runner;
		isModelBuilt = false;
	}
	
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public Calendar getModelStartDate() {
		return modelStartDate;
	}
	public void setModelStartDate(Calendar modelStartDate) {
		this.modelStartDate = modelStartDate;
	}
	public Calendar getModelEndDate() {
		return modelEndDate;
	}
	public void setModelEndDate(Calendar modelEndDate) {
		this.modelEndDate = modelEndDate;
	}
	public Calendar getLastEvalDateTime() {
		return lastEvalDateTime;
	}
	public void setLastEvalDateTime(Calendar lastEvalDateTime) {
		if (this.lastEvalDateTime == null) {
			this.lastEvalDateTime = Calendar.getInstance();
		}
		this.lastEvalDateTime.setTimeInMillis(lastEvalDateTime.getTimeInMillis());
	}
	public Calendar getThisEvalDateTime() {
		return thisEvalDateTime;
	}
	public void setThisEvalDateTime(Calendar thisEvalDateTime) {
		if (this.thisEvalDateTime == null) {
			this.thisEvalDateTime = Calendar.getInstance();
		}
		this.thisEvalDateTime.setTimeInMillis(thisEvalDateTime.getTimeInMillis());
	}
	
	public DailyRDAHMMRunner getRunner() {
		return runner;
	}

	public void setRunner(DailyRDAHMMRunner runner) {
		this.runner = runner;
	}
	
	public boolean isModelBuilt() {
		return isModelBuilt;
	}

	public void setModelBuilt(boolean isModelBuilt) {
		this.isModelBuilt = isModelBuilt;
	}
	
	/**
	 * initialize static variables 
	 * @param prop
	 */
	public static void initRdahmmProperties(Properties prop) {
		dataSource = prop.getProperty("data.source");
		noDataTreatment = prop.getProperty("preprocessing.treatment");
		repeatPeriodInDays = Integer.valueOf(prop.getProperty("repeat.period.days"));
		resAccessUrl = prop.getProperty("result.access.url");
		baseDestDir = prop.getProperty("base.dest.daily_dir");
		baseWorkDir = prop.getProperty("base.workdir");
		proNamePrefix = prop.getProperty("project_daily.name");
		binDir = prop.getProperty("bin.dir");
		defaultModelStartDate = prop.getProperty("dailyRdahmm.default.modelStartDate");
		defaultModelEndDate = prop.getProperty("dailyRdahmm.default.modelEndDate");
		modelDatesFilePath = prop.getProperty("dailyRdahmm.model.datesFile");
		stateChangeXMLPath = prop.getProperty("dailyRdahmm.stateChangeXML.path");
		stateChangeNumTracePath = prop.getProperty("dailyRdahmm.stateChangeNumTrace.path");
		allStationInputName = prop.getProperty("dailyRdahmm.allStationInput.name");
		inputContextGroup = prop.getProperty("dailyRdahmm.input.contextGroup");
		inputContextId = prop.getProperty("dailyRdahmm.input.contextId");
		videoServiceUrlPrefix = prop.getProperty("dailyRdahmm.video.serviceUrlPrefix");
		modelCmdPattern = prop.getProperty("dailyRdahmm.model.cmd.pattern");
		evalCmdPattern = prop.getProperty("dailyRdahmm.evaluate.cmd.pattern");
		plotCmdPattern = prop.getProperty("dailyRdahmm.plot.cmd.pattern");
		plotScnCmdPattern = prop.getProperty("dailyRdahmm.plotScn.cmd.pattern");
		zipCmdPattern = prop.getProperty("dailyRdahmm.zip.cmd.pattern");
		stationXmlPath = prop.getProperty("dailyRdahmm.stationXML.path");
		dupDataTime = prop.getProperty("dailyRdahmm.duplicate.time");
		minModelInputNum = Integer.valueOf(prop.getProperty("dailyRdahmm.min.model.input"));
		detrendLibDir = prop.getProperty("dailyRdahmm.detrend.libDir");
		detrendCmdPattern = prop.getProperty("dailyRdahmm.detrend.cmd.pattern");
		denoiseLibDir = prop.getProperty("dailyRdahmm.denoise.libDir");
		denoiseCmdPattern = prop.getProperty("dailyRdahmm.denoise.cmd.pattern");
	}
	
	/**
	 * build model for this station
	 * @return successful or not
	 */
	public boolean buildModel() {
		if (isModelBuilt)
			return true;
		
		if (modelStartDate == null) {
			modelStartDate = UtilSet.getDateFromString(defaultModelStartDate);
			runner.setStationXmlModelStartDate(stationId, defaultModelStartDate);
		}
		String modelDir = baseWorkDir + File.separator + modelBaseName;
		// check if a valid model already exists. "valid" means at least 200 lines of non-duplicated input
		File modelQFile = new File(modelDir + File.separator + modelBaseName + ".Q");
		if (modelQFile.exists() && modelQFile.isFile() && modelQFile.length() > 0) {
			String modelRawPath = modelDir + File.separator + modelBaseName + ".raw";
			int count = 0;
			String lastLine = null;
			try {
				BufferedReader br = new BufferedReader(new FileReader(modelRawPath));
				String line = br.readLine();
				while (line != null) {
					if (line.indexOf(dupDataTime) < 0) {
						lastLine = line;
						count++;
					}
					line = br.readLine();
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (count >= minModelInputNum) {
				String lastDateStr = getDateFromRawLine(lastLine);
				Calendar modelLastCal = UtilSet.getDateFromString(lastDateStr); 
				if (modelEndDate == null || modelEndDate.before(modelLastCal)) {
					modelEndDate = modelLastCal;
					runner.setStationXmlModelEndDate(stationId, UtilSet.getDateString(modelEndDate));
				}
				System.out.println("Valid model files found for station " + stationId);
				isModelBuilt = true;
				return true;
			} else {
				if (!UtilSet.deleteStuffInDir(new File(modelDir))) {
					System.out.println("Failed to delete invalid model files for station " + stationId);
					return false;			
				}
				String modelZipPath = baseDestDir + File.separator + modelBaseName + ".zip";
				File modelZipFile = new File(modelZipPath);
				if (modelZipFile.exists() && !modelZipFile.delete()) {
					System.out.println("Failed to delete invalid model zip file for station " + stationId);
					return false;
				}
			}
		}
		
		// if we get here, there must be no existing valid model files, and invalid ones are all deleted
		// choose initial endDate of modeling
		Calendar tmpEndCal = UtilSet.getDateFromString(defaultModelEndDate);
		Calendar oneYearAfterStartCal = Calendar.getInstance();
		oneYearAfterStartCal.setTimeInMillis(modelStartDate.getTimeInMillis());
		oneYearAfterStartCal.set(Calendar.YEAR, oneYearAfterStartCal.get(Calendar.YEAR) + 1);
		Calendar today = Calendar.getInstance();
		if (oneYearAfterStartCal.compareTo(tmpEndCal) > 0) {
			if (oneYearAfterStartCal.compareTo(today) <= 0) {
				tmpEndCal = oneYearAfterStartCal;
			} else {
				if (modelEndDate != null) {
					modelEndDate = null;
					runner.setStationXmlModelEndDate(stationId, "");
				}
				return false;
			}
		}
		
		// adjust endDate so that we have at least 200 lines of input
		File modelDirFile = new File(modelDir);
		if (!modelDirFile.exists()) {
			if (!modelDirFile.mkdirs()) {
				System.out.println("Failed to create model directory for station " + stationId);
				return false;
			}
		}	
		boolean needAdjust = true;
		String startDate = UtilSet.getDateString(modelStartDate);
		String fileName = "";
		String fileLocalPath = "";
		while (needAdjust) {
			String endDate = UtilSet.getDateString(tmpEndCal);
			String inputUrl = queryGrwsGetUrl(startDate, endDate);
			if (inputUrl.indexOf("ERROR") >= 0) {
				tmpEndCal.set(Calendar.YEAR, tmpEndCal.get(Calendar.YEAR) + 1);
				if (tmpEndCal.after(today)) {
					System.out.println("Failed to get model input for station " + stationId);
					return false;
				}
			} else {
				try {
					URL inputFileUrl = new URL(inputUrl);
					fileName = (new File(inputFileUrl.getFile())).getName();
					fileLocalPath = modelDir + File.separator + fileName;
					UtilSet.copyUrlToFile(inputFileUrl, fileLocalPath);

					int inputCount = countNonDuplicates(fileLocalPath);
					if (inputCount >= minModelInputNum) {
						needAdjust = false;
					} else {
						new File(fileLocalPath).delete();
						tmpEndCal.set(Calendar.YEAR, tmpEndCal.get(Calendar.YEAR) + 1);
						if (tmpEndCal.after(today)) {
							System.out.println("Can't get enough input for station " + stationId);
							return false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to get input from URL for station " + stationId);
				}
			}
		}
		
		// once we are here, fileLoalPath must have enough modeling input for stationId
		if (modelEndDate == null || modelEndDate.compareTo(tmpEndCal) < 0) {
			modelEndDate = tmpEndCal;
			runner.setStationXmlModelEndDate(stationId, UtilSet.getDateString(modelEndDate));
		}
		if (fileName.length() <= 0 || fileLocalPath.length() <= 0) {
			System.out.println("Failed to get model input for station " + stationId);
			return false;
		}
		
		// do de-trending on raw input
		String dtFilePath = fileLocalPath + ".detrend";
		detrendAndDenoiseData(fileLocalPath, dtFilePath);
		
		// fill missing data
		String modelRawPath = modelDir + File.separator + modelBaseName + ".raw";
		if (!fillMissingDataWithDup(dtFilePath, modelRawPath)) {
			System.out.println("Failed to fill missing input data for station " + stationId);
			return false;
		}
		
		// make input
		String modelInputPath = modelDir + File.separator + modelBaseName + ".input";
		if (!UtilSet.extractColumnsFromFile(modelRawPath, modelInputPath, 2, 4)) {
			System.out.println("Failed to extract input data for station " + stationId);
			return false;
		}
		
		// build model
		return executeModelCmd(modelInputPath);
	}
	
	/**
	 * zip the model files to an archive under baseDestDir
	 * @return
	 */
	protected boolean zipModelFiles() {
		String modelDirPath = baseWorkDir + File.separator + modelBaseName;
		String modelZipPath = baseDestDir + File.separator + modelBaseName + ".zip";
		File zipFile = new File(modelZipPath);
		if (zipFile.exists() && zipFile.isFile()) {
			return true;
		}
		String zipCmd = zipCmdPattern;
		zipCmd = zipCmd.replaceFirst("<archivePath>", modelZipPath);
		zipCmd = zipCmd.replaceFirst("<dirPath>", modelDirPath);
		String progPath = UtilSet.getProgFromCmdLine(zipCmd);
		String[] args = UtilSet.getArgsFromCmdLine(zipCmd);
		String outputPath = modelDirPath + File.separator + modelBaseName + ".zip_out";
		String errPath = modelDirPath + File.separator + modelBaseName + ".zip_err";
		UtilSet.antExecute(progPath, args, binDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Zipping model failed when executing modelling command:");
			System.out.println(zipCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * do de-trend on the raw file, and return success or not
	 * @param inputPath
	 * @param outputPath
	 * @return
	 */
	protected boolean detrendAndDenoiseData(String rawInputPath, String outputPath) {
		String nanRawPath = rawInputPath + ".nan";
		boolean res = fillMissingDataWithNan(rawInputPath, nanRawPath);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when fill missing data with NaN's!");
			return res;
		}
		String dtInputPath = nanRawPath + ".input";
		res = UtilSet.extractColumnsFromFile(nanRawPath, dtInputPath, 2, 4);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when extracting columns from the NaN-filled raw input!");
			return res;
		}
		String dtOutputPath = dtInputPath + ".detrend";
		res = executeDetrendCmd(dtInputPath, dtOutputPath);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when extracting columns from the NaN-filled raw input!");
			return res;
		}
		
		String dnOutputPath = dtOutputPath + ".denoise";
		res = executeDenoiseCmd(dtOutputPath, dnOutputPath);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when extracting columns from the NaN-filled raw input!");
			return res;
		}
		
		String dnPchOutputPath = dnOutputPath + ".pch";
		res = patchDnOutput(dtOutputPath, dnOutputPath, dnPchOutputPath);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when patching the de-noised raw input!");
			return res;
		}
		
		res = compositePostDnRawFile(nanRawPath, dnPchOutputPath, outputPath);
		if (!res) {
			System.out.println("Failed to detrend the raw input file, error when compositing the de-trended raw input!");
		}
		return res;
	}
	
	/**
	 * fill the "NaN" holes in the de-noised data 
	 * @param dnOutputPath
	 * @param patchOutputPath
	 */
	protected boolean patchDnOutput(String dtOutputPath, String dnOutputPath, String patchOutputPath) {
		BufferedReader brDt = null;
		BufferedReader brDn = null;
		PrintWriter pwPch = null;
		try {
			brDn = new BufferedReader(new FileReader(dnOutputPath));
			String nan = "NaN";
			String firstX = nan;
			String firstY = nan;
			String firstZ = nan;
			String lineDn = brDn.readLine();
			while (lineDn != null) {
				lineDn = lineDn.trim();
				StringTokenizer st = new StringTokenizer(lineDn);
				String curX = st.nextToken();
				String curY = st.nextToken();
				String curZ = st.nextToken();
				if (!curX.equals(nan) && firstX.equals(nan)) {
					firstX = curX;
				}
				if (!curY.equals(nan) && firstY.equals(nan)) {
					firstY = curY;
				}
				if (!curZ.equals(nan) && firstZ.equals(nan)) {
					firstZ = curZ;
				}
				if (!firstX.equals(nan) && !firstY.equals(nan) && !firstZ.equals(nan)) {
					break;
				}
				lineDn = brDn.readLine();
			}
			brDn.close();
			
			brDt = new BufferedReader(new FileReader(dtOutputPath));
			brDn = new BufferedReader(new FileReader(dnOutputPath));
			pwPch = new PrintWriter(new FileWriter(patchOutputPath));
			String lineDt = brDt.readLine();
			lineDn = brDn.readLine();
			String preX = firstX;
			String preY = firstY;
			String preZ = firstZ;
			while (lineDt != null) {
				if (lineDn == null) {
					System.out.println("Error when patching de-noised raw input: unequal lines of input!");
					return false;
				}
				lineDt = lineDt.trim();
				lineDn = lineDn.trim();
				// if the de-trended data is "NaN NaN NaN", just copy it to the patched results 
				if (lineDt.indexOf("NaN") >= 0) {
					pwPch.println(lineDt);
				} else {
					StringTokenizer st = new StringTokenizer(lineDn);
					String curX = st.nextToken();
					String curY = st.nextToken();
					String curZ = st.nextToken();
					
					if (curX.equals(nan)) {
						curX = preX;
					} else {
						preX = curX;
					}
					if (curY.equals(nan)) {
						curY = preY;
					} else {
						preY = curY;
					}
					if (curZ.equals(nan)) {
						curZ = preZ;
					} else {
						preZ = curZ;
					}
					
					pwPch.println(curX + ' ' + curY + ' ' + curZ);					
				}
				lineDt = brDt.readLine();
				lineDn = brDn.readLine();
			}
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (brDt != null)
					brDt.close();
				if (brDn != null)
					brDn.close();
				if (pwPch != null)
					pwPch.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * composite the de-trended and de-noised raw input file from the NaN-filled raw input 
	 * and the output from the de-noise command
	 * @param nanRawPath
	 * @param dnPatchPath
	 * @return
	 */
	protected boolean compositePostDnRawFile(String nanRawPath, String dnPatchPath, String outputPath) {
		BufferedReader brRaw = null;
		BufferedReader brDt = null;
		PrintWriter pwOut = null;
		try {
			brRaw = new BufferedReader(new FileReader(nanRawPath));
			brDt = new BufferedReader(new FileReader(dnPatchPath));
			pwOut = new PrintWriter(new FileWriter(outputPath));
			
			//lineRaw: "dond 2007-02-22T12:00:00 -2517566.0543 -4415531.3935 3841177.1618 0.0035 0.0055 0.0047"
			String lineRaw = brRaw.readLine();
			String lineDt = brDt.readLine();
			while (lineRaw != null) {
				if (lineDt == null) {
					System.out.println("Error when composite de-trended raw input: unequal lines of input!");
					return false;
				}
				lineRaw = lineRaw.trim();
				lineDt = lineDt.trim();
				if (lineRaw.length() == 0  || lineDt.length() == 0 || lineRaw.indexOf("NaN") >= 0) {
					lineRaw = brRaw.readLine();
					lineDt = brDt.readLine();
					continue;
				}
				int idx1 = lineRaw.indexOf(' ');
				int idx2 = lineRaw.indexOf(' ', idx1+1);
				int idx3 = lineRaw.indexOf(' ', idx2+1);
				int idx4 = lineRaw.indexOf(' ', idx3+1);
				int idx5 = lineRaw.indexOf(' ', idx4+1);
				String newLine = lineRaw.substring(0, idx2) + ' ' + lineDt + lineRaw.substring(idx5);
				pwOut.println(newLine);
				
				lineRaw = brRaw.readLine();
				lineDt = brDt.readLine();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (brRaw != null)
					brRaw.close();
				if (brDt != null)
					brDt.close();
				if (pwOut != null)
					pwOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * execute the de-trend command on the input data and return success or not 
	 * @param dtInputPath
	 * @param dtOutputPath
	 * @return
	 */
	protected boolean executeDetrendCmd(String dtInputPath, String dtOutputPath) {
		String detrendCmd = detrendCmdPattern;
		detrendCmd = detrendCmd.replaceFirst("<detrendDir>", binDir);
		detrendCmd = detrendCmd.replaceFirst("<detrendLibDir>", detrendLibDir);
		detrendCmd = detrendCmd.replaceFirst("<inputPath>", dtInputPath);
		detrendCmd = detrendCmd.replaceFirst("<outputPath>", dtOutputPath);
		
		String progPath = UtilSet.getProgFromCmdLine(detrendCmd);
		String[] args = UtilSet.getArgsFromCmdLine(detrendCmd);
		String outputPath = dtOutputPath + ".detrend_stdOut";
		String errPath = dtOutputPath + ".detrend_stdErr";
		UtilSet.antExecute(progPath, args, binDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Detrending failed when executing detrending command:");
			System.out.println(detrendCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
			return false;
		} else {
			return true;
		}	
	}
	
	/**
	 * execute the de-noise command on the input data and return success or not 
	 * @param dnInputPath
	 * @param dnOutputPath
	 * @return
	 */
	protected boolean executeDenoiseCmd(String dnInputPath, String dnOutputPath) {
		String denoiseCmd = denoiseCmdPattern;
		denoiseCmd = denoiseCmd.replaceFirst("<denoiseDir>", binDir);
		denoiseCmd = denoiseCmd.replaceFirst("<denoiseLibDir>", denoiseLibDir);
		denoiseCmd = denoiseCmd.replaceFirst("<inputPath>", dnInputPath);
		denoiseCmd = denoiseCmd.replaceFirst("<outputPath>", dnOutputPath);
		
		String progPath = UtilSet.getProgFromCmdLine(denoiseCmd);
		String[] args = UtilSet.getArgsFromCmdLine(denoiseCmd);
		String outputPath = dnOutputPath + ".denoise_stdOut";
		String errPath = dnOutputPath + ".denoise_stdErr";
		UtilSet.antExecute(progPath, args, binDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Denoising failed when executing denoising command:");
			System.out.println(denoiseCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * execute the rdahmm program to build the model
	 * @param modelInputPath
	 * @return successful or not
	 */
	protected boolean executeModelCmd(String modelInputPath) {
		int count = UtilSet.countLinesInFile(modelInputPath);
		if (count < 0) {
			System.out.println("Error when counting lines in model input.");
			return false;
		}
		String modelCmd = modelCmdPattern;
		modelCmd = modelCmd.replaceFirst("<rdahmmDir>", binDir).replaceFirst("<inputFile>", modelInputPath);
		modelCmd = modelCmd.replaceFirst("<dataCount>", Integer.toString(count)).replaceFirst("<dimensionCount>", "3");
		
		String progPath = UtilSet.getProgFromCmdLine(modelCmd);
		String[] args = UtilSet.getArgsFromCmdLine(modelCmd);
		String outputPath = modelInputPath + ".model_out";
		String errPath = modelInputPath + ".model_err";
		UtilSet.antExecute(progPath, args, binDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Building model failed when executing modelling command:");
			System.out.println(modelCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * do evaluation and plot GPS data
	 * @return
	 */
	public boolean evalAndPlot() {
		System.out.println("about to evaluate and plot for " + stationId + ", last evaluation time:" 
				+ UtilSet.getDateString(lastEvalDateTime));
		// if the time this evaluation is not set from outside, set it to now by default 
		if (thisEvalDateTime == null) {
			thisEvalDateTime = Calendar.getInstance();
		}
		String todayStr = UtilSet.getDateString(thisEvalDateTime);
		String projectName = modelBaseName + '_' + todayStr;
		String evalDir = baseDestDir + File.separator + projectName;
		
		// get evaluation input for the time from the day after modelEndDate to today 
		if (modelEndDate == null) {
			System.out.println("Error in evaluation: modelEndDate is null!");
			return false;
		}
		Calendar evalBeginCal = Calendar.getInstance();
		UtilSet.ndaysBeforeToday(modelEndDate, -1, evalBeginCal);
		String inputUrl = queryGrwsGetUrl(UtilSet.getDateString(evalBeginCal), todayStr);
		if (inputUrl.toUpperCase().indexOf("ERROR") >= 0) {
			// copy results from old evaluations
			if (!copyOldEvaluations(projectName)) {
				// if unsuccessful, then copy from model results, and plot the model files
				if (!copyModelToEval(projectName)) {
					System.out.println("Error in evaluation: failed to get input from GRWS, old evaluation, " +
							"or even model files for station " + stationId + "!");
					return false;
				}
				executePlotCmd(projectName);
			}
		} else {
			File evalDirFile = new File(evalDir);
			if (!evalDirFile.exists() && !evalDirFile.mkdirs()) {
				System.out.println("Error in evaluation for " + stationId + ": failed to create directory " + evalDir);
				return false;
			}
			// copy URL to local file
			String fileLocalPath = "";
			String fileName = "";
			try {
				URL inputFileUrl = new URL(inputUrl);
				fileName = (new File(inputFileUrl.getFile())).getName();
				fileLocalPath = evalDir + File.separator + fileName;
				UtilSet.copyUrlToFile(inputFileUrl, fileLocalPath);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in evaluation for staiton " + stationId + ": " + 
						"failed to get input from url " + inputUrl);
				return false;
			}
			
			// create evaluation input
			if (!makeEvalInput(projectName, fileName)) {
				return false;
			}
			
			// execute evaluation command
			if (!executeEvalCmd(projectName)) {
				return false;
			}
			
			// plot evaluation results
			executePlotCmd(projectName);
		}
		
		// delete the directory of last evaluation output
		if (lastEvalDateTime != null) {
			String lastEvalPath = baseDestDir + File.separator + modelBaseName + '_' + UtilSet.getDateString(lastEvalDateTime);
			if (!UtilSet.deleteDirectory(new File(lastEvalPath))) {
				System.out.println("Evaluation scceeded but failed to delete " + lastEvalPath);
			}
			lastEvalDateTime.setTimeInMillis(thisEvalDateTime.getTimeInMillis());
		} else {
			lastEvalDateTime = Calendar.getInstance();
			lastEvalDateTime.setTimeInMillis(thisEvalDateTime.getTimeInMillis());
		}
		return true;
	}
	
	/**
	 * execute the rdahmm program to do evaluation
	 * @param projectName
	 * @return successful or not
	 */
	protected boolean executeEvalCmd(String projectName) {
		String proDir = baseDestDir + File.separator + projectName;
		String evalInputPath = proDir + File.separator + projectName + ".all.input";
		int count = UtilSet.countLinesInFile(evalInputPath);
		if (count < 0) {
			System.out.println("Error when counting lines in evaluation input.");
			return false;
		}
		String evalCmd = evalCmdPattern;
		evalCmd = evalCmd.replaceFirst("<rdahmmDir>", binDir).replaceFirst("<proBaseName>", projectName);
		evalCmd = evalCmd.replaceFirst("<dataCount>", Integer.toString(count)).replaceFirst("<dimensionCount>", "3");
		evalCmd = evalCmd.replaceAll("<modelBaseName>", modelBaseName);
		
		String progPath = UtilSet.getProgFromCmdLine(evalCmd);
		String[] args = UtilSet.getArgsFromCmdLine(evalCmd);
		String outputPath = evalInputPath + ".eval_out";
		String errPath = evalInputPath + ".eval_err";
		UtilSet.antExecute(progPath, args, proDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Evaluation failed when executing evaluating command:");
			System.out.println(evalCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
			return false;
		} else {
			UtilSet.renameFile(proDir + File.separator + projectName + ".Q",
								proDir + File.separator + projectName + ".all.Q");
			return true;
		}
	}
	
	/**
	 * make evaluation input, and get ready for executing the evaluation command
	 * @param projectName
	 * @param grwsFileName
	 * @return
	 */
	protected boolean makeEvalInput(String projectName, String grwsFileName) {
		try {
			// first, de-trend the GRWS raw file
			String proDir = baseDestDir + File.separator + projectName;
			String grwsFilePath = proDir + File.separator + grwsFileName;
			String grwsDtFilePath = grwsFilePath + ".detrend";
			if (!detrendAndDenoiseData(grwsFilePath, grwsDtFilePath)) {
				System.out.println("Failed to create evaluation input for station " + stationId
									+ "! Error when de-trending the evaluation raw input.");
				return false;
			}
			
			// cat the GRWS file to the .raw file of the model file
			String modelDir = baseWorkDir + File.separator + modelBaseName;
			String tmpFilePath = proDir + File.separator + projectName + ".all.raw.tmp";
			String modelRawPath = modelDir + File.separator + modelBaseName + ".raw";
			UtilSet.catTwoFiles(modelRawPath, grwsDtFilePath, tmpFilePath);
			
			// fill missing data to the ".all.raw.tmp" file
			String allRawPath = proDir + File.separator + projectName + ".all.raw";
			if (!fillMissingDataWithDup(tmpFilePath, allRawPath)) {
				System.out.println("Failed to create evaluation input for station " + stationId);
				return false;
			}
			
			// extract the middle lines containing displacement data
			String allInputPath = proDir + File.separator + projectName + ".all.input";
			if (!UtilSet.extractColumnsFromFile(allRawPath, allInputPath, 2, 4)) {
				System.out.println("Failed to create evaluation input for station " + stationId);
				return false;
			}
			
			// copy model files to the project directory
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".A"), 
					new File(proDir + File.separator + modelBaseName + ".A"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".B"), 
					new File(proDir + File.separator + modelBaseName + ".B"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".L"), 
					new File(proDir + File.separator + modelBaseName + ".L"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".maxval"), 
					new File(proDir + File.separator + modelBaseName + ".maxval"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".minval"), 
					new File(proDir + File.separator + modelBaseName + ".minval"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".pi"), 
					new File(proDir + File.separator + modelBaseName + ".pi"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".range"), 
					new File(proDir + File.separator + modelBaseName + ".range"));
		} catch (Exception e) {
			System.out.println("Failed to carete evaluation input for staiton " + stationId);
			return false;
		}
		return true;
	}
	
	/**
	 * execute the plot command line for projectName
	 * @param projectName
	 */
	protected void executePlotCmd(String projectName) {
		String proDir = baseDestDir + File.separator + projectName;
		String pathAndProjectName = proDir + File.separator + projectName;
		String plotCmd = plotCmdPattern;
		plotCmd = plotCmd.replaceFirst("<plotDir>", binDir);
		plotCmd = plotCmd.replaceAll("<proBaseName>", pathAndProjectName);
		String progPath = UtilSet.getProgFromCmdLine(plotCmd);
		String[] args = UtilSet.getArgsFromCmdLine(plotCmd);
		String outputPath = proDir + File.separator + projectName + ".plot_out";
		String errPath = proDir + File.separator + projectName + ".plot_err";
		UtilSet.antExecute(progPath, args, binDir, null, null, outputPath, errPath);
		String stdOutStr = UtilSet.readFileContentAsString(new File(outputPath));
		String stdErrStr = UtilSet.readFileContentAsString(new File(errPath));
		if (stdOutStr.toLowerCase().indexOf("error") >= 0 || stdErrStr.toLowerCase().indexOf("error") >= 0) {
			System.out.println("Plotting failed when executing command:");
			System.out.println(plotCmd);
			System.out.println("Standard Output: " + stdOutStr);
			System.out.println("Standard Error: " + stdErrStr);
		}
	}
	
	/**
	 * copy model files to evaluation directory. the model input and state file will be used for plot
	 * @param projectName
	 * @return
	 */
	protected boolean copyModelToEval(String projectName) {
		String proDir = baseDestDir + File.separator + projectName;
		String modelDir = baseWorkDir + File.separator + modelBaseName;
		File proDirFile = new File(proDir);
		if (!proDirFile.exists() && !proDirFile.mkdirs()) {
			System.out.println("Failed to copy old evaluations for " + projectName + ": can't create dest dir!");
			return false;
		}
		try {
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".input"), 
					new File(proDir + File.separator + projectName + ".all.input"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".Q"), 
					new File(proDir + File.separator + projectName + ".all.Q"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".raw"), 
					new File(proDir + File.separator + projectName + ".all.raw"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".A"), 
					new File(proDir + File.separator + modelBaseName + ".A"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".B"), 
					new File(proDir + File.separator + modelBaseName + ".B"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".L"), 
					new File(proDir + File.separator + modelBaseName + ".L"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".maxval"), 
					new File(proDir + File.separator + modelBaseName + ".maxval"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".minval"), 
					new File(proDir + File.separator + modelBaseName + ".minval"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".pi"), 
					new File(proDir + File.separator + modelBaseName + ".pi"));
			UtilSet.copyFileToFile(new File(modelDir + File.separator + modelBaseName + ".range"), 
					new File(proDir + File.separator + modelBaseName + ".range"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to copy old evaluations for " + projectName + ": error in file copies!");
			return false;
		}

		return true;
	}
	
	
	/**
	 * copy evaluation results from the latest successful evaluation
	 * @param projectName
	 * @return
	 */
	protected boolean copyOldEvaluations(String projectName) {
		Vector<String> dirNames = new Vector<String>(3);
		String latestProName = "";
		Calendar latestProCal = Calendar.getInstance();
		latestProCal.set(Calendar.YEAR, 0);
		Calendar tmpCal = Calendar.getInstance();
		
		File destDirFile = new File(baseDestDir);
		File[] subDirFiles = destDirFile.listFiles();
		if (subDirFiles == null) {
			return false;
		}
		
		for (int i=0; i<subDirFiles.length; i++) {
			if (subDirFiles[i].isFile()) {
				continue;
			}
			String dirName = subDirFiles[i].getName(); 
			if (dirName.indexOf(stationId + '_') >= 0) {
				dirNames.add(dirName);
				String tmpDate = dirName.substring(dirName.lastIndexOf('_') + 1);
				UtilSet.setDateByString(tmpCal, tmpDate);
				if (latestProCal.before(tmpCal)) {
					latestProCal.setTimeInMillis(tmpCal.getTimeInMillis());
					latestProName = dirName;
				}
			}
		}
		
		if (latestProName.length() <= 0) {
			return false;
		}
		
		String proDir = baseDestDir + File.separator + projectName;
		String latestProDir = baseDestDir + File.separator + latestProName;
		File proDirFile = new File(proDir);
		if (!proDirFile.exists() && !proDirFile.mkdirs()) {
			System.out.println("Failed to copy old evaluations for " + projectName + ": can't create dest dir!");
			return false;
		}
		try {
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.input"), 
					new File(proDir + File.separator + projectName + ".all.input"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.input.X.png"), 
					new File(proDir + File.separator + projectName + ".all.input.X.png"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.input.Y.png"), 
					new File(proDir + File.separator + projectName + ".all.input.Y.png"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.input.Z.png"), 
					new File(proDir + File.separator + projectName + ".all.input.Z.png"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.Q"), 
					new File(proDir + File.separator + projectName + ".all.Q"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + latestProName + ".all.raw"), 
					new File(proDir + File.separator + projectName + ".all.raw"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".A"), 
					new File(proDir + File.separator + modelBaseName + ".A"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".B"), 
					new File(proDir + File.separator + modelBaseName + ".B"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".L"), 
					new File(proDir + File.separator + modelBaseName + ".L"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".maxval"), 
					new File(proDir + File.separator + modelBaseName + ".maxval"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".minval"), 
					new File(proDir + File.separator + modelBaseName + ".minval"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".pi"), 
					new File(proDir + File.separator + modelBaseName + ".pi"));
			UtilSet.copyFileToFile(new File(latestProDir + File.separator + modelBaseName + ".range"), 
					new File(proDir + File.separator + modelBaseName + ".range"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to copy old evaluations for " + projectName + ": error in file copies!");
			return false;
		}
		
		// delete old directories
		for (int i=0; i<dirNames.size(); i++) {
			UtilSet.deleteDirectory(new File(baseDestDir + File.separator + dirNames.get(i)));
		}		
		return true;
	}
	
	/**
	 * fill missing GPS data contained in srcPath with duplicated data, save filled data in destPath
	 * @param srcPath
	 * @param destPath
	 */
	protected boolean fillMissingDataWithDup(String srcPath, String destPath) {
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
				String lineSrcPre = lineSrc;
				String restLineSrcPre = lineSrcPre.substring(lineSrcPre.indexOf(' ', 5));
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar calTmp = Calendar.getInstance();

				lineSrc = brSrc.readLine();
				while (lineSrc != null) {
					prTmpDest.println(lineSrcPre);
					UtilSet.setDateByString(cal1, getDateFromRawLine(lineSrcPre));
					UtilSet.setDateByString(cal2, getDateFromRawLine(lineSrc));

					// if there is a gap between cal1+1day and cal2, fill it with the last line
					UtilSet.ndaysBeforeToday(cal1, -1, calTmp);
					while (calTmp.compareTo(cal2) < 0) {
						// we use the time 22:22:22 to denote a duplicated line that used to be missing data
						prTmpDest.println(stationId + ' ' + UtilSet.getDateString(calTmp) + 'T' +
											dupDataTime + restLineSrcPre);
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
	 * fill missing GPS data contained in srcPath with NaN's, save filled data in destPath
	 * @param srcPath
	 * @param destPath
	 */
	protected boolean fillMissingDataWithNan(String srcPath, String destPath) {
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
				String lineSrcPre = lineSrc;
				int idx1 = lineSrcPre.lastIndexOf(' ');
				int idx2 = lineSrcPre.lastIndexOf(' ', idx1-1);
				int idx3 = lineSrcPre.lastIndexOf(' ', idx2-1);
				String restLineSrcPre = lineSrcPre.substring(idx3+1);
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar calTmp = Calendar.getInstance();

				lineSrc = brSrc.readLine();
				while (lineSrc != null) {
					prTmpDest.println(lineSrcPre);
					UtilSet.setDateByString(cal1, getDateFromRawLine(lineSrcPre));
					UtilSet.setDateByString(cal2, getDateFromRawLine(lineSrc));

					// if there is a gap between cal1+1day and cal2, fill it with the last line
					UtilSet.ndaysBeforeToday(cal1, -1, calTmp);
					while (calTmp.compareTo(cal2) < 0) {
						// we use the time 22:22:22 to denote a duplicated line that used to be missing data
						prTmpDest.println(stationId + ' ' + UtilSet.getDateString(calTmp) + 'T' +
											dupDataTime + " NaN NaN NaN " + restLineSrcPre);
						UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
					}

					lineSrcPre = lineSrc;
					idx1 = lineSrcPre.lastIndexOf(' ');
					idx2 = lineSrcPre.lastIndexOf(' ', idx1-1);
					idx3 = lineSrcPre.lastIndexOf(' ', idx2-1);
					restLineSrcPre = lineSrcPre.substring(idx3+1);
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
	 * query GRWS service to get url for this station's GPS data between beginDate and endDate 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	protected String queryGrwsGetUrl(String beginDate, String endDate) {
		String resource = "procCoords";
		String contextGroup = inputContextGroup;
		String minMaxLatLon = "";
		String contextId = inputContextId;
		
		GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
		gsq.setFromServlet(stationId, beginDate, endDate, resource, contextGroup, contextId, minMaxLatLon, true);
		String dataUrl = gsq.getResource();
		System.out.println("GRWS data url for station " + stationId + " : " + dataUrl);
		return dataUrl;
	}
	
	/**
	 * get the evaluation project name in the form of "daily_project_dshs_2010-02-01" 
	 * @return
	 */
	public String getProjectName() {
		if (thisEvalDateTime == null) {
			thisEvalDateTime = Calendar.getInstance();
		}
		return modelBaseName + '_' + UtilSet.getDateString(thisEvalDateTime);
	}
	
	/**
	 * make the flat input file for this station, which will be added to the big flat input file
	 * containing all staion's input
	 * @return
	 */
	public boolean makeFlatInputFile() {
		String projectName = getProjectName();
		String proDir = baseDestDir + File.separator + projectName;
		String rawPath = proDir + File.separator + projectName + ".all.raw";
		String inputPath = proDir + File.separator + projectName + ".all.input";
		try {
			Calendar calWholeStart = UtilSet.getDateFromString(defaultModelStartDate);
			Calendar calWholeEnd = thisEvalDateTime;
			
			String flatInputPath = proDir + File.separator + projectName + ".all.input.flat";
			PrintWriter pwFlat = new PrintWriter(new FileWriter(flatInputPath));
			BufferedReader brInput = new BufferedReader(new FileReader(inputPath));
			BufferedReader brRaw = new BufferedReader(new FileReader(rawPath));
			String line = brInput.readLine();
			String rawLine = brRaw.readLine();
			
			// if there is no input at all, just fill all time with NaN and return
			Calendar calTmp = Calendar.getInstance();
			if (line == null) {
				calTmp.setTimeInMillis(calWholeStart.getTimeInMillis());
				while (calTmp.compareTo(calWholeEnd) <= 0) {
					pwFlat.println("NaN NaN NaN");
					UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
				}
				brInput.close();
				brRaw.close();
				pwFlat.flush();
				pwFlat.close();
				return true;
			}
			
			// fill the time before calRawStart with NaN
			Calendar calRawStart = UtilSet.getDateFromString(getDateFromRawLine(rawLine));
			calTmp.setTimeInMillis(calWholeStart.getTimeInMillis());
			while (calTmp.before(calRawStart)) {
				pwFlat.println("NaN NaN NaN");
				UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
			}
			
			// fill the time between calRawStart and calRawEnd with lines from the ".all.input" file
			String lastRawLine = "";
			while (line != null) {
				if (rawLine.indexOf(dupDataTime) < 0) {
					pwFlat.println(line);
				} else {
					pwFlat.println("NaN NaN NaN");
				}
				lastRawLine = rawLine;
				line = brInput.readLine();
				rawLine = brRaw.readLine();
			}
			brInput.close();
			brRaw.close();
			
			// fill the time after calRawEnd with NaN
			Calendar calRawEnd = UtilSet.getDateFromString(getDateFromRawLine(lastRawLine));
			UtilSet.ndaysBeforeToday(calRawEnd, -1, calTmp);
			calTmp.set(Calendar.HOUR_OF_DAY, 0);
			while (calTmp.compareTo(calWholeEnd) <= 0) {
				pwFlat.println("NaN NaN NaN");
				UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
			}
			pwFlat.flush();
			pwFlat.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to create flat input file for station " + stationId);
			return false;
		}
		return true;
	}
	
	/**
	 * count the number of non-duplicated data in an input file
	 * @param path
	 * @return
	 */
	public static int countNonDuplicates(String path) {
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				if (line.indexOf(dupDataTime) < 0) {
					count++;
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**  get the date string from the a line of the raw data file received from GRWS query */
	public static String getDateFromRawLine(String rawLine) {
		// a raw line is like "dond 2007-02-22T12:00:00 -2517566.0543 ..."
		int idx = rawLine.indexOf(' ');
		if (idx < 0)
			return null;
		
		int idx2 = rawLine.indexOf('T', idx);
		if (idx2 < 0)
			return null;
		
		return rawLine.substring(idx+1, idx2);
	}
	
	/**  get the date-time string from the a line of the raw data file received from GRWS query */
	public static String getDateTimeFromRawLine(String rawLine) {
		// a raw line is like "dond 2007-02-22T12:00:00 -2517566.0543 ..."
		int idx = rawLine.indexOf(' ');
		if (idx < 0)
			return null;
		
		int idx2 = rawLine.indexOf(' ', idx+1);
		if (idx2 < 0)
			return null;
		
		return rawLine.substring(idx+1, idx2);
	}

	public String getModelBaseName() {
		return modelBaseName;
	}

	public void setModelBaseName(String modelBaseName) {
		this.modelBaseName = modelBaseName;
	}
}

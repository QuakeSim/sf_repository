package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

public class DailyRDAHMMService extends RDAHMMService {
	
	String stationID;
	String modelBaseName;
	String modelWorkDir;
	Object runningLock = null;
	String beginDate;
	String endDate;
	static String stateChangeNumTxtPath = null;
	static String outputXmlPath = null; 
	static int DUP_LINE_TIME = 22;
	static String proNamePrefix = null;
	static String allStationInputName = null;
	static String propFileName = "dailyRdahmmSopacFill.properties";
	static String inputContextGroup = null;
	static String inputContextId = null;
	static String videoServiceUrlPrefix = null;
	static String dataSource = null;
	static String preProcessingTreat = null;
	
	public DailyRDAHMMService(boolean useClassLoader, String stationID) 
		throws Exception{
		super(useClassLoader);
		this.stationID = stationID;
		
		if (useClassLoader) {
			System.out.println("Using classloader");
			// This is useful for command line clients but does not work inside Tomcat.
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			properties = new Properties();
			properties.load(loader.getResourceAsStream(propFileName));
		} else {
			// Extract the Servlet Context
			System.out.println("Using Servlet Context");
			MessageContext msgC = MessageContext.getCurrentContext();
			ServletContext context = ((HttpServlet) msgC
					.getProperty(HTTPConstants.MC_HTTP_SERVLET))
					.getServletContext();

			String propertyFile = context.getRealPath("/") + File.separator + "WEB-INF" +
									File.separator + "classes" + File.separator + propFileName;
			System.out.println("Prop file location " + propertyFile);

			properties = new Properties();
			properties.load(new FileInputStream(propertyFile));
		}
		
		if (dataSource == null)
			dataSource = properties.getProperty("data.source");
		if (preProcessingTreat == null)
			preProcessingTreat = properties.getProperty("preprocessing.treatment");

		serverUrl = properties.getProperty("daily.rdahmm.service.url");
		baseWorkDir = properties.getProperty("base.workdir");
		binPath = properties.getProperty("bin.path");
		buildFilePath = properties.getProperty("build_daily.file.path");
		if (proNamePrefix == null)
			proNamePrefix = properties.getProperty("project_daily.name") + "_";
		if (allStationInputName == null)
			allStationInputName = properties.getProperty("dailyRdahmm.allStationInput.name");
		if (stateChangeNumTxtPath == null)
			stateChangeNumTxtPath = properties.getProperty("dailyRdahmm.stateChangeNumTrace.path");
		if (outputXmlPath == null)
			outputXmlPath = properties.getProperty("dailyRdahmm.output.path");
		if (inputContextGroup == null)
			inputContextGroup = properties.getProperty("dailyRdahmm.input.contextGroup");
		if (inputContextId == null)
			inputContextId = properties.getProperty("dailyRdahmm.input.contextId");
		if (videoServiceUrlPrefix == null)
			videoServiceUrlPrefix = properties.getProperty("dailyRdahmm.video.serviceUrlPrefix");
		modelBaseName = proNamePrefix + stationID;
		baseDestDir = properties.getProperty("base.dest.daily_dir");
		projectName = modelBaseName + "_" + UtilSet.getDateString(Calendar.getInstance());
		modelWorkDir = baseWorkDir + File.separator + modelBaseName;
		outputDestDir = baseDestDir + File.separator + projectName;
		beginDate = DailyRDAHMMThread.evalStartDate;
		endDate = UtilSet.getDateString(Calendar.getInstance());
	}
	
	public DailyRDAHMMService() 
		throws Exception{
		this(false, null);
	}
	
	/**
	 * This version is used to to hold response until RDAHMM finished executing.
	 * This is the full API.
	 */
	public String[] runBlockingRDAHMM(String inputFileUrlString,
			String baseWorkDir, String outputDestDir, String projectName,
			String binPath, int numModelStates, int randomSeed,
			String outputType, double annealStep, String buildFilePath,
			String antTarget) throws Exception {

		UtilSet.exec("mkdir " + baseDestDir);
		// Set up the work directory
		String workDir = baseWorkDir + File.separator + projectName;
		UtilSet.exec("mkdir " + workDir);
		
		
		System.out.println("inputFileUrlString in runBlockingRDAHMM: "	+ inputFileUrlString);
		if (inputFileUrlString.indexOf("ERROR") >= 0) {
			System.out.println("Failed to get GRWS input for station " + stationID);
			fakeEvaluation(UtilSet.getDateFromString(beginDate), UtilSet.getDateFromString(endDate));
			return getTheReturnFiles();
		}

		// Copy the input file to the working directory, if necessary.
		String rdahmmInputFile = makeRdahmmInputFile(inputFileUrlString,
				projectName, workDir);

		// Get the dimensions and number of observations.
		int ndim = getFileDimension(rdahmmInputFile);
		int nobsv = getLineCount(rdahmmInputFile);

		if (runningLock == null)
			runningLock = new Object();
		synchronized (runningLock) {
			String[] args = setUpArgArray(rdahmmInputFile, workDir,
					modelWorkDir, outputDestDir, projectName, modelBaseName,
					binPath, nobsv, ndim, numModelStates, randomSeed,
					outputType, annealStep, buildFilePath, antTarget);

			// Methods inherited from parent
			setArgs(args);
			run();
		}
		return getTheReturnFiles();
	}

	/** 
	 * do fake evaluation on the station: only called when there is no evaluation input
	  */
	protected void fakeEvaluation(Calendar startDate, Calendar endDate) {
		System.out.println("Fake Evaluation for project " + projectName);
		String workDir = baseWorkDir + File.separator + projectName;
		
		// no input to evaluate, just plot on the model data
		String res, fileset;
		res = UtilSet.exec("mkdir " + outputDestDir);
		fileset = modelWorkDir + File.separator + "*";
		res = UtilSet.exec("cp " + fileset + " " + outputDestDir + File.separator);
		res = UtilSet.exec("cp " + fileset + " " + workDir + File.separator);

		// plot images with model files
		String modelPath = outputDestDir + File.separator + modelBaseName;
		String proPath = outputDestDir + File.separator + projectName;
		// the model .input file is just the all.input file
		res = UtilSet.exec("cp " + modelPath + ".input " + proPath + ".all.input");

		String plotSh = binPath + File.separator + "plot_go.sh";
		System.out.println("about to executing " + plotSh + " " + proPath
				+ ".all.input " + modelPath + ".Q " + modelPath	+ ".raw ...");
		res = UtilSet.exec(plotSh + " " + proPath + ".all.input "
				+ modelPath + ".Q " + modelPath + ".raw ",	new File(binPath));
		System.out.println("result : " + res);
		res = UtilSet.exec("touch " + proPath + ".input");
		res = UtilSet.exec("touch " + proPath + ".Q");
		res = UtilSet.exec("touch " + proPath + ".raw");

		// create empty files in workDir
		String proWorkPath = workDir + File.separator + projectName;
		res = UtilSet.exec("touch " + proWorkPath + ".input");
		res = UtilSet.exec("touch " + proWorkPath + ".Q");
		res = UtilSet.exec("touch " + proWorkPath + ".raw");
		
		// fill the flat input file with "NaN NaN NaN" in every line
		try {
			PrintWriter pwFlat = new PrintWriter(new FileWriter(proPath + ".input.flat"));
			Calendar calTmp = Calendar.getInstance();
			calTmp.setTimeInMillis(startDate.getTimeInMillis());
			while (calTmp.compareTo(endDate) <= 0) {
				pwFlat.println("NaN NaN NaN");
				UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
			}
			pwFlat.flush();
			pwFlat.close();
			res = UtilSet.exec("cp " + proPath + ".input.flat " + proWorkPath + ".input.flat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
    /**
	 * generate the rdahmm input file from the url string of the input file
	 * 
	 * @param inputFileUrlString
	 * @param projectName
	 * @param workDir
	 * @return
	 * @throws Exception
	 */
	protected String makeRdahmmInputFile(String inputFileUrlString,
			String projectName, String workDir) throws Exception {
		
		String[] inputFileUrlArray = convertInputUrlStringToArray(inputFileUrlString);
		
		String[] localFileArray = downloadInputFile(inputFileUrlArray,workDir);
		
		String[] localFileArrayFiltered = filterResults(localFileArray, 2, 3);
		
		String rdahmmRawInputFile = workDir + File.separator + projectName + ".raw";
		mergeInputFiles(localFileArray,rdahmmRawInputFile);
		
		String rdahmmInputFile = workDir + File.separator + projectName + ".input";
		mergeInputFiles(localFileArrayFiltered,rdahmmInputFile);
		
		fillMissingData(rdahmmInputFile, rdahmmRawInputFile, beginDate, endDate);
		
		return rdahmmInputFile;
	}
	
	/**
	 * fill the missing data with the data on a date closet to the data-missing dates
	 * @param inputPath
	 * @param rawPath
	 * @param beginDate
	 * @param endDate
	 */
	protected void fillMissingData(String inputPath, String rawPath, String beginDate, String endDate) {
		String inputTmpPath = inputPath + ".tmp";
		String rawTmpPath =  rawPath + ".tmp";
		String inputFlatPath = inputPath + ".flat";	// this file is used for creating the big flat file
		Calendar calBegin = UtilSet.getDateFromString(beginDate);
		Calendar calEnd = UtilSet.getDateFromString(endDate);
		
		try {
			// a raw line is like 
			//"dond 2007-02-22T12:00:00 -2517566.0543 -4415531.3935 3841177.1618 0.0035 0.0055 0.0047"
			//while an input line contains only the three columns in the middle
			BufferedReader brInput = new BufferedReader(new FileReader(inputPath));
			BufferedReader brRaw = new BufferedReader(new FileReader(rawPath));
			
			PrintWriter prInputTmp = new PrintWriter(new FileWriter(inputTmpPath));
			PrintWriter prRawTmp = new PrintWriter(new FileWriter(rawTmpPath));
			PrintWriter prFlat = new PrintWriter(new FileWriter(inputFlatPath));
			
			String lineInput = brInput.readLine();
			String lineInputPre = lineInput;
			String lineRaw = brRaw.readLine();
			String lineRawPre = lineRaw;
			String restLineRawPre = lineRawPre.substring(lineRawPre.indexOf(' ', 5));
			
			Calendar cal1 = Calendar.getInstance();
			UtilSet.ndaysBeforeToday(calBegin, 1, cal1);
			Calendar cal2 = Calendar.getInstance();	
			Calendar calTmp = Calendar.getInstance();
			calTmp.setTimeInMillis(calBegin.getTimeInMillis());
			Calendar calLine = Calendar.getInstance();
			
			while (lineRaw != null) {
				UtilSet.setDateByString(cal2, DailyRDAHMMThread.getDateFromRawLine(lineRaw));
			
				// if there is a gap between cal1+1day and cal2, fill it with the last line
				UtilSet.ndaysBeforeToday(cal1, -1, calTmp);
				calLine.setTimeInMillis(calTmp.getTimeInMillis());
				calLine.set(Calendar.HOUR_OF_DAY, DUP_LINE_TIME);
				calLine.set(Calendar.MINUTE, DUP_LINE_TIME);
				calLine.set(Calendar.SECOND, DUP_LINE_TIME);
				cal1.setTimeInMillis(calTmp.getTimeInMillis());
				while (calTmp.compareTo(cal2) < 0) {
					if (cal1.getTimeInMillis() != calBegin.getTimeInMillis()){
						// we use the time 22:22:22 to denote a duplicated line that used to be missing data
						prRawTmp.println(stationID + ' ' + UtilSet.getDateTimeString(calLine) + restLineRawPre);
						prInputTmp.println(lineInputPre);		
					}
					prFlat.println("NaN NaN NaN");
					UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
					UtilSet.ndaysBeforeToday(calLine, -1, calLine);
				}
				prRawTmp.println(lineRaw);
				prInputTmp.println(lineInput);
				prFlat.println(lineInput);
				
				cal1.setTimeInMillis(cal2.getTimeInMillis());
				lineRawPre = lineRaw;
				restLineRawPre = lineRawPre.substring(lineRawPre.indexOf(' ', lineRawPre.indexOf(' ')+1));
				lineInputPre = lineInput;
				lineRaw = brRaw.readLine();
				lineInput = brInput.readLine();
			}
			brInput.close();
			brRaw.close();
			
			// if there is a gap between cal2+1day and calEnd, fill it with the last line
			UtilSet.ndaysBeforeToday(cal2, -1, calTmp);
			while (calTmp.compareTo(calEnd) <= 0) {
				prFlat.println("NaN NaN NaN");
				UtilSet.ndaysBeforeToday(calTmp, -1, calTmp);
			}
			prRawTmp.flush();
			prRawTmp.close();
			prInputTmp.flush();
			prInputTmp.close();
			prFlat.flush();
			prFlat.close();
			
			UtilSet.renameFile(inputTmpPath, inputPath);
			UtilSet.renameFile(rawTmpPath, rawPath);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * A dumb little method for constructing the URL outputs. This
     * will not get called if the execute()/run() method fails.
     */ 
    protected String[] getTheReturnFiles() {
		  
		  String[] extensions={".input",".range",".Q",".pi",".A",
									  ".minval",".maxval",".L",".B",
									  ".Q",".stdout",
									  ".all.input.X.png",".all.input.Y.png",".all.input.Z.png"};
		  
		  String[] returnFiles=new String[extensions.length];
		  for(int i=0;i<extensions.length;i++) {
			  // for daily rdahmm detection, the A, B, pi, minval, maxval, range files are the model files
			  if (i==1 || (i>=3 && i<=8))
				  returnFiles[i] = serverUrl + "/" + projectName + "/" + modelBaseName + extensions[i];
			  else
				  returnFiles[i] = serverUrl + "/" + projectName + "/" + projectName + extensions[i];
		  }
		  
		  return returnFiles;
    }
    
    /**
     * this set up the array of args for ant project
     * @param inputFileUrlString
     * @param workDir
     * @param modelWorkDir
     * @param outputDestDir
     * @param projectName
     * @param modelBaseName
     * @param binPath
     * @param nobsv
     * @param ndim
     * @param numModelStates
     * @param randomSeed
     * @param outputType
     * @param annealStep
     * @param buildFilePath
     * @param antTarget
     * @return
     * @throws Exception
     */
    private String[] setUpArgArray(String inputFileUrlString, String workDir, String modelWorkDir,
			String outputDestDir, String projectName, String modelBaseName, String binPath,
			int nobsv, int ndim, int numModelStates, int randomSeed,
			String outputType, double annealStep, String buildFilePath,
			String antTarget) throws Exception {

		String[] args = new String[18];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DmodelWorkDir.prop=" + modelWorkDir;
		args[2] = "-DprojectName.prop=" + projectName;
		args[3] = "-Dbindir.prop=" + binPath;
		args[4] = "-DRDAHMMBaseName.prop=" + projectName;
		args[5] = "-DmodelRDAHMMBaseName.prop=" + modelBaseName;
		args[6] = "-Dnobsv.prop=" + nobsv;
		args[7] = "-Dndim.prop=" + ndim;
		args[8] = "-Dnstates.prop=" + numModelStates;
		args[9] = "-Dranseed.prop=" + randomSeed;
		args[10] = "-Doutput_type.prop=" + outputType;
		args[11] = "-DannealStep.prop=" + annealStep;
		args[12] = "-Dbetamin.prop=" + betamin;
		args[13] = "-Dntries.prop=" + ntries;
		args[14] = "-DoutputDestDir.prop=" + outputDestDir;
		args[15] = "-buildfile";
		args[16] = buildFilePath;
		args[17] = antTarget;

		return args;
	}
    
    private void makeWorkDir(String workDir, String bf_loc) throws Exception {
		System.out.println("Working Directory is " + workDir);

		String[] args0 = new String[4];
		args0[0] = "-DworkDir.prop=" + workDir;
		args0[1] = "-buildfile";
		args0[2] = bf_loc;
		args0[3] = "MakeWorkDir";

		setArgs(args0);
		run();
	}  
    
    /**
     * This is a helper method to convert token-separated
     * inputFileUrlStrings into arrays.
     */
    private String[] convertInputUrlStringToArray(String inputFileUrlString){
		  inputFileUrlString.trim();
		  
		  String[] returnArray;
	
		  StringTokenizer st=new StringTokenizer(inputFileUrlString);
		  int arrayDim=st.countTokens();
		  if(arrayDim<2) {
				returnArray=new String[1];
				returnArray[0]=inputFileUrlString.trim();
		  }
		  else {
				int i=0;
				returnArray=new String[arrayDim];
				while(st.hasMoreTokens()) {
					 returnArray[i]=st.nextToken();
					 i++;
				}
		  }
		  return returnArray;
    }
    
    /**
     * Note that inputFileUrlString can be either single values or
     * else have multiple, space separated values.  It also
     * returns a space-separated set of values.
     * All files are written to the same directory.
     */
    private String[] downloadInputFile(String[] inputFileUrlString,
				       String inputFileDestDir)
		  throws Exception {
		  
		  //Convert to a URL. This will throw an exception if
		  //malformed.
		  
		  String[] fileLocalFullName=new String[inputFileUrlString.length];
		  for(int i=0;i<inputFileUrlString.length;i++) {
				
				URL inputFileUrl=new URL(inputFileUrlString[i]);
				
				String protocol=inputFileUrl.getProtocol();
				System.out.println("Protocol: "+protocol);
				String fileSimpleName = (new File(inputFileUrl.getFile())).getName();
				System.out.println(fileSimpleName);
				
				fileLocalFullName[i]=inputFileDestDir+File.separator
					 +fileSimpleName;
				
				if(protocol.equals(FILE_PROTOCOL)) {
					 String filePath=inputFileUrl.getFile();
					 fileSimpleName=inputFileUrl.getFile();
					 
					 System.out.println("File path is "+filePath);
					 File filePathObject=new File(filePath);
					 File destFileObject=new File(fileLocalFullName[i]);
					 
					 //See if the inputFileUrl and the dest file are the same.
					 if(filePathObject.getCanonicalPath().
						 equals(destFileObject.getCanonicalPath())) {
						  System.out.println("Files are the same.  We're done.");
						  return fileLocalFullName;
					 }
					 
					 //Otherwise, we will have to copy it.
					 copyFileToFile(filePathObject, destFileObject);
					 return fileLocalFullName;
				}	else if(protocol.equals(HTTP_PROTOCOL)) {
					 copyUrlToFile(inputFileUrl,fileLocalFullName[i]);
				}
				
				else {
					 System.out.println("Unknown protocol for accessing inputfile");
					 throw new Exception("Unknown protocol");
				}
		  }
		  return fileLocalFullName;
    }
    
    /**
     * Famous method that I googled. This copies a file to a new
     * place on the file system.
     */
    protected void copyFileToFile(File sourceFile,File destFile) 
		  throws Exception {
		  InputStream in=new FileInputStream(sourceFile);
		  OutputStream out=new FileOutputStream(destFile);
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
    }
	 
    /**
     * Another famous method that I googled. This downloads contents
     * from the given URL to a local file.
     */    
    protected void copyUrlToFile(URL inputFileUrl,String destFile) 
		  throws Exception {
		  InputStream in = inputFileUrl.openStream();
		  OutputStream out = new FileOutputStream(destFile);
		  
		  //Extract the name of the file from the url.
		  byte[] buf = new byte[1024];
		  int length;
		  while((length = in.read(buf))>0) {
			  out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
		  
    }
    
    /**
     * This version of the client gets back the URL of the
     * results, rather than the results directly.  The String
     * return type is used for JSF page navigation.
     */
    protected String querySOPACGetURL(String siteCode,
												  String beginDate,
												  String endDate) throws Exception {
    	this.beginDate = beginDate;
    	this.endDate = endDate;
    	
    	String resource = "procCoords";
		String contextGroup = inputContextGroup;
		String minMaxLatLon = "";
		String contextId = inputContextId;
		  
		System.out.println("about to query input url for site " + siteCode + " beginDate:" + beginDate + " endDate:" + endDate);
		  
		return querySOPACGetURL(siteCode,
								resource,
								contextGroup,
								contextId,
								minMaxLatLon,
								beginDate,
								endDate);
    }
	
}

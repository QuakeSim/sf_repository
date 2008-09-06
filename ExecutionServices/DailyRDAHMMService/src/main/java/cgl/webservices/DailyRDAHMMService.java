package cgl.webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class DailyRDAHMMService extends RDAHMMService {
	
	String stationID;
	String modelBaseName;
	String modelWorkDir;
	Object runningLock = null;
	
	public DailyRDAHMMService(boolean useClassLoader, String stationID) 
		throws Exception{
		super(useClassLoader);
		this.stationID = stationID;
		
		buildFilePath = properties.getProperty("build_daily.file.path");		  
		modelBaseName = properties.getProperty("project_daily.name") + "_" + stationID;
		baseDestDir = properties.getProperty("base.dest.daily_dir");
		projectName = modelBaseName + "_" + UtilSet.getDateString(Calendar.getInstance());
		modelWorkDir = baseWorkDir + File.separator + modelBaseName;
		outputDestDir = baseDestDir+ File.separator + projectName;		
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
			fakeEvaluation();
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
	protected void fakeEvaluation() {
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
		res = UtilSet.exec("cp " + modelPath + ".input "	+ proPath + ".all.input");

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
		
		return rdahmmInputFile;
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
		  
		  String resource="procCoords";
		  String contextGroup="sopacGlobk";
		  String minMaxLatLon="";
		  String contextId="58";
		  
		  System.out.println("about to query input url for site " + siteCode + " beginDate:" + beginDate + " endDate:" + endDate);
		  
		  return querySOPACGetURL(siteCode,
										  resource,
										  contextGroup,
										  contextId,
										  minMaxLatLon,
										  beginDate,
										  endDate);
		  
		  // 	GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
		  // 	gsq.setFromServlet(siteCode, beginDate, endDate, resource,
		  // 			   contextGroup, contextId, minMaxLatLon, true);
		  // 	String dataUrl=gsq.getResource();
		  // 	System.out.println("GRWS data url: "+dataUrl);
		  //	return dataUrl;
    }
	
}

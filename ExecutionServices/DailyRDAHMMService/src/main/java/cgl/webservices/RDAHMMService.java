package cgl.webservices;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

//Needed to get the ServletContext to read the properties.
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

/**
 * A simple wrapper for Ant.
 */

public class RDAHMMService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(RDAHMMService.class);

    final String FILE_PROTOCOL="file";
    final String HTTP_PROTOCOL="http";

    //These are the system properties that may have
    //default values.
    Properties properties;
    String serverUrl;
    String baseWorkDir;
    String baseDestDir;
    String outputDestDir;
    String projectName;
    String binPath;
    String outputType;
    int randomSeed;
    double annealStep;
    double betamin;
    int ntries;
    String buildFilePath;
    String antTarget;
    
    
    public RDAHMMService(boolean useClassLoader) 
		  throws Exception {
		  super();
		  
		  if(useClassLoader) {
				System.out.println("Using classloader");
				//This is useful for command line clients but does not work
				//inside Tomcat.
				ClassLoader loader=ClassLoader.getSystemClassLoader();
				properties=new Properties();
				
				//This works if you are using the classloader but not inside
				//Tomcat.
				properties.load(loader.getResourceAsStream("rdahmmconfig.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/rdahmmconfig.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }
		  
		  serverUrl=properties.getProperty("rdahmm.service.url");
		  baseWorkDir=properties.getProperty("base.workdir");
		  baseDestDir=properties.getProperty("base.dest.dir");
		  projectName=properties.getProperty("project.name");
		  binPath=properties.getProperty("bin.path");
		  outputType=properties.getProperty("output.type");
		  randomSeed=
				Integer.parseInt(properties.getProperty("random.seed"));
		  annealStep=
				Double.parseDouble(properties.getProperty("anneal.step"));
		  betamin=	    
				Double.parseDouble(properties.getProperty("betamin.prop"));
		  ntries=
				Integer.parseInt(properties.getProperty("ntries.prop"));
		  buildFilePath=properties.getProperty("build.file.path");
		  antTarget=properties.getProperty("ant.target");
		  
		  //Put a time stamp on the project name:
		  projectName += "-"+(new Date()).getTime();
		  
		  outputDestDir=baseDestDir+"/"+projectName;		  
		  
		  System.out.println("Here are some property values");
		  System.out.println(baseWorkDir);
		  System.out.println(projectName);
		  System.out.println(binPath);
		  System.out.println(outputType);
		  System.out.println(randomSeed);
		  System.out.println("Etc etc, done initializing");
    }
    
    public RDAHMMService() throws Exception{
		  this(false);
    }
	 
    /**
     * This merges multiple files into a single file,
     * duplicating UNIX paste.
     */ 
    public void mergeInputFiles(String[] inputFileArray, 
										  String mergedFileName) {
		  
		  //Find the shortest of the input files.
		  int shortCount=Integer.MAX_VALUE;
		  System.out.println("Max integer Value="+shortCount);
		  
		  for(int i=0;i<inputFileArray.length;i++){
				int lineCount=getLineCount(inputFileArray[i]);
				if(lineCount < shortCount) shortCount=lineCount;
		  }
		  System.out.println("Shortest file length="+shortCount);	
		  
		  // Now do the thing.
		  try {
				//This is our output file.
				PrintWriter pw=
					 new PrintWriter(new FileWriter(mergedFileName),true);
				
				//Set up bufferedreader array
				BufferedReader[] br=new BufferedReader[inputFileArray.length];
				for(int i=0;i<br.length;i++){
					 br[i]=new BufferedReader(new FileReader(inputFileArray[i]));
				}
				
				//Loop over each line of the file
				for(int i=0;i<shortCount;i++) {
					 String line="";		
					 for(int j=0;j<br.length;j++) {
						  line+=br[j].readLine();
					 }
					 pw.println(line);
				}
	    
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
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
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     *
     * This method can accepted either single-valued or
     * multiple valued entries.  These 
     */
    protected String[] filterResults(String[] tabbedFile,
				     int cutLeftColumns,
				     int cutRightColumns) throws Exception {
		  String[] filteredFileArray=new String[tabbedFile.length];
		  String space=" ";
		  StringTokenizer st;
		  for(int i=0;i<tabbedFile.length;i++){
				filteredFileArray[i]=tabbedFile[i]+".filtered";
				BufferedReader br=
					 new BufferedReader(new FileReader(tabbedFile[i]));
				PrintWriter printer=
					 new PrintWriter(new FileWriter(filteredFileArray[i]),true);
				String line=br.readLine();
				while(line!=null) {
					 //	    System.out.println(line);
					 st=new StringTokenizer(line);
					 String newLine="";
					 int tokenCount=st.countTokens();
					 for (int j=0;j<tokenCount;j++) {
						  String temp=st.nextToken();
						  if(j>=cutLeftColumns && j<(tokenCount-cutRightColumns)) {
								newLine+=temp+space;
						  }
					 }
					 //	    System.out.println(newLine);
					 printer.println(newLine);
					 line=br.readLine();
				}
		  }
		  return filteredFileArray;
    }
	 
    private void makeWorkDir(String workDir, 
									  String bf_loc)
		  throws Exception {
		  System.out.println("Working Directory is "+workDir);
		  
		  String[] args0=new String[4];
        args0[0]="-DworkDir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=bf_loc;
        args0[3]="MakeWorkDir";
		  
        setArgs(args0);
        run();
    }  
	 
    private String extractSimpleName(String extendedName) {
		  return (new File(extendedName)).getName();
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
				String fileSimpleName=extractSimpleName(inputFileUrl.getFile());
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
				}
				
				else if(protocol.equals(HTTP_PROTOCOL)) {
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
    private void copyFileToFile(File sourceFile,File destFile) 
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
    
    private void copyUrlToFile(URL inputFileUrl,String destFile) 
		  throws Exception {
		  
		  URLConnection uconn=inputFileUrl.openConnection();
		  InputStream in=inputFileUrl.openStream();
		  OutputStream out=new FileOutputStream(destFile);
		  
		  //Extract the name of the file from the url.
		  
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
		  
    }
    private String[] setUpArgArray(String inputFileUrlString,
											  String workDir,
											  String outputDestDir,
											  String projectName,
											  String binPath,
											  int nobsv,
											  int ndim,
											  int numModelStates,
											  int randomSeed,
											  String outputType,
											  double annealStep,
											  String buildFilePath,
											  String antTarget) throws Exception {
		  
		  String[] args=new String[16];
		  args[0]="-DworkDir.prop="+workDir;
		  args[1]="-DprojectName.prop="+projectName;
        args[2]="-Dbindir.prop="+binPath;
        args[3]="-DRDAHMMBaseName.prop="+projectName;
        args[4]="-Dnobsv.prop="+nobsv;
        args[5]="-Dndim.prop="+ndim;
        args[6]="-Dnstates.prop="+numModelStates;
        args[7]="-Dranseed.prop="+randomSeed;
        args[8]="-Doutput_type.prop="+outputType;
		  args[9]="-DannealStep.prop="+annealStep;
		  args[10]="-Dbetamin.prop="+betamin;
		  args[11]="-Dntries.prop="+ntries;
		  args[12]="-DoutputDestDir.prop="+outputDestDir;
        args[13]="-buildfile";
        args[14]=buildFilePath;
        args[15]=antTarget;
		  
		  return args;
    }
	 
    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then RDAHMM itself 
    // would probably not work either.
    //--------------------------------------------------
    protected int getFileDimension(String fileFullName) {
		  
		  boolean success=false;
		  int ndim=0;
		  StringTokenizer st;
		  try {
				
				BufferedReader buf=
					 new BufferedReader(new FileReader(fileFullName));
				
				String line=buf.readLine();	
				if(line!=null){
					 while(!success) {
						  if(line.trim().equals("")) {
								line=buf.readLine();
						  }
						  else {
								success=true;
								st=new StringTokenizer(line);
								ndim=st.countTokens();
						  }		   
					 }
				}
				buf.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  return ndim;
    }
	 
    //--------------------------------------------------
    // This counts the line number.
    //--------------------------------------------------
    protected int getLineCount(String fileFullName) {
		  int nobsv=0;
		  try {
				LineNumberReader lnr=
					 new LineNumberReader(new FileReader(fileFullName));
				
				String line2=lnr.readLine();
				while(line2!=null) {
					 line2=lnr.readLine();
				}
				lnr.close();
				nobsv=lnr.getLineNumber();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return nobsv;
		  
    }
	 
    /**
     * This version runs in non-blocking mode and gets
     * the data from the SOPAC data service.
     * It assumes default values for the contextGroup, contextId
     * resource, and bouding box.
     * 
     * This can take multiple site codes as a single string.
     */
    public String[] runNonblockingRDAHMM(String siteCode,
													  String beginDate,
													  String endDate,
													  int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
				return runNonblockingRDAHMM(dataUrl,numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }

    public RDAHMMResultsBean runNonblockingRDAHMM2(String siteCode,
																  String beginDate,
																  String endDate,
																  int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
				return createRDAHMMBean(runNonblockingRDAHMM(dataUrl,numModelStates));
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }
	 
    /**
     * This non-blocking version lets you pass in all values 
     * for querying the SOPAC data service.
     */
    public String[] runNonblockingRDAHMM(String siteCode,
													  String resource,
													  String contextGroup,
													  String contextId,
													  String minMaxLatLon,
													  String beginDate,
													  String endDate,
													  int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,
														  resource,
														  contextGroup,
														  contextId,
														  minMaxLatLon,
														  beginDate,
														  endDate);

				return runNonblockingRDAHMM(dataUrl,numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }

    public RDAHMMResultsBean runNonblockingRDAHMM2(String siteCode,
																	String resource,
																	String contextGroup,
																	String contextId,
																	String minMaxLatLon,
																	String beginDate,
																	String endDate,
																	int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,
														  resource,
														  contextGroup,
														  contextId,
														  minMaxLatLon,
														  beginDate,
														  endDate);
				return createRDAHMMBean(runNonblockingRDAHMM(dataUrl,numModelStates));
				//				return runNonblockingRDAHMM(dataUrl,numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }
	 
    /**
     * This version runs in blocking mode and gets
     * the data from the SOPAC data service.
     */
    public String[] runBlockingRDAHMM(String siteCode,
												  String beginDate,
												  String endDate,
												  int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
				return runBlockingRDAHMM(dataUrl,numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }
	 

    public RDAHMMResultsBean runBlockingRDAHMM2(String siteCode,
																String beginDate,
																String endDate,
																int numModelStates)
		  throws Exception {
		  try {
				String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
				return createRDAHMMBean(runBlockingRDAHMM(dataUrl,numModelStates));
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }
	 
    /**
     * This blocking version lets you pass in all values 
     * for querying the SOPAC data service.
     */
    public String[] runBlockingRDAHMM(String siteCode,
												  String resource,
												  String contextGroup,
												  String contextId,
												  String minMaxLatLon,
												  String beginDate,
												  String endDate,
												  int numModelStates)
		  throws Exception {
		  String token="::";
		  System.out.println(siteCode+token+resource
									+token+contextGroup
									+token+contextId
									+token+minMaxLatLon
									+token+beginDate
									+token+endDate
									+token+numModelStates);
		  try {
				String dataUrl=querySOPACGetURL(siteCode,
														  resource,
														  contextGroup,
														  contextId,
														  minMaxLatLon,
														  beginDate,
														  endDate);
				return runBlockingRDAHMM(dataUrl,numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }
	 
    public RDAHMMResultsBean runBlockingRDAHMM2(String siteCode,
																String resource,
																String contextGroup,
																String contextId,
																String minMaxLatLon,
																String beginDate,
																String endDate,
																int numModelStates)
		  throws Exception {
		  String token="::";
		  System.out.println(siteCode+token+resource
									+token+contextGroup
									+token+contextId
									+token+minMaxLatLon
									+token+beginDate
									+token+endDate
									+token+numModelStates);
		  try {
				String dataUrl=querySOPACGetURL(siteCode,
														  resource,
														  contextGroup,
														  contextId,
														  minMaxLatLon,
														  beginDate,
														  endDate);
				return createRDAHMMBean(runBlockingRDAHMM(dataUrl,numModelStates));
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		  }
    }

    /**
     * This is the simplified API that uses default values.
     */ 

    public String[] runBlockingRDAHMM(String inputFileUrlString,
												  int numModelStates) 
		  throws Exception {
		  System.out.println("Running blocking execution");
		  System.out.println(inputFileUrlString);
		  System.out.println(numModelStates);
		  
		  String[] returnVals=runBlockingRDAHMM(inputFileUrlString,
															 baseWorkDir,
															 outputDestDir,
															 projectName,
															 binPath,
															 numModelStates,
															 randomSeed,
															 outputType,
															 annealStep,
															 buildFilePath,
															 antTarget);

		  return returnVals;
    }

    public RDAHMMResultsBean runBlockingRDAHMM2(String inputFileUrlString,
																int numModelStates) 
		  throws Exception {
		  System.out.println("Running blocking execution");
		  System.out.println(inputFileUrlString);
		  System.out.println(numModelStates);
		  
		  String[] returnVals=runBlockingRDAHMM(inputFileUrlString,
															 baseWorkDir,
															 outputDestDir,
															 projectName,
															 binPath,
															 numModelStates,
															 randomSeed,
															 outputType,
															 annealStep,
															 buildFilePath,
															 antTarget);
		  

		  return createRDAHMMBean(returnVals);
    }
	 

    /**
     * This is the simplified API that uses default properties.
     */
    public String[] runNonblockingRDAHMM(String inputFileUrlString,
													  int numModelStates) 
		  throws Exception {
		  System.out.println("Running non-blocking execution");
		  System.out.println(inputFileUrlString);
		  System.out.println(numModelStates);
		  
		  
		  String[] returnVals=runNonblockingRDAHMM(inputFileUrlString,
																 baseWorkDir,
																 outputDestDir,
																 projectName,
																 binPath,
																 numModelStates,
																 randomSeed,
																 outputType,
																 annealStep,
																 buildFilePath,
																 antTarget);
		  return returnVals;
    }

    public RDAHMMResultsBean runNonblockingRDAHMM2(String inputFileUrlString,
																	int numModelStates) 
		  throws Exception {
		  System.out.println("Running non-blocking execution");
		  System.out.println(inputFileUrlString);
		  System.out.println(numModelStates);
		  
		  
		  String[] returnVals=runNonblockingRDAHMM(inputFileUrlString,
																 baseWorkDir,
																 outputDestDir,
																 projectName,
																 binPath,
																 numModelStates,
																 randomSeed,
																 outputType,
																 annealStep,
																 buildFilePath,
																 antTarget);
		  return createRDAHMMBean(returnVals);
    }
	 
	 
    /**
     * This version is used to to hold response until 
     * RDAHMM finished executing.  This is the full API.
     */
    public String[] runBlockingRDAHMM(String inputFileUrlString,
												  String baseWorkDir,
												  String outputDestDir,
												  String projectName,
												  String binPath,
												  int numModelStates,
												  int randomSeed,
												  String outputType,
												  double annealStep,
												  String buildFilePath,
												  String antTarget) throws Exception {
		  
		  
		  //Set up the work directory
		  String workDir = baseWorkDir+File.separator+projectName;
		  makeWorkDir(workDir,buildFilePath);
		  
		  String[] inputFileUrlArray = convertInputUrlStringToArray(inputFileUrlString);
			
			String[] localFileArray = downloadInputFile(inputFileUrlArray,workDir);
			
			String[] localFileArrayFiltered = filterResults(localFileArray, 2, 3);
			
			String rdahmmInputFile = workDir + "/" + projectName + ".input";
			mergeInputFiles(localFileArrayFiltered,rdahmmInputFile);
		  
		  
		  //Get the dimensions and number of observations.
		  int ndim=getFileDimension(rdahmmInputFile);
		  int nobsv=getLineCount(rdahmmInputFile);
		  
		  String[] args=setUpArgArray(rdahmmInputFile,
												workDir,
												outputDestDir,
												projectName,
												binPath,
												nobsv,
												ndim,
												numModelStates,
												randomSeed,
												outputType,
												annealStep,
												buildFilePath,
												antTarget);
		  
		  //Methods inherited from parent
        setArgs(args);
        run();
		return getTheReturnFiles();	
    }
    
    public RDAHMMResultsBean runBlockingRDAHMM2(String inputFileUrlString,
																String baseWorkDir,
																String outputDestDir,
																String projectName,
																String binPath,
																int numModelStates,
																int randomSeed,
																String outputType,
																double annealStep,
																String buildFilePath,
																String antTarget) throws Exception {
		  
		  
		  //Set up the work directory
		  String workDir=baseWorkDir+File.separator+projectName;
		  makeWorkDir(workDir,buildFilePath);
		  
		  String[] inputFileUrlArray = convertInputUrlStringToArray(inputFileUrlString);
			
			String[] localFileArray = downloadInputFile(inputFileUrlArray,workDir);
			
			String[] localFileArrayFiltered = filterResults(localFileArray, 2, 3);
			
			String rdahmmInputFile = workDir + "/" + projectName + ".input";
			mergeInputFiles(localFileArrayFiltered,rdahmmInputFile);
		  
		  
		  //Get the dimensions and number of observations.
		  int ndim=getFileDimension(rdahmmInputFile);
		  int nobsv=getLineCount(rdahmmInputFile);
		  
		  String[] args=setUpArgArray(rdahmmInputFile,
												workDir,
												outputDestDir,
												projectName,
												binPath,
												nobsv,
												ndim,
												numModelStates,
												randomSeed,
												outputType,
												annealStep,
												buildFilePath,
												antTarget);
		  
		  //Methods inherited from parent
        setArgs(args);
        run();
		  return createRDAHMMBean(getTheReturnFiles());
    }
    
    /**
     * This version immediately returns and is used
     * for programs that take longer to run.  This is the full
     * API.
     */
    public String[] runNonblockingRDAHMM(String inputFileUrlString,
													  String baseWorkDir,
													  String outputDestDir,
													  String projectName,
													  String binPath,
													  int numModelStates,
													  int randomSeed,
													  String outputType,
													  double annealStep,
													  String buildFilePath,
													  String antTarget) throws Exception {
		  
		  //Set up the work directory
		  String workDir=baseWorkDir+File.separator+projectName;
		  makeWorkDir(workDir,buildFilePath);
		  
		  //Copy the input file to the working directory, if 
		  //necessary.
		  String[] inputFileUrlArray = convertInputUrlStringToArray(inputFileUrlString);
		  String[] localFileArray = downloadInputFile(inputFileUrlArray,workDir);
		  String[] localFileArrayFiltered = filterResults(localFileArray, 2, 3);
		  String rdahmmInputFile = workDir + "/" + projectName + ".input";
		  mergeInputFiles(localFileArrayFiltered,rdahmmInputFile);
		  
		  //Get the dimensions and number of observations.
		  int ndim=getFileDimension(rdahmmInputFile);
		  int nobsv=getLineCount(rdahmmInputFile);
		  
		  
		  String[] args=setUpArgArray(rdahmmInputFile,
												workDir,
												outputDestDir,
												projectName,
												binPath,
												nobsv,
												ndim,
												numModelStates,
												randomSeed,
												outputType,
												annealStep,
												buildFilePath,
												antTarget);
		  
		  
		  //Methods inherited from parent
        setArgs(args);
        execute();
		  
		  return getTheReturnFiles();
    }

    public RDAHMMResultsBean runNonblockingRDAHMM2(String inputFileUrlString,
																	String baseWorkDir,
																	String outputDestDir,
																	String projectName,
																	String binPath,
																	int numModelStates,
																	int randomSeed,
																	String outputType,
																	double annealStep,
																	String buildFilePath,
																	String antTarget) throws Exception {
		  
		  //Set up the work directory
		  String workDir=baseWorkDir+File.separator+projectName;
		  makeWorkDir(workDir,buildFilePath);
		  
		  String[] inputFileUrlArray = convertInputUrlStringToArray(inputFileUrlString);
			
			String[] localFileArray = downloadInputFile(inputFileUrlArray,workDir);
			
			String[] localFileArrayFiltered = filterResults(localFileArray, 2, 3);
			
			String rdahmmInputFile = workDir + "/" + projectName + ".input";
			mergeInputFiles(localFileArrayFiltered,rdahmmInputFile);
		  
		  //Get the dimensions and number of observations.
		  int ndim=getFileDimension(rdahmmInputFile);
		  int nobsv=getLineCount(rdahmmInputFile);
		  
		  
		  String[] args=setUpArgArray(rdahmmInputFile,
												workDir,
												outputDestDir,
												projectName,
												binPath,
												nobsv,
												ndim,
												numModelStates,
												randomSeed,
												outputType,
												annealStep,
												buildFilePath,
												antTarget);
		  
		  
		  //Methods inherited from parent
        setArgs(args);
        execute();
		  
		  return createRDAHMMBean(getTheReturnFiles());
    }
	 
    /**
     * A dumb little method for constructing the URL outputs. This
     * will not get called if the execute()/run() method fails.
     */ 
    protected String[] getTheReturnFiles() {
		  String[] extensions={".input",".range",".Q",".pi",".A",
									  ".minval",".maxval",".L",".B",
									  ".Q",".stdout",
									  ".input.X.png",".input.Y.png",".input.Z.png"};
		  
		  String[] returnFiles=new String[extensions.length];
		  for(int i=0;i<extensions.length;i++) {
				returnFiles[i]=serverUrl+"/"+projectName
					 +"/"+projectName+extensions[i];
		  }
		  
		  return returnFiles;
    }
    
    /**
     * This is a bit verion with less defaults.
     * We can accept more than one site code as 
     * token-separated values (typically space-separated).
     * 
     * It returns a string that can also be 
     * multi-values (space-separated string).
     */
    protected String querySOPACGetURL(String siteCode,
												  String resource,
												  String contextGroup,
												  String contextId,
												  String minMaxLatLon,
												  String beginDate,
												  String endDate) throws Exception {
		  
		  //Make sure we don't have unnecessary space.
		  String dataUrl="";
		  System.out.println(siteCode);
		  
		  StringTokenizer st=new StringTokenizer(siteCode);
		  GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
		  while(st.hasMoreTokens()) {
				String siteCodeEntry=st.nextToken();
				System.out.println("Site entry:"+siteCodeEntry);
				gsq.setFromServlet(siteCodeEntry, beginDate, endDate, resource,
										 contextGroup, contextId, minMaxLatLon, true);
				
				dataUrl+=gsq.getResource()+" ";
				System.out.println("GRWS data url: "+dataUrl);
		  }
		  return dataUrl.trim();
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
		  String contextGroup="reasonComb";
		  String minMaxLatLon="";
		  String contextId="4";
		  
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
    
	 protected RDAHMMResultsBean createRDAHMMBean(String[] results) {
		  RDAHMMResultsBean rrb=new RDAHMMResultsBean();
		  
		  rrb.setInputUrl(results[0]);
		  rrb.setRangeUrl(results[1]);
		  rrb.setQUrl(results[2]);
		  rrb.setPiUrl(results[3]);
		  rrb.setAUrl(results[4]);
		  rrb.setMinvalUrl(results[5]);
		  rrb.setMaxvalUrl(results[6]);
		  rrb.setLUrl(results[7]);
		  rrb.setBUrl(results[8]);
		  rrb.setQUrl(results[9]);
		  rrb.setStdoutUrl(results[10]);
		  rrb.setInputXPngUrl(results[11]);
		  rrb.setInputYPngUrl(results[12]);
		  rrb.setInputZPngUrl(results[13]);
		  return rrb;
	 }
	 
    /** 
     * This is added for testing.
     */ 
	 
    public static void main(String[] args) {
		  String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-47353-20061008100245.txt";
		  int numModelStates=2;
		  try {
				//Since we are running on the command line, use 
				//the classloader to find the property files
				RDAHMMService rds=new RDAHMMService(true);
				System.out.println("----------------------------------");
				System.out.println("Testing blocking version");
				rds.runBlockingRDAHMM(dataUrl,
											 numModelStates);
				
				System.out.println("----------------------------------");
				System.out.println("Testing non-blocking version");
				rds.runNonblockingRDAHMM(dataUrl,
												 numModelStates);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
    }
}

package cgl.webservices;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

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
    String baseWorkDir;
    String baseDestDir;
    String outputDestDir;
    String projectName;
    String binPath;
    String outputType;
    int randomSeed;
    double annealStep;
    String buildFilePath;
    String antTarget;
    
    public RDAHMMService() throws Exception{
	super();	
	System.out.println("Constructing the RDAHMM Service");
	ClassLoader loader=ClassLoader.getSystemClassLoader();
	
	//Must fix this!!!!!!
	String propertyFile="/home/gateway/QuakeSim2/portal_deploy/apache-tomcat-5.5.12/webapps/rdahmmexec/WEB-INF/classes/rdahmmconfig.properties";

	properties=new Properties();
	System.out.println("Looking for props in the classpath");
	properties.load(new 
			FileInputStream(propertyFile));
	//	properties.load(loader.getResourceAsStream("rdahmmconfig.properties"));

	baseWorkDir=properties.getProperty("base.workdir");
	baseDestDir=properties.getProperty("base.dest.dir");
	projectName=properties.getProperty("project.name");
	binPath=properties.getProperty("bin.path");
	outputType=properties.getProperty("output.type");
	randomSeed=
	    Integer.parseInt(properties.getProperty("random.seed"));
	annealStep=
	    Double.parseDouble(properties.getProperty("anneal.step"));
	buildFilePath=properties.getProperty("build.file.path");
	antTarget=properties.getProperty("ant.target");
	
	//Put a time stamp on the project name:
	projectName+="-"+(new Date()).getTime();
	
	outputDestDir=baseDestDir+"/"+projectName;

	System.out.println("Here are some property values");
	System.out.println(baseWorkDir);
	System.out.println(projectName);
	System.out.println(binPath);
	System.out.println(outputType);
	System.out.println(randomSeed);
	System.out.println("Etc etc, done initializing");
	
    }

    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     */
    protected void filterResults(String tabbedFile,
				 String rdahmmInputFile,
				 int cutLeftColumns,
				 int cutRightColumns) throws Exception {
	String returnString="";
	String space=" ";
	StringTokenizer st;
	BufferedReader br=new BufferedReader(new FileReader(tabbedFile));
	PrintWriter printer=
	    new PrintWriter(new FileWriter(rdahmmInputFile),true);
	String line=br.readLine();
	while(line!=null) {
	    //	    System.out.println(line);
	    st=new StringTokenizer(line);
	    String newLine="";
	    int tokenCount=st.countTokens();
	    for (int i=0;i<tokenCount;i++) {
		String temp=st.nextToken();
		if(i>=cutLeftColumns && i<(tokenCount-cutRightColumns)) {
		    newLine+=temp+space;
		}
	    }
	    //	    System.out.println(newLine);
	    printer.println(newLine);
	    line=br.readLine();
	}
	return;
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
   
    
    private String downloadInputFile(String inputFileUrlString,
				     String inputFileDestDir)
	throws Exception {

	//Convert to a URL. This will throw an exception if
	//malformed.
	URL inputFileUrl=new URL(inputFileUrlString);
	
	String protocol=inputFileUrl.getProtocol();
	System.out.println("Protocol: "+protocol);
	String fileSimpleName=extractSimpleName(inputFileUrl.getFile());
	System.out.println(fileSimpleName);

	String fileLocalFullName=inputFileDestDir+File.separator
	    +fileSimpleName;

	if(protocol.equals(FILE_PROTOCOL)) {
	    String filePath=inputFileUrl.getFile();
	    fileSimpleName=inputFileUrl.getFile();

	    System.out.println("File path is "+filePath);
	    File filePathObject=new File(filePath);
	    File destFileObject=new File(fileLocalFullName);

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
	    copyUrlToFile(inputFileUrl,fileLocalFullName);
	}

	else {
	    System.out.println("Unknown protocol for accessing inputfile");
	    throw new Exception("Unknown protocol");
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
	
	String[] args=new String[14];
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
	args[10]="-DoutputDestDir.prop="+outputDestDir;
        args[11]="-buildfile";
        args[12]=buildFilePath;
        args[13]=antTarget;

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
    //--------------------------`------------------------
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
					      outputDestDir;
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


    /**
     * This is the simplified API that uses default properties.
     */
    public String[] runNonblockingRDAHMM(String inputFileUrlString,
					 int numModelStates) throws Exception {
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


    /**
     * This version is used to to hold response until 
     * RDAHMM finished executing.  This is the full API.
     */
    public String[] runBlockingRDAHMM(String inputFileUrlString,
				      String baseWorkDir,
				      String outputDestDir;
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
	String localFile=downloadInputFile(inputFileUrlString,workDir);

	//Filter the file
	String localFileFiltered=workDir+File.separator+projectName+".input";
	filterResults(localFile, localFileFiltered, 2, 3);
	
	//Get the dimensions and number of observations.
	int ndim=getFileDimension(localFileFiltered);
	int nobsv=getLineCount(localFileFiltered);

	String[] args=setUpArgArray(localFileFiltered,
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
	
	String[] extensions={".input",".range",".Q",".pi",
			   ".minval",".maxval",".L",".B",".Q",".stdout"};

	String[] returnFiles=new String[extensions.length];
	for(int i=0;i<extensions.length;i++) {
	    returnFiles[i]=projectName+extensions[i];
	}
	
	return returnFiles;
	
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
	
	String workDir=baseWorkDir+File.separator+projectName;

	//Make working directory
	makeWorkDir(workDir,buildFilePath);

	//Copy the input file to the working directory, if 
	//necessary.
	String localFile=downloadInputFile(inputFileUrlString,workDir);

	//Filter the file
	//Filter the file
	String localFileFiltered=workDir+File.separator+projectName+".input";
	filterResults(localFile, localFileFiltered, 2, 3);
	
	//Get the dimensions and number of observations.
	int ndim=getFileDimension(localFileFiltered);
	int nobsv=getLineCount(localFileFiltered);


	String[] args=setUpArgArray(localFileFiltered,
				    workDir,
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
	String[] extensions={".input",".range",".Q",".pi",
			   ".minval",".maxval",".L",".B",".Q",".stdout"};

	String[] returnFiles=new String[extensions.length];
	for(int i=0;i<extensions.length;i++) {
	    returnFiles[i]=projectName+extensions[i];
	}
	
	return returnFiles;
    }

    

    /** 
     * This is added for testing.
     */ 

    public static void main(String[] args) {

	
	String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-47353-20061008100245.txt";
	int numModelStates=2;
	try {
	    RDAHMMService rds=new RDAHMMService();
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

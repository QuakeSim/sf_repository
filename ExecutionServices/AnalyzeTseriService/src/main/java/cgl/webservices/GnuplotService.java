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
 * Despite the name, this is not a general purpose Gnuplot service.  It
 * is used to make plots of the GRWS time series data.
 */
public class GnuplotService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(GnuplotService.class);

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
    String buildFilePath;
    String antTarget;
    
    public GnuplotService(boolean useClassLoader) 
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
	    properties.load(loader.getResourceAsStream("gnuplotconfig.properties"));
	}
	else {
	    //Extract the Servlet Context
	    System.out.println("Using Servlet Context");
	    MessageContext msgC=MessageContext.getCurrentContext();
	    ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
	    
	    
	    String propertyFile=context.getRealPath("/")
		+"/WEB-INF/classes/gnuplotconfig.properties";
	    System.out.println("Prop file location "+propertyFile);
	    
	    properties=new Properties();	    
	    properties.load(new 
			    FileInputStream(propertyFile));
	}
	
	serverUrl=properties.getProperty("gnuplot.service.url");
	baseWorkDir=properties.getProperty("base.workdir");
	baseDestDir=properties.getProperty("base.dest.dir");
	projectName=properties.getProperty("project.name");
	binPath=properties.getProperty("bin.path");
	buildFilePath=properties.getProperty("build.file.path");
	antTarget=properties.getProperty("ant.target");
	
	//Put a time stamp on the project name:
	projectName+="-"+(new Date()).getTime();
	
	outputDestDir=baseDestDir+"/"+projectName;


	System.out.println("Here are some property values");
	System.out.println(baseWorkDir);
	System.out.println(projectName);
	System.out.println(binPath);
    }
    
    public GnuplotService() throws Exception{
	this(false);
	
    }

    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     *
     * Use -1 for both cutLeft and cutRight to do null
     * filtering (ie just renaming the file).
     */
    protected void filterResults(String tabbedFile,
				 String gnuplotInputFile,
				 int cutLeftColumns,
				 int cutRightColumns) throws Exception {
	String returnString="";
	String space=" ";
	StringTokenizer st;
	BufferedReader br=new BufferedReader(new FileReader(tabbedFile));
	PrintWriter printer=
	    new PrintWriter(new FileWriter(gnuplotInputFile),true);
	String line=br.readLine();
	while(line!=null) {
	    st=new StringTokenizer(line);
	    String newLine=line;
	    int tokenCount=st.countTokens();
	    if(cutRightColumns<0) cutRightColumns=tokenCount+1;
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
				   String buildFilePath,
				   String antTarget) throws Exception {
	
	String[] args=new String[8];
        args[0]="-DworkDir.prop="+workDir;
        args[1]="-DprojectName.prop="+projectName;
        args[2]="-Dbindir.prop="+binPath;
        args[3]="-DGnuplotBaseName.prop="+projectName;
	args[4]="-DoutputDestDir.prop="+outputDestDir;
        args[5]="-buildfile";
        args[6]=buildFilePath;
        args[7]=antTarget;

	return args;
    }


    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then Gnuplot itself 
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
     * This version runs in non-blocking mode and gets
     * the data from the SOPAC data service.
     */
    public String[] runNonblockingGnuplot(String siteCode,
					 String beginDate,
					  String endDate)
	throws Exception {
	try {
	    String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
	    return runNonblockingGnuplot(dataUrl);
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
    public String[] runBlockingGnuplot(String siteCode,
				       String beginDate,
				       String endDate)
	throws Exception {
	try {
	    String dataUrl=querySOPACGetURL(siteCode,beginDate,endDate);
	    return runBlockingGnuplot(dataUrl);
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Exception();
	}
    }

    /**
     * This is the simplified API that uses default values.
     */ 

    public String[] runBlockingGnuplot(String inputFileUrlString)
	throws Exception {
	System.out.println("Running blocking execution");
	System.out.println(inputFileUrlString);
	
	String[] returnVals=runBlockingGnuplot(inputFileUrlString,
					      baseWorkDir,
					      outputDestDir,
					      projectName,
					      binPath,
					      buildFilePath,
					      antTarget);
	return returnVals;
    }


    /**
     * This is the simplified API that uses default properties.
     */
    public String[] runNonblockingGnuplot(String inputFileUrlString)
	throws Exception {
	
	System.out.println("Running non-blocking execution");
	System.out.println(inputFileUrlString);

	
	String[] returnVals=runNonblockingGnuplot(inputFileUrlString,
						 baseWorkDir,
						 outputDestDir,
						 projectName,
						 binPath,
						 buildFilePath,
						 antTarget);
	return returnVals;
    }


    /**
     * This version is used to to hold response until 
     * Gnuplot finished executing.  This is the full API.
     */
    public String[] runBlockingGnuplot(String inputFileUrlString,
				      String baseWorkDir,
				      String outputDestDir,
				      String projectName,
				      String binPath,
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
 	filterResults(localFile, localFileFiltered, -1, -1);
	
// 	//Get the dimensions and number of observations.
// 	int ndim=getFileDimension(localFileFiltered);
// 	int nobsv=getLineCount(localFileFiltered);

	String[] args=setUpArgArray(localFile,
				    workDir,
				    outputDestDir,
				    projectName,
				    binPath,
				    buildFilePath,
				    antTarget);
	
	//Methods inherited from parent
        setArgs(args);
        run();
	return getTheReturnFiles();	
    }
    
    /**
     * This version immediately returns and is used
     * for programs that take longer to run.  This is the full
     * API.
     */
    public String[] runNonblockingGnuplot(String inputFileUrlString,
					 String baseWorkDir,
					 String outputDestDir,
					 String projectName,
					 String binPath,
					 String buildFilePath,
					 String antTarget) throws Exception {
	
	String workDir=baseWorkDir+File.separator+projectName;

	//Make working directory
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
				    buildFilePath,
				    antTarget);
		
	//Methods inherited from parent
        setArgs(args);
        execute();

	return getTheReturnFiles();
    }

    /**
     * A dumb little method for constructing the URL outputs. This
     * will not get called if the execute()/run() method fails.
     */ 
    protected String[] getTheReturnFiles() {

	String[] extensions={".input.xyz.X.png",".input.xyz.Y.png",".input.xyz.Z.png"};
	
	String[] returnFiles=new String[extensions.length];
	for(int i=0;i<extensions.length;i++) {
	    returnFiles[i]=serverUrl+"/"+projectName
		+"/"+projectName+extensions[i];
	}
	
	return returnFiles;
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

	GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	gsq.setFromServlet(siteCode, beginDate, endDate, resource,
			   contextGroup, contextId, minMaxLatLon, true);
	String dataUrl=gsq.getResource();
	System.out.println("GRWS data url: "+dataUrl);
	return dataUrl;
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
	    GnuplotService rds=new GnuplotService(true);
	    System.out.println("----------------------------------");
	    System.out.println("Testing blocking version");
	    rds.runBlockingGnuplot(dataUrl);

	    System.out.println("----------------------------------");
	    System.out.println("Testing non-blocking version");
	    rds.runNonblockingGnuplot(dataUrl);
}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}

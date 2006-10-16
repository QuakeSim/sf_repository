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
public class STFilterService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(STFilterService.class);

    final String FILE_PROTOCOL="file";
    final String HTTP_PROTOCOL="http";

    //These are names and such
    String estParameterFile,dataListFile, driverFileName, siteListFile;
    String globalDataDir;
    String siteCode;

    //These are file extensions.  The files will be named after the
    //project.
    private String mosesDataListExt=".list";
    private String mosesSiteListExt=".site";
    private String mosesParamFileExt=".para";
    private String residualFileExt=".resi";
    private String termOutFileExt=".mdl";
    private String outputFileExt=".out";
    private String driverFileExtension=".drv";
    private String sopacDataFileExt=".data";

    private String aprioriValueFile="itrf2000_final.net";
    private String mosesParamFile="moses_test.para";

    
    public STFilterService() {
	super();
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

    /**
     * This creates the file containing the estimate parameters.
     */
    private void createEstimatedParamFile(String workDir,
					  String projectName,
					  String allsitesList,
					  String myStationList)
	
	throws Exception {

	estParameterFile=projectName+mosesParamFileExt;
	PrintWriter pw=
	    new PrintWriter(new FileWriter(workDir+"/"+estParameterFile),true);
	if(myStationList!=null) {
	    pw.println("  2");
	    pw.println(allsitesList);
	    pw.println(myStationList);
	}
	else {
	    pw.println("  1");
	    pw.println(allsitesList);
	}
	pw.close();
    }

    /**
     * Creates the data file.
     */
    private void createDataListFile(String workDir, 
				    String projectName,
				    String siteCode)
	throws Exception {

	String slash="/";  // This is not File.separator of the webserver
	dataListFile=projectName+mosesDataListExt;
	System.out.println("Writing input file: "+workDir+"/"+dataListFile);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(workDir+"/"+dataListFile),true);

	pw.println(" 1   8");  //Need to make this more general.
	pw.println(siteCode+sopacDataFileExt);
	pw.close();
    }

    /**
     * Create the site list file.
     */
    private void createSiteListFile(String workDir,
				    String projectName,
				    String allsitesList,
				    String mysitesList)
	throws Exception {

	String slash="/";  // This is not File.separator of the webserver
	siteListFile=projectName+mosesSiteListExt;
	System.out.println("Writing input file: "+workDir+"/"+siteListFile);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(workDir+"/"+siteListFile),true);

	pw.println("  1");  //Need to make this more general.
	pw.println(siteCode.toUpperCase()+"_GPS");
	pw.close();
    }

    /**
     * Write the driver file.
     */
    private String createDriverFile(String workDir,
				    String projectName,				    				    int resOption,
				    int termOption,
				    double cutoffCriterion,
				    double estJumpSpan,
				    String[] weakObsCriteria,
				    String[] outlierCriteria,
				    String[] badObsCriteria,
				    String[] timeInterval)
	throws Exception {
	
	//Must use spaces here, don't use tabs or it will cause the
	//executable to cough blood.
	String twospace="  ";  
	String fivespace="     ";
	String slash="/";  // This is not File.separator of the webserver

	driverFileName=projectName+driverFileExtension;
	System.out.println("Writing input file: "+workDir+"/"+driverFileName);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(workDir+"/"+driverFileName),true);
	pw.println(twospace+"apriori value file:"+twospace+globalDataDir+slash+aprioriValueFile);
	pw.println(twospace+"input file:"+twospace+workDir+slash+projectName+mosesDataListExt);
	pw.println(twospace+"sit_list file:"+twospace+workDir+slash+projectName+mosesSiteListExt);
	pw.println(twospace+"est_parameter file:"+twospace+workDir+slash+projectName+mosesParamFileExt);
	//	pw.println(twospace+"est_parameter file:"+twospace+globalDataDir+mosesParamFile);
	pw.println(twospace+"output file:"+twospace+workDir+slash+projectName+outputFileExt);
	pw.println(twospace+"residual file:"+twospace+workDir+slash+projectName+residualFileExt);
	pw.println(twospace+"res_option:"+twospace+resOption);
	pw.println(twospace+"specific term_out file:"+twospace+workDir+slash+projectName+termOutFileExt);
	pw.println(twospace+"specific term_option:"+twospace+termOption);
	pw.println(twospace+"enu_correlation usage:"+twospace+"no");
	pw.println(twospace+"cutoff criterion (year):"+twospace+cutoffCriterion);
	pw.println(twospace+"span to est jump aper (est_jump_span):"+twospace+estJumpSpan);
	pw.println(twospace+"weak_obs (big sigma) criteria:"+twospace+weakObsCriteria[0]+twospace+weakObsCriteria[1]+twospace+weakObsCriteria[2]);
	pw.println(twospace+"outlier (big o-c) criteria mm:"+twospace+outlierCriteria[0]+twospace+outlierCriteria[1]+twospace+outlierCriteria[2]);
	pw.println(twospace+"very bad_obs criteria mm:"+twospace+badObsCriteria[0]+twospace+badObsCriteria[1]+twospace+badObsCriteria[2]);
	pw.println(twospace+"t_interval:"+twospace+timeInterval[0]+twospace+timeInterval[1]);
	pw.println(twospace+"end:");
	pw.println("---------- part 2 -- apriori information");
	pw.println(twospace+"exit:");
	pw.close();

	//Clean this up since it could be a memory drain.
	//	sopacDataFileContent=null;
	return "input-file-created";
    }

    private String[] setUpArgArray(String inputFileUrlString,
				   String workDir,
				   String projectName,
				   String binPath,
				   String buildFilePath,
				   String antTarget) throws Exception {
	
	String[] args=new String[7];
        args[0]="-DworkDir.prop="+workDir;
        args[1]="-DprojectName.prop="+projectName;
        args[2]="-Dbindir.prop="+binPath;
        args[3]="-DSTFILTERBaseName.prop="+projectName;
        args[4]="-buildfile";
        args[5]=buildFilePath;
        args[6]=antTarget;
	
	return args;
    }

    private String[] doSetupStuff(String inputFileUrlString,
				  String baseWorkDir,
				  String projectName,
				  String allsitesList,
				  String mysitesList,
				  String siteCode,
				  int resOption,
				  int termOption,
				  double cutoffCriterion,
				  double estJumpSpan,
				  String[] weakObsCriteria,
				  String[] outlierCriteria,
				  String[] badObsCriteria,
				  String[] timeInterval,
				  String binPath,
				  String buildFilePath,
				  String antTarget) throws Exception {
	
	//Set this.  For now, it is a fixed file in the bin
	//directory of STFilter.
	globalDataDir=binPath;

	//Set up the work directory
	String workDir=baseWorkDir+File.separator+projectName;
	makeWorkDir(workDir,buildFilePath);

	//Create the driver and input files.
	createEstimatedParamFile(workDir,projectName,allsitesList,mysitesList);
	createDataListFile(workDir,projectName,siteCode);
	createSiteListFile(workDir,projectName,allsitesList,mysitesList);
	createDriverFile(workDir,
			 projectName,
			 resOption,
			 termOption,
			 cutoffCriterion,
			 estJumpSpan,
			 weakObsCriteria,
			 outlierCriteria,
			 badObsCriteria,
			 timeInterval);
	
	//Copy the input file to the working directory, if 
	//necessary.
	String localFile=downloadInputFile(inputFileUrlString,workDir);
	
	String[] args=setUpArgArray(localFile,
				    workDir,
				    projectName,
				    binPath,
				    buildFilePath,
				    antTarget);
	return args;
    }

    
    /**
     * This version is used to to hold response until 
     * STFilter finished executing.
     */
    public String[] runBlockingSTFilter(String inputFileUrlString,
					String baseWorkDir,
					String projectName,
					String allsitesList,
					String mysitesList,
					String siteCode,
					int resOption,
					int termOption,
					double cutoffCriterion,
					double estJumpSpan,
					String[] weakObsCriteria,
					String[] outlierCriteria,
					String[] badObsCriteria,
					String[] timeInterval,
					String binPath,
					String buildFilePath,
					String antTarget) throws Exception {
	
	String[] args=doSetupStuff(inputFileUrlString,
				   baseWorkDir,
				   projectName,
				   allsitesList,
				   mysitesList,
				   siteCode,
				   resOption,
				   termOption,
				   cutoffCriterion,
				   estJumpSpan,
				   weakObsCriteria,
				   outlierCriteria,
				   badObsCriteria,
				   timeInterval,
				   binPath,
				   buildFilePath,
				   antTarget);
	
	//Methods inherited from parent
        setArgs(args);
        run();
	
	String [] returnFiles=new String[1];	
	return returnFiles;
    }
    
    /**
     * This version immediately returns and is used
     * for programs that take longer to run.
     */
    public String[] runNonblockingSTFilter(String inputFileUrlString,
					   String baseWorkDir,
					   String projectName,
					   String allsitesList,
					   String mysitesList,
					   String siteCode,
					   int resOption,
					   int termOption,
					   double cutoffCriterion,
					   double estJumpSpan,
					   String[] weakObsCriteria,
					   String[] outlierCriteria,
					   String[] badObsCriteria,
					   String[] timeInterval,
					   String binPath,
					   String buildFilePath,
					   String antTarget) throws Exception {

	String[] args=doSetupStuff(inputFileUrlString,
				   baseWorkDir,
				   projectName,
				   allsitesList,
				   mysitesList,
				   siteCode,
				   resOption,
				   termOption,
				   cutoffCriterion,
				   estJumpSpan,
				   weakObsCriteria,
				   outlierCriteria,
				   badObsCriteria,
				   timeInterval,
				   binPath,
				   buildFilePath,
				   antTarget);
	
	//Methods inherited from parent
        setArgs(args);
        execute();
	
	String [] returnFiles=new String[1];
	return returnFiles;
    }

    

    /** 
     * This is added for testing.
     */ 

    public static void main(String[] args) {
	STFilterService rds=new STFilterService();
	
// 	String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-47353-20061008100245.txt";
// 	String workDir="/tmp/";
// 	String projectName="test";
// 	String binPath="/home/gateway/GEMCodes/STFilter2/bin/";
// 	int numModelStates=2;
// 	int randomSeed=1;
// 	String outputType="gaussian";
// 	double annealStep=0.01;
// 	String buildFilePath="/home/gateway/GEMCodes/STFilter2/bin/build.xml";
// 	String antTarget="RunSTFilter";

// 	try {
// 	    System.out.println("----------------------------------");
// 	    System.out.println("Testing blocking version");
// 	    rds.runBlockingSTFilter(dataUrl,
// 				    workDir,
// 				    projectName,
// 				    binPath,
// 				    allsitesList,
// 				    mysitesList,
// 				    siteCode,
// 				    buildFilePath,
// 				    antTarget);	

// 	    System.out.println("----------------------------------");
// 	    System.out.println("Testing non-blocking version");
// 	    rds.runNonblockingSTFilter(dataUrl,
// 				       workDir,
// 				       projectName,
// 				       binPath,
// 				       allsitesList,
// 				       mysitesList,
// 				       siteCode,
// 				       buildFilePath,
// 				       antTarget);	
// 	}
// 	catch (Exception ex) {
// 	    ex.printStackTrace();
// 	}
    }
}

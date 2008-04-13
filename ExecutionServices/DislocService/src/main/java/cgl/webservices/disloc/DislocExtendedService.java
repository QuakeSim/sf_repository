package cgl.webservices.disloc;

//Need to import this parent.
import cgl.webservices.*;

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

//Needed for some number formatting.
import java.text.*;

//Needed for a unique id
import java.rmi.server.UID;

/**
 * A simple wrapper for Ant.
 */

public class DislocExtendedService extends DislocService implements Runnable {  

	 //Usefull variable;
	 final String space=" ";

    public DislocExtendedService() throws Exception {
		  this(false);
    }
    
    public DislocExtendedService(boolean useClassLoader) throws Exception {
		  super(useClassLoader);
		  System.out.println("Base:"+baseWorkDir);
    }
	 
    public DislocResultsBean runNonBlockingDislocExt(String userName,
						  String projectName,
						  Fault[] faults,
						  DislocParamsBean dislocParams,
						  ObsvPoint[] ObsvPoints,
						  String targetName) 
	throws Exception {
	System.out.println("RunNonBlocking called");
	if(targetName==null) targetName=DislocConstants.DISLOC_DEFAULT_TARGET;
	String jobStamp=generateJobStamp();
	
	String[] args=prefabDisloc(userName,
				   projectName,
				   dislocParams,
				   faults,
				   ObsvPoints,
				   targetName,
				   jobStamp);
	setArgs(args);
	execute();
	return createDislocResultsBean(userName,projectName,jobStamp);
    }
    
    public DislocResultsBean runBlockingDislocExt(String userName,
					       String projectName,
					       Fault[] faults,
					       DislocParamsBean dislocParams,
					       ObsvPoint[] ObsvPoints,
					       String targetName) 
	throws Exception {
		  for(int i=0;i<ObsvPoints.length;i++) {
					 System.out.println("Service X:"+ObsvPoints[i].getXPoint());
					 System.out.println("Service Y:"+ObsvPoints[i].getYPoint());
					 System.out.println("Service Lat:"+ObsvPoints[i].getLatPoint());
					 System.out.println("Service Lon:"+ObsvPoints[i].getLonPoint());
		  }

	if(targetName==null) targetName=DislocConstants.DISLOC_DEFAULT_TARGET;
	String jobStamp=generateJobStamp();
	String[] args=prefabDisloc(userName,
				   projectName,
				   dislocParams,
				   faults,
				   ObsvPoints,
				   targetName,
				   jobStamp);
	setArgs(args);
	run();
	return createDislocResultsBean(userName,projectName,jobStamp);
    }
    
    protected String[] prefabDisloc(String userName,
												String projectName,
												DislocParamsBean dislocParams,
												Fault[] faults,
												ObsvPoint[] ObsvPoints,
												String targetName,
												String jobStamp) 
		  throws Exception {
	

	workDir=generateWorkDir(userName,projectName,jobStamp);
	makeWorkDir(workDir);
	createDislocInputFile(userName,
			      projectName,
			      dislocParams,
			      faults,
			      ObsvPoints);

	String[] args=setUpArgs(workDir,
				projectName,
				targetName);
	
	return args;
    }
    
    protected void createDislocInputFile(String userName,
					 String projectName,
					 DislocParamsBean dislocParams,
					 Fault[] faults,
					 ObsvPoint[] ObsvPoints) 
	throws Exception {
	
	String inputFile=workDir+File.separator+projectName+".input";
	System.out.println("Input File: "+inputFile);
	PrintWriter pw=new PrintWriter(new FileWriter(inputFile),true);
	
	//Create the input file.  First create the grid points
	
	//Print the header line
	if(dislocParams.getObservationPointStyle()==1) {
	    pw.println(dislocParams.getOriginLat()
		       +space+dislocParams.getOriginLon()
		       +space+dislocParams.getObservationPointStyle());
	}
	else if(dislocParams.getObservationPointStyle()==0) {
	    pw.println(dislocParams.getOriginLat()
		       +space+dislocParams.getOriginLon()
		       +space+dislocParams.getObservationPointStyle()
		       +space+ObsvPoints.length);
	}
	
	else {
	    System.out.println("Malformed disloc problem");
	    throw new Exception();
	}
	
	
	//Print the observation point information
	if(dislocParams.getObservationPointStyle()==DislocConstants.GRID_OBSERVATION_STYLE) {
	    printGridObservationSites(pw, dislocParams);
	}
	else if(dislocParams.getObservationPointStyle()==DislocConstants.SCATTER_OBSERVATION_STYLE) {
	    printScatterObservationSites(pw,dislocParams,ObsvPoints);
	}
	
	//Now iterate over the faults.
	printFaultParams(pw,faults);
	pw.close();
	
    }
    
    protected String[] setUpArgs(String workDir,
				 String projectName,
				 String targetName) {
	
		  String[] args=new String[6];
		  args[0]="-Dbindir.prop="+binDir;
		  args[1]="-DworkDir.prop="+workDir;
		  args[2]="-DprojectName.prop="+projectName;
		  args[3]="-buildfile";
		  args[4]=buildFilePath;
		  args[5]=targetName;
		  
		  return args;
	 }

	 /**
	  * Create the name of the work directory
	  */
	 protected String generateWorkDir(String userName,
					  String projectName,
					  String timeStamp) {


	     String workDir=baseWorkDir+File.separator
		 +userName+File.separator
		 +projectName+File.separator+timeStamp;
	     
	     return workDir;
	 }
    
    /**
	  * Generate a ticket.  This can be used to 
	  * make "gentle" status queries later.
	  */
	 protected String generateJobStamp(){
		  return (new UID().toString());
	 }

    protected String generateBaseUrl(String userName,
												 String projectName,
												 String timeStamp) {
		  
		  //Need to be careful here because this must follow
		  //the workDir convention also.
		  String baseUrl=serverUrl+"/"+userName+"/"
				+projectName+"/"+"/"+timeStamp;
		  
		  return baseUrl;
    }
    
    protected DislocResultsBean createDislocResultsBean(String userName,
																		  String projectName,
																		  String jobUIDStamp) {
		  DislocResultsBean drb=new DislocResultsBean();
		  
		  String baseUrl=generateBaseUrl(userName,projectName,jobUIDStamp);
		  
		  drb.setJobUIDStamp(jobUIDStamp);
		  drb.setProjectName(projectName);
		  drb.setInputFileUrl(baseUrl+"/"+projectName+".input");
		  drb.setOutputFileUrl(baseUrl+"/"+projectName+".output");
		  drb.setStdoutUrl(baseUrl+"/"+projectName+".stdout");
		  
		  return drb;
    }
    
	 protected void printGridObservationSites(PrintWriter pw, 
															DislocParamsBean dislocParams) 
	     throws Exception {
	     
	     pw.println(dislocParams.getGridMinXValue()
						 +space+dislocParams.getGridXSpacing()
						 +space+dislocParams.getGridXIterations()
						 +space+dislocParams.getGridMinYValue()
						 +space+dislocParams.getGridYSpacing()
						 +space+dislocParams.getGridYIterations());
	     
	 }

    protected void printScatterObservationSites(PrintWriter pw, 
						DislocParamsBean dislocParams,
						ObsvPoint[] ObsvPoints)  
		  throws Exception {

		  for(int i=0;i<ObsvPoints.length;i++) {
				pw.println(ObsvPoints[i].getXPoint()
					   +" "+ObsvPoints[i].getYPoint());
		  }
    }
    
    private void makeWorkDir(String workDir) 
		  throws Exception {
		  
		  System.out.println("Working Directory is "+workDir);
		  
		  String[] args0=new String[4];
        args0[0]="-DworkDir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=buildFilePath;
        args0[3]="MakeWorkDir";
		  
        setArgs(args0);
        run();
    }  
	
	 protected void printFaultParams(PrintWriter pw, Fault[] faults)
		  throws Exception {
		  
		  for(int i=0;i<faults.length;i++) {
				pw.println(faults[i].getFaultLocationX()
							  +space+faults[i].getFaultLocationY()
							  +space+faults[i].getFaultStrikeAngle());

				pw.println(DislocConstants.FAULT_LINE_PREFIX
							  +space+faults[i].getFaultDepth()
							  +space+faults[i].getFaultDipAngle()
							  +space+faults[i].getFaultLameLambda()
							  +space+faults[i].getFaultLameMu()
							  +space+faults[i].getFaultStrikeSlip()
							  +space+faults[i].getFaultDipSlip()
							  +space+faults[i].getFaultTensileSlip()
							  +space+faults[i].getFaultLength()
							  +space+faults[i].getFaultWidth());

		  }
	 }
}  

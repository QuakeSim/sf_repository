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
//import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Needed for some number formatting.
import java.text.*;

//Needed for a unique id
import java.rmi.server.UID;

import org.apache.axis.*;

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
	
    public void testCall(ObsvPoint[] testBean) {
	for(int i=0;i<testBean.length;i++){
	    System.out.println(testBean[i].getLatPoint());
	    System.out.println(testBean[i].getLonPoint());
	    System.out.println(testBean[i].getXcartPoint());
	    System.out.println(testBean[i].getYcartPoint());
	}
    }

    public DislocResultsBean runNonBlockingDislocExt(String userName,
						     String projectName,
						     ObsvPoint[] obsvPoints,
						     Fault[] faults,
						     DislocParamsBean dislocParams,
						     String targetName) 
	throws Exception {
	System.out.println("RunNonBlocking called");
	if(targetName==null) targetName=DislocConstants.DISLOC_DEFAULT_TARGET;
	String jobStamp=generateJobStamp();
	
	String[] args=prefabDisloc(userName,
				   projectName,
				   dislocParams,
				   faults,
				   obsvPoints,
				   targetName,
				   jobStamp);
	setArgs(args);
	execute();
	return createDislocResultsBean(userName,projectName,jobStamp);
    }
    
    public DislocResultsBean runBlockingDislocExt(String userName,
						  String projectName,
						  ObsvPoint[] obsvPoints,
						  Fault[] faults,
						  DislocParamsBean dislocParams,
						  String targetName) 
	throws Exception {
	
	// try {
	//     System.out.println(AxisEngine.getCurrentMessageContext().getRequestMessage().getSOAPPartAsString());
	// } catch (AxisFault e) {
	//     e.printStackTrace();
	// }
	
	
	if(targetName==null) targetName=DislocConstants.DISLOC_DEFAULT_TARGET;
	String jobStamp=generateJobStamp();
	String[] args=prefabDisloc(userName,
				   projectName,
				   dislocParams,
				   faults,
				   obsvPoints,
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
				    ObsvPoint[] obsvPoints,
				    String targetName,
				    String jobStamp) 
	throws Exception {
	

	workDir=generateWorkDir(userName,projectName,jobStamp);
	makeWorkDir(workDir);
	createDislocInputFile(userName,
			      projectName,
			      dislocParams,
			      faults,
			      obsvPoints);

	String[] args=setUpArgs(workDir,
				projectName,
				targetName);
	
	return args;
    }
    
    protected void createDislocInputFile(String userName,
					 String projectName,
					 DislocParamsBean dislocParams,
					 Fault[] faults,
					 ObsvPoint[] obsvPoints) 
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
		       +space+obsvPoints.length);
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
	    printScatterObservationSites(pw,dislocParams,obsvPoints);
	}
	
	//Now iterate over the faults.
	printFaultParams(pw,faults);
	pw.close();
	
    }
    
    protected void printScatterObservationSites(PrintWriter pw, 
						DislocParamsBean dislocParams,
						ObsvPoint[] obsvPoints)  
		  throws Exception {

		  for(int i=0;i<obsvPoints.length;i++) {
				pw.println(obsvPoints[i].getXcartPoint()
					   +" "+obsvPoints[i].getYcartPoint());
		  }
    }
    
}  

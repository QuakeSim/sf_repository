package cgl.webservices.insarkml;

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

//Needed for some number formatting.
import java.text.*;

//Needed for a unique id
import java.rmi.server.UID;

/**
 * A simple wrapper for Ant.
 */

public class InsarKmlService extends AntVisco implements Runnable {  

    final String FILE_PROTOCOL="file";
    final String HTTP_PROTOCOL="http";

    Properties properties;
	 String serverUrl;
	 String baseWorkDir;
	 String projectName;
	 String binDir;
	 String buildFilePath;
	 String antTarget;
	 String workDir;

	 //Usefull variable;
	 final String space=" ";

	 public InsarKmlService() throws Exception {
		  this(false);
	 }

	 public static void main(String[] args) {
		  try{
				InsarKmlService iks=new InsarKmlService(true);
				iks.runBlockingInsarKml("mpierce",
												"junk",
												"http://156.56.104.99:8080//dislocexec/mpierce/TestKML//-69a88da0:127fe548b62:-8000/TestKML.output",
												"60",
												"0",
												"1.26",
												"http://localhost:8080/insarkkmlservice",
												"ExecInsarKml");
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
	 }
	 public InsarKmlService(boolean useClassLoader) throws Exception {
		  super();
		  
		  if(useClassLoader) {
				System.out.println("Using classloader");
				//This is useful for command line clients but does not work
				//inside Tomcat.
				ClassLoader loader=ClassLoader.getSystemClassLoader();
				properties=new Properties();
				
				//This works if you are using the classloader but not inside
				//Tomcat.
				properties.load(loader.getResourceAsStream("insarkml.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/insark.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }

		  serverUrl=properties.getProperty("insarkml.service.url");
		  baseWorkDir=properties.getProperty("base.workdir");
		  projectName=properties.getProperty("project.name");
		  binDir=properties.getProperty("bin.path");
		  buildFilePath=properties.getProperty("build.file.path");
		  antTarget=properties.getProperty("ant.target");
	 }
	 
	 /**
	  * Returns a URL to the generated KML file.
	  */
	 public String runNonBlockingInsarKml(String userName,
													  String projectName,
													  String dislocOutputUrl,
													  String elevation,
													  String azimuth,
													  String radarFrequency,
													  String imageBaseUrl,
													  String targetName)
		  throws Exception {
		  System.out.println("RunNonBlocking called");
		  String jobStamp=generateJobStamp();
		  
		  String[] args=prefabInsarKml(userName,
												 projectName,
												 dislocOutputUrl,
												 elevation,
												 azimuth,
												 radarFrequency,
												 imageBaseUrl,
												 targetName,
												 jobStamp);
		  setArgs(args);
		  execute();
		  //This is fragile as the output file specified by a URL
		  //that presumably matches its projectName here.
		  return imageBaseUrl+"/"+projectName+".output"+".kml";
	 }
	 
	 public String runBlockingInsarKml(String userName,
												  String projectName,
												  String dislocOutputUrl,
												  String elevation,
												  String azimuth,
												  String radarFrequency,
												  String imageBaseUrl,
												  String targetName)
		  throws Exception {
		  String jobStamp=generateJobStamp();
		  String[] args=prefabInsarKml(userName,
												 projectName,
												 dislocOutputUrl,
												 elevation,
												 azimuth,
												 radarFrequency,
												 imageBaseUrl,
												 targetName,
												 jobStamp);
		  setArgs(args);
		  run();
		  return imageBaseUrl+"/"+projectName+".output"+".kml";
	 }
	 
	 protected String[] prefabInsarKml(String userName,
												  String projectName,
												  String dislocOutputUrl,
												  String elevation,
												  String azimuth,
												  String radarFrequency,
												  String outputImageUrl,
												  String targetName,
												  String jobStamp) 
		  throws Exception {
		  workDir=generateWorkDir(userName,projectName,jobStamp);
		  makeWorkDir(workDir);
		  String destFile=workDir+File.separator+projectName+".output";
		  downloadDislocOutputFile(dislocOutputUrl, destFile);
		  
		  String[] args=setUpArgs(workDir,
										  projectName,
										  elevation,
										  azimuth,
										  radarFrequency,
										  outputImageUrl,
										  targetName);
		  
		  return args;
	 }

	 protected void downloadDislocOutputFile(String dislocOutputUrl,
														  String destFile) 
		  throws Exception{
		  
		  URL inputFileUrl=new URL(dislocOutputUrl);
		  
		  String protocol=inputFileUrl.getProtocol();
		  System.out.println("Protocol: "+protocol);
		  String fileSimpleName=(new File(destFile)).getName();
		  System.out.println(fileSimpleName);
		  		  
		  
		  if(protocol.equals(HTTP_PROTOCOL)) {
				copyUrlToFile(inputFileUrl,destFile);
		  }
		  else {
				System.out.println("Unknown protocol for accessing inputfile");
				throw new Exception("Unknown protocol");
		  }
		  
	 }

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

	 protected String[] setUpArgs(String workDir,
											String projectName,
											String elevation,
											String azimuth,
											String radarFrequency,
											String outputImageUrl,
											String targetName) {

		  String[] args=new String[10];
		  args[0]="-Dbindir.prop="+binDir;
		  args[1]="-DworkDir.prop="+workDir;
		  args[2]="-DprojectName.prop="+projectName;
		  args[3]="-Delevation.prop="+elevation;
		  args[4]="-Dazimuth.prop="+azimuth;
		  args[5]="-DradarFrequency.prop="+radarFrequency;
		  args[6]="-DoutputImageUrl.prop="+outputImageUrl;
		  args[7]="-buildfile";
		  args[8]=buildFilePath;
		  args[9]=targetName;
		  
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
    
    protected void makeWorkDir(String workDir) 
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

}  

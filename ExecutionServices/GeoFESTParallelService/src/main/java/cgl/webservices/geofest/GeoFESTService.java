package cgl.webservices.geofest;

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

//Import Google GData
import com.google.gdata.client.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.media.mediarss.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;

import com.google.gdata.client.*;
import com.google.gdata.data.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.URL;

//Google Calendar stuff
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.acl.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;


/**
 * A simple wrapper for Ant.
 */

public class GeoFESTService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(GeoFESTService.class);

    final String FILE_PROTOCOL="file";
    final String HTTP_PROTOCOL="http";

    //These are the system properties that may have
    //default values.
    Properties properties;
    String serverUrl;
    String baseWorkDir;
    String baseDestDir;
    String baseOutputDestDir;
    String projectName;
    String binDir;
    String buildFilePath;
    String antTarget;
    String queueServiceUrl;

    //Some useful values
    String comma=",";
    String quote="\"";

	 //These are google service-related things.
	 String clientId;
	 String developerKey;
	 GoogleService googleService;
	 String gServiceUserName;
	 String gServiceUserPass;
	 String googleBlogId;
	 CalendarService calendarService;

    /**
     * This is a main() for testing.
     */
    public static void main(String[] args) {
		  //Create fault.
		  Fault[] faults=new Fault[1];
		  faults[0]=new Fault();
		  
		  //Create layer.
		  Layer[] layers=new Layer[1];
		  layers[0]=new Layer();
		  
		  //Create geotrans params
		  GeotransParamsBean gpb=new GeotransParamsBean();
		  
		  String userName="duhFaultUser";
		  String projectName="faultsatmyfeet";
		  
		  try {
				//Make the mesh.
				GeoFESTService gfs=new GeoFESTService(true);
				
				//This will actually return before the job is 
				//finished, so we'll use ticket2 in later calculations.
				// 				System.out.println("Running non-blocking version");
				//  				String ticket1=gfs.runNonBlockingMeshGenerator(userName,
				// 																			  projectName,
				// 																			  faults,
				// 																			  layers,
				// 																			  "rare");
				
				System.out.println("Running blocking version");
				MeshRunBean mrb=gfs.runBlockingMeshGenerator(userName,
																			projectName,
																			faults,
																			layers,
																			"rare");
				
				// 				System.out.println("Packing input files");
				// 				gfs.runPackageGeoFESTFiles(userName,projectName,gpb,ticket2);
				
				System.out.println("Running GeoFEST");
				gfs.runGeoFEST(userName,projectName,gpb,mrb.getJobUIDStamp());
				
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
    }
    
    /**
     * The constructor. Set useClassLoader=true when running
     * on the command line.
     */
    public GeoFESTService(boolean useClassLoader) 
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
				properties.load(loader.getResourceAsStream("geofestconfig.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/geofestconfig.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }
		  
		  //Note these will be "global" for this class, so
		  //I will not explicitly pass them around.
		  serverUrl=properties.getProperty("geofest.service.url");
		  baseWorkDir=properties.getProperty("base.workdir");
		  baseDestDir=properties.getProperty("base.dest.dir");
		  projectName=properties.getProperty("project.name");
		  binDir=properties.getProperty("bin.path");
		  buildFilePath=properties.getProperty("build.file.path");
		  antTarget=properties.getProperty("ant.target");
		  baseOutputDestDir=properties.getProperty("output.dest.dir");
		  queueServiceUrl=properties.getProperty("queue.service.url");
		  clientId=properties.getProperty("google.client.id");
		  developerKey=properties.getProperty("google.developer.key");
		  gServiceUserName=properties.getProperty("google.user.name");
		  gServiceUserPass=properties.getProperty("google.user.pass");
		  googleBlogId=properties.getProperty("google.blog.id");

		  //Instantiate the Google Service
 		  // instantiateGoogleService("blogger",gServiceUserName,gServiceUserPass);
		  // instantiateCalendarService();
    }
    
    public GeoFESTService() throws Exception{
		  this(false);
    }
	 
    
    /**
     * Does all the generic parts for setting up the 
     * mesh generator run.  
     * 
     * Returns the time stamp, which is needed for
     * later querying.	  *
     */ 
    protected String prefabMeshGenerator(String userName,
													  String projectName,
													  Fault[] faults,
													  Layer[] layers,
													  String autoref_mode) 
		  throws Exception {
		  
		  String timeStamp=generateTimeStamp();
		  
		  String workDir=generateWorkDir(userName,projectName,timeStamp);
		  String outputDestDir=generateOutputDestDir(userName,
																	projectName,
																	timeStamp);
		  
		  createGeometryFiles(workDir,projectName,faults,layers);
		  String[] args=setUpMeshArgs(workDir,
												projectName,
												autoref_mode,
												outputDestDir,
												timeStamp,
												queueServiceUrl);
		  //Methods from parent
		  setArgs(args);
		  
		  return timeStamp;
    }
    
    /**
     *
     */
    protected String generateOutputDestDir(String userName,
														 String projectName,
														 String timeStamp) {
		  
		  String outputDestDir=baseOutputDestDir+File.separator
				+userName+File.separator
				+projectName+File.separator+timeStamp;
		  
		  return outputDestDir;
		  
    }
    
    /**
     *
     */
    protected String generateWorkDir(String userName,
												 String projectName,
												 String timeStamp) {
		  
		  String workDir=baseWorkDir+File.separator
				+userName+File.separator
				+projectName+File.separator+timeStamp+File.separator;
		  
		  return workDir;
		  
    }
    
    
	 
    /**
     * This runs the mesh generator code in blocking mode,
     * i.e., it does not return until the mesh is done.
     * 
     * Returns the time stamp, which is needed for
     * later querying.
     */
    public MeshRunBean runBlockingMeshGenerator(String userName,
																String projectName,
																Fault[] faults,
																Layer[] layers,
																String autoref_mode) 
		  throws Exception {
		  String timeStamp=prefabMeshGenerator(userName,
															projectName,
															faults,
															layers,
															autoref_mode);
		  run();
		  return getTheMeshGenReturnFiles(userName,projectName,timeStamp);
    }
    
    /**
     * Runs the meshgenerator in non-blocking mode, which
     * is necessary for large meshes. 
     * 
     * Returns a string array.  String[0] is a timestamp,
     * which is needed for later querying.  The rest of
     * the array consists of URLs for the project
     * 
     */
    public MeshRunBean runNonBlockingMeshGenerator(String userName,
																	String projectName,
																	Fault[] faults,
																	Layer[] layers,
																	String autoref_mode) 
		  throws Exception{
		  String timeStamp=prefabMeshGenerator(userName,
															projectName,
															faults,
															layers,
															autoref_mode);
		  execute();
		  return getTheMeshGenReturnFiles(userName,projectName,timeStamp);
    }
    
    
    protected MeshRunBean getTheMeshGenReturnFiles(String userName,
																	String projectName,
																	String jobUIDStamp) {
		  String baseUrl=generateBaseUrl(userName,projectName,jobUIDStamp);
		  
		  MeshRunBean mrb=new MeshRunBean();
		  mrb.setJobUIDStamp(jobUIDStamp);
		  mrb.setProjectName(projectName);
		  mrb.setAutoref(baseUrl+"/"+"autoref.out");
		  mrb.setAutorefError(baseUrl+"/"+"autoref.error");
		  mrb.setNodeUrl(baseUrl+"/"+projectName+".node");
		  mrb.setTetraUrl(baseUrl+"/"+projectName+".tetra");
		  mrb.setBcUrl(baseUrl+"/"+projectName+".bcmap");
		  mrb.setIndexUrl(baseUrl+"/"+projectName+".index");
		  mrb.setJunkBox(baseUrl+"/"+"junk.box");
		  mrb.setTstout(baseUrl+"/"+"tstout");
		  mrb.setLeeRefinerLog(baseUrl+"/"+"LeeRefiner.log");
		  mrb.setRefinerLog(baseUrl+"/"+"refiner.log");
		  mrb.setTagbigfltLog(baseUrl+"/"+"tagbigflt.log");
		  mrb.setViscoTarUrl(baseUrl+"/"+projectName+"-visco.tar.gz");
		  
		  return mrb;
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
    
	 /**
	  * Checks the running Mesh Generator service.
	  * Useful in non-blocking execution.
	  */
	 public void queryMeshGeneratorStatus() 
		  throws Exception {
	 }
	 
	 
	 /**
	  * This method is used to tar up the mesh and input files.
	  * This always runs in blocking mode.
	  * 
	  * It requires that you send it a time stamp, and thus
	  * it effectively requires that you have run the 
	  * mesh generator steps separately.
	  *
	  * Returns a string array.  String[0] is the time stamp.
	  * String[1] is the URL of the tar.gz of all files.
	  */
	 public String[] runPackageGeoFESTFiles(String userName,
														 String projectName,
														 GeotransParamsBean gpb,
														 String timeStamp)
		  throws Exception {

		  //The target is always "geotrans.tar".
		  String[] args=prefabGeoFESTCall(userName,
													 projectName,
													 gpb,
													 timeStamp,
													 "geotrans.tar");
		  
		  setArgs(args);
		  run();
		  return getTheGeoFESTInputFiles(userName,projectName,timeStamp);
	 }
	 
	 protected String[] prefabGeoFESTCall(String userName,
													  String projectName,
													  GeotransParamsBean gpb,
													  String timeStamp,
													  String targetName) 
		  throws Exception {
		  
		  String workDir=generateWorkDir(userName,projectName,timeStamp);

		  createGeoFESTInputFile(workDir,projectName,gpb);
		  String outputDestDir=generateOutputDestDir(userName,
																	projectName,
																	timeStamp);
		  String[] args=setUpGeoFESTArgs(workDir,
													projectName,
													targetName,
													outputDestDir,
													timeStamp,
													queueServiceUrl);
		  return args;
	 }

    /**
     * Actually runs GeoFEST.  Always runs in non-blocking mode.
     *
     * Returns the timestamp as String[0].  The rest are URLs
     * of various output files.  String[1] is everything in a
     * tar.gz.  String[2] is the GeoFEST input file.  String[3]
     * is the GeoFEST output file.  String [4] is the standard output
     * of geofest.  String [5] is the GeoFEST log file.
     */
    public GFOutputBean runGeoFEST(String userName,
				   String projectName,
				   GeotransParamsBean gpb,
				   String timeStamp)
		  throws Exception {
		  
		  //The target is always "tar.all".
		  String[] args=
				prefabGeoFESTCall(userName,projectName,gpb,timeStamp,"tar.all");
		  setArgs(args);
		  execute();
		  return getAllTheGeoFESTFiles(userName,projectName,timeStamp);
    }
    
    /**
     * Checks the status of a running GeoFEST job.
     */
    public String queryGeoFESTStatus() 
		  throws Exception {
		  return " ";
    }
    
    /**
     * 
     */
    protected String[] getTheGeoFESTInputFiles(String userName,
					       String projectName,
					       String timeStamp) {
	String baseUrl=generateBaseUrl(userName,projectName,timeStamp);
	String[] gfUrls=new String[2];
	gfUrls[0]=timeStamp;
	gfUrls[1]=baseUrl+"/"+projectName+".tar.gz";
	
	return gfUrls;
    }
    
    protected GFOutputBean getAllTheGeoFESTFiles(String userName,
																 String projectName,
																 String jobUIDStamp) {
		  
		  GFOutputBean gfoutput=new GFOutputBean();
		  String baseUrl=generateBaseUrl(userName,projectName,jobUIDStamp);
		  
		  gfoutput.setJobUIDStamp(jobUIDStamp);
		  gfoutput.setTarOfEverythingUrl(baseUrl+"/"+projectName+".tar.gz");
		  gfoutput.setInputUrl(baseUrl+"/"+projectName+".inp");
		  gfoutput.setOutputUrl(baseUrl+"/"+projectName+".out");
		  gfoutput.setLogUrl(baseUrl+"/"+projectName+".log");
		  gfoutput.setIndexUrl(baseUrl+"/"+projectName+".index");
		  gfoutput.setNodeUrl(baseUrl+"/"+projectName+".node");
		  gfoutput.setTetraUrl(baseUrl+"/"+projectName+".tetra");
		  gfoutput.setTetvolsUrl(baseUrl+"/"+projectName+".tetvols");
		  gfoutput.setToptrisUrl(baseUrl+"/"+projectName+".toptris");
		  gfoutput.setCghistUrl(baseUrl+"/"+"cghist.txt");
		  gfoutput.setJobStatusUrl(baseUrl+"/"+"jobstatus.log");
		  
		  System.out.println(gfoutput.getJobUIDStamp());
		  System.out.println(gfoutput.getTarOfEverythingUrl());
		  System.out.println(gfoutput.getInputUrl());
		  System.out.println(gfoutput.getOutputUrl());
		  System.out.println(gfoutput.getLogUrl());
		  System.out.println(gfoutput.getIndexUrl());
		  System.out.println(gfoutput.getNodeUrl());
		  System.out.println(gfoutput.getTetraUrl());
		  System.out.println(gfoutput.getTetvolsUrl());
		  System.out.println(gfoutput.getToptrisUrl());
		  System.out.println(gfoutput.getCghistUrl());
		  System.out.println(gfoutput.getJobStatusUrl());
		  
		  return gfoutput;
	 }
	 
	 /**
	  * This method writes out all the input files
	  * that are needed for creating the mesh.
	  */
	 protected void createGeometryFiles(String workDir,
													String projectName,
													Fault[] faults,
													Layer[] layers) 
		  throws Exception {
		  
		  makeWorkDir(workDir);
		  writeGroupFile(workDir,projectName,faults,layers);
		  writeBCProjectFile(workDir,projectName,faults);
		  writeAllFaultParamFiles(workDir,faults,layers[0]);
		  writeAllMaterialsFiles(workDir,layers);
		  writeAllFaultOutputFiles(workDir,faults,layers[0]);
		  writeAllLayerOutputFiles(workDir,layers);
	 }

	 /**
	  * Spit out the geotrans parameters.
	  */
	 protected void createGeoFESTInputFile(String workDir,
														String projectName,
														GeotransParamsBean gpb) 
		  throws Exception {

		  // --------------------------------------------------
		  // Set up the file to write.
		  // --------------------------------------------------
		  String geotrans_file = workDir+File.separator+projectName+".std";
		  System.out.println("Creating Geofest input file:"+geotrans_file);

		  PrintWriter pw = new PrintWriter(new FileWriter(geotrans_file), true);
		  
		  // --------------------------------------------------
		  // Now write out all the geotrans params to a file.
		  // --------------------------------------------------
		  String output_file = gpb.getOutputFileName();
		  String GFInput = gpb.getInputFileName();
		  
		  String br = "";
		  String space = " ";
		  
		  pw.println("output_filename" + space + gpb.getOutputFileName()+br);
		  pw.println("number_space_dimensions" + space
						 +gpb.getNumber_space_dimensions()+br);
		  
		  pw.println("number_degrees_freedom" + space
						 +gpb.getNumber_degrees_freedom()+br);
		  
		  pw.println("nrates" + space + gpb.getNrates()+br);
		  
		  pw.println("shape_flag" + space + gpb.getShape_flag()+br);
		  
		  pw.println("solver_flag" + space + gpb.getSolver_flag()+br);
		  
		  pw.println("number_time_groups" 
						 + space + gpb.getNumber_time_groups()+br);
		  
		  pw.println("reform_steps" + space + gpb.getReform_steps() +br);
		  
		  pw.println("backup_steps" + space + gpb.getBackup_steps()+br);
		  
		  
		  pw.println("fault_interval" + space + gpb.getFault_interval()+br);
		  
		  pw.println("end_time" + space + gpb.getEnd_time()+br);
		  
		  pw.println("alpha" + space + gpb.getAlpha()+br);
		  
		  pw.println("time_step" + space + gpb.getTime_step()+br);
		  
		  pw.println("top_bc" + space + gpb.getTop_bc() 
						 + space+ gpb.getTop_bc_value()+br);
		  
		  pw.println("east_bc" + space + gpb.getEast_bc() 
						 + space+ gpb.getEast_bc_value()+br);
		  
		  pw.println("west_bc" + space + gpb.getWest_bc() 
						 + space+ gpb.getWest_bc_value()+br);
		  
		  pw.println("north_bc" + space + gpb.getNorth_bc() 
						 + space+ gpb.getNorth_bc_value()+br);
		  
		  pw.println("south_bc" + space + gpb.getSouth_bc() 
						 + space+ gpb.getSouth_bc_value()+br);
		  
		  pw.println("bottom_bc" + space + gpb.getBottom_bc() 
						 + space+ gpb.getBottom_bc_value()+br);
		  
		  pw.println("reporting_nodes" + space + gpb.getReportingNodes()+br);
		  
		  pw.println("reporting_elements" 
						 + space + gpb.getReportingElements()+br);
		  
		  pw.println("print_times_type" + space +gpb.getPrintTimesType()+br);
		  
		  pw.println("start_from_file" + space + gpb.getRestartFile()+br);
		  
		  pw.println("checkpoint_file" + space + gpb.getCheckpointFile()+br);
		  
		  // Finally, handle the number_print_times variable, which
		  
		  if (gpb.getPrintTimesType().equalsIgnoreCase("steps")) {
				pw.println("number_print_times" + space
							  + gpb.getNumberofPrintTimes());
				pw.println("print_interval" + space
							  + gpb.getPrintTimesInterval());
				
		  } 
		  else if (gpb.getPrintTimesType().equalsIgnoreCase("list")) {
				String print_time_vals = gpb.getPrintTimesInterval();
				double dptv = Double.parseDouble(print_time_vals);
				double maxSteps = Double
					 .parseDouble(gpb.getEnd_time());
				int icount = 0;
				pw.print("print_times" + " ");
				while (icount * dptv < maxSteps) {
					 icount++;
					 pw.print(icount * dptv + " ");
				}
				pw.println("");
				pw.println("number_print_times" + " " + (icount - 1));
		  }
		  pw.flush();
		  pw.close();
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
     * multiple valued entries. 
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
	 
    private void makeWorkDir(String workDir) 
		  throws Exception {
				
		  System.out.println("Working Directory is "+workDir);

		  String[] args0=new String[4];
        args0[0]="-Dworkdir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=buildFilePath;
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
	 
    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then GeoFEST itself 
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
	  * Generate a ticket.  This can be used to 
	  * make "gentle" status queries later.
	  */
	 protected String generateTimeStamp(){
		  //String stringDate=(new Date().getTime())+"";
		  //String stringDate="NOW";
		  //short s=1;
		  //		  String stringDate=(new UID().toString());
		  String stringDate=new String((new UID().hashCode()));
		  return stringDate;
	 }

	 /**
	  * Write the group file
	  */
	 public void writeGroupFile(String projectDir,
										 String projectName,
										 Fault[] faults,
										 Layer[] layers) throws Exception {
		  
		  String outfile=projectDir+File.separator+projectName+".grp";

		  PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		  System.out.println(outfile);
		  
		  //Write first line
		  int length = faults.length + layers.length;
		  pw.println("1.0 " + length);
		  
		  for (int i = 0; i < layers.length; i++) {
				pw.println(layers[i].getLayerName() + ".sld");
		  }
		  
		  for (int i = 0; i < faults.length; i++) {
				pw.println(faults[i].getFaultName() + ".flt");
		  }
		  pw.flush();
		  pw.close();
	 }

	/**
	 * Write/update the materials file for a specific layer.
	 */
	 public void writeAllMaterialsFiles(String projectDir,
													Layer[] layers) 
		  throws Exception {

		  for(int i=0;i<layers.length;i++){
				writeMaterialsFile(projectDir,layers[i]);
		  }
	 }

	 public void writeMaterialsFile(String projectDir,
											  Layer layer)
		  throws Exception {
		  
		  String SPC=" ";
		  //Set up the printwriter
		  String outfile=projectDir
				+File.separator
				+layer.getLayerName()
				+".materials";
		  
		  PrintWriter pw = new PrintWriter(new FileWriter(outfile, false));
		  
		  pw.println("lamelambda" + SPC+layer.getLameLambda());
		  pw.println("lamemu" + SPC+layer.getLameMu());
		  pw.println("viscosity" +SPC+layer.getViscosity());
		  pw.println("exponent"+SPC+layer.getExponent());

		  pw.flush();
		  pw.close();
		  
	 }

	 /**
	  * Write out the fault parameters to a file named after the
	  * fault (ie Northridge2.params).
	  *
	  * NOTE: The "number" parameter I think is a constant set to 1.0.
	  * That is, it is obsolete/unimportant.
	  */ 

	 public void writeAllFaultParamFiles(String projectDir,
													 Fault[] faults,
													 Layer layer) 
		  throws Exception {
		  
		  for(int i=0;i<faults.length;i++){
				writeFaultParamFile(projectDir,
										  faults[i],
										  layer,
										  i);
		  }
	 }

	 public void writeFaultParamFile(String projectDir,
												Fault fault,
												Layer layer,
												int faultInt)
		  throws Exception {
		  
		  String outputFile=projectDir
				+ File.separator
				+ fault.getFaultName()
				+ ".params";

		  String tab = "\t";
		  String headerline = "number" + tab + "dip(o)" + tab + "strike(o)" + tab
				+ "slip(m)" + tab + "rake(o)" + tab + "length(km)" + tab
				+ "width(km)" + tab + "depth(km)" 
				+ tab + "origX" +tab + "origY"
				+ tab +"FirstEvent" + tab + "RepeatTime";
		  
		  String number="1.0";  //Note this is a legacy parameter.
		  String dip = fault.getFaultDipAngle();
		  String strike = fault.getFaultStrikeAngle();

		  String slip = fault.getFaultSlip();

		  String rake = fault.getFaultRakeAngle();

		  String length = fault.getFaultLength();
		  String width = fault.getFaultWidth();
		  String depth = fault.getFaultDepth();
		  String orig_x = fault.getFaultLocationX();
		  String orig_y = fault.getFaultLocationY();
		  String firstEvent=fault.getFirstEvent();
		  String repeatTime=fault.getRepeatTime();

		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
		  pw.println(headerline);
		  pw.print(number + tab + dip + tab + strike + tab + slip + tab + rake
					  + tab + length + tab + width + tab + depth + tab + orig_x 
					  + tab + orig_y + tab + firstEvent + tab + repeatTime);
		  pw.flush();
		  pw.close();
	 }


	 /**
	  * This writes the BC file required by the meshing codes.
	  */
	 public void writeBCProjectFile(String projectDir,
											  String projectName,
											  Fault[] faults) throws Exception {
		  
		  String EXT=".bc";
		  String SPC=" ";
		  String outputFile=projectDir+File.separator+projectName+EXT;
		  System.out.println("Project file:"+projectName+EXT);
		  System.out.println("Project Dir:"+projectDir);
		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
		  pw.println(faults.length+1);
		  pw.println("0"+SPC+"none");
		  for (int i=0;i<faults.length;i++) {
				int index=i+1;
				pw.println(index+SPC+faults[i].getFaultName());
		  }
		  pw.flush();
		  pw.close();
	 }
	 


	/**
	 * Write out the fault to the context using guiVisco/geoFEST format.
	 * This calculates the fault origin in cartesian coordinates
	 * relative to the origin of the layer being used.
	 */
	 public void writeAllFaultOutputFiles(String projectDir,
													  Fault[] faults,
													  Layer layer) 
		  throws Exception {
		  
		  for(int i=0;i<faults.length;i++) {
				writeFaultOutputFile(projectDir,
											faults[i],
											layer,
											i);
				
				writeFaultSLDOutputFile(projectDir,
												faults[i],
												layer,
												i);
		  }
	 }													  

	 public void writeFaultOutputFile(String projectDir,
												 Fault fault,
												 Layer layer,
												 int faultInt)
		  throws Exception {
		  
		  String SPC=" ";
		  String EXT = ".flt";
		  String outputFile=projectDir+File.separator+fault.getFaultName()+EXT;
		  
		  //		  double locX, locY;
		  
		  //Do this for backward compatibility.  You will
		  //get a number format exception for old style
		  //fault contexts.
		  //Get out the lat and lon from the context.
		  double latstart = Double.parseDouble(fault.getFaultLatStart());
		  double lonstart = Double.parseDouble(fault.getFaultLonStart());
		  double latend = Double.parseDouble(fault.getFaultLatEnd());
		  double lonend = Double.parseDouble(fault.getFaultLonEnd());
		  
		  //Get the layer origin's lat and lon
		  double layerLatOrigin = Double.parseDouble(layer.getLayerLatOrigin());
		  double layerLonOrigin = Double.parseDouble(layer.getLayerLonOrigin());
		  
		  //Calculate the fault start in cartesian coordinates relative to 
		  //the layer origin.  Layer origin cart coordinates are (0,0,0) 
		  //of course.
		  //Calculate the length
		  NumberFormat format = NumberFormat.getInstance();
		  double d2r = Math.acos(-1.0) / 180.0;
		  double factor = d2r
				* Math.cos(d2r * layerLatOrigin)
				* (6378.139 * (1.0 - Math.sin(d2r * layerLatOrigin)
									* Math.sin(d2r * layerLatOrigin) / 298.247));
		  
		  //These are the (x,y) for the fault's start.
// 		  locX = (lonstart - layerLonOrigin) * factor;
// 		  locY = (latstart - layerLatOrigin) * 111.32;

		  double locX=Double.parseDouble(fault.getFaultLocationX());
		  double locY=Double.parseDouble(fault.getFaultLocationY());
		  
		  //Get out the stuff directly stored in the context.
		  double locZ = Double.parseDouble(fault.getFaultLocationZ());
		  double length = Double.parseDouble(fault.getFaultLength());
		  double width = Double.parseDouble(fault.getFaultWidth());
		  double depth = Double.parseDouble(fault.getFaultDepth());
		  
		  double dip = Double.parseDouble(fault.getFaultDipAngle());
		  double strike = Double.parseDouble(fault.getFaultStrikeAngle());
		  
		  //Useful math.  See guiVisco documents for details.
		  double minus_depth = -depth;
		  double strike_deg = strike * Math.PI / 180.0;
		  double dip_deg = dip * Math.PI / 180.0;
		  
		  double P00 = locX;
		  double P01 = locY;
		  double P02 = minus_depth;
		  
		  double P10 = locX + length * Math.sin(strike_deg);
		  double P11 = locY + length * Math.cos(strike_deg);
		  double P12 = minus_depth;
		  
		  double P20 = locX + length * Math.sin(strike_deg) - width
				* Math.cos(strike_deg) * Math.cos(dip_deg);
		  double P21 = locY + length * Math.cos(strike_deg) + width
				* Math.sin(strike_deg) * Math.cos(dip_deg);
		  double P22 = minus_depth + width * Math.sin(dip_deg);
		  
		  double P30 = locX - width * Math.cos(strike_deg) * Math.cos(dip_deg);
		  double P31 = locY + width * Math.sin(strike_deg) * Math.cos(dip_deg);
		  double P32 = minus_depth + width * Math.sin(dip_deg);
		  
		  //Write it to file.
		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
		  pw.println("4");
		  pw.println(P00 + SPC + P01 + SPC + P02);
		  pw.println(P10 + SPC + P11 + SPC + P12);
		  pw.println(P20 + SPC + P21 + SPC + P22);
		  pw.println(P30 + SPC + P31 + SPC + P32);
		  
		  //Finally, write the following stuff
		  
		  pw.println("1");
		  pw.println(faultInt + " 1");
		  pw.println("\t 4 1 2 3 4");
		  
		  pw.flush();
		  pw.close();
	 }
	 
	 /**
	  * This is identical to the .flt files but needed for compatibility.
	  */
	 public void writeFaultSLDOutputFile(String projectDir,
												 Fault fault,
												 Layer layer,
												 int faultInt)
		  throws Exception {
		  
		  String SPC=" ";
		  String EXT = ".sld";
		  String outputFile=projectDir+File.separator+fault.getFaultName()+EXT;
		  
		  //		  double locX, locY;
		  
		  //Do this for backward compatibility.  You will
		  //get a number format exception for old style
		  //fault contexts.
		  //Get out the lat and lon from the context.
		  double latstart = Double.parseDouble(fault.getFaultLatStart());
		  double lonstart = Double.parseDouble(fault.getFaultLonStart());
		  double latend = Double.parseDouble(fault.getFaultLatEnd());
		  double lonend = Double.parseDouble(fault.getFaultLonEnd());
		  
		  //Get the layer origin's lat and lon
		  double layerLatOrigin = Double.parseDouble(layer.getLayerLatOrigin());
		  double layerLonOrigin = Double.parseDouble(layer.getLayerLonOrigin());
		  
		  //Calculate the fault start in cartesian coordinates relative to 
		  //the layer origin.  Layer origin cart coordinates are (0,0,0) 
		  //of course.
		  //Calculate the length
		  NumberFormat format = NumberFormat.getInstance();
		  double d2r = Math.acos(-1.0) / 180.0;
		  double factor = d2r
				* Math.cos(d2r * layerLatOrigin)
				* (6378.139 * (1.0 - Math.sin(d2r * layerLatOrigin)
									* Math.sin(d2r * layerLatOrigin) / 298.247));
		  
		  //These are the (x,y) for the fault's start.
// 		  locX = (lonstart - layerLonOrigin) * factor;
// 		  locY = (latstart - layerLatOrigin) * 111.32;

		  double locX=Double.parseDouble(fault.getFaultLocationX());
		  double locY=Double.parseDouble(fault.getFaultLocationY());
		  
		  //Get out the stuff directly stored in the context.
		  double locZ = Double.parseDouble(fault.getFaultLocationZ());
		  double length = Double.parseDouble(fault.getFaultLength());
		  double width = Double.parseDouble(fault.getFaultWidth());
		  double depth = Double.parseDouble(fault.getFaultDepth());
		  
		  double dip = Double.parseDouble(fault.getFaultDipAngle());
		  double strike = Double.parseDouble(fault.getFaultStrikeAngle());
		  
		  //Useful math.  See guiVisco documents for details.
		  double minus_depth = -depth;
		  double strike_deg = strike * Math.PI / 180.0;
		  double dip_deg = dip * Math.PI / 180.0;
		  
		  double P00 = locX;
		  double P01 = locY;
		  double P02 = minus_depth;
		  
		  double P10 = locX + length * Math.sin(strike_deg);
		  double P11 = locY + length * Math.cos(strike_deg);
		  double P12 = minus_depth;
		  
		  double P20 = locX + length * Math.sin(strike_deg) - width
				* Math.cos(strike_deg) * Math.cos(dip_deg);
		  double P21 = locY + length * Math.cos(strike_deg) + width
				* Math.sin(strike_deg) * Math.cos(dip_deg);
		  double P22 = minus_depth + width * Math.sin(dip_deg);
		  
		  double P30 = locX - width * Math.cos(strike_deg) * Math.cos(dip_deg);
		  double P31 = locY + width * Math.sin(strike_deg) * Math.cos(dip_deg);
		  double P32 = minus_depth + width * Math.sin(dip_deg);
		  
		  //Write it to file.
		  PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
		  pw.println("4");
		  pw.println(P00 + SPC + P01 + SPC + P02);
		  pw.println(P10 + SPC + P11 + SPC + P12);
		  pw.println(P20 + SPC + P21 + SPC + P22);
		  pw.println(P30 + SPC + P31 + SPC + P32);
		  
		  //Finally, write the following stuff
		  
		  pw.println("1");
		  pw.println(faultInt + " 1");
		  pw.println("\t 4 1 2 3 4");
		  
		  pw.flush();
		  pw.close();
	 }
	 
	 /**
	  *Formatting follows visco_layer.c from original code.
	  */
	 public void writeAllLayerOutputFiles(String projectDir,
													  Layer[] layers)
		  throws Exception {

		  for(int i=0;i<layers.length;i++) {
				writeLayerOutputFile(projectDir,layers[i]);
		  }
	 }

	 public void writeLayerOutputFile(String projectDir,
												 Layer layer)
		  throws Exception {
		  
		  String SPC=" ";
		  String EXT = ".sld";
		  String outfile=projectDir+File.separator+layer.getLayerName() + EXT;
		  
		  double originX = Double.parseDouble(layer.getLayerOriginX());
		  double originY = Double.parseDouble(layer.getLayerOriginY());
		  double originZ = Double.parseDouble(layer.getLayerOriginZ());
		  
		  double length = Double.parseDouble(layer.getLayerLength());
		  double width = Double.parseDouble(layer.getLayerWidth());
		  double depth = Double.parseDouble(layer.getLayerDepth());
		  
		  double P00 = originX;
		  double P01 = originY;
		  double P02 = originZ;
		  
		  double P10 = originX + length;
		  double P11 = originY;
		  double P12 = originZ;
		  
		  double P20 = originX + length;
		  double P21 = originY + width;
		  double P22 = originZ;
		  
		  double P30 = originX;
		  double P31 = originY + width;
		  double P32 = originZ;
		  
		  double P40 = originX;
		  double P41 = originY;
		  double P42 = originZ - depth;
		  
		  double P50 = originX + length;
		  double P51 = originY;
		  double P52 = originZ - depth;
		  
		  double P60 = originX + length;
		  double P61 = originY + width;
		  double P62 = originZ - depth;
		  
		  double P70 = originX;
		  double P71 = originY + width;
		  double P72 = originZ - depth;
		  
		  //Write it to file.
		  String TAB = "\t";
		  PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		  //Print the 8 points of the cube.
		  pw.println("8");
		  pw.println(P00 + SPC + P01 + SPC + P02);
		  pw.println(P10 + SPC + P11 + SPC + P12);
		  pw.println(P20 + SPC + P21 + SPC + P22);
		  pw.println(P30 + SPC + P31 + SPC + P32);
		  pw.println(P40 + SPC + P41 + SPC + P42);
		  pw.println(P50 + SPC + P51 + SPC + P52);
		  pw.println(P60 + SPC + P61 + SPC + P62);
		  pw.println(P70 + SPC + P71 + SPC + P72);
		  
		  //Now print this other mysterious stuff.
		  pw.println("6");
		  pw.println("0 1");
		  pw.println("\t 4 1 2 3 4");
		  pw.println("0 1");
		  pw.println("\t 4 1 2 6 5");
		  pw.println("0 1");
		  pw.println("\t 4 2 3 7 6");
		  pw.println("0 1");
		  pw.println("\t 4 3 4 8 7");
		  pw.println("0 1");
		  pw.println("\t 4 4 1 5 8");
		  pw.println("0 1");
		  pw.println("\t 4 5 6 7 8");
		  
		  pw.flush();
		  pw.close();
	 }
	 
	 /**
	  * Set up the arg array.  Note that the binDir and 
	  * buildFilePath variables are constants, so not
	  * explicitly passed in.
	  */
	 protected String[] setUpMeshArgs(String workDir,
												 String projectName,
												 String autoref_mode,
												 String outputDestDir,
												 String jobUID,
												 String queueServiceUrl) {
		  String[] args=new String[10];
		  args[0]="-Dbindir.prop="+binDir;
		  args[1]="-Dworkdir.prop="+workDir;
		  args[2]="-DprojectName.prop="+projectName;
		  args[3]="-Dmode.prop="+autoref_mode;
		  args[4]="-Doutputdest.prop="+outputDestDir;
		  args[5]="-DjobUID.prop="+jobUID;
		  args[6]="-DqueueServiceUrl.prop="+queueServiceUrl;
        args[7]="-buildfile";
        args[8]=buildFilePath;
        args[9]="autoref";
		  return args;
	 }

	 /**
	  * Set up the arg array.  Note that the binDir and 
	  * buildFilePath variables are constants, so not
	  * explicitly passed in.
	  */
	 protected String[] setUpGeoFESTArgs(String workDir,
													 String projectName,
													 String targetName,
													 String outputDestDir,
													 String jobUID,
													 String queueServiceUrl) {
		  
		  String[] args=new String[12];
		  args[0]="-Dbindir.prop="+binDir;
		  args[1]="-Dworkdir.prop="+workDir;
		  args[2]="-DprojectName.prop="+projectName;
		  args[3]="-DGFInput.prop="+projectName+".inp";
		  args[4]="-DGFOutput.prop="+projectName+".out";
		  args[5]="-DGFLog.prop="+projectName+".log";
		  args[6]="-Doutputdest.prop="+outputDestDir;
		  args[7]="-DjobUID.prop="+jobUID;
		  args[8]="-DqueueServiceUrl.prop="+queueServiceUrl;
        args[9]="-buildfile";
        args[10]=buildFilePath;
        args[11]=targetName;
		  return args;
	 }

	 /**
	  * Instantiate google service, typically Blogger.
	  */
	 public void instantiateGoogleService(String serviceName,
													  String serviceUserName,
													  String serviceUserPass) 
		  throws Exception {
		  googleService=new GoogleService(serviceName,"");
		  googleService.setUserCredentials(serviceUserName,
													  serviceUserPass);
		  
	 }

	 public void instantiateCalendarService() 
		  throws Exception {
		  calendarService=new CalendarService(clientId);
		  calendarService.setUserCredentials(gServiceUserName,gServiceUserPass);
		  
	 }
}


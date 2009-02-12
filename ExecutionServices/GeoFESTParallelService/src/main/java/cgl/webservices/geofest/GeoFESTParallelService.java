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

//Condor classes
import condor.ClassAdStructAttr;
import condor.ClassAdAttrType;
import condor.UniverseType;
import condor.CondorCollectorLocator;
import condor.CondorCollectorPortType;
import condor.ClassAdStructAttr;
import condor.FileInfo;
import birdbath.*;


/**
 * A simple wrapper for Ant.
 */

public class GeoFESTParallelService extends GeoFESTGridService {    
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
	  * A main for testing
	  */ 
	 public static void main (String[] args) {
		  //Create fault.
		  Fault[] faults=new Fault[1];
		  faults[0]=new Fault();
		  
		  //Create layer.
		  Layer[] layers=new Layer[1];
		  layers[0]=new Layer();
		  
		  try {
				GeoFESTParallelService gfps=new GeoFESTParallelService(true);
				GeotransParallelParamsBean gppb=new GeotransParallelParamsBean();
				
				MeshRunBean mrb=gfps.runBlockingMeshGenerator("duhfault","plan9",faults,layers,"rare");
				gfps.runGridGeoFEST("duhfault",
										  "plan9",
										  gppb,
										  "/bin/ls",
										  "-l",
										  "gt2 cosmos.jpl.nasa.gov",
										  "/tmp/x509up_cosmos",
										  "",
										  mrb.getJobUIDStamp());
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
	 }
		  
    /**
     * The constructor. Set useClassLoader=true when running
     * on the command line.
     */
    public GeoFESTParallelService(boolean useClassLoader) 
		  throws Exception {	
		  super(useClassLoader);
	 }
    
    public GeoFESTParallelService() throws Exception{
		  this(false);
    }

	 /**
	  * Run GeoFEST using Condor.
	  * @proxyLocation: set to null if you want to use the default.
	  */ 
    public GFOutputBean runGridGeoFEST(String userName,
													String projectName,
													GeotransParallelParamsBean gpb,
													String exec,
													String args,
													String gridResourceVal,
													String proxyLocation,
													String envSettings,
													String timeStamp)
		  throws Exception {
		  
		  if(proxyLocation==null) proxyLocation=getProxyLocation();

		  int[] jobstuff=new int[2];
		  jobstuff[0]=0;
		  jobstuff[1]=0;

		  //Set up the stuff.
		  String workDir=generateWorkDir(userName,projectName,timeStamp);
				
		  //Create the input file locally.  This will also generate the 
		  //output destination directory.
		  String[] geoArgs=prefabGeoFESTCall(userName,
														 projectName,
														 gpb,
														 timeStamp,
														 "gfmeshparse");
		  setArgs(geoArgs);
		  run();
		  
		  //String outputDestDir=generateOutputDestDir(userName,projectName,timeStamp);
		  
		  //This is the condor-formatted output file list.  These are files to be
		  //transferred back to the server.
		  String projectOutput=createGeoFESTOutput(projectName);
		  String projectOutputRemaps=createGeoFESTOutputRemaps(projectName,workDir);
		  
		  String baseUrl=generateBaseUrl(userName,projectName,timeStamp);
		  //				String envPathString=generateEnvPathString(userHome);
		  
		  //--------------------------------------------------
		  //These are the files needed for uploading.
		  //--------------------------------------------------
		  File[] files=new File[1];
		  files[0]=new File(workDir+"/"+projectName+".inp");
		  System.out.println("File is "+files[0].toString());
		  
		  //--------------------------------------------------
		  //Create the classadds
		  //--------------------------------------------------
		  ClassAdStructAttr[] extraAttributes =
				{
					 new ClassAdStructAttr("GridResource", ClassAdAttrType.value3,
												  gridResourceVal),
					 new ClassAdStructAttr("Out", ClassAdAttrType.value3,
												  workDir+"/"+projectName+".stdout"),
					 new ClassAdStructAttr("UserLog", ClassAdAttrType.value3,
												  workDir+"/"+projectName+".log"),
					 new ClassAdStructAttr("Err", ClassAdAttrType.value3,
												  workDir+"/"+projectName+".err"),
					 new ClassAdStructAttr("TransferExecutable",
												  ClassAdAttrType.value4, 
												  "FALSE"),
					 new ClassAdStructAttr("when_to_transfer_output",
												  ClassAdAttrType.value2, 
												  "\"ON_EXIT\""),
					 new ClassAdStructAttr("should_transfer_files",
												  ClassAdAttrType.value2, 
												  "\"YES\""),
					 new ClassAdStructAttr("StreamOut",
												  ClassAdAttrType.value4, 
												  "FALSE"),
					 new ClassAdStructAttr("StreamErr",
												  ClassAdAttrType.value4, 
												  "FALSE"),
					 new ClassAdStructAttr("TransferOutput",
												  ClassAdAttrType.value2, 
												  projectOutput),
					 new ClassAdStructAttr("TransferOutputRemaps",
												  ClassAdAttrType.value2, 
												  projectOutputRemaps),
					 new ClassAdStructAttr("Environment",
												  ClassAdAttrType.value2, 
												  envSettings),
					 new ClassAdStructAttr("GlobusRSL",
												  ClassAdAttrType.value2, 
												  "\"(maxWallTime=120)(maxTime=120)\""),
					 new ClassAdStructAttr("x509userproxy", 
												  ClassAdAttrType.value3, 
												  proxyLocation)
				};
		  
		  jobstuff=condorSubmit(userName,
										exec,
										args,
										workDir,
										collectorUrl,
										extraAttributes,
										files);
		  writeCondorJobId(workDir,geofestId,jobstuff[0],jobstuff[1]);
		  
		  return getAllTheGeoFESTFiles(userName, projectName, timeStamp);
    }

	 /**
	  * Must override this one since the Geotrans parameters are extended.
	  */
	 protected String[] prefabGeoFESTCall(String userName,
													  String projectName,
													  GeotransParallelParamsBean gpb,
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
	  * Spit out the geotrans parameters for new/parallel GeoFEST.
	  */
	 protected void createGeoFESTInputFile(String workDir,
														String projectName,
														GeotransParallelParamsBean gpb) 
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
		  
		  //These are new parameters, probably place holders
		  pw.println("ELASTIC1"+space+gpb.getElastic1());
		  pw.println("ELAS_OUT1"+space+gpb.getElasOut1());
		  pw.println("REFINE"+space+gpb.getRefine());
		  pw.println("REFINE_OUT"+space+gpb.getRefineOut());
		  pw.println("ELASTIC2"+space+gpb.getElastic2());
		  pw.println("ELAS_OUT2"+space+gpb.getElasOut2());
		  pw.println("VISCO"+space+gpb.getVisco());
						 
		  
		  pw.println("number_space_dimensions" + space
						 +gpb.getNumber_space_dimensions()+br);
		  
		  pw.println("number_degrees_freedom" + space
						 +gpb.getNumber_degrees_freedom()+br);
		  
		  pw.println("nrates" + space + gpb.getNrates()+br);

		  pw.println("velblock"+space+gpb.getVelblock());
		  
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

		  //Handle the ramp BC stuff
		  if(gpb.getRampBC()!=null) {
				for(int i=0;i<gpb.getRampBC().length;i++) {
					 pw.println("ramp_bc"+space+gpb.getRampBC());
				}
		  }
		  
		  // Finally, handle the number_print_times variable, which may be null
		  
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
	 
}


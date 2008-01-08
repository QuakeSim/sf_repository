/** 
 * This service extends the basic GeoFEST service to use Condor-G for job submission.
 */
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

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Needed for some number formatting.
import java.text.*;

//Needed for a unique id
import java.rmi.server.UID;

//Condor classes
import condor.ClassAdStructAttr;
import condor.ClassAdAttrType;
import condor.UniverseType;
import condor.CondorCollectorLocator;
import condor.CondorCollectorPortType;
import condor.ClassAdStructAttr;
import condor.FileInfo;
import birdbath.*;

public class GeoFESTGridService extends GeoFESTService{    

    //Set the universe type for condor.  Probably OK to assume it is always
    //Globus for now.
    UniverseType universeType = UniverseType.GLOBUS;
    Schedd schedd;
    String collectorUrl;
	 
    //Some useful constants
    String equals="=";	 
    String[] statusName = { "", "Idle", "Running", "Removed", 				      "Completed", "Held" };

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
	    GeoFESTGridService ggfs=new GeoFESTGridService(true);
	    String proxyLocation="/tmp/x509up_u501";
	    String gridResourceVal="gt2 tg-login.tacc.teragrid.org/jobmanager-fork";
	    String meshExec="/home/teragrid/tg459247/bin/autoref.pl";
	    MeshRunBean mrb=ggfs.runGridMeshGenerator(userName,
						      projectName,
						      faults,
						      layers,
						      "rare",
						      proxyLocation,
						      gridResourceVal,
						      meshExec);
	    
// 	    System.out.println("Running GeoFEST");
// 	    gfs.runGeoFEST(userName,projectName,gpb,mrb.getJobUIDStamp());
	    
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
    

	 public GeoFESTGridService() throws Exception {
		  this(false);
	 }
	 
	 public GeoFESTGridService(boolean useClassLoader) throws Exception {
		  super(useClassLoader);
		  //Good ol' condor 
		  collectorUrl=properties.getProperty("condor.collector.url");

	 }

    /**
     * This method gets the Schedd service's URL as a String from the
     * Collector, which is a little obscure.  Note this method assumes you
     * only have one scheduler for your system, which is co-located with
     * the collector.  
     */
    protected String getScheddUrl(String collectorUrl) throws Exception {
		  
		  String scheddLocationStr = null;
		  URL collectorLocation = new URL(collectorUrl);
		  
		  //These are Axis-generated stubs to the 
		  //Collector web service.
		  CondorCollectorLocator collectorLocator=
				new CondorCollectorLocator();
		  System.out.println(collectorLocator.toString());
		  
		  CondorCollectorPortType collector = 
				collectorLocator.getcondorCollector(collectorLocation);
		  
		  ClassAdStructAttr[][] casArray = 
				collector.queryScheddAds("HasSOAPInterface=?=TRUE");
		  
		  //This will actually loop over all the schedds in the cluster, which may
		  //not be what you want.
		  for(int i=0; i<casArray.length; i++ ) {
				for (int j=0; j<casArray[i].length; j++) {
					 if(casArray[i][j].getName().equals("ScheddIpAddr")) {
						  scheddLocationStr=casArray[i][j].getValue();
					 }
				}
		  }
		  
		  String tmpStr= "http://"
				+scheddLocationStr.substring(1,scheddLocationStr.length()-1);
		  
		  return tmpStr; 
    }
    /**
     * These are some condor submission helper methods.
     */ 
    protected Transaction createNewTransaction(String collectorUrl) 
		  throws Exception {
		  //Set up the schedd
		  String condorScheddUrl = getScheddUrl(collectorUrl);
		  System.out.println("Schedd URL: "+condorScheddUrl);
		  Schedd schedd = new Schedd(new URL(condorScheddUrl));
		  
		  setSchedd(schedd);
		  
		  //Use the schedd to create a transaction.
		  //Get the cluster and job ids.
		  Transaction xact = schedd.createTransaction();
		  return xact;
    }

    /**
     * Run things the condor way.  
     * @ userName is the portal user's name.
     * @ projectName is the name of the project.
     * @ faults are passed in from the portal, converted into
     * input files here.
     * @ layers are passed in from the portal, converted
     * into input files here.
     * @ proxyLocation is the location of the user's proxy credential
     * on the local file system.
     * @ gridResourceVal is the URL of the job manager.
     * @ meshExec is the path of the autoref.pl script on the grid resource.
     * Note we don't know this apriori. 
     * 
     * Here are some things we don't pass.
     * - collectorUrl is the url of the co-installed condor server.
     */ 
    public MeshRunBean runGridMeshGenerator(String userName,
														  String projectName,
														  Fault[] faults,
														  Layer[] layers,
														  String autoref_mode,
														  String proxyLocation,
														  String gridResourceVal,
														  String meshExec)
		  throws Exception {
		  
		  String meshArgs=projectName+" "+autoref_mode; 
		  //This creates all the input files. 
		  String timeStamp=generateTimeStamp();
		  try {
				String workDir=generateWorkDir(userName,projectName,timeStamp);
				
				createGeometryFiles(workDir,projectName,faults,layers);
				String outputDestDir=generateOutputDestDir(userName,
																		 projectName,
																		 timeStamp);
				
				//This is the output stuff.
				String projectOutput=createMeshProjectOutput(projectName);
				String projectOutputRemaps=createMeshProjectOutputRemaps(projectName,workDir);

				String baseUrl=generateBaseUrl(userName,projectName,timeStamp);
		
				//--------------------------------------------------
				//These are the files needed for uploading.
				//Each fault has .flt, .params, and .sld file (hence 3* size).
				//Each layer has a .sld file and a .materials file (hence 2*size).
				//Finally, there is one group file for all the metadata (hence +1).
				//--------------------------------------------------
				int fileSize=3*faults.length+2*layers.length+1;
				File[] files=new File[fileSize];
				
				int iter=0;
				for(int i=0;i<faults.length;i++) {
					 files[iter]=new File(workDir+"/"+faults[i].getFaultName()+".flt");
					 iter++;
					 files[iter]=new File(workDir+"/"+faults[i].getFaultName()+".params");
					 iter++;
					 files[iter]=new File(workDir+"/"+faults[i].getFaultName()+".sld");
					 iter++;
				}
				
				for (int i=0;i<layers.length;i++) {
					 files[iter]=new File(workDir+"/"+layers[i].getLayerName()+".sld");
					 iter++;
					 files[iter]=new File(workDir+"/"+layers[i].getLayerName()+".materials");
					 iter++;
				}
				//This is the group file.  Only one of these.
				files[iter]=new File(workDir+"/"+projectName+".grp");


				//--------------------------------------------------
				//Create the classadds
				//--------------------------------------------------
				ClassAdStructAttr[] extraAttributes =
					 {
						  new ClassAdStructAttr("GridResource", ClassAdAttrType.value3,
														gridResourceVal),
						  new ClassAdStructAttr("Out", ClassAdAttrType.value3,
														workDir+"/"+"autoref.out"),
						  new ClassAdStructAttr("UserLog", ClassAdAttrType.value3,
														workDir+"/"+"autoref.log"),
						  new ClassAdStructAttr("Err", ClassAdAttrType.value3,
														workDir+"/"+"autoref.err"),
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
						  new ClassAdStructAttr("x509userproxy", 
														ClassAdAttrType.value3, 
														proxyLocation)
					 };
				
				condorSubmit(userName,
								 meshExec,
								 meshArgs,
								 workDir,
								 collectorUrl,
								 extraAttributes,
								 files);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return getTheMeshGenReturnFiles(userName,projectName,timeStamp); 
    }
	 
    protected int[] condorSubmit(String userName,
				 String exec,
				 String args,
				 String workDir,
				 String collectUrl,
				 ClassAdStructAttr[] extraAttributes,
				 File[] files) throws Exception {
	int[] jobstuff=new int[2];		  
	jobstuff[0]=0;
	jobstuff[1]=0;
	try {
	    //Do the condor submission stuff
	    Transaction xact = createNewTransaction(collectorUrl);
	    xact.begin(30);
	    int clusterId = xact.createCluster();
	    int jobId = xact.createJob(clusterId);
	    
	    jobstuff[0]=clusterId;
	    jobstuff[1]=jobId;
	    
	    //Create a classad for the job.
	    
	    //Submit it all.
	    xact.submit(clusterId, jobId, userName, universeType,
			exec, args,"(TRUE)", extraAttributes, files);
	    xact.commit();
	    
	    getSchedd().requestReschedule();				
	}
	catch (Exception ex) {
		      ex.printStackTrace();
	}
	
	return jobstuff;
    }
    
    public void setSchedd(Schedd schedd) {
		  this.schedd=schedd;
    }
	 
    public Schedd getSchedd() {
		  return schedd;
    }
	
	 /**
	  * Run GeoFEST using Condor.
	  */ 
    public GFOutputBean runGridGeoFEST(String userName,
													String projectName,
													GeotransParamsBean gpb,
													String exec,
													String args,
													String gridResourceVal,
													String proxyLocation,
													String timeStamp)
		  throws Exception {
	int[] jobstuff=new int[2];
	jobstuff[0]=0;
	jobstuff[1]=1;

		  try {
				//Set up the stuff.
				String workDir=generateWorkDir(userName,projectName,timeStamp);
				createGeoFESTInputFile(workDir,projectName,gpb);
				String outputDestDir=generateOutputDestDir(userName,projectName,timeStamp);
			  				
				//This is the condor-formatted output file list.  These are files to be
				//transferred back to the server.
				String projectOutput=createGeoFESTOutput(projectName);
				String projectOutputRemaps=createGeoFESTOutputRemaps(projectName,workDir);

				String baseUrl=generateBaseUrl(userName,projectName,timeStamp);
		
				//--------------------------------------------------
				//These are the files needed for uploading.
				//--------------------------------------------------
				//Need to fix this temp stuff.
				File[] files=new File[0];

				//--------------------------------------------------
				//Create the classadds
				//--------------------------------------------------
				ClassAdStructAttr[] extraAttributes =
					 {
						  new ClassAdStructAttr("GridResource", ClassAdAttrType.value3,
														gridResourceVal),
						  new ClassAdStructAttr("Out", ClassAdAttrType.value3,
														workDir+"/"+projectName+".out"),
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
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }

		  return getAllTheGeoFESTFiles(userName, projectName, timeStamp,jobstuff[0],jobstuff[1]);
    }

	 /**
	  * This creates the condor-style list of GeoFEST output files to return.
	  */
	 protected String createGeoFESTOutput(String projectName) {
		  String returnString=quote+projectName+".out"+comma+"cghist.txt"+quote;
		  System.out.println(returnString);
		  return returnString;
	 }

	 protected String createGeoFESTOutputRemaps(String projectName,String workDir) {
		  String returnString=quote+projectName+".out" +equals+workDir+projectName+".out"
				+comma+"cghist.txt"+equals+workDir+"cghist.txt"+quote;
		  System.out.println(returnString);
		  return returnString;
	 }

	 /**
	  * This method creates a string of files to be returned using condor's TransferOutput
	  * parameter.
	  */
    protected String createMeshProjectOutput(String projectName) {
		  String returnString=quote+projectName+".index"+comma
				+ projectName+".node"+comma
				+ projectName+".tetra"+quote;
		  System.out.println(returnString);
		  return returnString;
    }

    protected String createMeshProjectOutputRemaps(String projectName,String workDir) {
		  String returnString=quote+projectName+".index"+equals+workDir+projectName+".index"
				+comma
				+ projectName+".node"+equals+workDir+projectName+".node"
				+comma
				+ projectName+".tetra"+equals+workDir+projectName+".tetra"
				+quote;
		  System.out.println(returnString);		  
		  return returnString;
    }

	 	 
     /**
     * Remove the selected job. Note the URL for the collector is
     * always the same, but the job and cluster IDs can change.
     */
    public void deleteGeoFESTJob(int clusterId, int jobId) throws Exception {
		  
		  String condorScheddUrl = getScheddUrl(collectorUrl);
		  Schedd schedd = new Schedd(new URL(condorScheddUrl));
		  
		  Transaction xact1=schedd.createTransaction();
		  xact1.closeSpool(clusterId,jobId);
		  boolean flag=xact1.removeJob(clusterId,jobId,null);
		  System.out.println("Remove job flag: "+flag);
		  //	xact1.commit();
    }


    /**
     * Get the job status.
     */
    public String queryGeoFESTStatus(int clusterId, int jobId) { 
		  int status = 0;
		  
		  try {		  
				String condorScheddUrl = getScheddUrl(collectorUrl);
				Schedd schedd = new Schedd(new URL(condorScheddUrl));
				// Querying job code
				
				ClassAd ad = new ClassAd(schedd.getJobAd(clusterId, jobId));
				status = Integer.parseInt(ad.get("JobStatus"));
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return statusName[status];
    }	

}
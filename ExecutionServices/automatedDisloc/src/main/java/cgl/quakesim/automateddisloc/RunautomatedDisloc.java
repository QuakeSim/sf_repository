package cgl.quakesim.automateddisloc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import cgl.quakesim.disloc.DislocExtendedService;
import cgl.quakesim.disloc.DislocExtendedServiceServiceLocator;
import cgl.quakesim.disloc.DislocParamsBean;
import cgl.quakesim.disloc.DislocProjectBean;
import cgl.quakesim.disloc.DislocProjectSummaryBean;
import cgl.quakesim.disloc.DislocResultsBean;
import cgl.quakesim.disloc.Fault;
import cgl.quakesim.disloc.InsarKmlService;
import cgl.quakesim.disloc.InsarKmlServiceServiceLocator;
import cgl.quakesim.disloc.InsarParamsBean;
import cgl.quakesim.disloc.ObsvPoint;

//These are not used.
//import cgl.webservices.KmlGenerator.ExtendedSimpleXDataKml;
//import cgl.webservices.KmlGenerator.ExtendedSimpleXDataKmlServiceLocator;
//import cgl.webservices.KmlGenerator.PointEntry;

import cgl.quakesim.disloc.SimpleXDataKml;
import cgl.quakesim.disloc.SimpleXDataKmlServiceLocator;
import cgl.quakesim.disloc.PointEntry;

import gekmlib.Document;
import gekmlib.Folder;
import gekmlib.Kml;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

//Commons logging
//import org.apache.log4j.Logger;
//import org.apache.log4j.Level;

//Quartz
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.DateBuilder.*;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunautomatedDisloc implements Job {
	 public final static String URL_FOR_AUTOCALLS="RSS_FEED_URL";
	 private static Logger logger=LoggerFactory.getLogger(RunautomatedDisloc.class);
	 //REVIEW: Shouldn't be hard coded.  Put in a property.
	 //REVIEW: actually, it isn't used.  The "url" variable passed in to run() is 
	 //the URL to use. 
	 //	 String mover5_rss_url = "http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml";
	 //	String mover5_rss_url = "http://localhost:8080/7day-M5.xml";
	private DecimalFormat df = new DecimalFormat(".###");
	private String contextBasePath;
	private String url = "";
	private String tomcatbase;
	
	private String dislocServiceUrl;
	private String dislocExtendedServiceUrl;
	private String faultDBServiceUrl;
	private String kmlGeneratorBaseurl;
	private String kmlGeneratorUrl;
	private String insarkmlServiceUrl;	
	private String insarKmlUrl;
	private String rssdisloc_dir_name;
	private String baseurl;
	 private String projectName;
	 private String kmlOutputDir;

	private String elevation = "60";
	private String azimuth = "0";
	private String frequency = "1.26";
	
	private String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private String kmlHead = "<kml xmlns=\"http://earth.google.com/kml/2.2\">";
	private String kmlEnd = "</kml>";
	private String pmBegin = "<Placemark>";
	private String pmEnd = "</Placemark>";
	private String lsBegin = "<LineString>";
	private String lsEnd = "</LineString>";
	private String pointBegin = "<Point>";
	private String pointEnd = "</Point>";
	private String coordBegin = "<coordinates>";
	private String coordEnd = "</coordinates>";
	private String docBegin = "<Document>";
	private String docEnd = "</Document>";
	private String comma = ", ";
	private String descBegin = "<description>";
	private String descEnd = "</description>";
	
	private int limitedsize = 800; 

	 private SimpleXDataKml kmlservice;
	 private Properties properties;
	 private static final DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	 //These are variables associated with the arrow scaling.
	 private double scale;
	 private double longestlength;
	 private double projectMinX;
	 private double projectMaxX;
	 private double projectMinY;
	 private double projectMaxY;

	 private boolean upToDate=false;

	 //These are File objects and a PrintWriter that are usefully given class-wide scope.
	 File destDir, oldFile;
	 PrintWriter out;

	 public RunautomatedDisloc(){
		  logger.info("Empty constructor of RunautomatedDisloc called.");
		  
		  resetScalingVariables();
		  
		  //Set up various things
		  Properties properties=loadProperties();
		  
	 }

	 public RunautomatedDisloc(String url) {
		  this();
		  this.url = url;
	 }

	 /**
	  * Required by Quartz
	  */
	 public void execute(JobExecutionContext context) throws JobExecutionException {
		  JobDataMap jobmap=context.getJobDetail().getJobDataMap();
		  url=jobmap.getString(URL_FOR_AUTOCALLS);
		  logger.info("Execution called, URL is "+url);
		  run(url);
	 }

	 /**
	  * Load the project properties and set values.
	  */
	public Properties loadProperties() {
		properties = new Properties();
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		// logger.info("[getContextBasePath] called");
		projectName=properties.getProperty("project.name");
		contextBasePath = properties.getProperty("output.dest.dir");
		dislocServiceUrl = properties.getProperty("dislocServiceUrl"); 
		dislocExtendedServiceUrl = properties.getProperty("dislocExtendedServiceUrl");		
		kmlGeneratorUrl = properties.getProperty("kmlGeneratorUrl");
		insarkmlServiceUrl = properties.getProperty("insarkmlServiceUrl"); 
		rssdisloc_dir_name= properties.getProperty("rssdisloc.dir.name");
		baseurl = properties.getProperty("baseurl");
		tomcatbase = properties.getProperty("tomcat.base");
		
		kmlOutputDir=tomcatbase+"/webapps/ROOT/";

		//Some infoging messages to make sure properties are read correctly.
		logger.info("[getContextBasePath] " + properties.getProperty("output.dest.dir"));
		logger.info("tomcat base:"+tomcatbase);
		logger.info("baseurl:"+baseurl);
		logger.info("rssdisloc_dir_name:"+rssdisloc_dir_name);
		
		return properties;
	}
	
	public String getContextBasePath() {
		return contextBasePath;
	}

	public void setContextBasePath(String contextBasePath) {
		this.contextBasePath = contextBasePath;
	}
	
	public String getTomcatbase() {
		return tomcatbase;
	}

	public void setTomcatbase(String tomcatbase) {
		this.tomcatbase = tomcatbase;
	}

	 /**
	  * This implements the required run() method for this thread. It does the following;
	  * 1. Check to see if it is time to update.  If not, do nothing else.
	  * 2. If time to update, run createProjectsFromRss().  
	  * 3. Update the project DB.  Temporary values are 
	  *    stored in a working DB that is copied to the "real" DB at the end.
	  */ 
	 public void run(String url) {
		  this.url=url;
		 
		  logger.info("Run method called");

		String dir = properties.getProperty("output.dest.dir");
		File logfile = new File(dir + "/" + "log.txt");		
		//		getDislocProjectSummaryBeanCount();

		//If it has been longer than the trigger time, check for more data.  Otherwise, do nothing.
		boolean getUpdates=timeToRunUpdate(logfile,AutomatedDislocBean.SCAN_TRIGGER_INTERVAL);
		
		if(getUpdates) {
			 logger.info("[RunautomatedDisloc/run] Time to run updates");
			 
			 //Make sure the project master directory exists.
			 File projectDir = new File(getContextBasePath() + "/overm5/");
			 if (!projectDir.exists()) {
				projectDir.mkdirs();
			 }
			 logger.info("Project directories set up");

			 //To allow other process to have access to overm5.db while this 
			 //webservice is updating, we update a temporary file and copy it at the end.

			 //First, delete the previous temporary DB if it is still around.
			 File f = new File(getContextBasePath() + "/overm5_temp.db");
			 if (f.exists()) f.delete();
			 
			 //This does the interesting work.
			 createProjectsFromRss(url);
			 logger.info("Projects created from RSS feed");

			 //Now attempt to overwrite the old file with the new one.
			 File oldFileDB = new File(getContextBasePath() + "/overm5_temp.db");			
			 File newFileDB = new File(getContextBasePath() + "/overm5.db");			 
			 try {
				  //Make sure the DB exists before copying.
				  //This may be redundant.
				  if (!newFileDB.exists()) newFileDB.createNewFile();
				  copyFile(oldFileDB, newFileDB);
				  logger.info("Project DB successfully copied");

			 } catch (Exception e) {
				  logger.error("Project DB not updated successfully");
				  logger.error(e.getMessage());
			 }

			 //Only update the timestamp log after we are done with everything else.
			 logwriter(logfile,dateFormat.format(new Date()));						  
		}
		setUpToDate(true);
	 }
	 
	 /**
	  * This method constructs the project from the USGS RSS file.
	  */ 
	protected void createProjectsFromRss(String url) {		
			
		 //First, parse the RSS feed.
		CglGeoRssParser cgrp = new CglGeoRssParser();
		cgrp.parse(url);		
		List <Entry> entry_list = cgrp.getEntryList();
		ArrayList <String> projectNameArray = new ArrayList();
		logger.info("[RunautomatedDisloc/run] entry_list.size() : " + entry_list.size());
		
		//		HashMap<String, Fault> hm = new HashMap<String, Fault>();
		
		//Construct a KML file for the results.
		Kml doc = new Kml();
		Folder root = new Folder();
		Document kmlDocument=new Document();
		root.setName("root Folder");
		root.setDescription("This is the root folder");
		kmlDocument.addFolder(root);
		doc.addDocument(kmlDocument);
		
		//REVIEW: Need a less clumsy way to handle this location.  Also should be
		//in this webapp for portability. 
		String newFaultFilename = "";
		String destDirname=kmlOutputDir;
		String destFilename="/overm5.kml";
		String localDestination = destDir+destFilename;
		
		try {
			 //Set everything up.  This also sets up the PrintWriter out.
			 setUpKmlFileLocations(destDirname, destFilename);
			 logger.info("Kml file locations set up");
			 
			 //Print header stuff.
			 out.println(xmlHead);
			 out.println(kmlHead);
			 out.println(docBegin);

			//Now go.  
			logger.info("Starting loop over entry objects");
			
			for (int nA = 0 ; nA < entry_list.size() ; nA++) {
				 Entry entry = (Entry) entry_list.get(nA);
				 //We check for 4 possible scenarios.
				 for (int nB = 0 ; nB < AutomatedDislocBean.EARTHQUAKE_SLIP_SCENARIOS ; nB++) {
					  
					  //This does all the stuff needed to set up a fault of type nB.
					  Fault fault=setFaultType(nB,entry);				
					  
					  //Create the project name
					  DislocParamsBean dislocParams = null;
					  String projectname=createProjectName(entry, fault, nB);

					  projectNameArray.add(projectname);
					  dislocParams=putProjectInDB(projectname, fault);
					  
					  //Now we are ready to run disloc.  It returns
					  //output URLs.
					  // Now we assign x20 density by -100, -100, 0.5, 420. 09/22/2010 Jun Ji
					  OutputURLs ouls = null;				
					  try {
							ouls = runBlockingDislocJSF(projectname, 
																 dislocParams, 
																 fault, 
																 entry.getM(), 
																 nB);
							//					getDislocProjectSummaryBeanCount();
					  } catch (Exception e) {
								 logger.error(e.getMessage());
					  }

					  //Make a shorter name for the project
					  String projectShortName=makeShortProjectName(projectname);
					  
					  //Now print out the KML for this earthquake
					  printKmlForEarthquakeEntry(entry, fault, out, ouls, projectname,projectShortName);
					  
				 }
			}

			logger.info("Loop over projects completed");
			
			out.println(docEnd);
			out.println(kmlEnd);
			out.flush();
			out.close();
		} 
		catch (Exception e) {
			 logger.error(e.getMessage());
		}		 
		finally {
			 if(out!=null) {
				  out.flush();
				  out.close();
			 }
		}

		//Finally, store the new project in the db
		// logger.info("Calling createProject()");
		// createProject(projectNameArray);	
	}
		
	 /**
	  * Using the pre-calculated insar images, disloc outputs. 09/23/2010 Jun Ji	
	  */
	 protected OutputURLs runBlockingDislocJSF(String projectName, 
															 DislocParamsBean currentParams, 
															 Fault fault, 
															 double M, 
															 int s_case) throws Exception {
		  
		logger.info("[AutomatedDislocBean/runBlockingDislocJSF] Started");
		OutputURLs ourls = new OutputURLs();

		try {

			Fault[] faults = new Fault[1];
			faults[0] = fault;
			
			ObsvPoint[] points = null;
			
			//--------------------------------------------------
			//This method call checks to see if Disloc has been run for this problem. If so,
			//results are reused, and if not, do a new calculation. 
			//--------------------------------------------------
			DislocResultsBean dislocResultsBean = getDislocResultsBean(projectName, 
																						  currentParams, 
																						  faults, 
																						  points, 
																						  M, 
																						  s_case);	
			
			ourls.setDislocoutputURL(dislocResultsBean.getOutputFileUrl());
			
			//--------------------------------------------------
			// This step makes the kml plots.  We allow this to fail; hence the
			// embedded try/catch block.
			//--------------------------------------------------
			String myKmlUrl = "";	
			if(!foundPreviousEntry(projectName)) {
				 logger.info("Can't find previous project, so make new KML plots");
				 try {
					  myKmlUrl = createKml(currentParams, dislocResultsBean, faults, projectName);
					  logger.info("[AutomatedDislocBean/runBlockingDislocJSF] KmlUrl : " + myKmlUrl);
				 }
				 catch (Exception e) {
					  logger.error(e.getMessage());
					  e.printStackTrace();
				 }
			}
			else {
				 logger.info("Found previous KML deformation plots");
				 myKmlUrl=getMyKmlUrlFromDB(projectName);
			} 
			ourls.setDisplacementkmlURL(myKmlUrl);

			//--------------------------------------------------
			// This step runs the insar plotting stuff.  We also allow this
			// to fail.			
			//--------------------------------------------------
			insarKmlUrl= getPrecalculatedInsar(projectName, fault.getFaultLonStart(), fault.getFaultLatStart(), M, s_case, dislocResultsBean.getJobUIDStamp(), dislocResultsBean);
			
			ourls.setInsarkmlURL(insarKmlUrl);
			storeProjectInContext("automatedDisloc", projectName, dislocResultsBean.getJobUIDStamp(), currentParams, dislocResultsBean, myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);
			
		} catch (Exception e) {
			 logger.error(e.getMessage());
			 e.printStackTrace();
		}
		logger.info("[AutomatedDislocBean/runBlockingDislocJSF] Finished");
		
		return ourls;
	}
	
	 /**
	  * Converts (x,y) values to lat/lon values.
	  */
	protected Double[] dxy2lonlat(double x, double y, double reflon, double reflat) {
		
	    double flattening = 1.0/298.247; 
	    double yfactor = 111.32;
	    
	    double xfactor = 111.32*Math.cos(Math.toRadians(reflat))*(1.0 - flattening*Math.pow(Math.sin(Math.toRadians(reflat)), 2));
	    logger.debug("x, y : " + x + ", " + y);
	    logger.debug("xfactor : " + xfactor);
	    
	    double lon2 = x/xfactor + reflon; 
	    double lat2 = y/yfactor + reflat;
	    
	    logger.debug("lon : " + lon2);
	    logger.debug("lat : " + lat2);
	    Double results[] = new Double[2];
	    results[0] = lon2;
	    results[1] = lat2;
	    
	    return results;
	}
	
	 /**
	  * For the automated disloc, all fault models for a given magnitude earthquake are identical,
	  * so the disloc results and interferrograms for a give M will be the same regardless of the 
	  * location. 
	  *
	  * This method is used to see if precalculated results are available.  If so, they are used.  
	  * If not, then new calculations for a given M are performed and the results are thereafter
	  * available. 
	  */ 
	protected String getPrecalculatedInsar(String projectName, 
													Double reflon, 
													Double reflat, 
													Double M, 
													int s_case, 
													String jobUID, 
													DislocResultsBean drb) {		
		
		 //The precalculated results will be in a file following the pattern below.
		File imageFile = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output.png");
		String insarURL = null;
		String insarimageURL = null;
		
		//If the insar image file has already been created, reuse it.
		if (imageFile.exists()) {		
			
			logger.info("[AutomatedDislocBean/getPrecalculatedInsar] " + projectName + " is using a precalculated insar image");
			
			String north = "";
			String south = "";
			String east = "";
			String west = "";
			
			insarimageURL = baseurl + "/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output.png";
			
			Double ts[] = null;
			//REVIEW: Hard coded (-100, -100) not understood.
			ts = dxy2lonlat(-100, -100, reflon, reflat);
			
			west = ts[0].toString();
			south = ts[1].toString();
			
			//REVIEW: Hard coded (109.5,109.5) not understood
			ts = dxy2lonlat(109.5, 109.5, reflon, reflat);
			
			east = ts[0].toString();
			north = ts[1].toString();
			
			File precalcInsarDir = new File(tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID);		
			if (!precalcInsarDir.exists()) {
				precalcInsarDir.mkdirs();
			}
			
			File precalcInsarKml = new File(tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID + "/" + projectName + ".output.kml");
			
			insarURL = baseurl + "/insar_precalculated/insar/" + jobUID + "/" + projectName + ".output.kml";
			
			Kml doc = new Kml();
			Folder root = new Folder();
			Document kmlDocument=new Document();
			
			root.setName("root Folder");
			root.setDescription("This is the root folder");
			kmlDocument.addFolder(root);
			doc.addDocument(kmlDocument);
			
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(precalcInsarKml));
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.println("<kml xmlns=\"http://earth.google.com/kml/2.2\">");
				out.println("  <Folder>");
				out.println("    <name>Ground Overlays</name>");
				out.println("    <description>" + projectName + "</description>");
				out.println("    <GroundOverlay>");
				out.println("      <Icon>");
				out.println("        <href>" + insarimageURL + "</href>");
				out.println("      </Icon>");
				out.println("      <LatLonBox>");
				out.println("        <north>" + north + "</north>");
				out.println("        <south>" + south + "</south>");
				out.println("        <east>" + east + "</east>");
				out.println("        <west>" + west + "</west>");
				out.println("      </LatLonBox>");
				out.println("    </GroundOverlay>");
				out.println("  </Folder>");
				out.println("</kml>");
	    
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
	
			out.flush();
			out.close();
		}
		
		//The insar image file for this calculation does not exist, so we need to make one.
		else {		
			
			logger.info("[AutomatedDislocBean/getPrecalculatedInsar] " + projectName + " is generating a new insar image. This will be stored for later earthquakes with the same magnitude.");
			
			InsarKmlService iks;
			try {
				iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));
				insarURL = iks.runBlockingInsarKml("automatedDisloc", projectName, drb.getOutputFileUrl(), elevation, azimuth, frequency, "ExecInsarKml");
				logger.info("[AutomatedDislocBean/runBlockingDislocJSF] insarKmlUrl : " + insarURL);
				
				File insarCaseDir = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case);
				if (!insarCaseDir.exists()) {
					insarCaseDir.mkdirs();
				}
				
				// File d = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case);		
				// if (!d.exists()){
				// 	d.mkdirs();
				// }
				
				insarimageURL = insarURL.replace(".kml", ".png");
				SavefileFromUrl(insarimageURL, tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output.png");
				
								 
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
			catch (Exception e) {
				 logger.error(e.getMessage());
			}
		}
		
		return insarURL;		
	}
	
	private void SavefileFromUrl(String inputUrl, String fileName) 	{	
			URL Url;
			
			try {
				
				Url = new URL(inputUrl);
				
				URLConnection Uc = Url.openConnection();
				
				FileOutputStream savedFile;
				
				int contentLength = 2048;
				InputStream inStream = new BufferedInputStream(Uc.getInputStream());
				
				savedFile = new FileOutputStream(fileName);
							
				byte[] data = new byte[contentLength];
				
				int bytesRead = 0;
				
				while ((bytesRead = inStream.read(data, 0, contentLength)) > 0) {		    	
					savedFile.write(data, 0, bytesRead);
					savedFile.flush();					
				}
				
				inStream.close();
				savedFile.close();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}			
	}
	
	 /**
	  * Get the disloc results. These may be precalculated, but if not, do a new
	  * disloc calculation.
	  */
	public DislocResultsBean getDislocResultsBean(String projectName, 
																 DislocParamsBean dislocParams, 
																 Fault[] faults, 
																 ObsvPoint[] obsvPoints, 
																 Double M, 
																 int s_case) throws Exception {

		File f = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output");
		DislocResultsBean drb = null;
		
		if (f.exists()) {
			
			logger.info("[AutomatedDislocBean/getDislocResultsBean] " + projectName + " is using a precalculated disloc results");
			
			drb = new DislocResultsBean();
			drb.setProjectName(projectName);
			drb.setJobUIDStamp((new UID().toString()));
			
			drb.setInputFileUrl(createDislocInputFile(projectName, dislocParams, faults, obsvPoints, drb.getJobUIDStamp()));
					
			String baseBaseUrl=baseurl + "/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case;
			String outputurl = baseBaseUrl + ".output";
			String stdouturl = baseBaseUrl + ".stdout";
			String kmlurl = baseBaseUrl + ".kml";
			
			drb.setOutputFileUrl(outputurl);
			drb.setStdoutUrl(stdouturl);
		}
		
		else {			
			
			logger.info("[AutomatedDislocBean/getDislocResultsBean] " + projectName + " is generating a new precalculated disloc results");

			//Set up the web service client
			DislocExtendedService dislocExtendedService = new DislocExtendedServiceServiceLocator().getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
			//Run the simulation
			String space=" ";
			logger.info("Project"+projectName);
			//			logger.info(obsvPoints[0]);
			logger.info("Fault:"+faults[0]+space+faults[0].getFaultName());
			logger.info("Disloc Params:"+dislocParams);
			
			try {
				 drb = dislocExtendedService.runBlockingDislocExt("automatedDisloc", projectName, obsvPoints, faults, dislocParams, null);
			}
			catch (Exception ex){
				 ex.printStackTrace();
			}

			f = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case);
			if (!f.exists()) f.mkdirs();
			
			SavefileFromUrl(drb.getOutputFileUrl(), tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output");
			
			String stdout = drb.getOutputFileUrl().replace(".output", ".stdout");
			SavefileFromUrl(stdout, tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".stdout");

		}
		return drb;
	}
	
	
	private String createDislocInputFile(String projectName, DislocParamsBean dislocParams, Fault[] faults, ObsvPoint[] obsvPoints, String jobUID) throws Exception {

				
		File f = new File(tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID + "/");
		if (!f.exists())
			f.mkdirs();
		
		String inputFile = tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID + "/" + projectName + ".input";
		String inputFileUrl = baseurl + "/insar_precalculated/insar/" + jobUID + "/" + projectName + ".input";
		logger.info("Input File: "+inputFile);
		PrintWriter pw=new PrintWriter(new FileWriter(inputFile),true);

		//Create the input file.  First create the gripoints

		//Print the header line
		if(dislocParams.getObservationPointStyle()==1) {
			pw.println(dislocParams.getOriginLat() + " " + dislocParams.getOriginLon() + " " + dislocParams.getObservationPointStyle());
		}
		else if(dislocParams.getObservationPointStyle()==0) {
			pw.println(dislocParams.getOriginLat() + " " + dislocParams.getOriginLon() + " "  + dislocParams.getObservationPointStyle() + " " + obsvPoints.length);
		}

		else {
			logger.info("Malformed disloc problem");
			throw new Exception();
		}


		//Print the observation point information
		if(dislocParams.getObservationPointStyle()==1) {
			printGridObservationSites(pw, dislocParams);
		}
		else if(dislocParams.getObservationPointStyle()==0) {
			printScatterObservationSites(pw,dislocParams,obsvPoints);
		}

		//Now iterate over the faults.
		printFaultParams(pw,faults);
		pw.close();
		
		return inputFileUrl;
	}
	
	protected void printFaultParams(PrintWriter pw, Fault[] faults) throws Exception {
		for(int i=0;i<faults.length;i++) {
			pw.println(faults[i].getFaultLocationX()
					+ " " + faults[i].getFaultLocationY()
					+ " " + faults[i].getFaultStrikeAngle());

			pw.println(1
					+ " " + faults[i].getFaultDepth()
					+ " " + faults[i].getFaultDipAngle()
					+ " " + faults[i].getFaultLameLambda()
					+ " " + faults[i].getFaultLameMu()
					+ " " + faults[i].getFaultStrikeSlip()
					+ " " + faults[i].getFaultDipSlip()
					+ " " + faults[i].getFaultTensileSlip()
					+ " " + faults[i].getFaultLength()
					+ " " + faults[i].getFaultWidth());
		}
	}
	
	protected void printGridObservationSites(PrintWriter pw, DislocParamsBean dislocParams) throws Exception {

		pw.println(dislocParams.getGridMinXValue()
				+ " " + dislocParams.getGridXSpacing()
				+ " " + dislocParams.getGridXIterations()
				+ " " + dislocParams.getGridMinYValue()
				+ " " + dislocParams.getGridYSpacing()
				+ " " + dislocParams.getGridYIterations());
	}
	 
	protected void printScatterObservationSites(PrintWriter pw, DislocParamsBean dislocParams, ObsvPoint[] obsvPoints) throws Exception {

		for(int i=0;i<obsvPoints.length;i++) {
			pw.println(obsvPoints[i].getXcartPoint() + " " + obsvPoints[i].getYcartPoint());
		}
	}
	 /**
	  * Creates a PointEntry[] array from the output data of a disloc run. This is
	  * input for the KML arrow plotting service.
	  *
	  * REVIEW: should this be public or protected?
	  */
	public PointEntry[] LoadDataFromUrl(String InputUrl) {
		 logger.info("[AutomatedDislocBean/LoadDataFromUrl] Creating Point Entry");
		 logger.info("Input URL:"+InputUrl);
		ArrayList dataset = new ArrayList();
		ArrayList dataset_temp = new ArrayList();
		try {
			String line = new String();
			int skipthreelines = 1;

			URL inUrl = new URL(InputUrl);
			URLConnection uconn = inUrl.openConnection();
			InputStream instream = inUrl.openStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(instream));

			// Need to make sure this will work with multiple faults.
			Pattern p = Pattern.compile(" {1,20}");
			while ((line = in.readLine()) != null) {
				String tmp[] = p.split(line);

				if (tmp[1].trim().equals("x") && tmp[2].trim().equals("y")) {
					logger.info("Past the faults");
					break;
				}
			}
			
			while ((line = in.readLine()) != null) {
				if (!line.trim().equalsIgnoreCase("")) {
					PointEntry tempPoint = new PointEntry();
					String tmp[] = p.split(line);

					// Look for NaN or other problems.
					for (int i = 0; i < tmp.length; i++) {
						String oldtmp = tmp[i];
						if (tmp[i].trim().equalsIgnoreCase("nan")) {
							tmp[i] = "0.0";
						}
					}

					tempPoint.setX(tmp[1].trim());
					tempPoint.setY(tmp[2].trim());
					tempPoint.setDeltaXName("dx");
					tempPoint.setDeltaXValue(tmp[3].trim());
					tempPoint.setDeltaYName("dy");
					tempPoint.setDeltaYValue(tmp[4].trim());
					tempPoint.setDeltaZName("dz");
					tempPoint.setDeltaZValue(tmp[5].trim());
					tempPoint.setFolderTag("point");
					dataset_temp.add(tempPoint);
				} else {
					break;
				}
			}			
			in.close();
			instream.close();

			int total_points = dataset_temp.size();
			
			if (total_points <= limitedsize)
				dataset = dataset_temp;
			
			else {
				 for (int nA = 0 ; nA < limitedsize ; nA++) {
					 double ratio = 1;
					 int dist = 1;
					 int start_point = 0;
					 int index_e = 0;
					 dist = limitedsize;
					 int nIndex = (int) ((total_points * ratio)/dist * nA) + index_e;
					 dataset.add(dataset_temp.get(nIndex));
				 }
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("[AutomatedDislocBean/LoadDataFromUrl] Finished");
		
		logger.info("Number of entries:"+dataset.size());
		PointEntry[] pointEntries=(PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
		return pointEntries;
	}
	
	 /**
	  * This method sets up and runs the methods for creating the point deformation plots
	  * in KML.
	  */ 
	protected String createKml(DislocParamsBean dislocParams, 
										DislocResultsBean dislocResultsBean, 
										Fault[] faults, 
										String projectName) throws Exception {

		 logger.info("Creating KML point deformation plots");

		 //Create a new kmlservice for this instance.
		 SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		 locator.setMaintainSession(true);		
		 kmlservice = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		// Get the project lat/lon origin. It is the lat/lon origin of the first fault.
		String origin_lat = dislocParams.getOriginLat() + "";
		String origin_lon = dislocParams.getOriginLon() + "";		
		PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());
		
		// These plot grid lines.
		double start_x, start_y, end_x, end_y, xiterationsNumber, yiterationsNumber;
		start_x = Double.valueOf(dislocParams.getGridMinXValue()).doubleValue();
		start_y = Double.valueOf(dislocParams.getGridMinYValue()).doubleValue();
		xiterationsNumber = Double.valueOf(dislocParams.getGridXIterations()).doubleValue();
		yiterationsNumber = Double.valueOf(dislocParams.getGridYIterations()).doubleValue();
		int xinterval = (int) (Double.valueOf(dislocParams.getGridXSpacing()).doubleValue());
		int yinterval = (int) (Double.valueOf(dislocParams.getGridYSpacing()).doubleValue());
		end_x = start_x + xinterval * (xiterationsNumber - 1);
		end_y = start_y + yinterval * (yiterationsNumber - 1);

		// ExtendedSimpleXDataKml kmlservice;		
		// ExtendedSimpleXDataKmlServiceLocator locator = new ExtendedSimpleXDataKmlServiceLocator();
		// locator.setMaintainSession(true);
		// kmlservice = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		kmlservice.setOriginalCoordinate(origin_lon, origin_lat);
		kmlservice.setCoordinateUnit("1000");
		
		logger.info("[AutomatedDislocBean/createKml] runMakeKml");
		
		kmlservice.setDatalist(tmp_pointentrylist);
		kmlservice.setOriginalCoordinate(origin_lon, origin_lat);
		kmlservice.setCoordinateUnit("1000");
		double arrowScale=setGlobalKmlArrowScale(tmp_pointentrylist);
		kmlservice.setArrowPlacemark("Arrow Layer", "#000000", 0.95,arrowScale);
		// Plot the faults
		for (int i = 0; i < faults.length; i++) {
			 kmlservice.setFaultPlot("", faults[i].getFaultName() + "",
											 faults[i].getFaultLonStart() + "", faults[i]
											 .getFaultLatStart()
											 + "", faults[i].getFaultLonEnd() + "", faults[i]
											 .getFaultLatEnd()
											 + "", "ff6af0ff", 5);
		}
		String myKmlUrl = kmlservice.runMakeKml("", "automatedDisloc", projectName,
															 (dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		
		//This is possibly an obsolete API call.
		//		String myKmlUrl = kmlservice.runMakeSubKmls(tmp_pointentrylist, "", "automatedDisloc", projectName, (dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		
		resetScalingVariables();
		logger.info("[AutomatedDislocBean/createKml] Finished");
		return myKmlUrl;
	}
	
	protected void storeProjectInContext(String userName, 
													 String projectName,
													 String jobUIDStamp, 
													 DislocParamsBean paramsBean,
													 DislocResultsBean dislocResultsBean, 
													 String kml_url,
													 String insarKmlUrl, 
													 String elevation, 
													 String azimuth,
													 String frequency) throws Exception {
		 
		DislocProjectSummaryBean summaryBean = new DislocProjectSummaryBean();
		summaryBean.setUserName(userName);
		summaryBean.setProjectName(projectName);
		summaryBean.setJobUIDStamp(jobUIDStamp);
		summaryBean.setParamsBean(paramsBean);
		summaryBean.setResultsBean(dislocResultsBean);
		summaryBean.setCreationDate(new Date().toString());
		summaryBean.setKmlurl(kml_url);
		// summaryBean.setInsarKmlUrl(insarKmlUrl);
		// summaryBean.setElevation(elevation);
		// summaryBean.setAzimuth(azimuth);
		// summaryBean.setFrequency(frequency);

		InsarParamsBean ipb = new InsarParamsBean();
		ipb.setUserName(userName);
		ipb.setProjectName(projectName);
		ipb.setJobUIDStamp(jobUIDStamp);
		ipb.setCreationDate(new Date().toString());
		ipb.setInsarKmlUrl(insarKmlUrl);
		ipb.setElevation(elevation);
		ipb.setAzimuth(azimuth);
		ipb.setFrequency(frequency);
		ipb.setDislocOutputUrl(dislocResultsBean.getOutputFileUrl());

		// Store the summary and insar params beans.
		// setCodedbs("storeProjectInContext");
		ObjectContainer codedb = null;
		
		try {
			
			// codedb = codedbs.openClient();
			codedb = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");			
		
			codedb.set(summaryBean);
			codedb.set(ipb);

			// Store the params bean for the current project,
			// deleting any old one as necessary.

			// codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			ObjectSet result = codedb.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				codedb.delete(tmp);
			}
			codedb.set(paramsBean);

			// Say goodbye.
			codedb.commit();
			
		} catch (Exception e) {
			
			logger.error("[AutomatedDislocBean/storeProjectInContext] " + e);
		}
		
		finally {
			if (codedb != null)
				codedb.close();			
		}		

	}
	
	 /**
	  * This is used to read the timestamp log file
	  */
	private String logreader(File file, boolean all) {
		String returnstr = "";
		String temp;
				
		if (file.exists()) {	
			try {
				
				BufferedReader loginput = new BufferedReader(new FileReader(file));
					
				while ((temp = loginput.readLine()) != null)	{
					 if (all == true) {
						returnstr += temp + "\n";
					 }
					
					 else {
						returnstr = temp;
					// logger.info(file.getAbsolutePath());
					// logger.info("logreader : " + returnstr);
					 }
				}
				
				loginput.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());								
			}
		}
		
		return returnstr;
	}
	
	private String logwriter (File file, String comments)
	{
		String returnstr = "";
				
		if (file.exists())
		{
			try {			
				BufferedWriter logout = new BufferedWriter(new FileWriter(file, false));
				logout.write(comments);
				logout.flush();
				logout.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				returnstr = e.toString();
			}
		}
		
		return returnstr;
	}
	
	 public void copyFile(File oldFileDB, File newFileDB) throws Exception {
		 
		  logger.info("[AutomatedDislocBean/copyFile] From " + oldFileDB.toString() + " to " + newFileDB.toString());
		  logger.info("[AutomatedDislocBean/copyFile] oldFileDB.exists() : " + oldFileDB.exists());
		  logger.info("[AutomatedDislocBean/copyFile] oldFileDB.canRead() : " + oldFileDB.canRead());
		  logger.info("[AutomatedDislocBean/copyFile] newFileDB.exists() : " + newFileDB.exists());
		  logger.info("[AutomatedDislocBean/copyFile] newFileDB.canWrite() : " + newFileDB.canWrite());
		  

		  if(oldFileDB.exists() && oldFileDB.canRead() 
			  && newFileDB.exists() && newFileDB.canWrite()) {
				FileInputStream from = null;
				FileOutputStream to = null;
				try {
					 from = new FileInputStream(oldFileDB);
					 to = new FileOutputStream(newFileDB);
					 byte[] buffer = new byte[4096];
					 int bytesRead;
					 
					 while ((bytesRead = from.read(buffer)) != -1)
						  to.write(buffer, 0, bytesRead); // write
				} finally {
					 if (from != null)
						  try {
								from.close();
						  } catch (IOException e) {
								;
						  }
					 if (to != null)
						  try {
								to.close();
						  } catch (IOException e) {
								;
						  }
				 }
		  }
		  else {
				System.err.println("[AutomatedDislocBean/copyFile] Copy failed");
		  }
	 }

	public String test(String name) {
		Properties properties = new Properties();
		
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		String dir = properties.getProperty("output.dest.dir");	
		
		// logger.info("[getContextBasePath] called");
		
		File logfile = new File(dir + "/" + "log.txt");
		
		try {
			logfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}



		Date date = new Date();
		Date date1 = new Date();
		String s = logreader(logfile, false);		

		try {
			if (s=="" || s==null)
				date1.setTime(0);
			else
				date1.setTime(dateFormat.parse(s).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		Date date2 = new Date(date.getTime()-date1.getTime());	
		
		//1 hour = 3600000
		if (date2.getTime() > 3600000)
			logwriter(logfile, dateFormat.format(date1));
		
		return "hello, " + name;
	}
	 
	 /**
	  * This method inspects the log file to see if it is time to update the
	  * runs.  
	  *
	  * @param File logfile the file that contains the timestamp of the previous update
	  * @param int timeInterval the minimum interval in milliseconds between updates
	  */
	 protected boolean timeToRunUpdate(File logfile, int timeIntervalInSeconds){
		  int timeIntervalInMilliseconds=timeIntervalInSeconds*1000;
		try {
			 if (!logfile.exists()) {
				logfile.createNewFile();
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 logger.error(e.getMessage());
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
		Date date = new Date();
		Date date1 = new Date();
		String s = logreader(logfile, false);
		
		// logger.info("[RunautomatedDisloc/run] " + s);
		try {
			if (s=="" || s==null)
				date1.setTime(0);
			else
				date1.setTime(dateFormat.parse(s).getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		Date date2 = new Date(date.getTime()-date1.getTime());	
		
		logger.info("[RunautomatedDisloc/run] the last update time : " + dateFormat.format(date1));
		logger.info("[RunautomatedDisloc/run] the current time : " +  dateFormat.format(date));

		if (date2.getTime() > timeIntervalInMilliseconds) {
			 //			 logwriter(logfile, dateFormat.format(date));	  
			 return true;
		}
		else {
			 return false;
		}

	 }
	
	 /**
	  * REVIEW: this doens't seem to be used.
	  */ 
	 public void getProject(String projectname) {
		  
		  ObjectContainer db = null;
		  
		  File projectDir = new File(getContextBasePath() + "/overm5/");
		  
		  if (!projectDir.exists())
				projectDir.mkdirs();
		  
		  try {
				db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
				
				DislocProjectBean tmp = new DislocProjectBean();
				ObjectSet results = db.get(DislocProjectBean.class);
				
				// Create a new project. This may be overwritten later
				DislocProjectBean currentProject = new DislocProjectBean();
				
				currentProject.setProjectName(projectname);
				
				// See if project already exists
				while (results.hasNext()) {
					 tmp = (DislocProjectBean) results.next();
					 // This is a screwed up project so delete it.
					 if (tmp == null || tmp.getProjectName() == null) {
						  db.delete(tmp);
					 }
					 // The project already exists, so update it.
					 else if (tmp.getProjectName().equals(projectname)) {					
						  currentProject = tmp;
						  // logger.info("[getProject]" + currentProject.getProjectName());
						  break;
					 }
				}
				
		  } catch (Exception e) {			
			logger.error("[RunautomatedDisloc/getProject] " + e);
		  }
		  finally {
				if (db != null) db.close();			
		  }	
	 }
	 
	 /**
	  * REVIEW: this is a strange method, not sure what it is doing.
	  */ 
	 public void getDislocProjectSummaryBeanCount() {
		  
		  ObjectContainer db = null;
		  
		  File projectDir = new File(getContextBasePath() + "/overm5/");
		  
		  if (!projectDir.exists()) projectDir.mkdirs();
		  
		  try {
				File f = new File(getContextBasePath() + "/overm5_temp.db");
				if (!f.exists())
					 logger.info("[RunautomatedDisloc/DislocProjectSummaryBean] overm5_temp doesn't existing.");
				
				else {
					 
					 db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
					 ObjectSet results = db.get(DislocProjectSummaryBean.class);	
					 logger.info("[RunautomatedDisloc/DislocProjectSummaryBean] the number of DislocProjectSummaryBean in the overm5_temp.db : " + results.size());
					 
					 results = db.get(InsarParamsBean.class);
					 logger.info("[RunautomatedDisloc/DislocProjectSummaryBean] the number of InsarParamasBean in the overm5_temp.db : " + results.size());
					 
				}					
				
		  } catch (Exception e) {			
				logger.error("[RunautomatedDisloc/getDislocProjectSummaryBeanCount] " + e);
		  }
		  finally {
				if (db != null){
					 db.close();
					 logger.info("[RunautomatedDisloc/getDislocProjectSummaryBeanCount] db closed");
				}
		  }	
	 }

	 /** 
	  * Utility method for setting up various class-scoped File objects.
	  * Returns a boolean: true if we will append to an existing file, false 
	  * if we need to start from scratch.
	  */
	 protected void setUpKmlFileLocations(String destDirName, String destFileName) throws Exception {
		  
		  boolean appendFile=false; 
		  
		  try {
				//Set up the destination directory object and create it on the file system if
				//necessary
				destDir=new File(destDirName);
				if (!destDir.exists()) destDir.mkdirs();	
				
				String localDestination=destDirName+"/"+destFileName;
				// File oldFile = new File(localDestination);
				// if (oldFile.exists()) {
				// 	 logger.info("[RunautomatedDisloc/run] Deleting old fault kml file");
				// 	 oldFile.delete();	
				// }
				//Set up also the printwriter.  This is not very clean.
				logger.info(localDestination);
				out = new PrintWriter(new FileWriter(localDestination));		
				
		  }
		  catch(Exception ex) {
				logger.error(ex.getMessage());
				throw ex;
		  }
	 }

	 /**
	  * This class closes some class-scoped IO objects.
	  */ 
	 protected void cleanupIOObjects() {
		  try {
				out.flush();
		  }
		  catch (Exception ex){
				logger.error(ex.getMessage());
		  }
		  finally {
				out.close();
				out=null;
		  }
	 }
	 /**
	  *
	  */
	 protected Fault setFaultType(int nB, Entry entry) {
		  
		  Fault fault = new Fault(); 
		  boolean thr=false;
		  double mu = 0.2E11;
		  String lat_start = entry.getGeorss_point().trim().split(" ")[0];
		  String lon_start = entry.getGeorss_point().trim().split(" ")[1];

		  fault.setFaultName(entry.getId().split(":")[3]);
		  fault.setFaultLonStart(Double.parseDouble(lon_start));
		  fault.setFaultLatStart(Double.parseDouble(lat_start));
		  fault.setFaultLameLambda(1.0);
		  fault.setFaultLameMu(1.0);
		  
		  //This is scenario #1
		  if (nB == 0) {					
				fault.setFaultDipAngle(90);
				fault.setFaultDipSlip(0);
				fault.setFaultStrikeSlip(0);
				fault.setFaultStrikeAngle(0);
				thr = false;
				}
		  
		  //This is scenario #2
		  else if (nB == 1) {
				fault.setFaultDipAngle(90);
				fault.setFaultDipSlip(0);
				fault.setFaultStrikeSlip(0);
				fault.setFaultStrikeAngle(45);
				thr = false;
		  }
		  
		  //Scenario #3
		  else if (nB == 2) {
				fault.setFaultDipAngle(45);
				fault.setFaultDipSlip(0);
				fault.setFaultStrikeSlip(0);
				fault.setFaultStrikeAngle(0);
				thr = true;
		  }
		  
		  //Scenario #4
		  else if (nB == 3) {
				fault.setFaultDipAngle(45);
				fault.setFaultDipSlip(0);
				fault.setFaultStrikeSlip(0);
				fault.setFaultStrikeAngle(90);
				thr = true;
		  }
		  
		  //Now set the fault length and width 
		  double length;
		  double width;
		  if (!entry.isMover7()) {
				//If magnitude is less than 7, the fault is square.
				length = Math.sqrt(Math.sqrt(Math.pow(10, (3*(entry.getM()+10.7)/2))/(mu*0.6E-10)))/1E5;
				width = length;
		  }
		  else {				
				//Use a fixed width for M>7; the fault is rectangular instead of square
				//REVIEW: "20" is some magic number.
				width = 20;
				length = Math.sqrt(Math.pow(10,(3*(entry.getM()+10.7)/2))/(mu*0.6E-10))/(width*1E10);
		  }
		  
		  fault.setFaultLength(length);
		  fault.setFaultWidth(width);
		  
		  //Now set the depth.
		  //REVIEW: next several lines need to be explained.
		  fault.setFaultDepth(width/2);
		  double slip = 0.6*length*width;				
		  if (thr) {
				fault.setFaultDipSlip(slip * 10);
		  }
		  else {
				fault.setFaultStrikeSlip(slip * 10);
		  }


		  //Now set the location in Cartesian and real-Earth coordinates
		  //We'll assume only one fault per simulation, so it will be 
		  //located at the origin
		  double x = 0; // because this will be always the first fault?
		  double y = 0; // because this will be always the first fault?
		  fault.setFaultLocationX(Double.parseDouble(df.format(x)));
		  fault.setFaultLocationY(Double.parseDouble(df.format(y)));
		  
		  //REVIEW: these should be inherited from the grandparent class.
		  //Also, all of this stuff should really be in a separate function.
		  double d2r = Math.acos(-1.0) / 180.0;
		  double flatten = 1.0/298.247;
		  double theFactor = d2r * Math.cos(d2r*Double.parseDouble(lat_start)) * 6378.139 * ( 1.0 - Math.sin(d2r * Double.parseDouble(lat_start)) * Math.sin(d2r * Double.parseDouble(lat_start)) * flatten);
		  
		  //Set the strike angle
		  double xval, yval;
		  double sa = fault.getFaultStrikeAngle();
		  if (sa == 0) {
				xval = 0;
				yval = length;
		  }
		  else if (sa == 90) {
				xval = length;
				yval = 0;
		  }
		  else if (sa == 180) {
				xval = 0;
				yval = (-1.0) * length;
		  }
		  else if (sa == 270) {
				xval = (-1.0) * length;
				yval = 0;
		  }				
		  else {
				double sval = 90 - sa;
				double thetan = Math.tan(sval * d2r);
				xval = length / Math.sqrt(1+thetan*thetan);
				yval = Math.sqrt(length * length - xval * xval);
				
				if (sa > 0 && sa < 90) {
					 xval = xval * 1.0;
					 yval = yval * 1.0;
				}
				else if (sa > 90 && sa < 180) {
					 xval = xval * 1.0;
					 yval = yval * (-1.0);
				}
				else if (sa > 180 && sa < 270) {
					 xval = xval * (-1.0);
					 yval = yval * (-1.0);
				}
				else if (sa > 270 && sa < 360) {
					 xval = xval * (-1.0);
					 yval = yval * 1.0;
				}
		  }

		  //Now set the fault ending location lat/lon.				
		  double lon_end = (xval * 1.0) / theFactor + (Double.parseDouble(lon_start) * 1.0);
		  double lat_end = (yval/111.32) + (Double.parseDouble(lat_start) * 1.0);
		  
		  lon_end = Math.round(lon_end * 100)/100.0;
		  lat_end = Math.round(lat_end * 100)/100.0;
		  
		  fault.setFaultLonEnd(lon_end);
		  fault.setFaultLatEnd(lat_end);
		  
		  return fault;
	 }	  

	 /**
	  * This creates and returns the project name.
	  */
	 protected String createProjectName(Entry entry, Fault fault, int nB){
		  return entry.getTitle() + "(" + entry.getId().split(":")[3] + ")_n_DA" + (double)Math.round((double)fault.getFaultDipAngle()*1000)/1000 + "_SA" + (double)Math.round((double)fault.getFaultStrikeAngle()*1000)/1000 + "_DS" + (double)Math.round((double)fault.getFaultDipSlip()*1000)/1000 + "_SS" + (double)Math.round((double)fault.getFaultStrikeSlip()*1000)/1000 + "_CASE_" + nB;

	 }

	 /**
	  * This method collects the code used to create the parameter bean.
	  */
	 protected DislocParamsBean createDislocParamsBean(String projectname,
																		Fault fault) {
		  DislocParamsBean tmp=new DislocParamsBean();
		  tmp = new DislocParamsBean();
		  tmp.setOriginLat(fault.getFaultLatStart());
		  tmp.setOriginLon(fault.getFaultLonStart());
		  
		  //Various magic numbers. These should be defined as static final
		  //fields somewhere.
		  tmp.setGridMinXValue(-100);
		  tmp.setGridMinYValue(-100);
		  tmp.setGridXIterations(420);
		  tmp.setGridXSpacing(0.5);
		  tmp.setGridYIterations(420);
		  tmp.setGridYSpacing(0.5);
		  
		  return tmp;
	 }

	 /**
	  * Puts the named proejct into the db.
	  */
	 protected DislocParamsBean putProjectInDB(String projectname, 
															 Fault fault) {
		  File f = new File(getContextBasePath() + "/overm5/" + projectname + ".db");
		  if (f.exists()) f.delete();
				
		  ObjectContainer db = null;
		  
		  DislocParamsBean tmp=null;
				
		  try {
				db = Db4o.openFile(getContextBasePath() + "/overm5/" + projectname + ".db");
				
				db.set(fault);
				db.commit();
				
				tmp = new DislocParamsBean();
				tmp.setOriginLat(fault.getFaultLatStart());
				tmp.setOriginLon(fault.getFaultLonStart());
					
				//Various magic numbers. These should be defined as static final
				//fields somewhere.
				tmp.setGridMinXValue(-100);
				tmp.setGridMinYValue(-100);
				tmp.setGridXIterations(420);
				tmp.setGridXSpacing(0.5);
				tmp.setGridYIterations(420);
				tmp.setGridYSpacing(0.5);
				
				db.set(tmp);
				db.commit();				
				
		  } catch (Exception e) {			
				logger.error("[RunautomatedDisloc/createProjectFromRss] " + e);
		  }
		  
		  finally {
				if (db != null) db.close();
		  }

		  return tmp;
	 }
	 
	 /**
	  * Print the KML for this fault.
	  */
	 protected void printKmlForEarthquakeEntry(Entry entry,
															 Fault fault, 
															 PrintWriter out, 
															 OutputURLs ouls, 
															 String projectname,
															 String projectShortName) {
				out.println(pmBegin);
				out.println("<name>"+projectname+"</name>");
				out.println("<shortName>"+projectShortName+"</shortName>");
				out.println(descBegin);				
				String s = "<![CDATA[<b>Fault Name</b>: " + projectShortName + "<br><b>LatStart</b>: " + fault.getFaultLatStart() + "<br><b>LonStart</b>: " + fault.getFaultLonStart() + "<br><b>Length</b>: " + fault.getFaultLength() + " <br><b>Width</b>: " + fault.getFaultWidth() + "<br><b>Depth</b>: " + fault.getFaultDepth() + "<br><b>Dip Angle</b>: " + fault.getFaultDipAngle() + "<br><b>Strike Angle</b>: " + fault.getFaultStrikeAngle() + "<br><b>Dip Slip</b>: " + fault.getFaultDipSlip() + "<br><b>Strike Slip</b>: " + fault.getFaultStrikeSlip() + "<br><b>Location [x, y]</b>: [" + fault.getFaultLocationX() + ", " + fault.getFaultLocationY() + "]<br><b>Updated</b>:  " + entry.getUpdated() + "<br><a href= \"" + ouls.getDislocoutputURL() + "\"><b>DislocOutputURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getDisplacementkmlURL() + "&t=p\"><b>DisplacementKmlURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getInsarkmlURL() +"&t=p\"><b>InsarKmlURL</b></a><br><b>Comment</b>: Source URL is <a href=\""+url+"\">"+url+"<br><a href=\"http://www.quakesim.org\">QuakeSim Project</a><br>]]>";				
				out.println(s);
				out.println(descEnd);
				
				out.println(lsBegin);
				
				out.println(coordBegin);
				out.println(fault.getFaultLonStart() + "," + fault.getFaultLatStart() + " " + fault.getFaultLonEnd() + "," + fault.getFaultLatEnd());				
				out.println(coordEnd);
				
				out.println(lsEnd);
				// 
				out.println("<DislocOutputURL>");
				out.println(ouls.getDislocoutputURL());
				out.println("</DislocOutputURL>");
				
				out.println("<DisplacementKmlURL>");
				out.println(ouls.getDisplacementkmlURL());
				out.println("</DisplacementKmlURL>");
				
				out.println("<InsarKmlURL>");
				out.println(ouls.getInsarkmlURL());
				out.println("</InsarKmlURL>");
				
				out.println(pmEnd);

	 }
	 /**
	  * This method is used to determine arrow scale.  It is stateful and uses
	  * class-scoped variables, in case we have several arrow layers that we want to
	  * put on the same plot with the same scale (which we do).
	  *
	  * REVIEW: this should go in some utility class in GenericQuakeSimProject
	  */ 
	 protected double setGlobalKmlArrowScale(PointEntry[] pointEntries){
		  
			logger.info("[SimplexDataKml/setArrowPlacemark] pointEntries.length : " 
							+ pointEntries.length);
			for (int i = 0; i < pointEntries.length; i++) {
				
				double x=Double.valueOf(pointEntries[i].getX());
				double y=Double.valueOf(pointEntries[i].getX());
				if(x<projectMinX) projectMinX=x;
				if(x>projectMaxX) projectMaxX=x;
				if(y<projectMinY) projectMinY=y;
				if(y>projectMaxY) projectMaxY=y;
				
				double dx = Double.valueOf(pointEntries[i].getDeltaXValue()).doubleValue(); 
				double dy = Double.valueOf(pointEntries[i].getDeltaYValue()).doubleValue();			 
				double length = Math.sqrt(dx * dx + dy * dy);
				
				// System.out.println("[SimpleXService/setArrowPlacemark] dx : " + dx);
				// System.out.println("[SimpleXService/setArrowPlacemark] dy : " + dy);
				
				
				if (i == 0)
					longestlength = length; 
				
				else if (length > longestlength)
					longestlength = length; 
			}
			logger.info("[SimpleXService/setArrowPlacemark] longestlength : " + longestlength);			
			
			double projectLength=(projectMaxX-projectMinX)*(projectMaxX-projectMinX);
			projectLength+=(projectMaxY-projectMinY)*(projectMaxY-projectMinY);
			projectLength=Math.sqrt(projectLength);
			
			//We arbitrarly set the longest displacement arrow to be 10% of the 
			//project dimension.
			double scaling = 0.7*projectLength/longestlength;
			
			logger.info("[SimpleXService/setArrowPlacemark] projectLength : " + projectLength);			
			return scaling;
		  
	 }

	 /**
	  * These are class-scoped variables that are stateful.  We 
	  * encapsulate here the steps to re-initialize them.
	  */
	 protected void resetScalingVariables (){
		  //All of these should be easily replaced.  Note value settings are 
		  //superficially counter-intuitive: we want the default min values to 
		  //be positive infinity because the first tested value will be less than
		  //this value and thus replace it.
		  scale=0.0;
		  longestlength = 0.;
		  projectMinX=Double.POSITIVE_INFINITY;
		  projectMaxX=Double.NEGATIVE_INFINITY;
		  projectMinY=Double.POSITIVE_INFINITY;
		  projectMaxY=Double.NEGATIVE_INFINITY;
	 }

	 public void setUpToDate(boolean upToDate){
		  this.upToDate=upToDate;
	 };

	 public boolean getUpToDate(){
		  return this.upToDate;
	 }
	 
	 /** 
	  * This makes a shorter name for the project by omitting the 
	  * extension metadata. This is only used by the KML
	  */
	 protected String makeShortProjectName(String projectName) {
		  return projectName.substring(0,projectName.indexOf("_n_"));
	 }
	 
	 /**
	  * See if we have made a calculation for this entry before. 
	  */
	 protected boolean foundPreviousEntry(String projectname) {
		  logger.info("Looking for previous entries:"+projectname);
		  ObjectContainer db=null;
		  boolean hasEntry=false;
		  try {
				//Open the old database
				db=Db4o.openFile(getContextBasePath() + "/overm5.db");
				ObjectSet results=db.get(DislocProjectSummaryBean.class);
				logger.info("Previous project summaries:"+results.size());
				
				//If we find a previous entry, we are done, so close up and return true.
				while(results.hasNext()){
					 DislocProjectSummaryBean tmp=(DislocProjectSummaryBean) results.next();
					 logger.info(tmp.getProjectName()+" "+projectname);
					 if(tmp.getProjectName().equals(projectname)){
						  hasEntry=true;
						  logger.info("Found previous entry for "+projectname+":"+hasEntry);
						  db.close();
						  return hasEntry;
					 }
				}
		  }
		  catch(Exception ex){
				hasEntry=false;
				ex.printStackTrace();
		  }
		  
		  finally {
				if(db!=null) db.close();
		  }
		  //If we get here, hasEntry is false.
		  logger.info("-------Found previous entry for "+projectname+":"+hasEntry+"-----------");
		  return hasEntry;
	 }
	 
	 protected String getMyKmlUrlFromDB(String projectName){
		  String kmlUrl="";
		  ObjectContainer db=null;
		  try {
				db=Db4o.openFile(getContextBasePath() + "/overm5.db");
				ObjectSet results=db.get(DislocProjectSummaryBean.class);
				while(results.hasNext()){
					 DislocProjectSummaryBean testbean=(DislocProjectSummaryBean)results.next();
					 if(testbean.getProjectName().equals(projectName)){
						  logger.info("Found KML URL in DB:"+testbean.getKmlurl());
						  return testbean.getKmlurl();
					 }
				}
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  finally {
				if(db!=null) db.close();
		  }
		  
		  return kmlUrl;
		  
	 }

	 /**
	  * This creates Disloc project beans for each entry name in the 
	  * ArrayList. In practice, it will create four projects for each
	  * earthquake entry in the RSS feed.
	  * 
	  * Note this assumes this service and the associated client
	  * (from RssDisloc3) are using the same file system.  
	  *
	  * REVIEW: The requirement that the client and server share
	  * a file system is bad design and needs to be rethought. Also the
	  * pattern "overm5" is used as a magic string in both
	  * the service and client webapps.
	  *
	  * REVIEW: What is the heck is this method doing anyway? It creates a bunch of
	  * empty DislocProjectBeans.
	  */
	protected void createProject(ArrayList <String> arrayList) {
		
		ObjectContainer db = null;		
		File projectDir = new File(getContextBasePath());
		
		if (!projectDir.exists()){
			 projectDir.mkdirs();
		}
		
		try {			
			 //REVIEW: "over5" name pattern is a magic string and should be moved to a
			 //system property.
			db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
			
			//REVIEW: are the next two lines necessary?
			DislocProjectBean tmp = new DislocProjectBean();
			ObjectSet results = db.get(DislocProjectBean.class);

			for (int nA = 0 ; nA < arrayList.size() ; nA++) {
				// Create a new project. This may be overwritten later
				DislocProjectBean currentProject = new DislocProjectBean();
				currentProject.setProjectName(arrayList.get(nA));
				db.set(currentProject);				
			}
			logger.info("[RunautomatedDisloc/createProject] finished");
			db.commit();
		} catch (Exception e) {			
			 logger.error("[RunautomatedDisloc/createProject] " + e.getMessage());
		}
		finally {
			if (db != null)  db.close();			
		}
	}


}

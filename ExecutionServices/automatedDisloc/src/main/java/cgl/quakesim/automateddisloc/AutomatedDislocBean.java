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

import cgl.webservices.KmlGenerator.ExtendedSimpleXDataKml;
import cgl.webservices.KmlGenerator.ExtendedSimpleXDataKmlServiceLocator;
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
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class AutomatedDislocBean {
	 static final int EARTHQUAKE_SLIP_SCENARIOS=4;
	 static final int ONE_HOUR_IN_MILLISECONDS=3600000;
	 private static Logger logger=Logger.getLogger(AutomatedDislocBean.class);

	 //The input is the URL of the RSS/Atom feed we are processing.
	 public void run(String url) {
		  logger.debug("URL for feed:"+url);
		  //Really necessary to use a thread here?
		  RunautomatedDisloc rd = new RunautomatedDisloc(url);
		  rd.start();
	 }	
}

class RunautomatedDisloc extends Thread {
	 private static Logger logger=Logger.getLogger(RunautomatedDisloc.class);
	 //REVIEW: Shouldn't be hard coded.  Put in a property.
	 //REVIEW: actually, it isn't used.  The "url" variable passed in to run() is 
	 //the URL to use. 
	 //	 String mover5_rss_url = "http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml";
	 //	String mover5_rss_url = "http://localhost:8080/7day-M5.xml";
	DecimalFormat df = new DecimalFormat(".###");
	String contextBasePath;
	String url = "";
	String tomcatbase;
	
	String dislocServiceUrl;
	String dislocExtendedServiceUrl;
	String faultDBServiceUrl;
	String kmlGeneratorBaseurl;
	String kmlGeneratorUrl;
	String insarkmlServiceUrl;	
	String insarKmlUrl;
	String rssdisloc_dir_name;
	String baseurl;
	
	String elevation = "60";
	String azimuth = "0";
	String frequency = "1.26";
	
	String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	String kmlHead = "<kml xmlns=\"http://earth.google.com/kml/2.2\">";
	String kmlEnd = "</kml>";
	String pmBegin = "<Placemark>";
	String pmEnd = "</Placemark>";
	String lsBegin = "<LineString>";
	String lsEnd = "</LineString>";
	String pointBegin = "<Point>";
	String pointEnd = "</Point>";
	String coordBegin = "<coordinates>";
	String coordEnd = "</coordinates>";
	String docBegin = "<Document>";
	String docEnd = "</Document>";
	String comma = ", ";
	String descBegin = "<description>";
	String descEnd = "</description>";
	
	int limitedsize = 800; 

	public RunautomatedDisloc(String url) {
		this.url = url;
	}

	 /**
	  * Load the project properties and set values.
	  */
	public void loadProperties() {
		Properties properties = new Properties();
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		// logger.debug("[getContextBasePath] called");
		contextBasePath = properties.getProperty("output.dest.dir");
		dislocServiceUrl = properties.getProperty("dislocServiceUrl"); 
		dislocExtendedServiceUrl = properties.getProperty("dislocExtendedServiceUrl");		
		// kmlGeneratorBaseurl = properties.getProperty("output.dest.dir");
		kmlGeneratorUrl = properties.getProperty("kmlGeneratorUrl");
		insarkmlServiceUrl = properties.getProperty("insarkmlServiceUrl"); 
		rssdisloc_dir_name= properties.getProperty("rssdisloc.dir.name");
		baseurl = properties.getProperty("baseurl");
		tomcatbase = properties.getProperty("tomcat.base");
		// insarKmlUrl = properties.getProperty("output.dest.dir");
		// logger.debug("[getContextBasePath] " + properties.getProperty("output.dest.dir"));

		logger.debug("tocmat base:"+tomcatbase);
		logger.debug("baseurl:"+baseurl);
		logger.debug("rssdisloc_dir_name:"+rssdisloc_dir_name);

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
	  * This creates a Disloc project.
	  * Note this assumes this service and the associated client
	  * (from RssDisloc3) are using the same file system.  
	  *
	  * REVIEW: This is bad design and needs to be rethought. Also the
	  * pattern "overm5" is used as a magic string in both
	  * the service and client webapps.
	  */
	public void createProject(String[] arrayList) {
		
		ObjectContainer db = null;		
		File projectDir = new File(getContextBasePath());
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		try {			
			 //REVIEW: "over5" name pattern is a magic string and should be moved to a
			 //system property.
			db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
			
			DislocProjectBean tmp = new DislocProjectBean();
			
			ObjectSet results = db.get(DislocProjectBean.class);

			for (int nA = 0 ; nA < arrayList.length ; nA++) {
				// Create a new project. This may be overwritten later
				DislocProjectBean currentProject = new DislocProjectBean();
				currentProject.setProjectName(arrayList[nA]);
				// logger.debug("[RunautomatedDisloc/createProject] " + arrayList[nA]);
				
				db.set(currentProject);				
			}
			logger.debug("[RunautomatedDisloc/createProject] finished");
			db.commit();
		} catch (Exception e) {			
			logger.error("[RunautomatedDisloc/createProject] " + e);
		}
		finally {
			if (db != null)  db.close();			
		}
		// logger.debug("[createProject] " + arrayList.length);
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
					// logger.debug("[getProject]" + currentProject.getProjectName());
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
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		try {
			File f = new File(getContextBasePath() + "/overm5_temp.db");
			if (!f.exists())
				logger.debug("[RunautomatedDisloc/DislocProjectSummaryBean] overm5_temp doesn't existing.");
				
			else {
				
				db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
				ObjectSet results = db.get(DislocProjectSummaryBean.class);	
				logger.debug("[RunautomatedDisloc/DislocProjectSummaryBean] the number of DislocProjectSummaryBean in the overm5_temp.db : " + results.size());
				
				results = db.get(InsarParamsBean.class);
				logger.debug("[RunautomatedDisloc/DislocProjectSummaryBean] the number of InsarParamasBean in the overm5_temp.db : " + results.size());
				
			}					
						
		} catch (Exception e) {			
			logger.error("[RunautomatedDisloc/getDislocProjectSummaryBeanCount] " + e);
		}
		finally {
			if (db != null){
				db.close();
				logger.debug("[RunautomatedDisloc/getDislocProjectSummaryBeanCount] db closed");
			}
		}	
	}

	 /**
	  * This implements the required run() method for this thread. 
	  */ 
	public void run() {
		
		Properties properties = new Properties();
		//REVIEW: this seems redundant with loadProperties below.
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		loadProperties();
		
		String dir = properties.getProperty("output.dest.dir");
		
		// logger.debug("[getContextBasePath] called");
		
		File logfile = new File(dir + "/" + "log.txt");
		
		try {
			 if (!logfile.exists()) {
				logfile.createNewFile();
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 logger.error(e.getMessage());
			 //			logger.error(e.getMessage());
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
		Date date = new Date();
		Date date1 = new Date();
		String s = logreader(logfile, false);
		
		// logger.debug("[RunautomatedDisloc/run] " + s);
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
		
		logger.debug("[RunautomatedDisloc/run] the last update time : " + dateFormat.format(date1));
		logger.debug("[RunautomatedDisloc/run] the current time : " +  dateFormat.format(date));
		
		getDislocProjectSummaryBeanCount();
		
		//If it has been longer than 1 hour, get more data.  Otherwise, do nothing.
		if (date2.getTime() > AutomatedDislocBean.ONE_HOUR_IN_MILLISECONDS) {
			logwriter(logfile, dateFormat.format(date));
			logger.debug("[RunautomatedDisloc/run] updated");

			//Make sure the project master directory exists.
			File projectDir = new File(getContextBasePath() + "/overm5/");
			if (!projectDir.exists()) {
				projectDir.mkdirs();
			}
			
			File f = new File(getContextBasePath() + "/overm5_temp.db");
			if (f.exists()) f.delete();
			
			//This does the interesting work.
			createProjectsFromRss(url);
			
			// to allow other process to have access to overm5.db while this webservice is updating, it's working on a temporary file and copying it at the end.
			File oldFileDB = new File(getContextBasePath() + "/overm5_temp.db");			
			File newFileDB = new File(getContextBasePath() + "/overm5.db");
			
			try {
				 if (!newFileDB.exists()) newFileDB.createNewFile();
				 copyFile(oldFileDB, newFileDB);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				 logger.error(e.getMessage());
				 //				logger.error(e.getMessage());
			}
		}
	}
	
	 /**
	  * This method constructs the project from the USGS RSS file.
	  */ 
	public void createProjectsFromRss(String url) {		
			
		CglGeoRssParser cgrp = new CglGeoRssParser();
		cgrp.parse(url);		
		List entry_list = new ArrayList();
		List pns = new ArrayList();
		entry_list = cgrp.getEntryList();
		logger.debug("[RunautomatedDisloc/run] entry_list.size() : " + entry_list.size());
		
		HashMap<String, Fault> hm = new HashMap<String, Fault>();
		
		//REVIEW: These should be static final and global.  Also, thr is thrust?  I think
		//this is boolean.
		double mu = 0.2E11;
		double thr = 0;
				
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
		String localDestination = getContextBasePath() + "/../../../../../" + "gridsphere" + "/overm5.kml";
		
		logger.debug("[RunautomatedDisloc/run] Old fault kml file:" + localDestination);
		
		File d = new File(getContextBasePath() + "/../../../../../" + "gridsphere");
		if (!d.exists()) d.mkdirs();	
		
		File oldFile = new File(localDestination);
		if (oldFile.exists()) {
			logger.debug("[RunautomatedDisloc/run] Deleting old fault kml file");
			oldFile.delete();			
		}
		
		long timeStamp = (new Date()).getTime();
		PrintWriter out = null;			 
		//REVIEW: I think this try-catch is not properly constructed.
		try {
			out = new PrintWriter(new FileWriter(localDestination));
			out.println(xmlHead);
			out.println(kmlHead);
			out.println(docBegin);
		} catch (IOException e) {
				// TODO Auto-generated catch block			
			 //			 logger.error(e.getMessage());			
			 logger.error(e.getMessage());
		}		 

		for (int nA = 0 ; nA < entry_list.size() ; nA++) {
			Entry entry = (Entry) entry_list.get(nA);
			String lat_start = entry.getGeorss_point().trim().split(" ")[0];
			String lon_start = entry.getGeorss_point().trim().split(" ")[1];
			
			// logger.debug("[createProjectsFromRss] " + entry.getTitle() + "/ " + lat_start + ", " + lon_start);
			
			//We check for 4 possible scenarios.
			for (int nB = 0 ; nB < AutomatedDislocBean.EARTHQUAKE_SLIP_SCENARIOS ; nB++) {
				
				// String projectname = entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1);
				// logger.debug("[createProjectsFromRss] " + projectname + "/" + entry.getId().split(":")[3]);
								
				Fault fault = new Fault(); 
				
				fault.setFaultName(entry.getId().split(":")[3]);
				fault.setFaultLonStart(Double.parseDouble(lon_start));
				fault.setFaultLatStart(Double.parseDouble(lat_start));
				fault.setFaultLameLambda(1.0);
				fault.setFaultLameMu(1.0);
				
				//This is scenario #1
				//REVIEW: width and length are set below, so why set dummy (0) values here.
				if (nB == 0) {					
					fault.setFaultLength(0);
					fault.setFaultDipAngle(90);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(0);
					fault.setFaultWidth(0);		
					thr = 0;
				}
				
				//This is scenario #2
				else if (nB == 1) {
					fault.setFaultLength(0);
					fault.setFaultDipAngle(90);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(45);
					fault.setFaultWidth(0);
					thr = 0;
				}
				
				//Scenario #3
				else if (nB == 2) {
					fault.setFaultLength(0);
					fault.setFaultDipAngle(45);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(0);
					fault.setFaultWidth(0);
					thr = 1;
				}
				
				//Scenario #4
				else if (nB == 3) {
					fault.setFaultLength(0);
					fault.setFaultDipAngle(45);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(90);
					fault.setFaultWidth(0);
					thr = 1;
				}
				
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

				//REVIEW: next several lines need to be explained.
				fault.setFaultDepth(width/2);
				double slip = 0.6*length*width;				
				if (thr == 1) {
					fault.setFaultDipSlip(slip * 10);
				}
				else {
					fault.setFaultStrikeSlip(slip * 10);
				}
				
				
				// double x = (lonStart - currentParams.getOriginLon()) * factor(currentParams.getOriginLon(), currentParams.getOriginLat());
				// double y = (latStart - currentParams.getOriginLat()) * 111.32;				

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
				
				double lon_end = (xval * 1.0) / theFactor + (Double.parseDouble(lon_start) * 1.0);
				double lat_end = (yval/111.32) + (Double.parseDouble(lat_start) * 1.0);
				
				lon_end = Math.round(lon_end * 100)/100.0;
				lat_end = Math.round(lat_end * 100)/100.0;
				
				fault.setFaultLonEnd(lon_end);
				fault.setFaultLatEnd(lat_end);

				//REVIEW and this code has little to do with what came before.  It should 
				//be in a separate function.
				String projectname =  entry.getTitle() + "(" + entry.getId().split(":")[3] + ")_n_DA" + (double)Math.round((double)fault.getFaultDipAngle()*1000)/1000 + "_SA" + (double)Math.round((double)fault.getFaultStrikeAngle()*1000)/1000 + "_DS" + (double)Math.round((double)fault.getFaultDipSlip()*1000)/1000 + "_SS" + (double)Math.round((double)fault.getFaultStrikeSlip()*1000)/1000 + "_CASE_" + nB;
				// String projectname = entry.getTitle() + "_" + nB;
				
				pns.add(projectname);
				hm.put(projectname, fault);
				
				// getProject(entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1));
				
				//REVIEW: more disconnected code. Also, note the DB is deleted.
				File f = new File(getContextBasePath() + "/overm5/" + projectname + ".db");
				if (f.exists()) f.delete();
				
				ObjectContainer db = null;
				DislocParamsBean tmp = null;
				
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

				OutputURLs ouls = null;
				
				// Now we assign x20 density by -100, -100, 0.5, 420. 09/22/2010 Jun Ji
				try {
					ouls = runBlockingDislocJSF(projectname, tmp, fault, entry.getM(), nB);
					getDislocProjectSummaryBeanCount();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				
				//REVIEW: Note these outs are associated with the the printwriter created
				//previously. This is terrible design.
				out.println(pmBegin);
				out.println("<name>"+projectname+"</name>");
				
				out.println(descBegin);				
				String s = "<![CDATA[<b>Fault Name</b>: " + projectname + "<br><b>LatStart</b>: " + fault.getFaultLatStart() + "<br><b>LonStart</b>: " + fault.getFaultLonStart() + "<br><b>Length</b>: " + fault.getFaultLength() + " <br><b>Width</b>: " + fault.getFaultWidth() + "<br><b>Depth</b>: " + fault.getFaultDepth() + "<br><b>Dip Angle</b>: " + fault.getFaultDipAngle() + "<br><b>Strike Angle</b>: " + fault.getFaultStrikeAngle() + "<br><b>Dip Slip</b>: " + fault.getFaultDipSlip() + "<br><b>Strike Slip</b>: " + fault.getFaultStrikeSlip() + "<br><b>Location [x, y]</b>: [" + fault.getFaultLocationX() + ", " + fault.getFaultLocationY() + "]<br><b>Updated</b>:  " + entry.getUpdated() + "<br><a href= \"" + ouls.getDislocoutputURL() + "\"><b>DislocOutputURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getDisplacementkmlURL() + "&t=p\"><b>DisplacementKmlURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getInsarkmlURL() +"&t=p\"><b>InsarKmlURL</b></a><br><b>Comment</b>: Source URL is <a href=\""+url+"\">"+url+"<br><a href=\"http://www.quakesim.org\">QuakeSim Project</a><br>]]>";				
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
		}
		
		out.println(docEnd);
		out.println(kmlEnd);
		out.flush();
		out.close();
		
		//REVIEW: More magic paths.
		File newFile_rssdisloc = new File(getContextBasePath() + "/../../../../../" + rssdisloc_dir_name + "/overm5.kml");
		oldFile = new File(localDestination);
		//REVIEW: what is d?
		d = new File(getContextBasePath() + "/../../../../../" + rssdisloc_dir_name);
		
		if (!d.exists()) d.mkdirs();
		
		try {
			if(!newFile_rssdisloc.exists())
				newFile_rssdisloc.createNewFile();
			copyFile(oldFile, newFile_rssdisloc);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 //			logger.error(e.getMessage());
			 logger.error(e.getMessage());
		}
		
		Set s = hm.keySet();
		String str[] = new String[pns.size()];
		
		for (int nA = 0 ; nA < pns.size() ; nA++)
			str[nA] = (String)pns.get(nA);
			
		// addFault(hm);
		createProject(str);	
	}
		
	// Using the pre-calculated insar images, disloc outputs. 09/23/2010 Jun Ji	
	private OutputURLs runBlockingDislocJSF(String projectName, DislocParamsBean currentParams, Fault fault, double M, int s_case) throws Exception {
		
		logger.debug("[AutomatedDislocBean/runBlockingDislocJSF] Started");
		OutputURLs ourls = new OutputURLs();

		try {

			Fault[] faults = new Fault[1];
			faults[0] = fault;
			
			ObsvPoint[] points = null;
			
			DislocResultsBean dislocResultsBean = getDislocResultsBean(projectName, currentParams, faults, points, M, s_case);	
			
			logger.debug("[AutomatedDislocBean/runBlockingDislocJSF] dislocResultsBean.getOutputFileUrl() :" + dislocResultsBean.getOutputFileUrl());
			ourls.setDislocoutputURL(dislocResultsBean.getOutputFileUrl());

			// This step makes the kml plots.  We allow this to fail.
			String myKmlUrl = "";			
			try {
				myKmlUrl = createKml(currentParams, dislocResultsBean, faults, projectName);
				 logger.debug("[AutomatedDislocBean/runBlockingDislocJSF] KmlUrl : " + myKmlUrl);
				 // setJobToken(dislocResultsBean.getJobUIDStamp());
			}
			catch (Exception e) {
				 logger.error(e.getMessage());
			}
			
			ourls.setDisplacementkmlURL(myKmlUrl);

			// This step runs the insar plotting stuff.  We also allow this
			// to fail.
			
			// 09/17/2010 We will assign pre-calculated images
			insarKmlUrl= getPrecalculatednsar(projectName, fault.getFaultLonStart(), fault.getFaultLatStart(), M, s_case, dislocResultsBean.getJobUIDStamp(), dislocResultsBean);
			
			ourls.setInsarkmlURL(insarKmlUrl);
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			// setInsarKmlUrl(insarKmlUrl);
			storeProjectInContext("automatedDisloc", projectName, dislocResultsBean.getJobUIDStamp(), currentParams, dislocResultsBean, myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);
			
		} catch (Exception e) {
			 logger.error(e.getMessage());
		}
		logger.debug("[AutomatedDislocBean/runBlockingDislocJSF] Finished");
		
		return ourls;
	}
	
	public Double[] dxy2lonlat(double x, double y, double reflon, double reflat) {
		
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
	public String getPrecalculatednsar(String projectName, Double reflon, Double reflat, Double M, int s_case, String jobUID, DislocResultsBean drb) {		
		
		 //The precalculated results will be in a file following the pattern below.
		File f = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output.png");
		String insarURL = null;
		String insarimageURL = null;
		
		if (f.exists()) {		
			
			logger.debug("[AutomatedDislocBean/getPrecalculatednsar] " + projectName + " is using a precalculated insar image");
			
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
			
			File d = new File(tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID);		
			if (!d.exists())
				d.mkdirs();
			
			d = new File(tomcatbase + "/webapps/insar_precalculated/insar/" + jobUID + "/" + projectName + ".output.kml");
			
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
				out = new PrintWriter(new FileWriter(d));
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
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
	
			out.flush();
			out.close();
		}
		
		else {		
			
			logger.debug("[AutomatedDislocBean/getPrecalculatednsar] " + projectName + " is generating a new precalculated insar image");
			
			InsarKmlService iks;
			try {
				iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));
				insarURL = iks.runBlockingInsarKml("automatedDisloc", projectName, drb.getOutputFileUrl(), elevation, azimuth, frequency, "ExecInsarKml");
				logger.debug("[AutomatedDislocBean/runBlockingDislocJSF] insarKmlUrl : " + insarURL);
				
				f = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case);
				if (!f.exists())
					f.mkdirs();
				
				
				File d = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case);		
				if (!d.exists())
					d.mkdirs();
				
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
	
	public DislocResultsBean getDislocResultsBean(String projectName, DislocParamsBean dislocParams, Fault[] faults, ObsvPoint[] obsvPoints, Double M, int s_case) throws Exception {
		
		File f = new File(tomcatbase + "/webapps/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output");
		DislocResultsBean drb = null;
		
		if (f.exists()) {
			
			logger.debug("[AutomatedDislocBean/getDislocResultsBean] " + projectName + " is using a precalculated disloc results");
			
			drb = new DislocResultsBean();
			drb.setProjectName(projectName);
			drb.setJobUIDStamp((new UID().toString()));
			
			drb.setInputFileUrl(createDislocInputFile(projectName, dislocParams, faults, obsvPoints, drb.getJobUIDStamp()));
					
			String outputurl = baseurl + "/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".output";
			String stdouturl = baseurl + "/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".stdout";
			String kmlurl = baseurl + "/insar_precalculated/" + "M" + M + "_" + s_case + "/M" + M + "_" + s_case + ".kml";
			
			drb.setOutputFileUrl(outputurl);
			drb.setStdoutUrl(stdouturl);
		}
		
		else {			
			
			logger.debug("[AutomatedDislocBean/getDislocResultsBean] " + projectName + " is generating a new precalculated disloc results");
			
			DislocExtendedService dislocExtendedService = new DislocExtendedServiceServiceLocator().getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
			drb = dislocExtendedService.runBlockingDislocExt("automatedDisloc", projectName, obsvPoints, faults, dislocParams, null);
			
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
		logger.debug("Input File: "+inputFile);
		PrintWriter pw=new PrintWriter(new FileWriter(inputFile),true);

		//Create the input file.  First create the grid points

		//Print the header line
		if(dislocParams.getObservationPointStyle()==1) {
			pw.println(dislocParams.getOriginLat() + " " + dislocParams.getOriginLon() + " " + dislocParams.getObservationPointStyle());
		}
		else if(dislocParams.getObservationPointStyle()==0) {
			pw.println(dislocParams.getOriginLat() + " " + dislocParams.getOriginLon() + " "  + dislocParams.getObservationPointStyle() + " " + obsvPoints.length);
		}

		else {
			logger.debug("Malformed disloc problem");
			throw new Exception();
		}


		//Print the observation point debugrmation
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
			
	public PointEntry[] LoadDataFromUrl(String InputUrl) {
		logger.debug("[AutomatedDislocBean/LoadDataFromUrl] Creating Point Entry");
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
					logger.debug("Past the faults");
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
		logger.debug("[AutomatedDislocBean/LoadDataFromUrl] Finished");
		
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}
	
	protected String createKml(DislocParamsBean dislocParams, DislocResultsBean dislocResultsBean, Fault[] faults, String projectName) throws Exception {

		// Get the project lat/lon origin. It is the lat/lon origin of the first fault.
		String origin_lat = dislocParams.getOriginLat() + "";
		String origin_lon = dislocParams.getOriginLon() + "";		
		PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());
		
		// logger.debug("[AutomatedDislocBean/createKml] The size of tmp_pointentrylist : " + tmp_pointentrylist.length);
		// logger.debug("[AutomatedDislocBean/createKml] The size of faults of this project : " + faults.length);
		logger.debug("[AutomatedDislocBean/createKml] The fault : " + faults[0].getFaultName());		
		logger.debug("[AutomatedDislocBean/createKml] the length the fault : " + faults[0].getFaultLength());
		logger.debug("[AutomatedDislocBean/createKml] the width the fault : " + faults[0].getFaultWidth());
		// logger.debug("[AutomatedDislocBean/createKml] dislocResultsBean.getOutputFileUrl() : " + dislocResultsBean.getOutputFileUrl());

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

		SimpleXDataKml kmlservice;
		SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		locator.setMaintainSession(true);		
		kmlservice = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		kmlservice.setOriginalCoordinate(origin_lon, origin_lat);
		kmlservice.setCoordinateUnit("1000");
		
		// Plot the faults
		for (int i = 0; i < faults.length; i++) {
			// kmlService.setFaultPlot("", faults[i].getFaultName() + "", faults[i].getFaultLonStart() + "", faults[i].getFaultLatStart() + "", faults[i].getFaultLonEnd() + "", faults[i].getFaultLatEnd() + "", "ff6af0ff", 5.);
			kmlservice.setFaultPlot("", faults[i].getFaultName() + "", faults[i].getFaultLonStart() + "", faults[i].getFaultLatStart() + "", faults[i].getFaultLonEnd() + "", faults[i].getFaultLatEnd() + "", "ff6af0ff", 5.);			
		}
		logger.debug("[AutomatedDislocBean/createKml] going to runMakeSubKmls");
		
		String myKmlUrl = kmlservice.runMakeKml("", "automatedDisloc", projectName,
				(dislocResultsBean.getJobUIDStamp()).hashCode() + "");

		//This is possibly an obsolete API call.
		//		String myKmlUrl = kmlservice.runMakeSubKmls(tmp_pointentrylist, "", "automatedDisloc", projectName, (dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		
		logger.debug("[AutomatedDislocBean/createKml] Finished");
		return myKmlUrl;
	}
	
	protected void storeProjectInContext(String userName, String projectName,
			String jobUIDStamp, DislocParamsBean paramsBean,
			DislocResultsBean dislocResultsBean, String kml_url,
			String insarKmlUrl, String elevation, String azimuth,
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
	
	
	private String logreader(File file, boolean all)
	{
		String returnstr = "";
		String temp;
				
		if (file.exists())
		{	
			try {
				
				BufferedReader loginput = new BufferedReader(new FileReader(file));
					
				while ((temp = loginput.readLine()) != null)
				{
					if (all == true)
						returnstr += temp + "\n";
					
					else
						returnstr = temp;
					// logger.debug(file.getAbsolutePath());
					// logger.debug("logreader : " + returnstr);
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
		 
		  logger.debug("[AutomatedDislocBean/copyFile] From " + oldFileDB.toString() + " to " + newFileDB.toString());
		  logger.debug("[AutomatedDislocBean/copyFile] oldFileDB.exists() : " + oldFileDB.exists());
		  logger.debug("[AutomatedDislocBean/copyFile] oldFileDB.canRead() : " + oldFileDB.canRead());
		  logger.debug("[AutomatedDislocBean/copyFile] newFileDB.exists() : " + newFileDB.exists());
		  logger.debug("[AutomatedDislocBean/copyFile] newFileDB.canWrite() : " + newFileDB.canWrite());
		  

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
		
		// logger.debug("[getContextBasePath] called");
		
		File logfile = new File(dir + "/" + "log.txt");
		
		try {
			logfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}


		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
}


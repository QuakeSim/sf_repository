package cgl.quakesim.automateddisloc;


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
import java.net.URL;
import java.net.URLConnection;
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
import cgl.quakesim.disloc.PointEntry;
import cgl.quakesim.disloc.SimpleXDataKml;
import cgl.quakesim.disloc.SimpleXDataKmlServiceLocator;
import gekmlib.Document;
import gekmlib.Folder;
import gekmlib.Kml;


import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
// import com.sun.jersey.api.client.Client;
// import com.sun.jersey.api.client.WebResource;



public class AutomatedDislocBean
{
	public void run(String url) {
		
		RunautomatedDisloc rd = new RunautomatedDisloc(url);
		rd.start();
	}
	
	/*
	public static void main( String[] args ) {
	
		// AutomatedDislocBean adb = new AutomatedDislocBean();
		// adb.createProjectsFromRssph(adb.mover5_rss_url);
		// adb.test("abc");
		
		Client c = Client.create();
		WebResource webResource = c.resource("http://129.79.49.68:8080/axis2/services/AutomatedDislocBean/run?url=http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml");
		// webResource.get(String.class);
		System.out.println("[main] " + webResource.get(String.class));
	}
	*/
}


class RunautomatedDisloc extends Thread {
	
	String mover5_rss_url = "http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml";
	DecimalFormat df = new DecimalFormat(".###");
	String contextBasePath;
	String url = "";
	
	String dislocServiceUrl;
	String dislocExtendedServiceUrl;
	String faultDBServiceUrl;
	String kmlGeneratorBaseurl;
	String kmlGeneratorUrl;
	String insarkmlServiceUrl;	
	String insarKmlUrl;
	String rssdisloc_dir_name;
	
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
	
	public RunautomatedDisloc(String url) {
		this.url = url;
	}

	public String test(String name) {
		Properties properties = new Properties();
		
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String dir = properties.getProperty("output.dest.dir");	
		
		// System.out.println("[getContextBasePath] called");
		
		File logfile = new File(dir + "/" + "log.txt");
		
		try {
			logfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
		}
		
		Date date2 = new Date(date.getTime()-date1.getTime());	
		
		
		//1 hour = 3600000
		if (date2.getTime() > 3600000)
			logwriter(logfile, dateFormat.format(date1));
		
		
		return "hello, " + name;
	}
	
	public void loadProperties() {
		Properties properties = new Properties();
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// System.out.println("[getContextBasePath] called");
		contextBasePath = properties.getProperty("output.dest.dir");
		
		dislocServiceUrl = properties.getProperty("dislocServiceUrl"); 
		dislocExtendedServiceUrl = properties.getProperty("dislocExtendedServiceUrl");		
		// kmlGeneratorBaseurl = properties.getProperty("output.dest.dir");
		kmlGeneratorUrl = properties.getProperty("kmlGeneratorUrl");
		insarkmlServiceUrl = properties.getProperty("insarkmlServiceUrl"); 
		rssdisloc_dir_name= properties.getProperty("rssdisloc.dir.name");
		// insarKmlUrl = properties.getProperty("output.dest.dir");
		// System.out.println("[getContextBasePath] " + properties.getProperty("output.dest.dir"));
	}
	
	public String getContextBasePath() {
		return contextBasePath;
	}

	public void setContextBasePath(String contextBasePath) {
		this.contextBasePath = contextBasePath;
	}

	public void createProject(String[] arrayList) {
		
		ObjectContainer db = null;
		
		File projectDir = new File(getContextBasePath());
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		try {			
			
			
			db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
			
			DislocProjectBean tmp = new DislocProjectBean();
			
			
			ObjectSet results = db.get(DislocProjectBean.class);

			for (int nA = 0 ; nA < arrayList.length ; nA++) {
				// Create a new project. This may be overwritten later
				DislocProjectBean currentProject = new DislocProjectBean();
				currentProject.setProjectName(arrayList[nA]);
				// System.out.println("[RunautomatedDisloc/createProject] " + arrayList[nA]);
				
				db.set(currentProject);				
			}
			System.out.println("[RunautomatedDisloc/createProject] finished");
			db.commit();
		} catch (Exception e) {			
			System.out.println("[RunautomatedDisloc/createProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		// System.out.println("[createProject] " + arrayList.length);
	}
	
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
					// System.out.println("[getProject]" + currentProject.getProjectName());
					break;
				}
			}
			
						
		} catch (Exception e) {			
			System.out.println("[RunautomatedDisloc/getProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}	
	}

	public void getDislocProjectSummaryBeanCount() {
		
		ObjectContainer db = null;
		
		File projectDir = new File(getContextBasePath() + "/overm5/");
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		try {
			db = Db4o.openFile(getContextBasePath() + "/overm5_temp.db");
			
			
			ObjectSet results = db.get(DislocProjectSummaryBean.class);

			System.out.println("[RunautomatedDisloc/DislocProjectSummaryBean] results.size() : " + results.size());

					
						
		} catch (Exception e) {			
			System.out.println("[RunautomatedDisloc/getProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}	
	}
	
	
	public void addFault(HashMap hm) {
		
		Set entryset = hm.entrySet();
		Iterator iter = entryset.iterator();
				
		File projectDir = new File(getContextBasePath() + "/overm5/");
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		int i = 0 ;
		
		while (iter.hasNext()) {
			ObjectContainer db = null;
			try {
					Map.Entry e = (Map.Entry)iter.next();
					
					// System.out.println("[addFault] " + e.getKey() + "/" + ((Fault)e.getValue()).getFaultName());
						
					File f = new File(getContextBasePath() + "/overm5/" + e.getKey() + ".db");
					if (f.exists())
						f.delete();
										
					db = Db4o.openFile(getContextBasePath() + "/overm5/" + e.getKey() + ".db");			
					
					db.set((Fault)e.getValue());
					db.commit();
					
					DislocParamsBean tmp = new DislocParamsBean();
					tmp.setOriginLat(((Fault)e.getValue()).getFaultLatStart());
					tmp.setOriginLon(((Fault)e.getValue()).getFaultLonStart());
					db.set(tmp);
					db.commit();
									
					
					runBlockingDislocJSF(e.getKey().toString(), tmp, (Fault)e.getValue());					
					
					i++;
				// setProjectOrigin(projectName);
			} catch (Exception e) {			
				System.out.println("[RunautomatedDisloc/addFault] " + e);
			}
			finally {
				if (db != null)
					db.close();
			}
		}
		// System.out.println("[addFault] " + i);
	}
	
	public void run() {
		
		Properties properties = new Properties();
		
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("automatedDisloc.properties");						
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loadProperties();
		
		String dir = properties.getProperty("output.dest.dir");
		
		// System.out.println("[getContextBasePath] called");
		
		File logfile = new File(dir + "/" + "log.txt");
		
		try {
			if (!logfile.exists())
				logfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		Date date = new Date();
		Date date1 = new Date();
		String s = logreader(logfile, false);
		
		// System.out.println("[RunautomatedDisloc/run] " + s);
		try {
			if (s=="" || s==null)
				date1.setTime(0);
			else
				date1.setTime(dateFormat.parse(s).getTime());
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date date2 = new Date(date.getTime()-date1.getTime());	
		
		System.out.println("[RunautomatedDisloc/run] the last update time : " + dateFormat.format(date1));
		System.out.println("[RunautomatedDisloc/run] the current time : " +  dateFormat.format(date));
		
		//1 hour = 3600000
		 getDislocProjectSummaryBeanCount();
		
		if (date2.getTime() > 3600000) {
			logwriter(logfile, dateFormat.format(date));
			System.out.println("[RunautomatedDisloc/run] updated");
			
			File projectDir = new File(getContextBasePath() + "/overm5/");
			
			if (!projectDir.exists())
				projectDir.mkdirs();
			
			File f = new File(getContextBasePath() + "/overm5_temp.db");
			if (f.exists())
				f.delete();
			
			createProjectsFromRss(url);
			
			// to allow other process to have access to overm5.db while this webservice is updating, it's working on a temporary file and copying it at the end.
			File oldFileDB = new File(getContextBasePath() + "/overm5_temp.db");
			File newFileDB = new File(getContextBasePath() + "/overm5.db");
			
			try {
				copyFile(oldFileDB, newFileDB);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public void createProjectsFromRss(String url) {		
			
		CglGeoRssParser cgrp = new CglGeoRssParser();
		cgrp.parse(url);		
		List entry_list = new ArrayList();
		List pns = new ArrayList();
		entry_list = cgrp.getEntryList();
		System.out.println("[RunautomatedDisloc/run] entry_list.size() : " + entry_list.size());
		
		HashMap<String, Fault> hm = new HashMap<String, Fault>();
		
		double mu = 0.2E11;
		double thr = 0;
				
		Kml doc = new Kml();
		Folder root = new Folder();
		Document kmlDocument=new Document();
		
		root.setName("root Folder");
		root.setDescription("This is the root folder");
		kmlDocument.addFolder(root);
		doc.addDocument(kmlDocument);
		
		String newFaultFilename = "";
		String localDestination = getContextBasePath() + "/../../../../../" + "gridsphere" + "/overm5.kml";
		
		System.out.println("[RunautomatedDisloc/run] Old fault kml file:" + localDestination);
		File oldFile = new File(localDestination);
		if (oldFile.exists()) {
			System.out.println("[RunautomatedDisloc/run] Deleting old fault kml file");
			oldFile.delete();			
		}
		
		long timeStamp = (new Date()).getTime();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(localDestination));
			out.println(xmlHead);
			out.println(kmlHead);
			out.println(docBegin);
		} catch (IOException e) {
				// TODO Auto-generated catch block			
			e.printStackTrace();			
		}		 
		
		for (int nA = 0 ; nA < entry_list.size() ; nA++) {
			
			Entry entry = (Entry) entry_list.get(nA);
			String lat_start = entry.getGeorss_point().trim().split(" ")[0];
			String lon_start = entry.getGeorss_point().trim().split(" ")[1];
			
			// System.out.println("[createProjectsFromRss] " + entry.getTitle() + "/ " + lat_start + ", " + lon_start);
			
			for (int nB = 0 ; nB < 4 ; nB++) {
				
				// String projectname = entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1);
				
				// System.out.println("[createProjectsFromRss] " + projectname + "/" + entry.getId().split(":")[3]);
								
				Fault fault = new Fault(); 
				
				fault.setFaultName(entry.getId().split(":")[3]);
				fault.setFaultLonStart(Double.parseDouble(lon_start));
				fault.setFaultLatStart(Double.parseDouble(lat_start));
				fault.setFaultLameLambda(1.0);
				fault.setFaultLameMu(1.0);
												
				if (nB == 0) {					
					fault.setFaultLength(0);
					fault.setFaultDipAngle(90);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(0);
					fault.setFaultWidth(0);		
					thr = 0;
				}
				
				else if (nB == 1) {
					fault.setFaultLength(0);
					fault.setFaultDipAngle(90);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(45);
					fault.setFaultWidth(0);
					thr = 0;
				}
				
				else if (nB == 2) {
					fault.setFaultLength(0);
					fault.setFaultDipAngle(45);
					fault.setFaultDipSlip(0);
					fault.setFaultStrikeSlip(0);
					fault.setFaultStrikeAngle(0);
					fault.setFaultWidth(0);
					thr = 1;
				}
				
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
					length = Math.sqrt(Math.sqrt(Math.pow(10, (3*(entry.getM()+10.7)/2))/(mu*0.6E-10)))/1E5;
					width = length;
				}
				
				else {				
					width = 20;
					length = Math.sqrt(Math.pow(10,(3*(entry.getM()+10.7)/2))/(mu*0.6E-10))/(width*1E10);
				}
				
				fault.setFaultLength(length);
				fault.setFaultWidth(width);
				fault.setFaultDepth(width/2);
				double slip = 0.6*length*width;
				// System.out.println("[AutomatedDislocBean/createProjectFromRss] slip = 0.6 * length * width (" + slip + " = 0.6 * " + length + " " + width);
				
				if (thr == 1)
					fault.setFaultDipSlip(slip * 10);
				else
					fault.setFaultStrikeSlip(slip * 10);
				
				
				// double x = (lonStart - currentParams.getOriginLon()) * factor(currentParams.getOriginLon(), currentParams.getOriginLat());
				// double y = (latStart - currentParams.getOriginLat()) * 111.32;				
				double x = 0; // because this will be always the first fault?
				double y = 0; // because this will be always the first fault?
				fault.setFaultLocationX(Double.parseDouble(df.format(x)));
				fault.setFaultLocationY(Double.parseDouble(df.format(y)));
				
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
				
				
				String projectname =  entry.getTitle() + "(" + entry.getId().split(":")[3] + ")_n_DA" + (double)Math.round((double)fault.getFaultDipAngle()*1000)/1000 + "_SA" + (double)Math.round((double)fault.getFaultStrikeAngle()*1000)/1000 + "_DS" + (double)Math.round((double)fault.getFaultDipSlip()*1000)/1000 + "_SS" + (double)Math.round((double)fault.getFaultStrikeSlip()*1000)/1000;
				
				pns.add(projectname);
				hm.put(projectname, fault);
				
				// getProject(entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1));
				
				File f = new File(getContextBasePath() + "/overm5/" + projectname + ".db");
				if (f.exists())
					f.delete();
				
				ObjectContainer db = null;
				DislocParamsBean tmp = null;
				
				try {
					db = Db4o.openFile(getContextBasePath() + "/overm5/" + projectname + ".db");			
				
					db.set(fault);
					db.commit();
					
					tmp = new DislocParamsBean();
					tmp.setOriginLat(fault.getFaultLatStart());
					tmp.setOriginLon(fault.getFaultLonStart());
					db.set(tmp);
					db.commit();				
					
				} catch (Exception e) {			
					System.out.println("[RunautomatedDisloc/createProjectFromRss] " + e);
				}
				
				finally {
					if (db != null)
						db.close();
				}
				
				
				// System.out.println("[addFault] " + e.getKey() + "/" + tmp.getOriginLat() + ", " + tmp.getOriginLon());
				
				OutputURLs ouls = null;
				
				try {
					ouls = runBlockingDislocJSF(projectname, tmp, fault);
					getDislocProjectSummaryBeanCount();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				out.println(pmBegin);
				out.println("<name>"+projectname+"</name>");
				
				out.println(descBegin);				
				String s = "<![CDATA[<b>Fault Name</b>: " + projectname + "<br><b>Length</b>: " + fault.getFaultLength() + " <br><b>Width</b>: " + fault.getFaultWidth() + "<br><b>Depth</b>: " + fault.getFaultDepth() + "<br><b>DipAngle</b>: " + fault.getFaultDipAngle() + "<br><b>Strike Angel</b>: " + fault.getFaultStrikeAngle() + "<br><b>Dip Slip</b>: " + fault.getFaultDipSlip() + "<br><b>Strike Slip</b>: " + fault.getFaultStrikeSlip() + "<br><b>Location [x, y]</b>: [" + fault.getFaultLocationX() + ", " + fault.getFaultLocationY() + "]<br><b>Updated</b>:  " + entry.getUpdated() + "<br><a href= \"" + ouls.getDislocoutputURL() + "\"><b>DislocOutputURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getDisplacementkmlURL() + "&t=p\"><b>DisplacementKmlURL</b></a><br><a href=\"http://maps.google.com/maps?q=" + ouls.getInsarkmlURL() +"&t=p\"><b>InsarKmlURL</b></a><br><b>Comment</b>: http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml,<br>Community Grids Lab<br>]]>";				
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
		
		File newFile_rssdisloc = new File(getContextBasePath() + "/../../../../../" + rssdisloc_dir_name + "/overm5.kml");
		
		oldFile = new File(localDestination);
		
		try {
			copyFile(oldFile, newFile_rssdisloc);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Set s = hm.keySet();
		String str[] = new String[pns.size()];
		
		for (int nA = 0 ; nA < pns.size() ; nA++)
			str[nA] = (String)pns.get(nA);
			
		// addFault(hm);
		createProject(str);	
	}
	
	
	private OutputURLs runBlockingDislocJSF(String projectName, DislocParamsBean currentParams, Fault fault) throws Exception {
		
		System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] Started");
		OutputURLs ourls = new OutputURLs();

		try {

			Fault[] faults = new Fault[1];
			faults[0] = fault;
			
			ObsvPoint[] points = null;
			DislocExtendedService dislocExtendedService = new DislocExtendedServiceServiceLocator().getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
			
			System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] The project name: " + projectName);
			System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] Faults : " + faults[0].getFaultName());
			

			// This step runs disloc
			DislocResultsBean dislocResultsBean = dislocExtendedService.runBlockingDislocExt("automatedDisloc", projectName, points, faults, currentParams, null);
			// setJobToken(dislocResultsBean.getJobUIDStamp());
			
			System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] dislocResultsBean.getOutputFileUrl() :" + dislocResultsBean.getOutputFileUrl());
			ourls.setDislocoutputURL(dislocResultsBean.getOutputFileUrl());

			// This step makes the kml plots.  We allow this to fail.
			String myKmlUrl = "";			
			try {
				 myKmlUrl = createKml(currentParams, dislocResultsBean, faults, projectName);
				 System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] KmlUrl : " + myKmlUrl);
				 // setJobToken(dislocResultsBean.getJobUIDStamp());
			}
			catch (Exception ex) {
				 ex.printStackTrace();
			}
			
			ourls.setDisplacementkmlURL(myKmlUrl);

			// This step runs the insar plotting stuff.  We also allow this
			// to fail.
			InsarKmlService iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));

			insarKmlUrl="";
			
			try {
				 insarKmlUrl = iks.runBlockingInsarKml("automatedDisloc", projectName, dislocResultsBean.getOutputFileUrl(), elevation, azimuth, frequency, "ExecInsarKml");
				 System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] insarKmlUrl : " + insarKmlUrl);
			}			
			
			catch (Exception ex) {
				 ex.printStackTrace();
			}
			
			ourls.setInsarkmlURL(insarKmlUrl);
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			// setInsarKmlUrl(insarKmlUrl);
			storeProjectInContext("automatedDisloc", projectName, dislocResultsBean.getJobUIDStamp(), currentParams, dislocResultsBean, myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);
			
		} catch (Exception ex) {
			 ex.printStackTrace();
		}
		System.out.println("[AutomatedDislocBean/runBlockingDislocJSF] Finished");
		
		return ourls;
	}
	
	public PointEntry[] LoadDataFromUrl(String InputUrl) {
		System.out.println("[AutomatedDislocBean/LoadDataFromUrl] Creating Point Entry");
		ArrayList dataset = new ArrayList();
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
					System.out.println("[AutomatedDislocBean/LoadDataFromUrl] Past the faults");
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
					dataset.add(tempPoint);
				} else {
					break;
				}
			}
			in.close();
			instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[AutomatedDislocBean/LoadDataFromUrl] Finished");
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}
	
	protected String createKml(DislocParamsBean dislocParams, DislocResultsBean dislocResultsBean, Fault[] faults, String projectName)
			throws Exception {
		System.out.println("[AutomatedDislocBean/createKml] Started");
		System.out.println("[AutomatedDislocBean/createKml] Creating the KML file at " + kmlGeneratorUrl);
		

		// Get the project lat/lon origin. It is the lat/lon origin of the first fault.
		String origin_lat = dislocParams.getOriginLat() + "";
		String origin_lon = dislocParams.getOriginLon() + "";		

		// System.out.println("[AutomatedDislocBean/createKml] The origin: " + origin_lon + " " + origin_lat);

		// get my kml
		SimpleXDataKml kmlService;
		SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		locator.setMaintainSession(true);		
		kmlService = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());
		
		// System.out.println("[AutomatedDislocBean/createKml] The size of tmp_pointentrylist : " + tmp_pointentrylist.length);
		// System.out.println("[AutomatedDislocBean/createKml] The size of faults of this project : " + faults.length);
		System.out.println("[AutomatedDislocBean/createKml] The fault : " + faults[0].getFaultName());		
		System.out.println("[AutomatedDislocBean/createKml] the length the fault : " + faults[0].getFaultLength());
		System.out.println("[AutomatedDislocBean/createKml] the width the fault : " + faults[0].getFaultWidth());
		// System.out.println("[AutomatedDislocBean/createKml] dislocResultsBean.getOutputFileUrl() : " + dislocResultsBean.getOutputFileUrl());

		kmlService.setDatalist(tmp_pointentrylist);
		kmlService.setOriginalCoordinate(origin_lon, origin_lat);
		kmlService.setCoordinateUnit("1000");

		// These plot grid lines.
		double start_x, start_y, end_x, end_y, xiterationsNumber, yiterationsNumber;
		start_x = Double.valueOf(dislocParams.getGridMinXValue()).doubleValue();
		start_y = Double.valueOf(dislocParams.getGridMinYValue()).doubleValue();
		xiterationsNumber = Double.valueOf(dislocParams.getGridXIterations())
				.doubleValue();
		yiterationsNumber = Double.valueOf(dislocParams.getGridYIterations())
				.doubleValue();
		int xinterval = (int) (Double.valueOf(dislocParams.getGridXSpacing())
				.doubleValue());
		int yinterval = (int) (Double.valueOf(dislocParams.getGridYSpacing())
				.doubleValue());
		end_x = start_x + xinterval * (xiterationsNumber - 1);
		end_y = start_y + yinterval * (yiterationsNumber - 1);

		// kmlService.setGridLine("Grid Line", start_x, start_y, end_x, end_y,
		// xinterval,yinterval);
		// kmlService.setPointPlacemark("Icon Layer");
		// kmlService.setArrowPlacemark("Arrow Layer", "ff66a1cc", 2);
		kmlService.setArrowPlacemark("Arrow Layer", "#000000", 0.95);

		// Plot the faults
		for (int i = 0; i < faults.length; i++) {
			kmlService.setFaultPlot("", faults[i].getFaultName() + "", faults[i].getFaultLonStart() + "", faults[i].getFaultLatStart() + "", faults[i].getFaultLonEnd() + "", faults[i].getFaultLatEnd() + "", "ff6af0ff", 5);
		}

		String myKmlUrl = kmlService.runMakeKml("", "automatedDisloc", projectName, (dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		
		System.out.println("[AutomatedDislocBean/createKml] Finished");
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
			codedb.commit();
			
			codedb.set(ipb);

			// Say goodbye.
			codedb.commit();
			

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
			
			System.out.println("[AutomatedDislocBean/storeProjectInContext] " + e);
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
					// System.out.println(file.getAbsolutePath());
					// System.out.println("logreader : " + returnstr);
				}
				
				loginput.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();								
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
				e.printStackTrace();
				returnstr = e.toString();
			}
		}
		
		return returnstr;
	}
	
	 public void copyFile(File oldFileDB, File newFileDB) throws Exception {
		 
		  System.out.println("[AutomatedDislocBean/copyFile] From " + oldFileDB.toString() + " to " + newFileDB.toString());
		  System.out.println("[AutomatedDislocBean/copyFile] oldFileDB.exists() : " + oldFileDB.exists());
		  System.out.println("[AutomatedDislocBean/copyFile] oldFileDB.canRead() : " + oldFileDB.canRead());
		  System.out.println("[AutomatedDislocBean/copyFile] newFileDB.exists() : " + newFileDB.exists());
		  System.out.println("[AutomatedDislocBean/copyFile] newFileDB.canWrite() : " + newFileDB.canWrite());
		  

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
}


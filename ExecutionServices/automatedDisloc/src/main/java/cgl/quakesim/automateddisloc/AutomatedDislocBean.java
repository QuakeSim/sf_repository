package cgl.quakesim.automateddisloc;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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


import cgl.quakesim.disloc.DislocParamsBean;
import cgl.quakesim.disloc.DislocProjectBean;
import cgl.quakesim.disloc.Fault;


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
	String url="";
	
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
	
	public String getContextBasePath() {
		
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
		// System.out.println("[getContextBasePath] " + properties.getProperty("output.dest.dir"));
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
			File f = new File(getContextBasePath() + "/overm5.db");
			if (f.exists())
				f.delete();
			db = Db4o.openFile(getContextBasePath() + "/overm5.db");
			
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
		
		File projectDir = new File(getContextBasePath());
		
		if (!projectDir.exists())
			projectDir.mkdirs();
		
		try {
			db = Db4o.openFile(getContextBasePath() + "/overm5.db");
			
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
					
					// System.out.println("[addFault] " + e.getKey() + "/" + tmp.getOriginLat() + ", " + tmp.getOriginLon());
					
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
		if (date2.getTime() > 3600000) {
			logwriter(logfile, dateFormat.format(date));
			System.out.println("[RunautomatedDisloc/run] updated");
			createProjectsFromRss(url);			
		}		
	}

	public void createProjectsFromRss(String url) {		
			
		CglGeoRssParser cgrp = new CglGeoRssParser();
		cgrp.parse(url);		
		List entry_list = new ArrayList();
		List pns = new ArrayList();
		entry_list = cgrp.getEntryList();
		
		HashMap<String, Fault> hm = new HashMap<String, Fault>();
		
		double mu = 0.2E11;		
		double thr = 0;
		
		for (int nA = 0 ; nA < entry_list.size() ; nA++) {
			
			Entry entry = (Entry) entry_list.get(nA);
			String lat_start = entry.getGeorss_point().trim().split(" ")[0];
			String lon_start = entry.getGeorss_point().trim().split(" ")[1];
			
			// System.out.println("[createProjectsFromRss] " + entry.getTitle() + "/ " + lat_start + ", " + lon_start);
			
			for (int nB = 0 ; nB < 4 ; nB++) {
				
				String projectname = entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1);
				
				// System.out.println("[createProjectsFromRss] " + projectname + "/" + entry.getId().split(":")[3] );
								
				Fault fault = new Fault(); 
				
				fault.setFaultName(entry.getId().split(":")[3]);
				fault.setFaultLonStart(Double.parseDouble(lon_start));
				fault.setFaultLatStart(Double.parseDouble(lat_start));
				
								
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
					length = Math.sqrt(Math.sqrt((10*(3*(entry.getM()+10.7)/2))/(mu*0.6E-10)))/1E5;
					width = length;
				}
				
				else {					
					length = (Math.sqrt((10*(3*(entry.getM()+10.7)/2))/(mu*0.6E-10)))/(20*1E10);					
					width = 20;					
				}
				
				fault.setFaultLength(length);
				fault.setFaultWidth(width);
				fault.setFaultDepth(width/2);
				double slip = 0.6*length*width;
				
				if (thr == 0)
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
				
				pns.add(projectname);
				hm.put(projectname, fault);				
				// getProject(entry.getTitle() + "(" + entry.getId().split(":")[3] + ")" + (nB+1));
			}			
		}
		
		Set s = hm.keySet();
		String str[] = new String[pns.size()];
		
		for (int nA = 0 ; nA < pns.size() ; nA++)
			str[nA] = (String)pns.get(nA);
			
		addFault(hm);
		createProject(str);
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
}


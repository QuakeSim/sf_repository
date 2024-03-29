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
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;

public class AutomatedDislocBean implements Runnable, ServiceLifeCycle {
	 static final int EARTHQUAKE_SLIP_SCENARIOS=4;
	 static final int ONE_HOUR_IN_MILLISECONDS=3600*1000; //3600000;
	 static final int ONE_HOUR_IN_SECONDS=3600;
	 static final int SCAN_TRIGGER_INTERVAL=2*ONE_HOUR_IN_SECONDS;
	 private static Logger logger=LoggerFactory.getLogger(AutomatedDislocBean.class);

	 private Scheduler scheduler;
	 private String url;

	 public AutomatedDislocBean(){
	 	  logger.info("----------AutomatedDislocBean initiated--------");
	 }
	 
	 /**
	  * This is the method that we use.  The URL is passed in through the command line, typically.
	  */
	 public void runInBackground(String url) {
		  this.url=url;
		  run();
		  // Thread background=new Thread(this);
		  // background.start();
	 }
	 
	 public void setUrl(String url) { this.url = url; }
	 public String getUrl() { return this.url; }

	 public void startUp(ConfigurationContext configContext, AxisService service){
		  logger.info("---------------Starting up Automated Disloc--------------");
	 }
	 public void shutDown(ConfigurationContext configContext, AxisService service){
		  try {
				logger.info("----------Shutting down the Quartz scheduler-----------");
				scheduler.shutdown();
		  }
		  catch(Exception ex){
				ex.printStackTrace();
		  }
	 }

	 public void run() {
	 	  //Quartz stuff
	 	  try {
	 			scheduler=StdSchedulerFactory.getDefaultScheduler();
	 			scheduler.start();
	 			JobDetail job=newJob(RunautomatedDisloc.class).withIdentity("job1","group1").build();
	 			job.getJobDataMap().put(RunautomatedDisloc.URL_FOR_AUTOCALLS,getUrl());

	 			Trigger trigger=newTrigger().withIdentity("trigger1","group1").startNow()
	 				 .withSchedule(simpleSchedule().withIntervalInSeconds(SCAN_TRIGGER_INTERVAL).repeatForever()).build();
	 			scheduler.scheduleJob(job,trigger);

	 			scheduler.start();
	 			// Thread.sleep(60L*1000L);
	 			// scheduler.shutdown();
	 	  }
	 	  catch(SchedulerException ex) {
	 			ex.printStackTrace();
	 	  }
	 	  catch(Exception ex2) {
	 			ex2.printStackTrace();
	 	  }
	 }

	 public static void printUsage(){
		  System.out.println("--------------------------------------------------");
		  System.out.println("Usage: java http://url.of.usgs/rss/feed.xml");
		  System.out.println("Typically, run with the command mvn exec:java -Dexec.args=\"http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml\" -Dlog4j.configuration=file:///path/to/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/common/classes/log4j.xml");
		  System.out.println("--------------------------------------------------");
	 }

	 /**
	  * This is a main method for running on the command line.
	  */
	 public static void main(String[] args) {
		  if(args.length!=1) {
				printUsage();
		  }
		  else {
				try {
					 AutomatedDislocBean autoBean=new AutomatedDislocBean();
					 autoBean.runInBackground(args[0]);
				}
				catch(Exception ex) {
					 ex.printStackTrace();
					 printUsage();
				}
		  }
	 }	 
}

	 

package cgl.webservices.gridinfo;

/**
 * This is a version of the GridInfoService that loads a NASA-specific property file.
 * It is a trivial extension.  
 */ 

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

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

public class NASAGridInfoService extends GridInfoBean {
	 
	 Properties properties;
	 String basePropertyFile="cosmos.properties";
	 String USERNAME="username";
	 String HOME="home";
	 String GRAM="gram";
	 String DOT=".";
	 String FORK="fork";
	 
	 String[] hosts;
	 TeraGridObject[] tgObject;

	 Hashtable userName=new Hashtable();
	 Hashtable jobManager=new Hashtable();
	 Hashtable userHome=new Hashtable();
	 Hashtable forkManager=new Hashtable();

	 public NASAGridInfoService() throws Exception {
		  this(false);
	 }
	 
	 public NASAGridInfoService(boolean useClassLoader) throws Exception {
		  super();
		  if(useClassLoader) {
				System.out.println("Using classloader");
				//This is useful for command line clients but does not work
				//inside Tomcat.
				ClassLoader loader=ClassLoader.getSystemClassLoader();
				properties=new Properties();
				
				//This works from the command line but not inside Tomcat
				//because of classloader issues.
				properties.load(loader.getResourceAsStream(basePropertyFile));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/"+basePropertyFile;
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }
		  hosts=populateHostList(properties);
		  populateDataObjects(properties,hosts);		  
	 }
	 
	 public String getHomeDirectory(String host) {
		  return (String)userHome.get(host);
	 }
	 
	 public String getJobManager(String host) {
		  return (String)jobManager.get(host);
	 }

	 public String getUserName(String host) {
		  return (String)userName.get(host);
	 }

	 public String getForkManager(String host) {
		  return (String)forkManager.get(host);
	 }

	 public String[] getHosts() {
		  return hosts;
	 }
	 
	 protected String[] populateHostList(Properties props) {
		  String hostlist=props.getProperty("hostname.list");
		  System.out.println(hostlist);
		  StringTokenizer st=new StringTokenizer(hostlist,",");
		  String[] hosts=new String[st.countTokens()];
		  int i=0;
		  while(st.hasMoreTokens()){
				hosts[i]=st.nextToken();
				//System.out.println(hosts[i]);
				i++;
		  }
		  return hosts;
	 }

	 protected void populateDataObjects(Properties props, String[] hosts){
		  for(int i=0;i<hosts.length;i++) {
				jobManager.put(hosts[i],props.getProperty(GRAM+DOT+hosts[i]));
				userName.put(hosts[i],props.getProperty(USERNAME+DOT+hosts[i]));
				userHome.put(hosts[i],props.getProperty(HOME+DOT+hosts[i]));
				forkManager.put(hosts[i],props.getProperty(FORK+DOT+hosts[i]));
		  }
	 }
}
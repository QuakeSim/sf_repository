package cgl.webservices.gridinfo;

/**
 * This is a service class that extends a JavaBean.  I decided to do it this way to keep the
 * bean unpolluted by Axis specific stuff, while noting that the service "is-a" and not 
 * "has-a" GridInfoBean.
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

public class GridInfoService extends GridInfoBean {
	 
	 Properties properties;
	 String basePropertyFile="gridinfo.properties";
	 String USERNAME="username";
	 String HOME="home";
	 String GRAM="gram";
	 String DOT=".";
	 
	 String[] hosts;
	 TeraGridObject[] tgObject;

	 public GridInfoService() throws Exception {
		  this(false);
	 }
	 
	 public GridInfoService(boolean useClassLoader) throws Exception {
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
	 
	 protected String[] populateHostList(Properties props) {
		  String hostlist=props.getProperty("hostname.list");
		  StringTokenizer st=new StringTokenizer(hostlist,",");
		  String[] hosts=new String[st.countTokens()];
		  for(int i=0;i<st.countTokens();i++) {
				hosts[i]=st.nextToken();
		  }
		  return hosts;
	 }

	 protected void populateDataObjects(Properties props, String[] hosts){
		  for(int i=0;i<hosts.length;i++) {
				tgObject[i].setHostName(hosts[i]);
				tgObject[i].setJobManager(props.getProperty(GRAM+DOT+hosts[i]));

				tgObject[i].setUserName(props.getProperty(USERNAME+DOT+hosts[i]));
				
				tgObject[i].setUserHome(props.getProperty(HOME+DOT+hosts[i]));
		  }
	 }
	 
	 public String getHomeDirectory() {
		  return "";
	 }

}
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

public class NASAGridInfoService extends GridInfoService {
	 
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
	 
}
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
	 }
}
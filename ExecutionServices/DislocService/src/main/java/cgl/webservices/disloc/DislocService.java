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

/**
 * A simple wrapper for Ant.
 */

public class DislocService extends AntVisco implements Runnable {  
	 
	 public DislocService(boolean useClassLoader) throws Exception {
		  this(false);
	 }

	 public DislocService(boolean useClassLoader) throws Exception {
		  super();

		  if(useClassLoader) {
				System.out.println("Using classloader");
				//This is useful for command line clients but does not work
				//inside Tomcat.
				ClassLoader loader=ClassLoader.getSystemClassLoader();
				properties=new Properties();
				
				//This works if you are using the classloader but not inside
				//Tomcat.
				properties.load(loader.getResourceAsStream("dislocconfig.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/dislocconfig.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }
	 }
	 

	 
}  

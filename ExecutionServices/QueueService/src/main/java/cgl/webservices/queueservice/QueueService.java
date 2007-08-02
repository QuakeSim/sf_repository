package cgl.webservices.queueservice;

//Need to import this parent.
import cgl.webservices.*;

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

public class QueueService {
    
    Properties properties;
    String serverUrl;
    String baseWorkDir;
    String projectName;
    String binDir;
    String buildFilePath;
    String antTarget;
    String workDir;
    
    //Usefull variable;
    final String space=" ";
    
    public QueueService() throws Exception {
	this(false);
    }
    
    public QueueService(boolean useClassLoader) throws Exception {
	super();
	
	if(useClassLoader) {
	    System.out.println("Using classloader");
	    //This is useful for command line clients but does not work
	    //inside Tomcat.
	    ClassLoader loader=ClassLoader.getSystemClassLoader();
	    properties=new Properties();
	    
	    //This works if you are using the classloader but not inside
	    //Tomcat.
	    properties.load(loader.getResourceAsStream("queueconfig.properties"));
	}
	else {
	    //Extract the Servlet Context
	    System.out.println("Using Servlet Context");
	    MessageContext msgC=MessageContext.getCurrentContext();
	    ServletContext context=((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
	    
	    String propertyFile=context.getRealPath("/")
		+"/WEB-INF/classes/queueconfig.properties";
	    System.out.println("Prop file location "+propertyFile);
	    
	    properties=new Properties();	    
	    properties.load(new FileInputStream(propertyFile));
	}

	serverUrl=properties.getProperty("queue.service.url");
	baseWorkDir=properties.getProperty("base.workdir");
	projectName=properties.getProperty("project.name");
    }
    
    protected String generateJobStamp(){
	return (new UID().toString());
    }
    
    protected String generateBaseUrl(String userName,
				     String projectName,
				     String timeStamp) {
	
	//Need to be careful here because this must follow
	//the workDir convention also.
	String baseUrl=serverUrl+"/"+userName+"/"
	    +projectName+"/"+"/"+timeStamp;
	
		  return baseUrl;
    }
    
    protected QueueResponseBean createQueueResponseBean(String userName,
							String projectName,
							String jobUIDStamp) {
	QueueResponseBean drb=new QueueResponseBean();
	String baseUrl=generateBaseUrl(userName,projectName,jobUIDStamp);
	
	drb.setJobUIDStamp(jobUIDStamp);
	drb.setProjectName(projectName);
	return drb;
    }
}  

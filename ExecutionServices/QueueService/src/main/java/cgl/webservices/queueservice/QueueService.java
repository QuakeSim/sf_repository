package cgl.webservices.queueservice;

//Need to import this parent.
import cgl.webservices.*;

//Not explicitly naming these because tey are famous.
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

//Import stuff from db4o
import com.db4o.*;

public class QueueService {
    
    Properties properties;
    String serverUrl;
    String baseWorkDir;
    String projectName;
    String binDir;
    String buildFilePath;
    String antTarget;
    String workDir;
	 String dbFullName;

	 String NULL_QUEUE_NAME="Null queue name";
	 String NO_SUCH_QUEUE="No such queue";

    //Usefull variable;
    final String space=" ";

	 ObjectContainer db=null;
    
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
				properties.load(loader.getResourceAsStream("queueservice.properties"));
		  }
		  else {
				//Extract the Servlet Context
				System.out.println("Using Servlet Context");
				MessageContext msgC=MessageContext.getCurrentContext();
				ServletContext context=
					 ((HttpServlet)msgC.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext();
				
				String propertyFile=context.getRealPath("/")
					 +"/WEB-INF/classes/queueservice.properties";
				System.out.println("Prop file location "+propertyFile);
				
				properties=new Properties();	    
				properties.load(new FileInputStream(propertyFile));
		  }

		  serverUrl=properties.getProperty("queue.service.url");
		  baseWorkDir=properties.getProperty("base.workdir");
		  projectName=properties.getProperty("project.name");
		  dbFullName=properties.getProperty("db.fullname");
    }

	 /**
	  * Create a new queue. 
	  * If the queue name is null or empty, bad things happen.
	  */
	 public void createQueue(String queueBaseName) throws Exception {
		  try {
				db=Db4o.openFile(dbFullName);
				if(queueBaseName==null || queueBaseName.equals("")){
					 System.out.println(NULL_QUEUE_NAME);
					 db.close();
					 throw new Exception(NULL_QUEUE_NAME);
				}
				else {
					 QueueBean qb=new QueueBean();
					 qb.setQueueName(queueBaseName);
					 db.set(qb);
				}
				db.commit();
				db.close();
		  }
		  catch (Exception ex){
				ex.printStackTrace();
				if(db!=null) db.close();
		  }
	 }

	 /**
	  * Write the message
	  */ 
    public void writeQueueMessage(String queueBaseName,
											 String message) throws Exception {
		  try {
				db=Db4o.openFile(dbFullName);
				QueueBean qb=new QueueBean();
				qb.setQueueName(queueBaseName);
				
				ObjectSet results=db.get(qb);
				if(results.hasNext()) {
					 qb=(QueueBean)results.next();
					 qb.setQueueMessage(message);
					 qb.setQueueTime((new Date()).toString());
					 db.set(qb);
				}
				else {
					 System.out.println(NO_SUCH_QUEUE);
					 throw new Exception(NO_SUCH_QUEUE);
				}
				db.commit();
				db.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
				if(db!=null) db.close();
		  }
	 }
	 
	 /**
	  * Delete the queue
	  */
	 public void deleteQueue(String queueBaseName) throws Exception {
		  try {
				db=Db4o.openFile(dbFullName);
				QueueBean qb=new QueueBean();
				qb.setQueueName(queueBaseName);
				ObjectSet results=db.get(qb);
				while(results.hasNext()) {
					 qb=(QueueBean)results.next();
					 db.delete(qb);
				}
				db.commit();
				db.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
				if(db!=null) db.close();
		  }
		  
	 }

	 /**
	  * Get back some or all of the messages in the specified queue.
	  */
	 public String readQueueMessage(String queueBaseName) throws Exception {				
		  String theMessage="";
		  try {
				db=Db4o.openFile(dbFullName);
				QueueBean qb=new QueueBean();
				qb.setQueueName(queueBaseName);
				ObjectSet results=db.get(qb);
				if(results.hasNext()) {
					 qb=(QueueBean)results.next();
					 theMessage=qb.getQueueMessage();
				}
				db.commit();
				db.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
				if(db!=null) db.close();
		  }
		  return theMessage;
	 }

	 public String getQueueUpdateTime(String queueBaseName) {
		  String updateTime="";
		  try {
				db=Db4o.openFile(dbFullName);
				QueueBean qb=new QueueBean();
				qb.setQueueName(queueBaseName);
				ObjectSet results=db.get(qb);
				if(results.hasNext()) {
					 qb=(QueueBean)results.next();
					 updateTime=qb.getQueueTime();
				}
				db.commit();
				db.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
				if(db!=null) db.close();
		  }

		  return updateTime;
	 }

	 /**
	  * This creates a UID.
	  */
    protected String generateJobStamp(){
		  return (new UID().toString());
    }    
}  

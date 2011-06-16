package cgl.webservices;
import java.util.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

/**
 * A simple wrapper for Ant.
 */
public class AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(AntVisco.class);
    final public static String DONE="done", NOT_DONE="not done", FAILED="failed";
    final String SPACE=" ";

    String status=NOT_DONE;

    static String args[]=null;
    String buildfile="";

    Thread background;

    public AntVisco() {
		  BasicConfigurator.configure();
    }

    public void setArgs(String[] args) {
		  //Create a dummy array if the args are empty.
		  if(args==null) {
				logger.info("Received null argument array");
				args=new String[1];
				args[0]="";
		  }
		  else {
				this.args=args;
		  }
    }
    
    public void stop() {
		  if(background!=null) {
				background.interrupt();
				background=null;
		  }
    }
    
    public String[] getArgs() {
		  System.out.println(args.length);
		  return args;
    }
	 
    public void setBuildFile(String filename) {
		  buildfile=filename;
    }
	 
	 /**
	  * This is a placeholder callback method that is invoked when the thread run completes
	  * successfully.  Subclasses should implement.
	  */
	 public void callbackSuccess(){
	 }

	 /**
	  * This is a placeholder callback method that is invoked when the thread run completes
	  * unsuccessfully.
	  */
	 public void callbackFailure(){
	 }
	 
	 /**
	  * This is the required run method for threads.  It is invoked through start() by a thread but 
	  * it can also be called directly within a single-threaded invocation.
	  */ 
    public void run(){
		  try {
				cgl.webservices.MyMain2.main(getArgs());
				status=DONE;
				callbackSuccess();
		  }
		  catch (Exception ex){
				System.err.println(ex.toString());
				status=FAILED;
				callbackFailure();
				throw new RuntimeException();
		  }
		  notifyAll();
    }
    
    public String getStatus(){
		  return status;
    }
	 
    public void execute() throws Exception{
		  background=new Thread(this);
		  //	logger.info("Executing thread");
		  background.start();
    }
	 
    
    public static void main(String[] args) {
		  AntVisco at=new AntVisco();
		  at.setArgs(args);
		  try{
				//	at.execute();
				at.run();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
    }
}

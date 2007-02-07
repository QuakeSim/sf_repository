package cgl.webservices;

import java.util.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

/**
 * A simple wrapper for Ant.
 */
public class AntVisco implements Runnable {
	static Logger logger = Logger.getLogger(AntVisco.class);

	final String DONE = "done", NOT_DONE = "not done";

	final String SPACE = " ";

	String status = NOT_DONE;

	static String args[] = null;

	String buildfile = "";

	Thread background;

	public AntVisco() {
		BasicConfigurator.configure();
		// 	publisher=new NBPublisher();
		// 	try {
		// 	    publisher.initializeNB("jpl.antservice");
		// 	}
		// 	catch(Exception ex) {
		// 	    ex.printStackTrace();
		// 	}
	}

	public void setArgs(String[] args) {
		//Create a dummy array if the args are empty.
		if (args == null) {
			logger.info("Received null argument array");
			args = new String[1];
			args[0] = "";
		} else {
			// 	    for(int i=0;i<args.length;i++) {
			// 		logger.info(args[i]);
			// 	    }
			this.args = args;
		}
	}

	public void stop() {
		if (background != null) {
			background.interrupt();
			background = null;
		}
	}

	public String[] getArgs() {
		System.out.println(args.length);
		return args;
	}

	public void setBuildFile(String filename) {
		buildfile = filename;
	}

	public void run() {
		try {
			cgl.webservices.MyMain2.main(getArgs());
		} catch (Exception ex) {
			System.err.println(ex.toString());
			throw new RuntimeException();
		}
	}

	public String getStatus() {
		return status;
	}

	public void execute() throws Exception {
		background = new Thread(this);
		//	logger.info("Executing thread");
		background.start();
	}

	public static void main(String[] args) {
		AntVisco at = new AntVisco();
		at.setArgs(args);
		try {
			//	at.execute();
			at.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

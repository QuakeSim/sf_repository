package cgl.webservices;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

/**
 * A simple wrapper for Ant.
 */
public class RDAHMMService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(RDAHMMService.class);

    final String FILE_PROTOCOL="file://";
    final String HTTP_PROTOCOL="http://";
    
    public RDAHMMService() {
	super();
    }

    
    private void makeWorkDir(String workDir, String bf_loc)
	throws Exception {
	
	String[] args0=new String[4];
        args0[0]="-DworkDir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=bf_loc;
        args0[3]="MakeWorkDir";
	
        setArgs(args0);
        run();
    }  
    
    private void downloadInputFile(String inputFileUrlString,
				   String inputFileDest)
	throws Exception {

	//Convert to a URL. This will throw an exception if
	//malformed.
	URL inputFileUrl=new URL(inputFileUrlString);
	
	String protocol=inputFileUrl.getProtocol();
	if(protocol.equals(FILE_PROTOCOL)) {
	    String filePath=inputFileUrl.getFile();
	    System.out.println("File path is "+filePath);
	    
	    File filePathObject=new File(filePath);
	    File destDirObject=new File(inputFileDest);

	    //See if the inputFileUrl and the dest file are the same.
	    if(filePathObject.getCanonicalPath().
	       equals(destDirObject.getCanonicalPath())) {
		System.out.println("Files are the same.  We're done.");
		return;
	    }
	    
	    //Otherwise, we will have to copy it.
	    copyFileToFile(filePathObject, destDirObject);
	    return;
	}

	else if(protocol.equals(HTTP_PROTOCOL)) {
	    copyUrlToFile(inputFileUrl,inputFileDest);
	}

	else {
	    System.out.println("Unknown protocol for accessing inputfile");
	    throw new Exception("Unknown protocol");
	}
	return;
    }
    

    /**
     * Famous method that I googled. This copies a file to a new
     * place on the file system.
     */
    private void copyFileToFile(File sourceFile,File destFile) 
	throws Exception {
	InputStream in=new FileInputStream(sourceFile);
	OutputStream out=new FileOutputStream(destFile);
	byte[] buf=new byte[1024];
	int length;
	while((length=in.read(buf))>0) {
	    out.write(buf,0,length);
	}
	in.close();
	out.close();
    }

    /**
     * Another famous method that I googled. This downloads contents
     * from the given URL to a local file.
     */
    
    private void copyUrlToFile(URL inputFileUrl,String destFile) 
	throws Exception {

	URLConnection uconn=inputFileUrl.openConnection();
	InputStream in=inputFileUrl.openStream();
	OutputStream out=new FileOutputStream(destFile);

	//Extract the name of the file from the url.
	
	byte[] buf=new byte[1024];
	int length;
	while((length=in.read(buf))>0) {
	    out.write(buf,0,length);
	}
	in.close();
	out.close();
	
    }

    private String[] setUpArgArray(String inputFileUrlString,
				   String workDir,
				   String projectName,
				   String binPath,
				   int nobsv,
				   int ndim,
				   int numModelStates,
				   int randomSeed,
				   String outputType,
				   double annealStep,
				   String buildFilePath,
				   String antTarget) throws Exception {
	//Run RDAHMM
	String[] args=new String[13];
        args[0]="-DworkDir.prop="+workDir;
        args[1]="-DprojectName.prop="+projectName;
        args[2]="-Dbindir.prop="+binPath;
        args[3]="-DRDAHMMBaseName.prop="+projectName;
        args[4]="-Dnobsv.prop="+nobsv;
        args[5]="-Dndim.prop="+ndim;
        args[6]="-Dnstates.prop="+numModelStates;
        args[7]="-Dranseed.prop="+randomSeed;
        args[8]="-Doutput_type.prop="+outputType;
	args[9]="-DannealStep.prop="+annealStep;
        args[10]="-buildfile";
        args[11]=buildFilePath;
        args[12]=antTarget;

	return args;
    }
    
    /**
     * This version is used to to hold response until 
     * RDAHMM finished executing.
     */
    public void runblockingRDAHMM(String inputFileUrlString,
				  String workDir,
				  String projectName,
				  String binPath,
				  int nobsv,
				  int ndim,
				  int numModelStates,
				  int randomSeed,
				  String outputType,
				  double annealStep,
				  String buildFilePath,
				  String antTarget) throws Exception {
	
	
	//Set up the work directory
	makeWorkDir(workDir, buildFilePath);
	
	//Copy the input file to the working directory, if 
	//necessary.
	downloadInputFile(inputFileUrlString,workDir);


	String[] args=setUpArgArray(inputFileUrlString,
				    workDir,
				    projectName,
				    binPath,
				    nobsv,
				    ndim,
				    numModelStates,
				    randomSeed,
				    outputType,
				    annealStep,
				    buildFilePath,
				    antTarget);
	
	//Methods inherited from parent
        setArgs(args);
        run();
    }
    
    /**
     * This version immediately returns and is used
     * for programs that take longer to run.
     */
    public void execNonblockingRDAHMM(String inputFileUrlString,
				      String workDir,
				      String projectName,
				      String binPath,
				      int nobsv,
				      int ndim,
				      int numModelStates,
				      int randomSeed,
				      String outputType,
				      double annealStep,
				      String buildFilePath,
				      String antTarget) throws Exception {
	
	//Make working directory
	makeWorkDir(workDir,buildFilePath);

	//Copy the input file to the working directory, if 
	//necessary.
	downloadInputFile(inputFileUrlString,workDir);
	
	String[] args=setUpArgArray(inputFileUrlString,
				    workDir,
				    projectName,
				    binPath,
				    nobsv,
				    ndim,
				    numModelStates,
				    randomSeed,
				    outputType,
				    annealStep,
				    buildFilePath,
				    antTarget);
	
	
	//Methods inherited from parent
        setArgs(args);
        execute();
	
    }
}

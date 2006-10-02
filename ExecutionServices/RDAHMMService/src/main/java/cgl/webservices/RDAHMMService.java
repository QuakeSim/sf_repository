package cgl.webservices;
import java.util.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;

/**
 * A simple wrapper for Ant.
 */
public class RDAHMMService extends AntVisco implements Runnable{    
    static Logger logger=Logger.getLogger(RDAHMMService.class);
    
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
    
    /**
     * This version is used to to hold response until 
     * RDAHMM finished executing.
     */
    public void runblockingRDAHMM(String inputFileUrl,
				  String workDir,
				  String projectName,
				  String binPath,
				  int nobsv,
				  int ndim,
				  int numModelStates,
				  int randomSeed,
				  String outputType,
				  double annealStep,
				  String buildFile) throws Exception {
	
	String bf_loc=binPath+"/"+"build.xml";
	
	//Set up the work directory
	makeWorkDir(workDir, bf_loc);
	
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
        args[11]=bf_loc;
        args[12]="RunRDAHMM";
	
	//Methods inherited from parent
        setArgs(args);
        run();
	
    }

    /**
     * This version immediately returns and is used
     * for programs that take longer to run.
     */
    public void execNonblockingRDAHMM(String inputFileUrl,
				      String workDir,
				      String projectName,
				      String binPath,
				      int nobsv,
				      int ndim,
				      int numModelStates,
				      int randomSeed,
				      String outputType,
				      double annealStep,
				      String buildFile) throws Exception {
	
	String bf_loc=binPath+"/"+"build.xml";
	
	//Make working directory
	makeWorkDir(workDir,bf_loc);
	
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
        args[11]=bf_loc;
        args[12]="RunRDAHMM";


	//Methods inherited from parent
        setArgs(args);
        execute();
	
    }
}

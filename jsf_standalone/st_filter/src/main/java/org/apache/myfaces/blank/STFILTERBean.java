package org.apache.myfaces.blank;

//Imports from the mother ship
import org.servogrid.genericproject.GenericSopacBean;
import org.servogrid.genericproject.GenericProjectBean;
import org.servogrid.genericproject.Utility;
import org.servogrid.genericproject.ProjectBean;

//Some Faces Context stuff
//Faces classes
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.portlet.PortletContext;

//QuakeSim Web Service clients
import WebFlowClient.cm.*;
import WebFlowClient.fsws.*;
import cgl.webclients.*;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Usual java stuff.
import java.net.URL;
import java.io.File;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Date;

/**
 * Everything you need to set up and run STFILTER.
 */

public class STFILTERBean extends GenericSopacBean {

    //Internal properties
//     boolean isInitialized=false;
//     ContextManagerImp cm=null;
//     String contextName;
//     String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};
    
//    private ContextManagerImp cm;

    //Some internal fields.
    String twospace="  ";  //Used to format the driver file.
    boolean projectCreated=false;

    //STFILTER properties
    private String codeName="STFILTER";
    private String projectName="";
    private int resOption;
    private int termOption;
    private double cutoffCriterion;
    private double estJumpSpan;
    private String weakObsCriteria;
    private String outlierCriteria;
    private String badObsCriteria;
    private String timeInterval;
    private String inputFileName="";
    private String inputFileContent="";
    private String inputFileExtension=".drv";

    private String aprioriValueFile="";
    private String mosesDataList="";
    private String mosesSiteList="";
    private String mosesParamFile="";
    private String residualFile="";
    private String termOutFile="";

    //Project properties
    private String chosenProject="";

    private String[] contextList;
    private Hashtable contextListHash;
    private Vector contextListVector;
    
    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------

    public String getOutlierCriteria() {
	return outlierCriteria;
    }

    public void  setOutlierCriteria(String outlierCriteria) {
	this.outlierCriteria=outlierCriteria;
    }

    public int getResOption() {
	return resOption;
    }

    public void setResOption(int resOption) {
	this.resOption=resOption;
    }

    public int getTermOption() {
	return termOption;
    }

    public void setTermOption(int termOption) {
	this.termOption=termOption;
    }

    public double getCutoffCriterion() {
	return cutoffCriterion;
    }

    public void setCutoffCriterion(double cutoffCriterion) {
	this.cutoffCriterion=cutoffCriterion;
    }

    public double getEstJumpSpan() {
	return estJumpSpan;
    }

    public void setEstJumpSpan(double estJumpSpan) {
	this.estJumpSpan=estJumpSpan;
    }

    public String getWeakObsCriteria() {
	return weakObsCriteria;
    }

    public void setWeakObsCriteria(String weakObsCriteria) {
	this.weakObsCriteria=weakObsCriteria;
    }

    public String getBadObsCriteria() {
	return badObsCriteria;
    }

    public void setBadObsCriteria(String badObsCriteria) {
	this.badObsCriteria=badObsCriteria;
    }

    public String getTimeInterval() {
	return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
	this.timeInterval=timeInterval;
    }

    
    /**
     * default empty constructor
     */
    public STFILTERBean(){   
	super();
	cm=getContextManagerImp();
    }

    /**
     * Method that is backed to a submit button of a form.
     */
    public String newProject() throws Exception{
	if(!isInitialized) {
	    initWebServices();
	}
	return ("new-project-created");
    }
    
    public String paramsThenTextArea() throws Exception {
	setParameterValues();
	return "parameters-to-textfield";
    }

    public String paramsThenDB() throws Exception {
	//	setParameterValues();
	createNewProject();
	return "parameters-to-database";
    }

    public String paramsThenMap() throws Exception {
	//	setParameterValues();
	createNewProject();
	return "parameters-to-googlemap";
    }

    public String createNewProject() throws Exception {
	System.out.println("Creating new project");
	
	//Store the request values persistently
	contextName=codeName+"/"+projectName;
	String hostName=getHostName();
	cm.addContext(contextName);
	cm.setCurrentProperty(contextName,"projectName",projectName);
	cm.setCurrentProperty(contextName,"hostName",hostName);	
	projectCreated=true;
	
	return "new-project-created";
    }

    public String setParameterValues() throws Exception {
	//This should always be true at this point, but check for 
	//safety.
	if(projectCreated!=true) {
	    createNewProject();
	}

	//Now set the rest of the parameters.
	cm.setCurrentProperty(contextName,"resOption",resOption+"");
	cm.setCurrentProperty(contextName,"termOption",termOption+"");
	cm.setCurrentProperty(contextName,"cutoffCriterion",
			      cutoffCriterion+"");	
	cm.setCurrentProperty(contextName,"estJumpSpan",estJumpSpan+"");
	cm.setCurrentProperty(contextName,"weakObsCriteria",
			      weakObsCriteria);
	cm.setCurrentProperty(contextName,"outlierCriteria",outlierCriteria);
	cm.setCurrentProperty(contextName,"badObsCriteria",badObsCriteria);
	cm.setCurrentProperty(contextName,"timeInterval",timeInterval);
	return "parameters-set";
    }
    
    public String loadDataArchive()throws Exception{
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("load-data-archive");
    }
    
    public String loadProject() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("list-old-projects");
    }

    public String loadProjectPlots() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("list-project-plots");
    }
    
    public String launchSTFILTER() throws Exception {
	String inputFileName=projectName+inputFileExtension;
	String cfullName=codeName+"/"+projectName;
	String contextDir=cm.getCurrentProperty(cfullName,"Directory");

	createInputFile(contextDir,inputFileName,inputFileContent);
	String value=executeSTFILTER(contextDir,inputFileName,cfullName);
	return "stfilter-launched";

    }

    public String populateAndPlot() throws Exception {
	populateProject();
	launchPlot();
	return "plot-created";
    }

    /**
     * Currently empty.
     */
    public String launchPlot() throws Exception {

	return "does nothing";
    }
    
    public String createInputFile(String contextDir,
				  String inputFileName,
				  String inputFileContent) 
	throws Exception {
	System.out.println("Writing input file: "+contextDir+"/"+inputFileName);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+inputFileName),true);
	pw.println(twospace+"apriori value file: \t"+aprioriValueFile);
	pw.println(twospace+"input file: \t"+mosesDataList);
	pw.println(twospace+"sit_list file: \t"+mosesSiteList);
	pw.println(twospace+"est_parameter file: \t"+mosesParamFile);
	pw.println(twospace+"output file: \t"+projectName+".out");
	pw.println(twospace+"residual file: \t"+residualFile);
	pw.println(twospace+"res_option: \t"+resOption);
	pw.println(twospace+"specific term_out file: \t"+termOutFile);
	pw.println(twospace+"specific term_option: \t"+termOption);
	pw.println(twospace+"enu_correlation usage: \t"+"no");
	pw.println(twospace+"cutoff criterion (year): \t"+cutoffCriterion);
	pw.println(twospace+"span to est jump aper (est_jump_span): \t"+estJumpSpan);
	pw.println(twospace+"weak_obs (big sigma) criteria: \t"+weakObsCriteria);
	pw.println(twospace+"outlier (big o-c) criteria mm: \t"+outlierCriteria);
	pw.println(twospace+"very bad_obs criteria mm: \t"+badObsCriteria);
	pw.println(twospace+"t_interval: \t"+timeInterval);
	pw.println(twospace+"end:");
	pw.println("---------- part 2 -- apriori information");
	pw.println(twospace+"exit:");
	pw.close();

	//Clean this up since it could be a memory drain.
	//	inputFileContent=null;
	return "input-file-created";
    }


    public String populateProject() throws Exception{
	System.out.println("Chosen project: "+chosenProject);
	String contextName=codeName+"/"+chosenProject;
	projectName=cm.getCurrentProperty(contextName,"projectName");
	hostName=cm.getCurrentProperty(contextName,"hostName");
	resOption=Integer.parseInt(cm.getCurrentProperty(contextName,"resOption"));
	termOption=
	    Integer.parseInt(cm.getCurrentProperty(contextName,"termOption"));
	cutoffCriterion=
	    Double.parseDouble(cm.getCurrentProperty(contextName,"cutoffCriterion"));
	estJumpSpan=
	    Double.parseDouble(cm.getCurrentProperty(contextName,"estJumpSpan"));
	weakObsCriteria=cm.getCurrentProperty(contextName,"weakObsCriteria");
	outlierCriteria=cm.getCurrentProperty(contextName,"outlierCriteria");
	badObsCriteria=cm.getCurrentProperty(contextName,"badObsCriteria");
	timeInterval=cm.getCurrentProperty(contextName,"timeInterval");
	
	inputFileName=cm.getCurrentProperty(contextName,"inputFileName");
	inputFileContent=setSTFILTERInputFile(projectName);
	return "project-populated";
    }

    public String executeSTFILTER(String contextDir,
				String inputFileName,
				String cfullName) 
	throws Exception{
	
	System.out.println("FileService URL:"+fileServiceUrl);
	System.out.println("AntService URL:"+antUrl);
	
	String workDir=baseWorkDir+File.separator
	    +userName+File.separator+projectName;

	//--------------------------------------------------
	// Set up the Ant Service and make the directory
	//--------------------------------------------------
	AntVisco ant=new AntViscoServiceLocator().getAntVisco(new URL(antUrl));
	String bf_loc=binPath+"/"+"build.xml";
	String[] args0=new String[4];
        args0[0]="-DworkDir.prop="+workDir;
        args0[1]="-buildfile";
        args0[2]=bf_loc;
        args0[3]="MakeWorkDir";
	
        ant.setArgs(args0);
        ant.run();
	
	//--------------------------------------------------
	// Set up the file service and move the file.
	//--------------------------------------------------
	FSClientStub fsclient=new FSClientStub();
	String destfile=workDir+"/"+inputFileName; 
	try {
	    fsclient.setBindingUrl(fileServiceUrl);    	
	    fsclient.uploadFile(contextDir+"/"+inputFileName,destfile);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
	
	//--------------------------------------------------
	// Record the names of the input, output, and log
	// files on the remote server.
	//--------------------------------------------------
	String remoteOutputFile=workDir+"/"+projectName+".output";
	String remoteLogFile=workDir+"/"+projectName+".stdout";
	
	cm.setCurrentProperty(cfullName,"RemoteInputFile",destfile);
	cm.setCurrentProperty(cfullName,"RemoteOutputFile",remoteOutputFile);
	cm.setCurrentProperty(cfullName,"RemoteLogFile",remoteLogFile);
	
	//--------------------------------------------------
	// Run the code.
	//--------------------------------------------------	
	
	String[] args=new String[7];
        args[0]="-DworkDir.prop="+workDir;
        args[1]="-DprojectName.prop="+projectName;
        args[2]="-Dbindir.prop="+binPath;
        args[3]="-DSTFILTERBaseName.prop="+projectName;
        args[4]="-buildfile";
        args[5]=bf_loc;
        args[6]="RunSTFILTER";
	
        ant.setArgs(args);
        ant.execute();
	
	return "stfilter-executing";
    }

    /**
     * This is similar to executeSTFILTER but it must take place on
     * a host with gnuplot installed on it.  Note this assumes
     * for historical reasons that stfilter and the plotting tool
     * (gnuplot) are on separate machines.
     *
     * This method is currently empty.
     */
    public String createDataPlot(String contextDir,
				 String inputFileName,
				 String cfullName) 
	throws Exception{
	
// 	String workDir=baseWorkDir+File.separator
// 	    +userName+File.separator+projectName;

// 	String gnuplotWorkDir=gnuplotBaseWorkDir+File.separator
// 	    +userName+File.separator+projectName;

// 	//--------------------------------------------------
// 	// Set up the Ant Service and make the directory on 
// 	// the gnuplot host.
// 	//--------------------------------------------------
// 	AntVisco ant=
// 	    new AntViscoServiceLocator().getAntVisco(new URL(gnuplotAntUrl));
// 	String bf_loc=gnuplotBinPath+"/"+"build.xml";

// 	String[] args0=new String[4];
//         args0[0]="-DworkDir.prop="+workDir;
//         args0[1]="-buildfile";
//         args0[2]=bf_loc;
//         args0[3]="MakeWorkDir";
	
//         ant.setArgs(args0);
//         ant.run();
	
// 	//--------------------------------------------------
// 	// Set up the file service and move the file.
// 	//--------------------------------------------------
// 	FSClientStub fsclient=new FSClientStub();
// 	String sourceFile=workDir+"/"+inputFileName; 
// 	String inputdestfile=gnuplotBinPath+"/"+inputFileName;
	
// 	String qSourceFile=workDir+"/"+projectName+".Q"; 
// 	String qDestFile=gnuplotBinPath+"/"+projectName+".Q"; 

// 	String plotFileNameX=gnuplotBinPath+"/"+inputFileName+".X.png";
// 	String plotFileNameY=gnuplotBinPath+"/"+inputFileName+".Y.png";
// 	String plotFileNameZ=gnuplotBinPath+"/"+inputFileName+".Z.png";

// 	try {
// 	    fsclient.setBindingUrl(fileServiceUrl);    	
// 	    fsclient.crossload(sourceFile,gnuplotFileServiceUrl,inputdestfile);
// 	    fsclient.crossload(qSourceFile,gnuplotFileServiceUrl,qDestFile);
// 	}
// 	catch(Exception ex) {
// 	    ex.printStackTrace();
// 	    throw new Exception();
// 	}
	
// 	String[] args=new String[7];
//         args[0]="-DworkDir.prop="+gnuplotWorkDir;
//         args[1]="-DbinDir.prop="+gnuplotBinPath;
//         args[2]="-DinputFile.prop="+inputFileName;
//         args[3]="-DqFile.prop="+projectName+".Q";
//         args[4]="-buildfile";
//         args[5]=bf_loc;
//         args[6]="ExecGnuplot";
	
//         ant.setArgs(args);
//         ant.run();
	
// 	cm.setCurrentProperty(cfullName,"PlotFileNameX",plotFileNameZ);
// 	cm.setCurrentProperty(cfullName,"PlotFileNameY",plotFileNameY);
// 	cm.setCurrentProperty(cfullName,"PlotFileNameZ",plotFileNameZ);
	
// 	//Download the image file
	
// 	ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
//         Object context=ec.getContext();
// 	String basePath="";
// 	if(context instanceof ServletContext){
// 	    basePath=((ServletContext)context).getRealPath("/");
// 	}
// 	else if(context instanceof PortletContext){
// 	    basePath=((PortletContext)context).getRealPath("/");
// 	}
	
// 	long timestamp=(new Date()).getTime();
// 	String realImageFileX=basePath+"/"+"junkX_"+timestamp+".png";
// 	String realImageFileY=basePath+"/"+"junkY_"+timestamp+".png";
// 	String realImageFileZ=basePath+"/"+"junkZ_"+timestamp+".png";
// 	localImageFileX="junkX_"+(new Date()).getTime()+".png";
// 	localImageFileY="junkY_"+(new Date()).getTime()+".png";
// 	localImageFileZ="junkZ_"+(new Date()).getTime()+".png";
// 	fsclient.setBindingUrl(gnuplotFileServiceUrl);    	

//  	try {
// 	    fsclient.downloadFile(plotFileNameX,realImageFileX);
// 	}
// 	catch(Exception ex) {
// 	    ex.printStackTrace();
// 	}
//  	try {
// 	    fsclient.downloadFile(plotFileNameY,realImageFileY);
// 	}
// 	catch(Exception ex) {
// 	    ex.printStackTrace();
// 	}
//  	try {
// 	    fsclient.downloadFile(plotFileNameZ,realImageFileZ);
// 	}
// 	catch(Exception ex) {
// 	    ex.printStackTrace();
// 	}

	
	return "gnuplot-plot-created";
    }
    
    /**
     * Override this method.
     */ 
    public String querySOPAC() throws Exception {
	
	String minMaxLatLon=null;
	
	System.out.println("Do the query");
	System.out.println("Use bounding box:"+bboxChecked);
	System.out.println(siteCode);
	System.out.println(beginDate);
	System.out.println(endDate);
	System.out.println(resource);	
	System.out.println(contextGroup);	
	System.out.println(contextId);	
	System.out.println(minMaxLatLon);	


	if(bboxChecked) {
	    minMaxLatLon=minLatitude+" "+minLongitude+
		" "+maxLatitude+" "+maxLongitude;
	}
	
	GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	gsq.setFromServlet(siteCode, beginDate, endDate, resource,
			   contextGroup, contextId, minMaxLatLon);
	sopacQueryResults=gsq.getResource();
	System.out.println("Query Results");
	System.out.println(sopacQueryResults);
	//	sopacQueryResults=filterResults(sopacQueryResults,2,3);
	
	inputFileContent=sopacQueryResults;
		
	String codeName=getCodeName();
	codeName=codeName.toLowerCase();
	System.out.println("Sopac query action string:"+codeName+"-display-query-results");
	return codeName+"-display-query-results";
    }

    private String setSTFILTERInputFile(String projectName) {
	String inputFileContent="Null Content; please re-enter";
	String inputFileName=projectName+inputFileExtension;
	try {
	    String thedir=cm.getCurrentProperty(codeName
						+"/"+projectName,"Directory");
	    System.out.println(thedir+"/"+inputFileName);
	    
	    BufferedReader buf=
		new BufferedReader(new FileReader(thedir+"/"+inputFileName));
	    String line=buf.readLine();
	    inputFileContent=line+"\n";
	    while(line!=null) {
		System.out.println(line);
		line=trimLine(line);	
		inputFileContent+=line+"\n";
		line=buf.readLine();
	    }
	    buf.close();
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
	return inputFileContent;
    }
}

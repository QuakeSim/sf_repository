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
 * Everything you need to set up and run RDAHMM.
 */

public class RDAHMMBean extends GenericSopacBean{
    
    //Variables that we need to get from the parent.
//     ContextManagerImp cm=null;
//     boolean isInitialized=false;

    //RDAHMM file extensions
    String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};

    //RDAHMM properties
    protected String codeName="RDAHMM";
    protected int numModelStates=2;
    protected int randomSeed=1;
    protected String outputType="";
    //    protected String inputFileName="";
    //    protected String inputFileContent="";
    protected double annealStep=0.01;    
    
    //RDAHMM Gnuplot stuff properties
    protected String gnuplotAntUrl;
    protected String gnuplotBinPath;
    protected String gnuplotBaseWorkDir;
    protected String gnuplotFileServiceUrl;

    protected String localImageFileX;
    protected String localImageFileY;    
    protected String localImageFileZ;

    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------

    public String getLocalImageFileX() {
	return localImageFileX;
    }
    
    public void setLocalImageFileX(String localImageFileX){
	this.localImageFileX=localImageFileX;
    }

    public String getLocalImageFileY() {
	return localImageFileY;
    }
    
    public void setLocalImageFileY(String localImageFileY){
	this.localImageFileY=localImageFileY;
    }

    public String getLocalImageFileZ() {
	return localImageFileZ;
    }
    
    public void setLocalImageFileZ(String localImageFileZ){
	this.localImageFileZ=localImageFileZ;
    }

    public String getGnuplotFileServiceUrl() {
	return gnuplotFileServiceUrl;
    }

    public void setGnuplotFileServiceUrl(String gnuplotFileServiceUrl) {
	this.gnuplotFileServiceUrl=gnuplotFileServiceUrl;
    }

    public String getGnuplotBaseWorkDir() {
	return gnuplotBaseWorkDir;
    }

    public void setGnuplotBaseWorkDir(String gnuplotBaseWorkDir) {
	this.gnuplotBaseWorkDir=gnuplotBaseWorkDir;
    }

    public String getGnuplotBinPath() {
	return gnuplotBinPath;
    }

    public void setGnuplotBinPath(String gnuplotBinPath) {
	this.gnuplotBinPath=gnuplotBinPath;
    }

    public String getGnuplotAntUrl() {
	return gnuplotAntUrl;
    }

    public void setGnuplotAntUrl(String gnuplotAntUrl) {
	this.gnuplotAntUrl=gnuplotAntUrl;
    }

    public String getGnuplotHostName() {
	return gnuplotHostName;
    }

    public void setGnuplotHostName(String gnuplotHostName) {
	this.gnuplotHostName=gnuplotHostName;
    }

    public double getAnnealStep(){
	return annealStep;
    }

    public void setAnnealStep(double annealStep){
	this.annealStep=annealStep;
    }

    public int getNumModelStates() {
	return numModelStates;
    }

    public void setNumModelStates(int numModelStates){
	this.numModelStates=numModelStates;
    }

    public int getRandomSeed() {
	return randomSeed;
    }

    public void setRandomSeed(int randomSeed){
	this.randomSeed=randomSeed;
    }
    
    public String getOutputType() {
	return outputType;
    }

    public void setOutputType(String outputType){
	this.outputType=outputType;
    }

    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
	super();
	cm=getContextManagerImp();
	System.out.println("RDAHMM Bean Created");
    }
    
    /**
     * These are methods associated with Faces
     * navigations.
     */
    public String newProject() throws Exception{
	isInitialized=getIsInitialized();
	if(!isInitialized) {
	    initWebServices();
	}
	return ("rdahmm-new-project-created");
    }
    
    public String paramsThenTextArea() throws Exception {
	setParameterValues();
	return "rdahmm-parameters-to-textfield";
    }

    public String paramsThenDB() throws Exception {
	setParameterValues();
	return "rdahmm-parameters-to-database";
    }

    public String paramsThenMap() throws Exception {
	setParameterValues();
	return "rdahmm-parameters-to-googlemap";
    }

    public String setParameterValues() throws Exception {
        //Do real logic
	System.out.println("Creating new project");
	
	//Store the request values persistently
	contextName=codeName+"/"+projectName;
	cm.addContext(contextName);
	cm.setCurrentProperty(contextName,"projectName",projectName);
	cm.setCurrentProperty(contextName,"hostName",hostName);
	cm.setCurrentProperty(contextName,"numModelStates",
			      numModelStates+"");
	cm.setCurrentProperty(contextName,"randomSeed",randomSeed+"");
	cm.setCurrentProperty(contextName,"annealStep",annealStep+"");
	cm.setCurrentProperty(contextName,"outputType",outputType);
	return "rdahmm-parameters-set";
    }
    
    public String loadDataArchive()throws Exception{
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("rdahmm-load-data-archive");
    }
    
    public String loadProject() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("rdahmm-list-old-projects");
    }

    public String loadProjectPlots() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("rdahmm-list-project-plots");
    }
    
    public String launchRDAHMM() throws Exception {
	String inputFileName=projectName+".input";
	String cfullName=codeName+"/"+projectName;
	String contextDir=cm.getCurrentProperty(cfullName,"Directory");
	String inputFileContent=getInputFileContent();

	createInputFile(contextDir,inputFileName,inputFileContent);
	String value=executeRDAHMM(contextDir,inputFileName,cfullName);
	return "rdahmm-rdahmm-launched";

    }

    public String populateAndPlot() throws Exception {
	populateProject();
	launchPlot();
	return "rdahmm-plot-created";
    }

    public String launchPlot() throws Exception {
	String inputFileName=projectName+".input";
	String cfullName=codeName+"/"+projectName;
	String contextDir=cm.getCurrentProperty(cfullName,"Directory");

	createInputFile(contextDir,inputFileName,inputFileContent);
	String value=createDataPlot(contextDir,inputFileName,cfullName);
	return "rdahmm-gnuplot-launched";

    }

    //Possibly obsolete--need to check.
    public String launchProject() {
	return "rdahmm-project-launched";
    }

    public String populateProject() throws Exception{
	System.out.println("Chosen project: "+chosenProject);
	String contextName=codeName+"/"+chosenProject;
	projectName=cm.getCurrentProperty(contextName,"projectName");
	hostName=cm.getCurrentProperty(contextName,"hostName");
	numModelStates=
	    Integer.parseInt(cm.getCurrentProperty(contextName,"numModelStates"));
	randomSeed=
	    Integer.parseInt(cm.getCurrentProperty(contextName,"randomSeed"));
	outputType=cm.getCurrentProperty(contextName,"outputType");
	annealStep=
	    Double.parseDouble(cm.getCurrentProperty(contextName,
						     "annealStep"));
	
	inputFileName=cm.getCurrentProperty(contextName,"inputFileName");
	inputFileContent=setRDAHMMInputFile(projectName);
	System.out.println("Input File:"+inputFileContent);
	return "rdahmm-project-populated";
    }


    public String executeRDAHMM(String contextDir,
				String inputFileName,
				String cfullName) 
	throws Exception{
	
	System.out.println("FileService URL:"+fileServiceUrl);
	System.out.println("AntService URL:"+antUrl);
	
	String workDir=baseWorkDir+File.separator
	    +userName+File.separator+projectName;

	int ndim=getFileDimension(contextDir,inputFileName);
	int nobsv=getLineCount(contextDir,inputFileName);
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
	// Set up the Ant Service.
	//--------------------------------------------------
	//	AntVisco ant=new AntViscoServiceLocator().getAntVisco(new URL(antUrl));
	
	
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
	
        ant.setArgs(args);
        ant.execute();
	
	return "rdahmm-rdahmm-executing";
    }

    /**
     * This is similar to executeRDAHMM but it must take place on
     * a host with gnuplot installed on it.  Note this assumes
     * for historical reasons that rdahmm and the plotting tool
     * (gnuplot) are on separate machines.
     */
    public String createDataPlot(String contextDir,
				 String inputFileName,
				 String cfullName) 
	throws Exception{
	
	String workDir=baseWorkDir+File.separator
	    +userName+File.separator+projectName;

	String gnuplotWorkDir=gnuplotBaseWorkDir+File.separator
	    +userName+File.separator+projectName;

	//--------------------------------------------------
	// Set up the Ant Service and make the directory on 
	// the gnuplot host.
	//--------------------------------------------------
	AntVisco ant=
	    new AntViscoServiceLocator().getAntVisco(new URL(gnuplotAntUrl));
	String bf_loc=gnuplotBinPath+"/"+"build.xml";

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
	String sourceFile=workDir+"/"+inputFileName; 
	String inputdestfile=gnuplotBinPath+"/"+inputFileName;
	
	String qSourceFile=workDir+"/"+projectName+".Q"; 
	String qDestFile=gnuplotBinPath+"/"+projectName+".Q"; 

	String plotFileNameX=gnuplotBinPath+"/"+inputFileName+".X.png";
	String plotFileNameY=gnuplotBinPath+"/"+inputFileName+".Y.png";
	String plotFileNameZ=gnuplotBinPath+"/"+inputFileName+".Z.png";

	try {
	    fsclient.setBindingUrl(fileServiceUrl);    	
	    fsclient.crossload(sourceFile,gnuplotFileServiceUrl,inputdestfile);
	    fsclient.crossload(qSourceFile,gnuplotFileServiceUrl,qDestFile);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	    throw new Exception();
	}
	
	String[] args=new String[7];
        args[0]="-DworkDir.prop="+gnuplotWorkDir;
        args[1]="-DbinDir.prop="+gnuplotBinPath;
        args[2]="-DinputFile.prop="+inputFileName;
        args[3]="-DqFile.prop="+projectName+".Q";
        args[4]="-buildfile";
        args[5]=bf_loc;
        args[6]="ExecGnuplot";
	
        ant.setArgs(args);
        ant.run();
	
	cm.setCurrentProperty(cfullName,"PlotFileNameX",plotFileNameZ);
	cm.setCurrentProperty(cfullName,"PlotFileNameY",plotFileNameY);
	cm.setCurrentProperty(cfullName,"PlotFileNameZ",plotFileNameZ);
	
	//Download the image file
	
	ExternalContext ec=FacesContext.getCurrentInstance().getExternalContext();
        Object context=ec.getContext();
	String basePath="";
	if(context instanceof ServletContext){
	    basePath=((ServletContext)context).getRealPath("/");
	}
	else if(context instanceof PortletContext){
	    basePath=((PortletContext)context).getRealPath("/");
	}
	
	long timestamp=(new Date()).getTime();
	String realImageFileX=basePath+"/"+"junkX_"+timestamp+".png";
	String realImageFileY=basePath+"/"+"junkY_"+timestamp+".png";
	String realImageFileZ=basePath+"/"+"junkZ_"+timestamp+".png";
	localImageFileX="junkX_"+(new Date()).getTime()+".png";
	localImageFileY="junkY_"+(new Date()).getTime()+".png";
	localImageFileZ="junkZ_"+(new Date()).getTime()+".png";
	fsclient.setBindingUrl(gnuplotFileServiceUrl);    	

 	try {
	    fsclient.downloadFile(plotFileNameX,realImageFileX);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
 	try {
	    fsclient.downloadFile(plotFileNameY,realImageFileY);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
 	try {
	    fsclient.downloadFile(plotFileNameZ,realImageFileZ);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}


	return "rdahmm-gnuplot-plot-created";
    }

    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then RDAHMM itself 
    // would probably not work either.
    //--------------------------------------------------
    
    protected int getFileDimension(String contextDir, 
				 String inputFileName) {
	
	boolean success=false;
	int ndim=0;
	StringTokenizer st;
	try {

	    BufferedReader buf=
		new BufferedReader(new FileReader(contextDir+"/"+inputFileName));
	    
	    String line=buf.readLine();	
	    if(line!=null){
		while(!success) {
		    if(line.trim().equals("")) {
			line=buf.readLine();
		    }
		    else {
			success=true;
			st=new StringTokenizer(line);
			ndim=st.countTokens();
		    }		   
		}
	    }
	    buf.close();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
	return ndim;
    }

    //--------------------------------------------------
    // This counts the line number.
    //--------------------------`------------------------
    protected int getLineCount(String contextDir, String inputFileName) {
	int nobsv=0;
	try {
	    LineNumberReader lnr=
		new LineNumberReader(new FileReader(contextDir+"/"+inputFileName));
	    
	    String line2=lnr.readLine();
	    while(line2!=null) {
		line2=lnr.readLine();
	    }
	    lnr.close();
	    nobsv=lnr.getLineNumber();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
	
	return nobsv;

    }

    protected String setRDAHMMInputFile(String projectName) {
	String inputFileContent="Null Content; please re-enter";
	String inputFileName=projectName+".input";
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
    
    
    public String querySOPAC() throws Exception {
	String retString=super.querySOPAC();
	inputFileContent=filterResults(inputFileContent, 2, 3);
	return retString;
    }

    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     */
    protected String filterResults(String tabbedString,
				 int cutLeftColumns,
				 int cutRightColumns) throws Exception {
	String returnString="";
	String space=" ";
	StringTokenizer st;
	BufferedReader br=new BufferedReader(new StringReader(tabbedString));
	String line=br.readLine();
	while(line!=null) {
	    st=new StringTokenizer(line);
	    String newLine="";
	    int tokenCount=st.countTokens();
	    for (int i=0;i<tokenCount;i++) {
		String temp=st.nextToken();
		if(i>=cutLeftColumns && i<(tokenCount-cutRightColumns)) {
		    newLine+=temp+space;
		}
	    }
	    returnString+=newLine+"\n";
	    line=br.readLine();
	}
	System.out.println(returnString);
	return returnString;
    }


}

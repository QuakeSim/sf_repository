package org.apache.myfaces.blank;

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

public class RDAHMMBean {

    //Internal properties
    boolean isInitialized=false;
    ContextManagerImp cm=null;
    String contextName;
    String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};

    //These are SOPAC properties
    private String siteCode="sio5";
    private String beginDate="2006-01-01";
    private String endDate="2006-01-10";
    private boolean bboxChecked=false;
    private double minLatitude=32.0;
    private double maxLatitude=33.4;
    private double minLongitude=-120.0;
    private double maxLongitude=-117.0;
    private String resource;
    private String contextGroup;
    private String contextId="4";
    private String sopacQueryResults="";


    //RDAHMM properties
    private String userName;
    private String defaultName="defaultUser";
    private String contextUrl;
    private String contextBasePath;
    private String FS="FS";
    private String codeName="RDAHMM";
    private String projectName="";
    private int numModelStates=2;
    private int randomSeed=1;
    private String outputType="";
    private String inputFileName="";
    private String inputFileContent="";
    private double annealStep=0.01;

    //Project properties
    private String chosenProject="";

    private String[] contextList;
    private Hashtable contextListHash;
    private Vector contextListVector;
    
    private String binPath;
    private String baseWorkDir;
    private String antUrl;

    private String fileServiceUrl;

    
    //This will just be hard coded for now.
    private String hostName="danube.ucs.indiana.edu";

    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------
    public double getAnnealStep(){
	return annealStep;
    }
    public void setAnnealStep(double annealStep){
	this.annealStep=annealStep;
    }

    public String getSopacQueryResults() {
	return sopacQueryResults;
    }
    public void setSopacQueryResults(String sopacQueryResults) {
	this.sopacQueryResults=sopacQueryResults;
    }

    public String getUserName() {
	return userName;
    }
    public void setUserName(String userName) {
	this.userName=userName;
    }
    public String getBinPath() {
	return binPath;
    }
    public void setBinPath(String binPath){
	this.binPath=binPath;
    }
    public String getBaseWorkDir() {
	return baseWorkDir;
    }
    public void setBaseWorkDir(String baseWorkDir){
	this.baseWorkDir=baseWorkDir;
    }
    public String getAntUrl() {
	return antUrl;
    }
    public void setAntUrl(String antUrl){
	this.antUrl=antUrl;
    }
    public String getFileServiceUrl(){
	return fileServiceUrl;
    }
    public void setFileServiceUrl(String fileServiceUrl){
	this.fileServiceUrl=fileServiceUrl;
    }
   
    public String getChosenProject() {
	return chosenProject;
    }
    public void setChosenProject(String chosenProject) {
	this.chosenProject=chosenProject;
    }

    public Hashtable getContextListHash() {
	return contextListHash;
    }

    public void setContextListHash(Hashtable contextListHash) {
	this.contextListHash=contextListHash;
    }

    public Vector getContextListVector() {
	return contextListVector;
    }

    public void setContextListVector(Vector contextListVector) {
	this.contextListVector=contextListVector;
    }

    public String getContextGroup() {
	return contextGroup;
    }
    public void setContextGroup(String contextGroup) {
	this.contextGroup=contextGroup;
    }

    public String getContextId() {
	return contextId;
    }

    public void setContextId(String contextId) {
	this.contextId=contextId;
    }

    public String getResource() {
	return resource;
    }
    public void setResource(String resource) {
	this.resource=resource;
    }

    public String getSiteCode() {
	return siteCode;
    }
    public void setSiteCode(String siteCode) {
	this.siteCode=siteCode;
    }
    
    public String getBeginDate() {
	return beginDate;
    }
    public void setBeginDate(String beginDate) {
	this.beginDate=beginDate;
    }
    public String getEndDate() {
	return endDate;
    }
    public void setEndDate(String endDate) {
	this.endDate=endDate;
    }
    
    public boolean getBboxChecked() {
	return bboxChecked;
    }
    public void setBboxChecked(boolean bboxChecked) {
	this.bboxChecked=bboxChecked;
    }
    
    public double getMinLatitude() {
	return minLatitude;
    }
    
    public void setMinLatitude(double minLatitude) {
	this.minLatitude=minLatitude;
    }

    public double getMaxLatitude() {
	return maxLatitude;
    }
    
    public void setMaxLatitude(double maxLatitude) {
	this.maxLatitude=maxLatitude;
    }
    
    public double getMinLongitude() {
	return minLongitude;
    }
    
    public void setMinLongitude(double minLongitude) {
	this.minLatitude=minLatitude;
    }
    
    public void setMaxLongitude(double maxLongitude) {
	this.maxLongitude=maxLongitude;
    }
    public double getMaxLongitude() {
	return maxLongitude;
    }
    
    public void setContextList(String[] cl) {
	System.arraycopy(cl,0,this.contextList,0,cl.length);
    }
    public String[] getContextList() {
	return this.contextList;
    }
    
    public void setHostName(String hostName){
	this.hostName=hostName;
    }
    public String getHostName() {
	return this.hostName;
    }

    public void  setInputFileName(String inputFileName) {
	this.inputFileName=inputFileName;
    }
    public String getInputFileName() {
	return this.inputFileName;
    }
    public void  setInputFileContent(String inputFileContent) {
	this.inputFileContent=inputFileContent;
    }
    public String getInputFileContent() {
	return this.inputFileContent;
    }

    public boolean getIsInitialized() {
	return isInitialized;
    }
    
    public void setIsInitialized(boolean isInitialized) {
	this.isInitialized=isInitialized;
    }

    public String getProjectName() {
	return projectName;
    }
    public void setProjectName(String projectName){
	this.projectName=projectName;
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

    public String getCodeName() {
	return codeName;
    }
    
    public void setCodeName(String codeName) {
	this.codeName=codeName;
    }

    public String getContextUrl() {
	System.out.println(this.toString()+":getContextUrl:"+contextUrl);
	return contextUrl;
    }

    public void setContextUrl(String cUrl) {
	this.contextUrl=cUrl;
	System.out.println(this.toString()+":setContextUrl:"+contextUrl);
    }

    public String getContextBasePath() {
	System.out.println(this.toString()+":getContextBasePath:"+contextBasePath);
	return contextBasePath;
    }

    public void setContextBasePath(String basepath) {
	this.contextBasePath=basepath;
	System.out.println(this.toString()+":setContextBasePath:"+contextBasePath);
    }
    
    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
	System.out.println("RDAHMM Bean Created");
	userName=getPortalUserName();
	contextListVector=new Vector();
	contextListHash=new Hashtable();
    }

    public void initWebServices() {
	System.out.println("Initializing web services");

	try {
	    //--------------------------------------------------
	    //Set up the context manager service.
	    //--------------------------------------------------	
	    String base_userpath=getContextBasePath()+
		File.separator+userName+File.separator+codeName;
	    System.out.println("baseuserpath:"+base_userpath);
	    

	    cm=(new ContextManagerImpServiceLocator()).
		getContextManager(new URL(contextUrl));
	    ((ContextManagerSoapBindingStub) cm).setMaintainSession(true);
	    System.out.println("Stub initialized");

	    cm.setContextStorage(FS);
	    cm.init(userName,base_userpath);
	    cm.addContext(codeName);

	    System.out.println("We're done, take it home.");
	    
	    isInitialized=true;
	}
	catch(Exception ex) {
	    System.out.println("We got an exception");
	    ex.printStackTrace();
	}
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
	setParameterValues();
	return "parameters-to-database";
    }

    public String paramsThenMap() throws Exception {
	setParameterValues();
	return "parameters-to-googlemap";
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
	return "parameters-set";
    }
    
    private void setContextList() throws Exception {
	contextList=cm.listContext(codeName);
	if(contextList==null || contextList.length<=0) {
	    System.out.println(contextList.toString());
	    System.out.println("No archived projects");
	}
	else {
	    convertContextList();
	    System.out.println("Context has "+contextList.length+" elements");
	    for(int i=0;i<contextList.length;i++) {
		System.out.println(contextList[i]);
	    }
	}
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
    
    public String launchRDAHMM() throws Exception {
	String inputFileName=projectName+".input";
	String cfullName=codeName+"/"+projectName;
	String contextDir=cm.getCurrentProperty(cfullName,"Directory");

	createInputFile(contextDir,inputFileName,inputFileContent);
	String value=executeRDAHMM(contextDir,inputFileName,cfullName);
	return "rdahmm-launched";

    }
    
    public String createInputFile(String contextDir,
				  String inputFileName,
				  String inputFileContent) 
	throws Exception {
	//The value should be set by JSF from the associated JSP page.
	//We just need to clean it up and add it to the context
	
	//	cm.setCurrentProperty(contextName,"inputFileName",inputFileName);
	System.out.println("Writing input file: "+contextDir+"/"+inputFileName);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+inputFileName),true);
	pw.println(inputFileContent);
	pw.close();

	//Clean this up since it could be a memory drain.
	//	inputFileContent=null;
	return "input-file-created";
    }

    public String getPortalUserName() {
	userName=Utility.getUserName(defaultName);
	System.out.println("Username is "+userName);
	return userName;
    }

    //This is the command that runs the thing.
    public String launchProject() {
	return "project-launched";
    }

    private String trimLine(String line) {
	String endLine="\015";
	while(line.lastIndexOf(endLine)>0) {
	    line=line.substring(line.lastIndexOf(endLine));
	}
	
	return line;
    }

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
	System.out.println(sopacQueryResults);
	sopacQueryResults=filterResults(sopacQueryResults,2,3);
	
	inputFileContent=sopacQueryResults;
	
	
//  	if (returnedResource!=null 
// 	    && !returnedResource.startsWith("ERROR")) {
// 	    System.out.println(returnedResource);
	    
// 	}
	
	return "display-query-results";
    }

    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     */
    private String filterResults(String tabbedString,
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

    private void convertContextList() throws Exception {
	Hashtable returnHash=new Hashtable();
	String creationDate=null;
	String contextname=null;
	ProjectBean projectBean;
	contextListVector.clear();
	if(contextList!=null && contextList.length>0) {
	    for(int i=0;i<contextList.length;i++) {
		projectBean=new ProjectBean();
		
		contextName=codeName+"/"+contextList[i];
		creationDate=cm.getCurrentProperty(contextName,"LastTime");
		projectBean.setProjectName(contextList[i]);
		projectBean.setCreationDate(convertDate(creationDate));
		projectBean.setHostName(hostName);
		projectBean.setBaseWorkDir(baseWorkDir);
		projectBean.setFileServiceUrl(fileServiceUrl);
		contextListVector.add(projectBean);
		returnHash.put(contextList[i],contextList[i]);
	    }
	}
	setContextListHash(returnHash);
    }

    private String convertDate(String longIntForm){
	long longDate=Long.parseLong(longIntForm);
	return (new Date(longDate).toString());
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
	return "project-populated";
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
	
	return "rdahmm-executing";

    }

    //--------------------------------------------------
    // Find the first non-blank line and count columns.
    // Note this can screw up if input file is not
    // formated correctly, but then RDAHMM itself 
    // would probably not work either.
    //--------------------------------------------------
    
    private int getFileDimension(String contextDir, 
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
    private int getLineCount(String contextDir, String inputFileName) {
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

    private String setRDAHMMInputFile(String projectName) {
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
}

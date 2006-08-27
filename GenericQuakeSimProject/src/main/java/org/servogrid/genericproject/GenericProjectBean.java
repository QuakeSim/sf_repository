/**
 * This class is intended to be extended by specific projects.
 */
package org.servogrid.genericproject;

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

public class GenericProjectBean {

    //Internal properties
    boolean isInitialized=false;
    ContextManagerImp cm=null;
    String contextName;
    String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};

    //Generic project properties
    private String userName;
    private String defaultName="defaultUser";
    private String contextUrl;
    private String contextBasePath;
    private String FS="FS";
    private String projectName="";
    private String chosenProject="";
    private String[] contextList;
    private Hashtable contextListHash;
    private Vector contextListVector;
    private String codeName;
    
    //Host properties
    private String binPath;
    private String baseWorkDir;
    private String antUrl;

    private String fileServiceUrl;
    private String hostName="danube.ucs.indiana.edu";
    private String gnuplotHostName="gf2.ucs.indiana.edu";

    //--------------------------------------------------
    // These are universal accessor methods.
    //--------------------------------------------------

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
	System.out.println(this.toString()+":getContextBasePath:"
			   +contextBasePath);
	return contextBasePath;
    }

    public void setContextBasePath(String basepath) {
	this.contextBasePath=basepath;
	System.out.println(this.toString()+":setContextBasePath:"
			   +contextBasePath);
    }
    
    /**
     * default empty constructor
     */
    public GenericProjectBean(){   
	System.out.println("Generic Project Bean Created");
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

    public String loadProjectPlots() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("list-project-plots");
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
}

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
import java.util.ArrayList;

/**
 * Everything you need to set up and run RDAHMM.
 */

public class GenericProjectBean {

    //Internal properties
    protected boolean isInitialized=false;
    protected ContextManagerImp cm=null;
    protected String contextName;

    //Generic project properties
    protected String userName;
    protected String defaultName="defaultUser";
    protected String contextUrl;
    protected String contextBasePath;
    protected String FS="FS";
    protected String projectName="";
    protected String chosenProject="";
    protected String[] contextList;
    protected Hashtable contextListHash;
    protected Vector contextListVector;
    protected String codeName;
    
    //Host properties
    protected String binPath;
    protected String baseWorkDir;
    protected String antUrl;

    protected String fileServiceUrl;
    protected String hostName="danube.ucs.indiana.edu";
    protected String gnuplotHostName="gf2.ucs.indiana.edu";

    protected ArrayList projectsToDelete;

    //--------------------------------------------------
    // These are universal accessor methods.
    //--------------------------------------------------

    public ArrayList getProjectsToDelete() {
	return projectsToDelete;
    }
    
    public void setProjectsToDelete(ArrayList projectsToDelete) {
	this.projectsToDelete=projectsToDelete;
    }

    public ContextManagerImp getContextManagerImp() {
	return cm;
    }

    public void setContextManagerImp(ContextManagerImp cm) {
	this.cm=cm;
    }

    public String getContextName() {
	return contextName;
    }

    public void setContextName(String contextName) {
	this.contextName=contextName;
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
	projectsToDelete=new ArrayList();
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
    
    protected void setContextList() throws Exception {
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

    
    public String getPortalUserName() {
	userName=Utility.getUserName(defaultName);
	System.out.println("Username is "+userName);
	return userName;
    }

    //This is the command that runs the thing.
    public String launchProject() {
	return "project-launched";
    }

    protected String trimLine(String line) {
	String endLine="\015";
	while(line.lastIndexOf(endLine)>0) {
	    line=line.substring(line.lastIndexOf(endLine));
	}
	
	return line;
    }

    protected void convertContextList() throws Exception {
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

    protected String convertDate(String longIntForm){
	long longDate=Long.parseLong(longIntForm);
	return (new Date(longDate).toString());
    }
}

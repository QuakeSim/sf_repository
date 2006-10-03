/**
 * This is a straightforward extension of the GenericProjectBean
 * class.  It includes accessor methods for interacting with 
 * Sopac services.
 */
package org.servogrid.genericproject;

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

public class GenericSopacBean extends GenericProjectBean{

    //These are SOPAC properties
    protected String siteCode="sio5";
    protected String beginDate="2004-01-01";
    protected String endDate="2006-01-10";
    protected boolean bboxChecked=false;
    protected double minLatitude=32.0;
    protected double maxLatitude=33.4;
    protected double minLongitude=-120.0;
    protected double maxLongitude=-117.0;
    protected String resource;
    protected String contextGroup;
    protected String contextId="4";
    protected String sopacQueryResults="";
    protected boolean returnUrl;
    protected String sopacDataFileName;
    protected String sopacDataFileContent;
    protected String sopacDataFileUrl;
    
    /**
     * default empty constructor
     */
    public GenericSopacBean(){   
	super();
	System.out.println("Generic Sopac Bean Created");
    }

    /**
     * This version of the client gets back the URL of the
     * results, rather than the results directly.  The String
     * return type is used for JSF page navigation.
     */
    public String querySOPACGetURL() throws Exception {

	String minMaxLatLon=null;

	if(bboxChecked) {
	    minMaxLatLon=minLatitude+" "+minLongitude+
		" "+maxLatitude+" "+maxLongitude;
	}
	
	GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	gsq.setFromServlet(siteCode, beginDate, endDate, resource,
			   contextGroup, contextId, minMaxLatLon, true);
	sopacQueryResults=gsq.getResource();
	System.out.println(sopacQueryResults);
	
	sopacDataFileUrl=sopacQueryResults;
		
	String codeName=getCodeName();
	codeName=codeName.toLowerCase();
	System.out.println("Sopac query action string:"
			   +codeName+"-display-query-results");
	return codeName+"-query-results-url";
    }
    
    
    /**
     * Queries the SOPAC web service and returns the results directly.
     * The string return type is used for page navigation.
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
	System.out.println(sopacQueryResults);
	
	sopacDataFileContent=sopacQueryResults;
		
	String codeName=getCodeName();
	codeName=codeName.toLowerCase();
	System.out.println("Sopac query action string:"+codeName+"-display-query-results");
	return codeName+"-display-query-results";
    }
    
    /**
     * This method is used to write the output returned by 
     * querySOPAC() to a context file for storage.
     */
    public String createSopacDataFile(String contextDir,
				      String sopacDataFileName,
				      String sopacDataFileContent) 
	throws Exception {
	//The value should be set by JSF from the associated JSP page.
	//We just need to clean it up and add it to the context
	
	//	cm.setCurrentProperty(contextName,"sopacDataFileName",sopacDataFileName);
	System.out.println("Writing input file: "+contextDir+"/"+sopacDataFileName);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+sopacDataFileName),true);
	pw.println(sopacDataFileContent);
	pw.close();
	
	//Clean this up since it could be a memory drain.
	//	sopacDataFileContent=null;
	return "input-file-created";
    }
    
    
    public String setTheStation() {
	System.out.println("Station set: "+siteCode);
	return "parameters-to-database";
    }
    

    /**
     * Have to override this to add the siteCode.
     */
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
		projectBean.setSiteCode(siteCode);
		contextListVector.add(projectBean);
		returnHash.put(contextList[i],contextList[i]);
	    }
	}
	setContextListHash(returnHash);
    }
    
    //--------------------------------------------------
    // These are universal accessor methods.
    //--------------------------------------------------

    public boolean getReturnUrl(){
	return returnUrl;
    }

    public void setReturnUrl(boolean reutrnUrl){
	this.returnUrl=returnUrl;
    }

    public String getSopacQueryResults() {
	return sopacQueryResults;
    }

    public void setSopacQueryResults(String sopacQueryResults) {
	this.sopacQueryResults=sopacQueryResults;
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
	this.siteCode=this.siteCode.toLowerCase();
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
    public void  setSopacDataFileName(String sopacDataFileName) {
	this.sopacDataFileName=sopacDataFileName;
    }

    public String getSopacDataFileName() {
	return this.sopacDataFileName;
    }

    public void  setSopacDataFileContent(String sopacDataFileContent) {
	this.sopacDataFileContent=sopacDataFileContent;
    }

    public String getSopacDataFileContent() {
	return this.sopacDataFileContent;
    }

    public String getSopacDataFileUrl(){
	return this.sopacDataFileUrl;
    }

    public void setSopacDataFileUrl(String sopacDataFileUrl){
	this.sopacDataFileUrl=sopacDataFileUrl;
    }

}

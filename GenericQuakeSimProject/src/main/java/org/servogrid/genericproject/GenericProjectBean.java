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
import java.net.*;
import java.io.*;
import java.util.*;

//Import stuff from db4o
import com.db4o.*;

//Fault DB interface
import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;

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
    protected String hostName;
    protected String gnuplotHostName;
    //    protected String hostName="danube.ucs.indiana.edu";
    //    protected String gnuplotHostName="gf2.ucs.indiana.edu";

    protected ArrayList projectsToDelete;

    ObjectContainer db=null;

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
		  //Get rid of dubious characters
		  projectName=filterTheBadGuys(projectName);
		  
		  //Remove spaces and less dubious stuff.
		  projectName=URLDecoder.decode(projectName);
		  projectName=URLEncoder.encode(projectName);
		  
		  this.projectName=projectName;
    }
    
    public String getCodeName() {
		  return codeName;
    }
    
    public void setCodeName(String codeName) {
		  this.codeName=codeName;
    }

    public String getContextUrl() {
		  //		  System.out.println(this.toString()+":getContextUrl:"+contextUrl);
		  return contextUrl;
    }
	 
    public void setContextUrl(String cUrl) {
		  this.contextUrl=cUrl;
		  System.out.println(this.toString()+":setContextUrl:"+contextUrl);
    }
	 
    public String getContextBasePath() {
		  return contextBasePath;
    }
	 
    public void setContextBasePath(String basepath) {
		  this.contextBasePath=basepath;
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
	 
    protected String convertDate(String longIntForm){
		  long longDate=Long.parseLong(longIntForm);
		  return (new Date(longDate).toString());
    }
	 
	 /**
	  * This returns the real path to the webapps directory on the host server.
	  */
	 protected String getBasePath() {
		  String realPath="";
		  //		  System.out.println("Here is the real path from context");
		  FacesContext fc=FacesContext.getCurrentInstance();
		  ExternalContext ec=fc.getExternalContext();
		  Object context=ec.getContext();
		 
		  if(context instanceof PortletContext) {
				realPath=((PortletContext)context).getRealPath("");
		  }
		  else if(context instanceof ServletContext) {
				realPath=((ServletContext)context).getRealPath("");
		  }
		  else {
				realPath="/tmp/";
		  }

		  //Remove any of the trailing web app specific name.
		  if(realPath.indexOf("/webapps/")>-1) {
				realPath=realPath.substring(0,realPath.indexOf("/webapps/")+"/webapps/".length());
		  }
		  //		  System.out.println("realPath is "+realPath);
		  return realPath;
	 }

	 /**
	  * The "factor" is used in numerous calculations to convert between cartesian and lat/lon
	  * coordinates.
	  */
	 public double factor(double refLon, double refLat) {
		  double d2r = Math.acos(-1.0) / 180.0;
		  double flatten=1.0/298.247;
		  
		  double theFactor = d2r* Math.cos(d2r * refLat)
				* 6378.139 * (1.0 - Math.sin(d2r * refLon) * Math.sin(d2r * refLon) * flatten);

		  return theFactor;
	 }

	 /**
	  * This gets rid of the dubious characters that may be used as input.
	  */
	 public String filterTheBadGuys(String toFilter) {
		  String badguys="[~!@#$%^&*()_+=/<>?]";
		  return toFilter.replaceAll(badguys,"");
	 }


    /**
     * Delete object instance from the object store.  
     * "details" is some helper string (like project name) that may be
     * useful for deletion. It can be null.
     */ 
    public void deletePersistentObj(String dbFilePath, 
				    Object obj, 
				    String details) {
		  db=Db4o.openFile(dbFilePath);
		  db.close();
    }

    public void deletePersistentObj(String dbFilePath, Object obj) {
		  db=Db4o.openFile(dbFilePath);
		  ObjectSet results=db.get(obj);
		  System.out.println("Delete result seet: "+results.size());
		  if(results.hasNext()){
				db.delete(obj);
		  }
		  db.close();
    }
	 
    public void updatePersistentObj(String dbFilePath, Object obj) {
		  db=Db4o.openFile(dbFilePath);
		  db.set(obj);
		  db.close();
    }
	 
    public void addPersistentObj(String dbFilePath, Object obj) {
		  db=Db4o.openFile(dbFilePath);
		  db.set(obj);
		  db.close();
    }
	 
    /**
     * Return an array of objects matching the template object's pattern.
     */ 
    public ArrayList fetchPersistentObj(String dbFilePath, 
													 Object template) {
		  ArrayList objectList=new ArrayList();
		  db=Db4o.openFile(dbFilePath);
		  ObjectSet results=db.get(template);
		  while(results.hasNext()) {
				objectList.add(results.next());
		  }
		  return objectList;
    }


    protected List createFaultList(String getSegmentList,
				    String getAuthorList,
				    String getLatStartList,
				    String getLatEndList,
				    String getLonStartList,
				    String getLonEndList,
				    String getFaultList,
				    String getInterpId) {

	List faultSegmentNameList = QueryFaultsBySQL(getSegmentList);
	List faultAuthorList = QueryFaultsBySQL(getAuthorList);
	List faultLatStarts = QueryFaultsBySQL(getLatStartList);
	List faultLatEnds = QueryFaultsBySQL(getLatEndList);
	List faultLonStarts = QueryFaultsBySQL(getLonStartList);
	List faultLonEnds = QueryFaultsBySQL(getLonEndList);
	List tmp_faultNameList = QueryFaultsBySQL(getFaultList);
	List tmp_interpIdList=QueryFaultsBySQL(getInterpId);
	
	List myFaultDBEntryList=new ArrayList();

	for (int i = 0; i < tmp_faultNameList.size(); i++) {
	    String tmp1 = tmp_faultNameList.get(i).toString();
	    FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
	    tmp_FaultDBEntry.faultName = new SelectItem(tmp1 + "@"
							+ faultSegmentNameList.get(i).toString(), tmp1);
	    tmp_FaultDBEntry.faultAuthor = faultAuthorList.get(i).toString();
	    tmp_FaultDBEntry.faultSegmentName = faultSegmentNameList.get(i)
		.toString();
	    tmp_FaultDBEntry.faultSegmentCoordinates = "("
		+ faultLatStarts.get(i).toString() + ","
		+ faultLatEnds.get(i).toString() + ")-("
		+ faultLonStarts.get(i).toString() + ","
		+ faultLonEnds.get(i).toString() + ")";
	    tmp_FaultDBEntry.interpId=tmp_interpIdList.get(i).toString();

	    myFaultDBEntryList.add(tmp_FaultDBEntry);
	    
	}
	return myFaultDBEntryList;
    }

    public List QueryFaultsBySQL(String tmp_query_sql, 
				 String faultDBServiceUrl) {
	List tmp_list = new ArrayList();
	try {
	    
	    String DB_RESPONSE_HEADER = "results of the query:";
	    SelectService ss = new SelectServiceLocator();
	    Select select = ss.getSelect(new URL(faultDBServiceUrl));
	    
	    // --------------------------------------------------
	    // Make queries.
	    // --------------------------------------------------
	    String tmp_str = select.select(tmp_query_sql);
	    tmp_str = tmp_str.substring(DB_RESPONSE_HEADER.length());
	    StringTokenizer st1 = new StringTokenizer(tmp_str, "\n");
	    // They begin with blank lines ?!
	    st1.nextToken();
	    st1.nextToken();
	    tmp_list.clear();
	    while (st1.hasMoreTokens()) {
		String tmp1 = st1.nextToken().trim();
		if (tmp1 == null || tmp1.equals("null"))
		    tmp1 = "N/A";
		tmp_list.add(tmp1);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return tmp_list;
    }
    
}

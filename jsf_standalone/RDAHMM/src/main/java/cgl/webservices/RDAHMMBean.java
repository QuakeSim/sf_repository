package cgl.webservices;

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

//Import stuff from db4o
import com.db4o.*;

/**
 * Everything you need to set up and run RDAHMM.
 */

public class RDAHMMBean extends GenericSopacBean{
	 
	 String rdahmmServiceUrl="http://gf19.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec";
	 //    String rdahmmServiceUrl;
	 
    //Some properties with default values
	 
    String dataUrl="";
    RDAHMMService rdservice;
	 
    //These are the output files
    String projectStdout;
    String projectRange;
    String projectQ;
    String projectPi;
    String projectMinval;
    String projectMaxval;
    String projectL;
    String projectInput;
    String projectB;
    String projectA;

    String projectGraphX;
    String projectGraphY;
    String projectGraphZ;

    //These are properties needed if you want to include a query
    //to the GRWS web service.
    String siteCode="sio5";
    String beginDate="2005-01-01";
    String endDate="2006-01-10";
	 String resource="procCoords";
	 String contextGroup="sopacGlobk";
	 String minMaxLatLon="";
	 String contextId="5";

	 Vector rdahmmRunValues=new Vector();
	 
    //RDAHMM file extensions
    String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};
	 
    //RDAHMM properties
    protected String codeName="RDAHMM";
    protected int numModelStates=4;
    protected int randomSeed=1;
    protected String outputType="";
    protected double annealStep=0.01;    
    
    //RDAHMM Gnuplot stuff properties
    protected String gnuplotAntUrl;
    protected String gnuplotBinPath;
    protected String gnuplotBaseWorkDir;
    protected String gnuplotFileServiceUrl;
	 
    protected String localImageFileX;
    protected String localImageFileY;    
    protected String localImageFileZ;

	 RdahmmProjectBean rdahmmProjectBean=new RdahmmProjectBean();
	 RDAHMMResultsBean resultsBean=null;
	 ObjectContainer db;

    /**
     * default empty constructor
     */
    public RDAHMMBean() throws Exception {   
		  super();
		  //Set the context manager.
		  cm=getContextManagerImp();
		  
		  //Create the service stub.  
		  System.out.println("Service to contact: "+rdahmmServiceUrl);
		  rdservice=(new RDAHMMServiceServiceLocator()).
				getRDAHMMExec(new URL(rdahmmServiceUrl));

		  System.out.println("RDAHMM Bean Created");
    }
    
    /**
     * These are methods associated with Faces
     * navigations.
     */
    public String newProject() throws Exception{
		  System.out.println("New project created");
		  isInitialized=getIsInitialized();
		  if(!isInitialized) {
				initWebServices();
		  }
		  
// 		  RdahmmProjectBean localBean=getRdahmmProjectBean();
// 		  makeProjectDirectory();
// 		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+".db");
// 		  db.set(localBean);
// 		  db.commit();
// 		  db.close();

		  rdahmmRunValues.clear();
		  
		  return ("rdahmm-new-project-created");
    }

	 /**
	  * Clears the local memory, not the DB.
	  */
	 public String discardProject() throws Exception {
		  System.out.println("Discarding project");
		  //Clear the in-memory value.
		  setResultsBean(null);

		  return ("rdahmm-project-saved");
	 }

    public String saveProject() throws Exception{
		  System.out.println("Saving project");
		  System.out.println(getContextBasePath()+"/"+userName+"/"+codeName+".db");
		  RdahmmProjectBean localBean=getRdahmmProjectBean();
		  localBean.setResultsBean(getResultsBean());

		  makeProjectDirectory();
		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+".db");
		  db.set(localBean);
		  db.commit();
		  db.close();
		  
		  //Clear the in-memory value.
		  setResultsBean(null);

		  return ("rdahmm-project-saved");
    }
    
	 protected void makeProjectDirectory() {
		  File projectDir=new File(getContextBasePath()+"/"+userName+"/"+codeName+"/");
		  System.out.println(getContextBasePath()+"/"+userName+"/"+codeName+"/");
		  projectDir.mkdirs();
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
    
	 /**
	  * This is the nonblocking version of the full invocation.
	  * It uses the results bean rather than the string array.
	  */ 
	 public String runBlockingRDAHMM_Full() throws Exception {
		  
		  newProject();
		  RdahmmProjectBean localBean=getRdahmmProjectBean();
		  RDAHMMResultsBean resultsBean=
				rdservice.runBlockingRDAHMM2(localBean.getSiteCode(),
													  localBean.getResource(),
													  localBean.getContextGroup(),
													  localBean.getContextId(),
													  localBean.getMinMaxLatLon(),
													  localBean.getBeginDate(),
													  localBean.getEndDate(),
													  localBean.getNumModelStates());
		  setResultsBean(resultsBean);
		  //		  setPropertyVals(projectName, returnBean);
		  //		  setParameterValues(projectName);
		  return "rdahmm-output-display";
	 }

	 /**
	  * This is the nonblocking version of the full invocation.
	  * It uses the results bean rather than the string array.
	  */ 
	 public String runNonblockingRDAHMM_Full() throws Exception {
		  System.out.println("Running RDAHMM");
		  RDAHMMResultsBean returnBean=
				rdservice.runNonblockingRDAHMM2(siteCode,
														 resource,
														 contextGroup,
														 contextId,
														 minMaxLatLon,
														 beginDate,
														 endDate,
														 numModelStates);
		  setResultsBean(resultsBean);
		  //		  setPropertyVals(projectName,returnBean);
		  //		  setParameterValues();
		  return "rdahmm-output-display";
	 }
	 
    public String populateProject() 
		  throws Exception{

// 		  System.out.println("Chosen project: "+chosenProject);
// 		  String contextName=codeName+"/"+chosenProject;
// 		  projectName=cm.getCurrentProperty(contextName,"projectName");
// 		  hostName=cm.getCurrentProperty(contextName,"hostName");
// 		  numModelStates=
// 				Integer.parseInt(cm.getCurrentProperty(contextName,"numModelStates"));
		  return "rdahmm-project-populated";
    }
	 
	 
    protected void setPropertyVals(String projectName, RDAHMMResultsBean rrb) {
		  //These are the output files.
		  rdahmmRunValues.clear();

		  try {
				makeProjectDirectory();
				db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				db.set(rrb);
				db.commit();
				db.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
    }

    /**
     * Below is psycho-bean property freakout madness.
     */
    public void setNumModelStates(int numModelStates){
		  this.numModelStates=numModelStates;
    }
	 
    public int getNumModelStates() {
		  return numModelStates;
    }
    
    public void setDataUrl(String dataUrl) {
		  this.dataUrl=dataUrl;
    }
	 
    public String getDataUrl() {
		  return dataUrl;
    }
	 
    public void setRdahmmServiceUrl(String rdahmmServiceUrl) {
		  this.rdahmmServiceUrl=rdahmmServiceUrl;
    }
	 
    public String getRdahmmServiceUrl(){
		  return rdahmmServiceUrl;
    }
	 
    public String getProjectStdout() {
		  return projectStdout;
    }
	 
    public void setProjectStdout(String projectStdout) {
		  this.projectStdout=projectStdout;
    }
	 
    public String getProjectRange(){
		  return projectRange;
    }
    
    public void setProjectRange(String projectRange){
		  this.projectRange=projectRange;
    }
	 
    public String getProjectQ(){
		  return projectQ;
    }
    
    public void setProjectQ(String projectQ){
		  this.projectQ=projectQ;
    }
	 
    public String getProjectPi(){
		  return projectPi;
    }
	 
    public void setProjectPi(String projectPi){
		  this.projectPi=projectPi;
    }
	 
    public String getProjectMinval(){
		  return projectMinval;
    }
	 
    public void setProjetcMinval(String projectMinval){
		  this.projectMinval=projectMinval;
    }
	 
    public String getProjectMaxval(){
		  return projectMaxval;
    }
	 
    public void setProjectMaxval(String projectMaxval){
		  this.projectMaxval=projectMaxval;
    }

    public String getProjectL(){
		  return projectL;
    }
	 
    public void setProjectL(String projectL){
		  this.projectL=projectL;
    }
	 
    public String getProjectInput(){
		  return projectInput;
    }
    public void setProjectInput(String projectInput){
		  this.projectInput=projectInput;
    }
	 
    public String getProjectB(){
		  return projectB;
    }

    public void setProjectB(String projectB){
		  this.projectB=projectB;
    }
	 
    public String  getProjectA(){
		  return projectA;
    }
	 
    public void setProjectA(String projectA){
		  this.projectA=projectA;
    }
	 
    public String  getProjectGraphX(){
		  return projectGraphX;
    }
	 
    public void setProjectGraphX(String projectGraphX){
		  this.projectGraphX=projectGraphX;
    }
	 
    public String  getProjectGraphY(){
		  return projectGraphY;
    }
	 
    public void setProjectGraphY(String projectGraphY){
		  this.projectGraphY=projectGraphY;
    }
	 
    public String  getProjectGraphZ(){
		  return projectGraphZ;
    }
	 
    public void setProjectGraphZ(String projectGraphZ){
		  this.projectGraphZ=projectGraphZ;
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
	 
	 public void setRdahmmRunValues(Vector rdahmmRunValues){
		  this.rdahmmRunValues=rdahmmRunValues;
	 }
	 
	 public Vector getRdahmmRunValues() {
		  return rdahmmRunValues;
	 }

	 public RdahmmProjectBean getRdahmmProjectBean() {
		  return rdahmmProjectBean;
	 }
	 
	 public void setRdahmmProjectBean(RdahmmProjectBean rdahmmProjectBean) {
		  this.rdahmmProjectBean=rdahmmProjectBean;
	 }

	 public void setResultsBean(RDAHMMResultsBean resultsBean) {
		  this.resultsBean=resultsBean;
	 }

	 public RDAHMMResultsBean getResultsBean(){
		  return resultsBean;
	 }
	 
}

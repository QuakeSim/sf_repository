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

/**
 * Everything you need to set up and run RDAHMM.
 */

public class RDAHMMBean extends GenericSopacBean{
	 
	 //    String rdahmmServiceUrl="http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec";
    String rdahmmServiceUrl="";
	 
    //Some properties with default values
    int numModelStates=2;
	 
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
	 String contextGroup="reasonComb";
	 String minMaxLatLon="";
	 String contextId="4";
    
	 
    //RDAHMM file extensions
    String[] fileExtension={".input",".stdout",".A",".B",".L",".Q",".pi"};
	 
    //RDAHMM properties
    protected String codeName="RDAHMM";
    protected int numModelStates=2;
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
		  String sopacDataFileName=projectName+".input";
		  String cfullName=codeName+"/"+projectName;
		  String contextDir=cm.getCurrentProperty(cfullName,"Directory");
		  String sopacDataFileContent=getSopacDataFileContent();
		  
		  createSopacDataFile(contextDir,sopacDataFileName,sopacDataFileContent);
		  String value=executeRDAHMM(contextDir,sopacDataFileName,cfullName);
		  return "rdahmm-rdahmm-launched";
		  
    }
	 
    public String populateAndPlot() throws Exception {
		  populateProject();
		  launchPlot();
		  return "rdahmm-plot-created";
    }
	 
    public String launchPlot() throws Exception {
		  String sopacDataFileName=projectName+".input";
		  String cfullName=codeName+"/"+projectName;
		  String contextDir=cm.getCurrentProperty(cfullName,"Directory");
		  
		  createSopacDataFile(contextDir,sopacDataFileName,sopacDataFileContent);
		  String value=createDataPlot(contextDir,sopacDataFileName,cfullName);
		  return "rdahmm-gnuplot-launched";
		  
    }
	 
    //Possibly obsolete--need to check.
    public String launchProject() {
		  return "rdahmm-project-launched";
    }

	 /**
	  * This is a no-argument method that can be called by the 
	  * associated JSF page.  It assumes all the parameters have
	  * been obtained from the form set methods.
	  *
	  * It returns URLs for the plots, numerical values, etc.
	  */
	 public String[] runBlockingRDAHMM_Full() throws Exception {
		  String [] returnStrings=
				rdservice.runBlockingRDAHMM(siteCode,
													 resource,
													 contextGroup,
													 minMaxLatLon,
													 beginDate,
													 endDate,
													 numModelStates);
		  setPropertyVals(returnStrings);
		  return returnStrings;
	 }

	 /**
	  * This is the nonblocking version of the full invocation.
	  */ 
	 public String[] runNonBlockingRDAHMM_Full() throws Exception {
		  String [] returnStrings=
				rdservice.runNonBlockingRDAHMM(siteCode,
														 resource,
														 contextGroup,
														 minMaxLatLon,
														 beginDate,
														 endDate,
														 numModelStates);
		  setPropertyVals(returnStrings);
		  return returnStrings;
	 }
	 
    public String populateProject() 
		  throws Exception{

		  System.out.println("Chosen project: "+chosenProject);
		  String contextName=codeName+"/"+chosenProject;
		  projectName=cm.getCurrentProperty(contextName,"projectName");
		  hostName=cm.getCurrentProperty(contextName,"hostName");
		  numModelStates=
				Integer.parseInt(cm.getCurrentProperty(contextName,"numModelStates"));
		  return "rdahmm-project-populated";
    }
	 
	 
    /**
     * This is the no-argument version. It requires these values to 
     * be set outside the method.  This is typical usage in a JSF
     * page.
     */
    public String runNonblockingRDAHMM() throws Exception {
		  if(dataUrl!=null && numModelStates>0) {
				String[] vals=rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);	    
				setPropertyVals(vals);
		  }
		  else throw new Exception();
		  return "simple-rdahmm-client-nav1";
    }
	 
    /**
     * This method is also suitable for use in a JSF page.
     */
    public String runBlockingRDAHMM()  throws Exception {
		  if(dataUrl!=null && numModelStates>0) {
				String [] vals=rdservice.runBlockingRDAHMM(dataUrl,numModelStates);	
				setPropertyVals(vals);
		  }
		  else throw new Exception();
		  return "simple-rdahmm-client-nav1";
    }
	 
    /**
     * This method is also suitable for use in a JSF page.
     * This one needs a sitecode, begin and end dates.
     */
    public String runBlockingRDAHMM2()  throws Exception {
		  if(dataUrl!=null && numModelStates>0) {
				String [] vals=rdservice.runBlockingRDAHMM(siteCode,
																		 beginDate,
																		 endDate,
																		 numModelStates);	
				setPropertyVals(vals);
		  }
		  else throw new Exception();
		  return "simple-rdahmm-client-nav1";
    }
	 
    /**
     * This method is also suitable for use in a JSF page.
     * This one needs a sitecode, begin and end dates.
     */
    public String runNonblockingRDAHMM2()  throws Exception {
		  if(dataUrl!=null && numModelStates>0) {
				String [] vals=rdservice.runBlockingRDAHMM(siteCode,
																		 beginDate,
																		 endDate,
																		 numModelStates);	
				setPropertyVals(vals);
		  }
		  else throw new Exception();
		  return "simple-rdahmm-client-nav1";
    }
	 
    /** 
     * This is a more full-fledged method.  The returned strings
     * are URLs for the output values.
     */
    public String[] runBlockingRDAHMM(String dataUrl,
												  int numModelStates) 
		  throws Exception {
		  
		  setDataUrl(dataUrl);
		  setNumModelStates(numModelStates);
		  return rdservice.runBlockingRDAHMM(dataUrl,numModelStates);
    }
	 
    public String[] runNonblockingRDAHMM(String dataUrl,
													  int numModelStates) 
		  throws Exception {

		  setDataUrl(dataUrl);
		  setNumModelStates(numModelStates);
		  return rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);
    }
	 
    //This method is pretty dumb. The values's order is hard-coded
    //on the server.
    protected void setPropertyVals(String[] vals) {
		  //These are the output files.
		  projectInput=vals[0];
		  projectRange=vals[1];
		  projectQ=vals[2];
		  projectPi=vals[3];
		  projectA=vals[4];
		  projectMinval=vals[5];
		  projectMaxval=vals[6];
		  projectL=vals[7];
		  projectB=vals[8];
		  projectQ=vals[9];
		  projectStdout=vals[10];
		  
		  //These are the images
		  projectGraphX=vals[11];
		  projectGraphY=vals[12];
		  projectGraphZ=vals[13];
		  
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
    

}

package cgl.webservices;

//Imports from the mother ship
import org.servogrid.genericproject.GenericSopacBean;
import org.servogrid.genericproject.GenericProjectBean;
import org.servogrid.genericproject.Utility;
import org.servogrid.genericproject.ProjectBean;

//Faces classes
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

//Servlet and portlet API stuff.
import javax.servlet.ServletContext;
import javax.portlet.PortletContext;

//QuakeSim Web Service clients
import WebFlowClient.cm.*;
import WebFlowClient.fsws.*;
//import cgl.webclients.*;
import cgl.webservices.*;

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
    
    /**
     * default empty constructor
     */
    public STFILTERBean(){   
	super();
	cm=getContextManagerImp();
	setSiteCode("LBC1");  //Use this for testing.
	
	//Set up here the station list vectors.
	masterList=new MasterParamList();
	myStationList=new StationParamList();
	allsitesList=new AllSitesParamList();

	//Set up here the station conntainer
 	myStation=new MyStationContainer("LBC1");
	myStation.setEstParamVector(myStationList.getStationParamList());
	myStation.setMasterParamList(masterList.getStationParamList());
	
	//Set up the default station list.
	allsites=new AllStationsContainer();
	allsites.setEstParamVector(allsitesList.getStationParamList());
	allsites.setMasterParamList(masterList.getStationParamList());
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
	System.out.println("Project name is "+projectName);
	
	//Store the request values persistently
	contextName=codeName+"/"+projectName;
	String hostName=getHostName();
	cm.addContext(contextName);
	cm.setCurrentProperty(contextName,"projectName",projectName);
	cm.setCurrentProperty(contextName,"hostName",hostName);	
	projectCreated=true;
	
	//This is the working directory of the execution host.
	workDir=getBaseWorkDir()+File.separator
	    +userName+File.separator+projectName;

	globalDataDir=getBinPath();

	return "new-project-created";
    }

    public String setParameterValues() throws Exception {
	//This should always be true at this point, but check for 
	//safety.
	if(projectCreated!=true) {
	    createNewProject();
	}

	//Now set the rest of the parameters.
	//The cm object is inherited.
	cm.setCurrentProperty(contextName,"resOption",resOption+"");
	cm.setCurrentProperty(contextName,"termOption",termOption+"");
	cm.setCurrentProperty(contextName,"cutoffCriterion",
			      cutoffCriterion+"");	
	cm.setCurrentProperty(contextName,"estJumpSpan",estJumpSpan+"");
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

    public String loadProjectKillList() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("list-death-row");
    }
    

    public String loadProjectPlots() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	setContextList();
        return ("list-project-plots");
    }

    /**
     * This is a no-arg wrapper method for invoking the service.
     * The values of siteCode, beginDate, and endDate are all
     * passed in via the bean accessor methods.
     *
     * Similarly, the returned String is purely for JSF 
     * navigation purposes.  The real data is placed in other
     * variables.
     */
    public String runBlockingGnuplot() 
	throws Exception {

	//Create the client stub for the gnuplot service.
	System.out.println(gnuplotServiceUrl);
	try {
	    gnuplotService=(new GnuplotServiceServiceLocator()).
		getGnuplotExec(new URL(gnuplotServiceUrl));
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}

	String[] results=gnuplotService.runBlockingGnuplot(siteCode, 
							   beginDate,
							   endDate);
	localImageFileX=results[0];
	localImageFileY=results[1];
	localImageFileZ=results[2];
	

	for(int i=0;i<results.length;i++) {
	    System.out.println("Query results:"+results[i]);
	}

	return "plot-time-series";
    }

    /**
     * This client invokes the remote Gnuplot service.
     * This version explicitly requires the parameters to be
     * passed in from the arguments.
     */ 
    public String[] runBlockingGnuplot(String siteCode,
				       String beginDate,
				       String endDate) 
	throws Exception {
	
	String[] results=gnuplotService.runBlockingGnuplot(siteCode,
							   beginDate,
							   endDate);
	return results;
    }

    
//     public String launchSTFILTER() throws Exception {
// 	//Do this here.
// 	setParameterValues();

// 	String sopacDataFileName=getSiteCode()+sopacDataFileExt;
// 	String cfullName=codeName+"/"+projectName;
// 	String contextDir=cm.getCurrentProperty(cfullName,"Directory");

// 	createDriverFile(contextDir);
// 	createSopacDataFile(contextDir,sopacDataFileName,sopacDataFileContent);
// 	createSiteListFile(contextDir);
// 	createDataListFile(contextDir);
// 	createEstimatedParamFile(contextDir);
// 	String value=executeSTFILTER(contextDir,sopacDataFileName,cfullName);
// 	return "stfilter-launched";
//     }

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

    /** 
     * Create the site list file.  Currently we only support
     * one site and the XYZ format (ie "1   8").
     */
    public void createSiteListFile(String contextDir)
	throws Exception {

	String slash="/";  // This is not File.separator of the webserver
	siteListFile=projectName+mosesSiteListExt;
	System.out.println("Writing input file: "+contextDir+"/"+siteListFile);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+siteListFile),true);

	pw.println("  1");  //Need to make this more general.
	pw.println(getSiteCode().toUpperCase()+"_GPS");
	pw.close();
    }

    public void createEstimatedParamFile(String contextDir)
	throws Exception {
	estParameterFile=projectName+mosesParamFileExt;
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+estParameterFile),true);
	if(myStation.printContents()!=null) {
	    pw.println("  2");
	    pw.println(allsites.printContents());
	    pw.println(myStation.printContents());
	}
	else {
	    pw.println("  1");
	    pw.println(allsites.printContents());
	}
	pw.close();
    }

    public void createDataListFile(String contextDir)
	throws Exception {

	String slash="/";  // This is not File.separator of the webserver
	dataListFile=projectName+mosesDataListExt;
	System.out.println("Writing input file: "+contextDir+"/"+dataListFile);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+dataListFile),true);

	pw.println(" 1   8");  //Need to make this more general.
	pw.println(getSiteCode()+sopacDataFileExt);
	pw.close();
    }

    /**
     * Create the stfilter driver file.
     */
    public String createDriverFile(String contextDir)
	throws Exception {

	String fivespace="     ";
	String slash="/";  // This is not File.separator of the webserver
	driverFileName=projectName+driverFileExtension;
	System.out.println("Writing input file: "+contextDir+"/"+driverFileName);
	PrintWriter pw=
	    new PrintWriter(new FileWriter(contextDir+"/"+driverFileName),true);
	pw.println(twospace+"apriori value file:"+twospace+globalDataDir+slash+aprioriValueFile);
	pw.println(twospace+"input file:"+twospace+workDir+slash+projectName+mosesDataListExt);
	pw.println(twospace+"sit_list file:"+twospace+workDir+slash+projectName+mosesSiteListExt);
	pw.println(twospace+"est_parameter file:"+twospace+workDir+slash+projectName+mosesParamFileExt);
	//	pw.println(twospace+"est_parameter file:"+twospace+globalDataDir+mosesParamFile);
	pw.println(twospace+"output file:"+twospace+workDir+slash+projectName+outputFileExt);
	pw.println(twospace+"residual file:"+twospace+workDir+slash+projectName+residualFileExt);
	pw.println(twospace+"res_option:"+twospace+resOption);
	pw.println(twospace+"specific term_out file:"+twospace+workDir+slash+projectName+termOutFileExt);
	pw.println(twospace+"specific term_option:"+twospace+termOption);
	pw.println(twospace+"enu_correlation usage:"+twospace+"no");
	pw.println(twospace+"cutoff criterion (year):"+twospace+cutoffCriterion);
	pw.println(twospace+"span to est jump aper (est_jump_span):"+twospace+estJumpSpan);
	pw.println(twospace+"weak_obs (big sigma) criteria:"+twospace+weakObsCriteria.getEast()+twospace+weakObsCriteria.getNorth()+twospace+weakObsCriteria.getUp());
	pw.println(twospace+"outlier (big o-c) criteria mm:"+twospace+outlierCriteria.getEast()+twospace+outlierCriteria.getNorth()+twospace+outlierCriteria.getUp());
	pw.println(twospace+"very bad_obs criteria mm:"+twospace+badObsCriteria.getEast()+twospace+badObsCriteria.getNorth()+twospace+badObsCriteria.getUp());
	pw.println(twospace+"t_interval:"+twospace+timeInterval.getBeginTime()+twospace+timeInterval.getEndTime());
	pw.println(twospace+"end:");
	pw.println("---------- part 2 -- apriori information");
	pw.println(twospace+"exit:");
	pw.close();

	//Clean this up since it could be a memory drain.
	//	sopacDataFileContent=null;
	return "input-file-created";
    }

    
    public String deleteProject() throws Exception {
	//projectsToDelete is an ArrayList inherited from GenericProjectBean.
	//It is set by the calling faces page.
	if(projectsToDelete!=null && projectsToDelete.size()>0) {
	    for(int i=0;i<projectsToDelete.size();i++) {
		String contextName=codeName+"/"
		    +(String)projectsToDelete.get(i);
		cm.removeContext(contextName);
	    }
	    projectsToDelete.clear();
	}
	setContextList();
	return "project-removed";
    }

    /**
     * As currently written, this method sets properties that are
     * specific to the backend application.
     */
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
	sopacDataFileName=cm.getCurrentProperty(contextName,"sopacDataFileName");
	sopacDataFileContent=setSTFILTERInputFile(projectName);
	return "project-populated";
    }


//     public String executeSTFILTER(String contextDir,
// 				String sopacDataFileName,
// 				String cfullName) 
// 	throws Exception{
	
// 	System.out.println("FileService URL:"+fileServiceUrl);
// 	System.out.println("AntService URL:"+gnuplotServiceUrl);
	

// 	//--------------------------------------------------
// 	// Set up the Ant Service and make the directory
// 	//--------------------------------------------------
// 	AntVisco ant=new AntViscoServiceLocator().getAntVisco(new URL(gnuplotServiceUrl));
// 	String bf_loc=binPath+"/"+"build.xml";
// 	String[] args0=new String[4];
//         args0[0]="-DworkDir.prop="+workDir;
//         args0[1]="-buildfile";
//         args0[2]=bf_loc;
//         args0[3]="MakeWorkDir";
	
//         ant.setArgs(args0);
//         ant.run();
	
// 	//--------------------------------------------------
// 	// Set up the file service and upload the driver,
// 	// site list, and gps data files.
// 	//--------------------------------------------------
// 	FSClientStub fsclient=new FSClientStub();
// 	String sopacDestfile=workDir+"/"+sopacDataFileName; 
// 	String driverDestfile=workDir+"/"+driverFileName; 
// 	String siteListDestfile=workDir+"/"+siteListFile;
// 	String dataListDestfile=workDir+"/"+dataListFile;
// 	String estParamDestfile=workDir+"/"+estParameterFile;

// 	try {
// 	    fsclient.setBindingUrl(fileServiceUrl);    	
// 	    fsclient.uploadFile(contextDir+"/"+sopacDataFileName,sopacDestfile);
// 	    fsclient.uploadFile(contextDir+"/"+siteListFile,siteListDestfile);
// 	    fsclient.uploadFile(contextDir+"/"+dataListFile,dataListDestfile);
// 	    fsclient.uploadFile(contextDir+"/"+driverFileName,driverDestfile);
// 	    fsclient.uploadFile(contextDir+"/"+estParameterFile,estParamDestfile);
// 	}
// 	catch(Exception ex) {
// 	    ex.printStackTrace();
// 	}

// 	//--------------------------------------------------
// 	// Run the code.
// 	//--------------------------------------------------	
	
// 	String[] args=new String[7];
//         args[0]="-DworkDir.prop="+workDir;
//         args[1]="-DprojectName.prop="+projectName;
//         args[2]="-Dbindir.prop="+binPath;
//         args[3]="-DSTFILTERBaseName.prop="+projectName;
//         args[4]="-buildfile";
//         args[5]=bf_loc;
//         args[6]="RunSTFILTER";
	
//         ant.setArgs(args);
//         ant.execute();
	
// 	return "stfilter-executing";
//     }

    /**
     * This is similar to executeSTFILTER but it must take place on
     * a host with gnuplot installed on it.  Note this assumes
     * for historical reasons that stfilter and the plotting tool
     * (gnuplot) are on separate machines.
     *
     * This method is currently empty.
     */
    public String createDataPlot(String contextDir,
				 String sopacDataFileName,
				 String cfullName) 
	throws Exception{
	
	return "gnuplot-plot-created";
    }    

    private String setSTFILTERInputFile(String projectName) {
	String sopacDataFileContent="Null Content; please re-enter";
	String sopacDataFileName=projectName+driverFileExtension;
	try {
	    String thedir=cm.getCurrentProperty(codeName
						+"/"+projectName,"Directory");
	    System.out.println(thedir+"/"+sopacDataFileName);
	    
	    BufferedReader buf=
		new BufferedReader(new FileReader(thedir+"/"+sopacDataFileName));
	    String line=buf.readLine();
	    sopacDataFileContent=line+"\n";
	    while(line!=null) {
		System.out.println(line);
		line=trimLine(line);	
		sopacDataFileContent+=line+"\n";
		line=buf.readLine();
	    }
	    buf.close();
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
	return sopacDataFileContent;
    }

    //Some internal fields.
    String twospace="  ";  //Used to format the driver file.
    boolean projectCreated=false;

    //This is the gnuplot service url.  Duh.  We get the value
    //from the faces-config.xml file.
    String gnuplotServiceUrl;

    //STFILTER properties
    private String codeName="STFILTER";
    private int resOption=387;
    private int termOption=556;
    private double cutoffCriterion=1.0;
    private double estJumpSpan=1.0;
    private WeakObsCriteria weakObsCriteria=
	new WeakObsCriteria(30.0,30.0,50.0);
    private OutlierCriteria outlierCriteria=
	new OutlierCriteria(800.0,800.0,800.0);
    private BadObsCriteria badObsCriteria=
	new BadObsCriteria(10000.0, 10000.0, 10000.0);
    private TimeInterval timeInterval=new TimeInterval(1998.0, 2006.800);


    //This is the file that will hold the 
    //results of the GPS station query.
    private String sopacDataFileName="";
    private String sopacDataFileContent="";
    private String sopacDataFileExt=".data";

    //This is the working diretory for running the 
    //code on the execution host.  The global data
    //directory is the location of things like the
    //apriori file.
    private String workDir="";
    private String globalDataDir="";

    //This is the driver file and its constituent lines.
    private String driverFileName="";
    private String driverFileContent="";
    private String driverFileExtension=".drv";

    //These are fixed files, at least for now.
    private String aprioriValueFile="itrf2000_final.net";
    private String mosesParamFile="moses_test.para";

    //These are file extensions.  The files will be named after the
    //project.
    private String mosesDataListExt=".list";
    private String mosesSiteListExt=".site";
    private String mosesParamFileExt=".para";
    private String residualFileExt=".resi";
    private String termOutFileExt=".mdl";
    private String outputFileExt=".out";

    //This is the site list file
    private String siteListFile;
    private String dataListFile;
    private String estParameterFile;

    //Project properties
    private String[] contextList;
    private Hashtable contextListHash;
    private Vector contextListVector;

    //These contain the site estimate params.  Note
    //this needs to be generalized, as I'm assuming only 
    //one site is used at a time.
    StationContainer myStation;
    StationContainer allsites;

    StationParamList myStationList,allsitesList;
    MasterParamList masterList;
    
    //Some useful rendering constants
    boolean renderAllSites=false;
    boolean renderMySite=false;
    boolean renderMasterParamList1=false;
    boolean renderMasterParamList2=false;

    //Used for accessing the images
    protected String localImageFileX;
    protected String localImageFileY;    
    protected String localImageFileZ;

    //The gnuplot service client stub
    GnuplotService gnuplotService;


    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------
    public void toggleRenderMPL1(ActionEvent ev){
	renderMasterParamList1=!renderMasterParamList1;
	//	return renderMasterParamList;
    }

    public boolean getRenderMasterParamList1(){
	return renderMasterParamList1;
    }

    public void setRenderMasterParamList1(boolean renderMasterParamList1){
	this.renderMasterParamList1=renderMasterParamList1;
    }

    public void toggleRenderMPL2(ActionEvent ev){
	renderMasterParamList2=!renderMasterParamList2;
	//	return renderMasterParamList;
    }

    public boolean getRenderMasterParamList2(){
	return renderMasterParamList2;
    }

    public void setRenderMasterParamList2(boolean renderMasterParamList2){
	this.renderMasterParamList2=renderMasterParamList2;
    }
    public AllStationsContainer getAllsites(){
	return (AllStationsContainer)allsites;
    }

    public MyStationContainer getMyStation(){
	return (MyStationContainer)myStation;
    }
    
    public String getDriverFileName() {
	return driverFileName;
    }

    public void setDriverFileName(String driverFileName){
	this.driverFileName=driverFileName;
    }

    public OutlierCriteria getOutlierCriteria() {
	return outlierCriteria;
    }

    public void  setOutlierCriteria(OutlierCriteria outlierCriteria) {
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

    public WeakObsCriteria getWeakObsCriteria() {
	return weakObsCriteria;
    }

    public void setWeakObsCriteria(WeakObsCriteria wobc) {
	weakObsCriteria=wobc;
    }

    public BadObsCriteria getBadObsCriteria() {
	return badObsCriteria;
    }

    public void setBadObsCriteria(BadObsCriteria badObsCriteria) {
	this.badObsCriteria=badObsCriteria;
    }

    public TimeInterval getTimeInterval() {
	return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
	this.timeInterval=timeInterval;
    }

    public void setGnuplotServiceUrl(String gnuplotServiceUrl) {
	this.gnuplotServiceUrl=gnuplotServiceUrl;
    }
    
    public String getGnuplotServiceUrl() {
	return gnuplotServiceUrl;
    }

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

}

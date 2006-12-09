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
import cgl.webservices.*;

//GPS Metadata Stuff
import cgl.sensorgrid.sopac.gps.GetStationsRSS;

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
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

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
	setSiteCode("LBC1");  //Use this for testing.
	setUpGPSNetwork();
	setUpOverlayNetwork();
	System.out.println("Constructor completed");
    }

    /**
     * These are methods for 
     */  
    public void setUpGPSNetwork() {
	System.out.println("Setting up GPS network stuff");
	RSSBeanID=new GetStationsRSS();
	networkNames=RSSBeanID.networkNames();
	String[] center_xy=RSSBeanID.getMapCenter();
	mapCenterX=center_xy[0];
	mapCenterY=center_xy[1];
    }

    public void setUpOverlayNetwork() {
	System.out.println("Setting up overlay");
	networkBeanArray=new GPSNetworkInfoBean[networkNames.size()];
	for(int j=0;j<networkNames.size();j++) {
	    String networkName=(String)networkNames.get(j);
	    networkBeanArray[j]=new GPSNetworkInfoBean();
	    networkBeanArray[j].setNetworkName(networkName);
	    networkBeanArray[j].assignNetworkIconUrl(getColor(j));

	    //Translate stationsVec into something JSF can handle
	    stationsVec=RSSBeanID.getStationsVec(networkName);
	    GPSStationBean[] stationBeanArray=
		new GPSStationBean[stationsVec.size()];
	    for(int i=0;i<stationsVec.size();i++) {
		String stationName=(String)stationsVec.get(i);
		double lon=Double.parseDouble(RSSBeanID.getStationInfo(stationName)[0]);
		double lat=Double.parseDouble(RSSBeanID.getStationInfo(stationName)[1]);
		stationBeanArray[i]=new GPSStationBean();
		stationBeanArray[i].setParentNetwork(networkName);
		stationBeanArray[i].setStationName(stationName);
		stationBeanArray[i].setStationLat(lat);
		stationBeanArray[i].setStationLon(lon);
	    }
	    networkBeanArray[j].setStationsBeanArray(stationBeanArray);
	}
    }

    private String getColor(int j) {
	int k=0;
	if(j>colors.length) return colors[k];
	else return colors[j];
    }

    public String loadTabTemplateFile() throws Exception{
	
	String str_tabcontent="<div page=\"1\" label=\"Current\" class=\"active\" style=\"font-family: Verdana,Geneva,Arial,Helvetica,sans-serif; font-size: 11px;\"> <div style=\"border-bottom: 1px solid rgb(204, 204, 204); width: 450px; padding-bottom: 2px; font-weight: bold;\"> <span class=\"style3\">Station Name: <span class=\"style1\">{!name!}</span> | Network:<span class=\"style1\">{!networkName!}</span> | Lat: <span class=\"style1\">{!lat!}</span> | Lon: <span class=\"style1\">{!lon!}</span></span> </div> <img src=\"{!output_png!}\" height=\"335\" width=\"447\"> </div>";
	
	return str_tabcontent;
    }
    
    //Note this method assumes tabContent and siteCode have been obtained
    public void constructPlotHtml(String str_tabcontent) {
	stationsVec=RSSBeanID.getAllStationsVec();
	
	siteCodeLat = RSSBeanID.getStationInfo(siteCode)[0];
	siteCodeLon = RSSBeanID.getStationInfo(siteCode)[1];
	
	tabContent[0]=new String(str_tabcontent);
	tabContent[1]=new String(str_tabcontent);
	tabContent[2]=new String(str_tabcontent);
	
	tabContent[0]=tabContent[0].replace("{!name!}",siteCode);
	tabContent[0]=tabContent[0].replace("{!networkName!}","Need to Fix");
	tabContent[0]=tabContent[0].replace("{!lon!}",siteCodeLon);
	tabContent[0]=tabContent[0].replace("{!lat!}",siteCodeLat);
	tabContent[0]=tabContent[0].replace("{!output_png!}",localImageFileX);
	tabContent[1]=tabContent[1].replace("{!name!}",siteCode);
	tabContent[1]=tabContent[1].replace("{!networkName!}","Need to Fix");
	tabContent[1]=tabContent[1].replace("{!lon!}",siteCodeLon);
	tabContent[1]=tabContent[1].replace("{!lat!}",siteCodeLat);
	tabContent[1]=tabContent[1].replace("{!output_png!}",localImageFileY);
	tabContent[2]=tabContent[2].replace("{!name!}",siteCode);
	tabContent[2]=tabContent[2].replace("{!networkName!}","Need to Fix");
	tabContent[2]=tabContent[2].replace("{!lon!}",siteCodeLon);
	tabContent[2]=tabContent[2].replace("{!lat!}",siteCodeLat);
	tabContent[2]=tabContent[2].replace("{!output_png!}",localImageFileZ);
    }
    
    /**
     * This is a method suitable for calling through JSF EL 
     */
    public String runGnuplotAndPlot() throws Exception {
	runBlockingGnuplot();
	constructPlotHtml(loadTabTemplateFile());
	return "plot-time-series";
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
     * This client invokes the remote AnalyzeTseri service.
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
    

    //Some internal fields.
    String twospace="  ";  //Used to format the driver file.
    boolean projectCreated=false;

    //This is the gnuplot service url.  Duh.  We get the value
    //from the faces-config.xml file.
    String gnuplotServiceUrl;
    String analyzeTseriServiceUrl;

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
    AnalyzeTseriService analyzeTseriService;
    GnuplotService gnuplotService;

    //Stuff for creating the maps with default values.
    String mapCenterX="33.036";
    String mapCenterY="-117.24";
    String siteCodeLon, siteCodeLat;
	
    //Used to making the tabbed display pages.
    String str_tabcontent;
    String[] tabContent=new String[3];
    GetStationsRSS RSSBeanID;
    Vector networkNames, stationsVec;
    GPSNetworkInfoBean[] networkBeanArray;
    int networkBeanArraySize;
    
    //Needed for GPS map display
    String[] colors={"red","green","blue","black","white","yellow","purple","brown"};

    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------
    public GPSNetworkInfoBean[] getNetworkBeanArray() {
	return networkBeanArray;
    }
    
    public int getNetworkBeanArraySize() {
	return networkBeanArray.length;
    }

    public String getSiteCodeLon() {
	return siteCodeLon;
    }

    public void setSiteCodeLon(String siteCodeLon) {
	this.siteCodeLon=siteCodeLon;
    }

    public String getSiteCodeLat() {
	return siteCodeLat;
    }

    public void setSiteCodeLat(String siteCodeLat) {
	this.siteCodeLat=siteCodeLat;
    }

    public String[] getTabContent() {
	return tabContent;
    }
    
    public void setTabContent(String[] tabContent){
	for(int i=0;i<tabContent.length;i++){
	    this.tabContent[i]=tabContent[i];
	}
    }

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

    public void setAnalyzeTseriServiceUrl(String analyzeTseriServiceUrl) {
	this.analyzeTseriServiceUrl=analyzeTseriServiceUrl;
    }
    
    public String getAnalyzeTseriServiceUrl() {
	return analyzeTseriServiceUrl;
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

    public void setMapCenterX(String mapCenterX){
	this.mapCenterX=mapCenterX;
    }

    public String getMapCenterX(){
	return mapCenterX;
    }

    public void setMapCenterY(String mapCenterY){
	this.mapCenterY=mapCenterY;
    }

    public String getMapCenterY(){
	return mapCenterY;
    }

}
